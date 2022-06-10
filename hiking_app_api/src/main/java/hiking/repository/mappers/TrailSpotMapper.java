package hiking.repository.mappers;

import hiking.models.TrailSpot;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrailSpotMapper implements RowMapper<TrailSpot> {
    @Override
    public TrailSpot mapRow(ResultSet rs, int rowNum) throws SQLException {
        TrailSpot trailSpot = new TrailSpot();
        trailSpot.setSpotId(rs.getInt("spot_id"));
        trailSpot.setTrailId(rs.getInt("trail_id"));
        return trailSpot;
    }
}
