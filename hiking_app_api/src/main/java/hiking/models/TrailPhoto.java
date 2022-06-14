package hiking.models;

import java.util.Objects;

public class TrailPhoto {

    private int photoId;
    private String photoUrl;
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
