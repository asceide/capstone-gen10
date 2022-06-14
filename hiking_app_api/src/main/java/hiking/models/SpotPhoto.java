package hiking.models;

import java.util.Objects;

public class SpotPhoto {

    private int photoId;
    private String photoUrl;
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
