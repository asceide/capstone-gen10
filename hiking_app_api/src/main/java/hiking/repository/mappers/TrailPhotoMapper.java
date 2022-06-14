package hiking.repository.mappers;


import hiking.models.Trail;
import hiking.models.TrailPhoto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrailPhotoMapper implements RowMapper<TrailPhoto> {
    @Override
    public TrailPhoto mapRow(ResultSet rs, int rowNum) throws SQLException {
        TrailPhoto photo = new TrailPhoto();
        photo.setPhotoId(rs.getInt("photo_id"));
        photo.setPhotoUrl(rs.getString("photo_url"));
        photo.setTrailId(rs.getInt("trail_id"));
        return photo;
    }
}