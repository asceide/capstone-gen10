package hiking.models;

import hiking.validation.NoDuplicateUser;
import hiking.validation.ValidUsState;

import javax.validation.constraints.*;
import java.util.Objects;

@NoDuplicateUser(message = "User already exists")
public class AppUser {

    @Min(value = 0, message="ID must be 1 or greater when editing, 0 when adding")
    private int app_user_id;

    @NotNull(message="Email is required")
    @NotBlank(message="Email cannot be blank")
    @Email(message = "Email is not valid", regexp = EmailRegex.Constants.REGEX)
    @Size(max=50, message="Email cannot be longer than 50 characters")
    private String email;

    @NotNull(message="Password is required")
    @NotBlank(message="Password cannot be blank")
    @Size(max=2048, message="Password/Password Hash cannot be greater than 2048 characters")
    private String password_hash;

    @NotBlank(message="First name cannot be blank if used.")
    @Size(max=20, message="First name cannot be longer than 20 characters")
    private String first_name;

    @NotBlank(message="Last name cannot be blank if used")
    @Size(max=20, message="Last name cannot be longer than 20 characters")
    private String last_name;

    @NotBlank(message="City cannot be blank if used")
    @Size(max=30, message="City cannot be longer than 20 characters")
    private String city;

    @NotBlank(message="State cannot be blank if used")
    @Size(max=2, message="State should only use the State abbreviation")
    @ValidUsState(message="State should use a US state abbreviation")
    private String state;

    private boolean enabled=true;

    public AppUser(){

    }

    public AppUser(int app_user_id, String email, String password_hash, String first_name, String last_name, String city, String state, boolean enabled) {
        this.app_user_id = app_user_id;
        this.email = email;
        this.password_hash = password_hash;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.state = state;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(int app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return email.equals(appUser.email) && password_hash.equals(appUser.password_hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password_hash);
    }
}
