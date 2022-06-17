package hiking.repository.mappers;

import hiking.models.SpotPhoto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpotPhotoMapper implements RowMapper<SpotPhoto> {
    @Override
    public SpotPhoto mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoId(rs.getInt("photo_id"));
        photo.setPhotoUrl(rs.getString("photo_url"));
        photo.setSpotId(rs.getInt("spot_id"));
        return photo;
    }
}
