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
        return trail;
    }
}
