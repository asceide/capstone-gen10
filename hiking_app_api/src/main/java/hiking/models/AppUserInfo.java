package hiking.models;


import hiking.validation.NonBlankCityState;
import hiking.validation.NullOrNotBlankEmpty;
import hiking.validation.ValidUsState;

import javax.validation.constraints.*;

@NonBlankCityState
public class AppUserInfo {

    @Min(value = 1, message = "User ID must be set when adding user info")
    private int appUserId;

    @NullOrNotBlankEmpty
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @NullOrNotBlankEmpty
    @Size(min = 1, max = 20, message = "Last name must be between 1 and 50 characters if using")
    private String lastName;

    @NullOrNotBlankEmpty
    @Size(min = 1, max = 30, message = "City must be between 1 and 30 characters if using")
    private String city;

    @NullOrNotBlankEmpty
    @ValidUsState
    @Size(min = 2, max = 2, message = "State must be 2 characters if using")
    private String state;

    public AppUserInfo() {

    }

    public AppUserInfo(int appUserId, String firstName, String lastName, String city, String state) {
        this.appUserId = appUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    public String toString() {
        return "AppUserInfo{" +
                "appUserId=" + appUserId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
