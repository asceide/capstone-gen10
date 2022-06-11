package hiking.models;

import javax.validation.constraints.*;

public class Spot {

    @Min(value=0, message="Id must be greater than or equal to 0")
    private int spotId;

    @NotNull(message="Name is required")
    @NotBlank(message="Name cannot be blank")
    @Size(max=100, message="Name cannot be longer than 100 characters")
    private String name;

    @Min(value=0, message="Photo id must be greater than or equal to 0")
    private int photoId;

    @DecimalMin(value="-90.0000000000", message="Latitude must be between -90.0 and 90.0")
    @DecimalMax(value="90.0000000000", message="Latitude must be between -90.0 and 90.0")
    private double gpsLat;

    @DecimalMin(value="-180.0000000000", message="Longitude must be between -180.0 and 180.0")
    @DecimalMax(value="180.0000000000", message="Longitude must be between -180.0 and 180.0")
    private double gpsLong;

    @Min(value=0, message="Rating must not be less than 0")
    @Max(value=5, message="Rating must not be greater than 5")
    private int rating;

    @NotBlank(message="Description cannot be blank")
    @Size(max=1000, message="Description cannot be longer than 1000 characters")
    private String description;

    @Min(value=0, message="User id must be greater than or equal to 0")
    private int appUserId;

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

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public double getGpsLat() {
        return gpsLat;
    }

    public void setGpsLat(double gpsLat) {
        this.gpsLat = gpsLat;
    }

    public double getGpsLong() {
        return gpsLong;
    }

    public void setGpsLong(double gpsLong) {
        this.gpsLong = gpsLong;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
