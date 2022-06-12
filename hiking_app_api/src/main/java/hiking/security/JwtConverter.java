package hiking.security;

/*
    This class consolidates JWT token concerns by generating a new JWT token from a Spring Security User
    and instantiates a User based on a valid JWT token.
 */

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    // 1. Signing Key - Each time that this component is instantiated, a new key is generated.
    // For larger scaled that must be able to restart without interruption or are load balanced
    // The key should be generated from configuration with a secret
    // It is never safe to instantiate JwtConverter directly, which generates a new key.
    // There can only be one instance of JwtConverter because a stable key is required for signing
    // and confirming the token
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 2. Configurable constants - This is more likely to be stored as configuration
    private final String ISSUER = "hiking-app";
    private final int EXPIRATION_MINUTES = 60;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    public String getTokenFromUser(User user){
        String authorities = user.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        // 3. Create a JWT token - Set the token issuer, subject (username), claims (role), expiration, and sign with the key
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public User getUserFromToken(String token){
        if(token==null || !token.startsWith("Bearer ")){
            return null;
        }

        try{
            // 4. Use JJWT classes to read a token - the parser asserts an expectation for the issurer and expiration (which is automatic). Once we have the Jws<Claims> instance, read the username and roles.
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7));

            String username = jws.getBody().getSubject();
            String authStr = (String) jws.getBody().get("authorities");
            List<GrantedAuthority> authorities = Arrays.stream(authStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new User(username, username, authorities);
        } catch (JwtException e) {
            // 5. JWT failures are modeled as exceptions
            System.out.println(e);
        }

        return null;
    }

}
