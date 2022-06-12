package hiking.security;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * A servlet filter is a class that is allowed to intercept and possibly alter an HTTP request or response.
 * They execute before or after application code.
 * Authentication filters must occur before application code to establish permissions before a response is generated.
 * Filters are chained, with a filter's output being the input for another input, and if a request is not suitable, the filter can end the chain and send an early response.
 * There is no need to build an authentication filter from scratch with Spring Security, as it provides several filter base classes that can server as a starting point for your own filters.
 * JwtRequestFilter.java extends BasicAuthenticationFilter.java which is a base class for all authentication filters.
 */
public class JwtRequestFilter extends BasicAuthenticationFilter {

    private final JwtConverter converter;

    public JwtRequestFilter(AuthenticationManager manager, JwtConverter converter) {
        super(manager); // 1. BasicAuthenticationManager requires an AuthenticationManager to be provided in its constructor. Include it as a dependency and pass it via super. AuthenticationManager is a class that is responsible for managing users and their roles.
        this.converter = converter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 2. Read the authorization value from the request. We check the Authorization header. It is not required for all reqeusts, so we dont reject requests without it.
        // Instead, check if the request authorization contains the prefix "Bearer ", in which case we can assume it must contain a JWT.
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer ")) {
            // 3. If the value is not null and starts with 'Bearer ' then confirm it with JwtConverter.
            // Use JwtConverter to create a User from the token. If the user is null, it means something was wrong with the token.
            // this may include expired tokens, wrong content, or malformed tokens.
            User user = converter.getUserFromToken(authorization);
            if (user == null){
                response.setStatus(403); // If the user is not found, then set the response status to 403.
            } else {
                // 4. If the user is confirmed and is authorized, Set the authorization for this single request.
                // By the user not being null, the token was valid. We create a single-request Spring Security authentication token and attach it to the security context. This establishes identity
                // and roles so our security configuration can make decisions about what is allowed and what is not allowed.
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        // 5. Keep the chain going. The filter never stops a request prematurely.
        chain.doFilter(request, response);
    }

}
