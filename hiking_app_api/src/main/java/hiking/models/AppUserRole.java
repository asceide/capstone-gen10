package hiking.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AppUserRole {

    @Min(value=1, message="AppUserRole id must be greater than 0")
    int appUserId;
    String username;

    @NotBlank(message="Role name must not be blank")
    @NotNull(message="Role name must be specified")
    String role;

    public AppUserRole() {

    }

    public AppUserRole(int appUserId, String username, String role) {
        this.appUserId = appUserId;
        this.username = username;
        this.role = role;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
