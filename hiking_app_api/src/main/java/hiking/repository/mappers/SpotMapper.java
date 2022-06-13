package hiking.repository.mappers;

import hiking.models.Spot;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpotMapper implements RowMapper<Spot> {
    @Override
    public Spot mapRow(ResultSet rs, int rowNum) throws SQLException {
        Spot spot = new Spot();
        spot.setSpotId(rs.getInt("spot_id"));
        spot.setName(rs.getString("name"));
        spot.setGpsLat(rs.getDouble("gps_lat"));
        spot.setGpsLong(rs.getDouble("gps_long"));
        spot.setRating(rs.getInt("rating"));
        spot.setDescription(rs.getString("description"));
        spot.setAppUserId(rs.getInt("app_user_id"));
        spot.setRatingCount(rs.getInt("rating_count"));
        return spot;
    }
}
