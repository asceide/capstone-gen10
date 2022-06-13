package hiking.repository;

import hiking.models.SpotPhoto;
import hiking.models.Trail;
import hiking.models.TrailPhoto;
import hiking.repository.mappers.SpotPhotoMapper;
import hiking.repository.mappers.TrailPhotoMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PhotoJdbcTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    public PhotoJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public List<SpotPhoto> findBySpotId(int spotId) {
        final String sql = "select photo_id, spot_id, photo_url from spot_photo where spot_id = ?;";

        return jdbcTemplate.query(sql, new SpotPhotoMapper(), spotId);
    }

    public List<TrailPhoto> findByTrailId(int trailId) {
        final String sql = "select photo_id, trail_id, photo_url from trail_photo where trail_id = ?;";

        return jdbcTemplate.query(sql, new TrailPhotoMapper(), trailId);
    }

    public SpotPhoto addSpotPhoto(SpotPhoto photo) {
        final String sql = "insert into spot_photo (photo_url, spot_id) values (?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, photo.getPhotoUrl());
            statement.setInt(2, photo.getSpotId());
            return statement;
        }, keyHolder);

        if (rowsAffected > 0) {
            photo.setPhotoId(keyHolder.getKey().intValue());
            return photo;
        }

        return null;
    }

    public TrailPhoto addTrailPhoto(TrailPhoto photo) {
        final String sql = "insert into trail_photo (photo_url, trail_id) values (?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, photo.getPhotoUrl());
            statement.setInt(2, photo.getTrailId());
            return statement;
        }, keyHolder);

        if (rowsAffected > 0) {
            photo.setPhotoId(keyHolder.getKey().intValue());
            return photo;
        }

        return null;
    }

    public boolean updateSpotPhoto(SpotPhoto photo) {
        final String sql = "update spot_photo set photo_url = ?, spot_id = ? where photo_id = ?;";

        return jdbcTemplate.update(sql, photo.getPhotoUrl(), photo.getSpotId(), photo.getPhotoId()) > 0;
    }

    public boolean updateTrailPhoto(TrailPhoto photo) {
        final String sql = "update trail_photo set photo_url = ?, trail_id = ? where photo_id = ?;";

        return jdbcTemplate.update(sql, photo.getPhotoUrl(), photo.getTrailId(), photo.getPhotoId()) > 0;
    }

    public boolean deleteSpotPhoto(int photoId) {
        return jdbcTemplate.update("delete from spot_photo where photo_id = ?;", photoId) > 0;
    }

    public boolean deleteTrailPhoto(int photoId) {
        return jdbcTemplate.update("delete from trail_photo where photo_id = ?;", photoId) > 0;
    }
}
