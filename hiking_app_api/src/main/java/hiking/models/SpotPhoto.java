package hiking.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class SpotPhoto {

    @Min(value=0, message="Id must be greater than or equal to 0")
    private int photoId;

    @Pattern(message = "Photo url must be a url",
            regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_\\+.~#?&\\/\\/=]*)")
    private String photoUrl;

    @Min(value=0, message="Id must be greater than or equal to 0")
    private int spotId;

    public int getPhotoId() {return photoId;}

    public void setPhotoId(int photoId) {this.photoId = photoId;}

    public String getPhotoUrl() {return photoUrl;}

    public void setPhotoUrl(String photoUrl) {this.photoUrl = photoUrl;}

    public int getSpotId() {return spotId;}

    public void setSpotId(int spotId) {this.spotId = spotId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotPhoto photo = (SpotPhoto) o;
        return photoId == photo.photoId && spotId == photo.spotId && Objects.equals(photoUrl, photo.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, photoUrl, spotId);
    }
}
