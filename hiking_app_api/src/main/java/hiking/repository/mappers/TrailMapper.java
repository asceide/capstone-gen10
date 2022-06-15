package hiking.repository.mappers;

import hiking.models.Trail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrailMapper implements RowMapper<Trail> {
    @Override
    public Trail mapRow(ResultSet rs, int rowNum) throws SQLException {
        Trail trail = new Trail();
        trail.setTrailId(rs.getInt("trail_id"));
        trail.setName(rs.getString("name"));
        trail.setCity(rs.getString("city"));
        trail.setState(rs.getString("state"));
        trail.setTrailLength(rs.getInt("trail_length"));
        trail.setRating(rs.getString("rating"));
        trail.setTrailMap(rs.getString("trail_map"));
        trail.setDescription(rs.getString("description"));
        trail.setAppUserId(rs.getInt("app_user_id"));

        return trail;
    }
}
