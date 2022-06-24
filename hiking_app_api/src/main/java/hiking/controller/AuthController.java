package hiking.controller;

import hiking.models.AppUser;
import hiking.security.AppUserService;
import hiking.security.Cryptography;
import hiking.security.JwtConverter;
import hiking.service.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.security.PrivateKey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@ConditionalOnWebApplication
@RestController
public class AuthController {

    // The 'AuthenticationManage' has a single method authenticate() which processes an Authentication request.
    private final AuthenticationManager manager;
    // Spring DI will inject the JwtConverter bean into this field. Authorized credentials are used to generate JWT tokens to return to the front end or user.
    private final JwtConverter converter;
    private final AppUserService userService;

    private final Cryptography cryptography;

    public AuthController(AuthenticationManager manager, JwtConverter converter, AppUserService userService, Cryptography cryptography) {
        this.manager = manager;
        this.converter = converter;
        this.userService = userService;
        this.cryptography = cryptography;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {
        // The password will be encrypted with Base64 encoding  when receiving it, so we need to decode and then decrypt.
        String password = null;
        try{
            // Decode the password and then decrypt it, set the unencrypted password to be sent for hashing.
            password = decryptPassword(credentials.get("password"));
        }catch (Exception e){
            HashSet<String> errors = new HashSet<>();
            errors.add("Could not load private key");
           return new ResponseEntity<>(errors,HttpStatus.I_AM_A_TEAPOT);
        }

        if(password==null || password.isEmpty()) {
            HashSet<String> errors = new HashSet<>();
            errors.add("Password is empty");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }


        // The 'UsernamePasswordAuthenticationToken' class is an Authentication implementation that
        // is designed for simple presentation of a username and password.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                credentials.get("username"), password);
        try{
            //The 'Authentication' interface represents the token for an authentication request
            // or for an autheticated principal once the request has been processed by the
            // AuthenticationManager.authenticate() method.
            // The authenticate method responds to POST requests for the /authenticate endpoint.
            // It accepts a username and password that is sent as JSON in the request body. (Username and password daata should only be shared via https in production applications)
            Authentication authentication = manager.authenticate(token);

            if(authentication.isAuthenticated()){
                // Gets the jwt token from the 'User' from the user via the authetication object.
                String jwtToken = converter.getTokenFromUser((User) authentication.getPrincipal());
                HashMap<String, String> map = new HashMap<>();
                // Put it in a map where the key is 'jwt_token' (or token) and the value is the JWT token.
                map.put("jwt_token", jwtToken);

                // Return the response with the JWT token.
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }catch (AuthenticationException ex){
            System.out.println(ex);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /*
        Do not create long-lived tokens. To keep the user from having to login frequently, you can have the app request token refreshes before the current token expires.
        The app can then use the new token to make requests to the server
        So we use this mapping just for that.
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<Map<String, String>> refreshToken(UsernamePasswordAuthenticationToken principal){
         User user = new User(principal.getName(), principal.getName(), principal.getAuthorities());
         String jwtToken = converter.getTokenFromUser(user);
         HashMap<String, String> map = new HashMap<>();
         map.put("jwt_token", jwtToken);

         return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Map<String, String> credentials) {
        AppUser user = null;
        Result <AppUser> result = new Result<>();

        String password = null;
        try{
            // Decode the password and then decrypt it, set the unencrypted password to be sent for hashing.
            password = decryptPassword(credentials.get("password"));
        }catch(Exception e){
            HashSet<String> errors = new HashSet<>();
            errors.add("Could not load private key");
            return new ResponseEntity<>(errors,HttpStatus.CHECKPOINT);
        }

        if(password==null || password.isEmpty()) {
            HashSet<String> errors = new HashSet<>();
            errors.add("Password is empty");
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }


        try {
            String username = credentials.get("username");

            result = userService.create(username, password);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("The provided email already exists"), HttpStatus.BAD_REQUEST);
        }
        if(!result.isSuccess()){
            return ErrorResponse.build(result);
        }
        user = result.getPayload();

        HashMap<String, Object> map = new HashMap<>();
        map.put("appUserId", user.getAppUserId());
        map.put("username", user.getUsername());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    // This method decrypts the password from the request.
    private String decryptPassword(String password) throws Exception {
        // load up the private key file and send it to be decrypted.
        String path="keys/privateKey";

        PrivateKey privateKey = cryptography.getPrivateKey(path);
        return cryptography.decrypt(password, privateKey);
    }
}
