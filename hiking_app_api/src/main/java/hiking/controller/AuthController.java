package hiking.controller;

import hiking.security.JwtConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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

import java.util.HashMap;
import java.util.Map;

@ConditionalOnWebApplication
@RestController
public class AuthController {

    // The 'AuthenticationManage' has a single method authenticate() which processes an Authentication request.
    private final AuthenticationManager manager;
    // Spring DI will inject the JwtConverter bean into this field. Authorized credentials are used to generate JWT tokens to return to the front end or user.
    private final JwtConverter converter;

    public AuthController(AuthenticationManager manager, JwtConverter converter) {
        this.manager = manager;
        this.converter = converter;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {
        // The 'UsernamePasswordAuthenticationToken' class is an Authentication implementation that
        // is designed for simple presentation of a username and password.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                credentials.get("username"), credentials.get("password"));
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
}
