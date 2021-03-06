package hiking.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    This class is also annotated with @ConditionalOnWebApplication, in order to allow
    tests to be run without a web environment.
    We do this because Spring Security ignores the fact that we are trying to do the tests
    without starting a web app, and it tries to load the Security Suite into a web context which
    subsequently fails.
 */
@ConditionalOnWebApplication
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Add JwtConverter as a dependency
    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // This method allows configuring web-based security for specific http requests.
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // Not using HTML forms, so CSRF is disabled
        http.csrf().disable();

        // Spring Security allows CORS related requests such as preflight checks
        http.cors();

        // antMatcher() order matters because they are evaluated in the order they are added
        http.authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/refresh_token").authenticated()
                // ***** DONT TOUCH THE ABOVE *******/
                .antMatchers(HttpMethod.GET, "/api/spot").permitAll()
                .antMatchers(HttpMethod.POST, "/api/spot").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/spot/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/spot/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/spot/rate/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/spot/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/trail/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/trail").permitAll()
                .antMatchers(HttpMethod.POST, "/api/trail").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/trail/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/trail/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/photo").permitAll()
                .antMatchers(HttpMethod.POST, "/api/photo").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/photo/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/photo/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/trail").permitAll()
                .antMatchers(HttpMethod.GET, "/api/trail/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/trail").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/trail/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/trail/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/user/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/getinfo").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/id").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/").hasAnyRole("USER", "ADMIN") // Can only be done by authenticated users in the future
                .antMatchers(HttpMethod.PUT, "/api/user/").hasAnyRole("USER", "ADMIN") // Can only be done by authenticated users in the future
                .antMatchers(HttpMethod.DELETE, "/api/user/**").hasRole("ADMIN") // Can only be done by admins in the future
                .antMatchers(HttpMethod.GET,"/api/user/authorities").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/api/user/authorities").hasRole("ADMIN")
                .antMatchers("/**").denyAll()
                .and()// Deny all other requests
                .addFilter(new JwtRequestFilter(authenticationManager(), converter)) // Since Spring Security is mostly Spring MVC filters, we model our custom JWT authentication with filters. The filter intercepts the HTTP request early and determines authentication and authorization.
                .sessionManagement()// The antMatchers apply their rules after the filter is complete.
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /*
        You can configure CORS globally instead of controller by controller with @CrossOrigin.
        Use a global WebMvcConfigurer with a Cors registry
        This is because when CORS is managed per controller, the permissions response does not contain the proper CORS header, so the HTTP client returns a CORS error instead of acknowledging the request.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        // Configure CORS globally as opposed to per controller
        // Still, it can be combined with @CrossOrigin
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Allow all origins, methods and headers. Adjust in the future.
            }
        };
    }

    // A new BCryptPasswordEncoder is created and provided to Spring DI and consumed via Autowired above.
    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

}
