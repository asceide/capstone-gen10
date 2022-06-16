package hiking.models;

import javax.validation.constraints.*;

public class Trail {

    @Min(value = 0, message = "Trail id must be greater than or equal to 0")
    private int trailId;

    private int spotId;


    @NotNull(message="Name is required")
    @NotBlank(message="Name cannot be blank")
    @Size(max=100, message="Name cannot be longer than 100 characters")
    private String name;

    @NotNull(message="City is required")
    @NotBlank(message="City cannot be blank")
    @Size(max=100, message="City cannot be longer than 100 characters")
    private String city;

    @NotNull(message="State is required")
    @NotBlank(message="State cannot be blank")
    @Size(max=20, message="State cannot be longer than 20 characters")
    private String state;

    @Min(value=0, message="Trail Lenght must not be less than 0")
    private int trailLength;


    private String rating;


    private String trailMap;

    @NotBlank(message="Description cannot be blank")
    @Size(max=1000, message="Description cannot be longer than 1000 characters")
    private String description;

    @Min(value=0, message="User id must be greater than or equal to 0")
    private int appUserId;




    public int getTrailId() {
        return trailId;
    }

    public void setTrailId(int trailId) {
        this.trailId = trailId;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getTrailLength() {
        return trailLength;
    }

    public void setTrailLength(int trailLength) {
        this.trailLength = trailLength;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTrailMap() {
        return trailMap;
    }

    public void setTrailMap(String trailMap) {
        this.trailMap = trailMap;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }
}
