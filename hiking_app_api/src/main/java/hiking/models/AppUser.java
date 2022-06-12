package hiking.models;

import hiking.validation.NoDuplicateUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoDuplicateUser
public class AppUser extends User {

    private static final String AUTHORITY_PREFIX = "ROLE_";

    @Min(value = 0, message = "User ID must be greater than 0 if updating, 0 if adding")
    private int appUserId;

    private List<String> roles = new ArrayList<>();

    public AppUser(int appUserId, String username, String password, boolean enabled, List<String> roles){
        super(username, password, enabled, true, true, true, convertRolesToAuthorities(roles));
        this.appUserId = appUserId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public static List<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
        for(String role : roles){
            Assert.isTrue(!role.startsWith(AUTHORITY_PREFIX),
                    () -> String.format("Role '%s' cannot start with '%s'", role, AUTHORITY_PREFIX));
            authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role));
        }

        return authorities;
    }

    public static List<String> convertAuthoritiesToRoles(Collection<GrantedAuthority> authorities){
        return authorities.stream()
                .map(authority -> authority.getAuthority().substring(AUTHORITY_PREFIX.length()))
                .collect(Collectors.toList());
    }


}
