package hiking.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class TrailPhoto {

    @Min(value=0, message="Id must be greater than or equal to 0")
    private int photoId;

    @NotNull(message = "Photo url is required")
    @NotBlank(message = "Photo url is required")
//    @Pattern(message = "Photo url must be a url",
//            regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_\\+.~#?&\\/\\/=]*)")
    private String photoUrl;

    @Min(value=0, message="Id must be greater than or equal to 0")
    private int trailId;

    public int getPhotoId() {return photoId;}

    public void setPhotoId(int photoId) {this.photoId = photoId;}

    public String getPhotoUrl() {return photoUrl;}

    public void setPhotoUrl(String photoUrl) {this.photoUrl = photoUrl;}

    public int getTrailId() {
        return trailId;
    }

    public void setTrailId(int trailId) {this.trailId = trailId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrailPhoto that = (TrailPhoto) o;
        return photoId == that.photoId && trailId == that.trailId && Objects.equals(photoUrl, that.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, photoUrl, trailId);
    }
}
