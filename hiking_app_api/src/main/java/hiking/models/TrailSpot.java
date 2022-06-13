package hiking.models;

import javax.validation.constraints.Min;

public class TrailSpot {

    @Min(value = 0, message = "Trail id must be greater than or equal to 0")
    private int trailId;

    @Min(value = 0, message = "Spot id must be greater than or equal to 0")
    private int spotId;


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
}
