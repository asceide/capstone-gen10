package hiking.repository;

import hiking.models.SpotPhoto;
import hiking.models.TrailPhoto;
import hiking.repository.mappers.SpotPhotoMapper;
import hiking.repository.mappers.TrailPhotoMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PhotoJdbcTemplateRepository implements PhotoRepository {

    private final JdbcTemplate jdbcTemplate;

    public PhotoJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    @Override
    public List<SpotPhoto> findBySpotId(int spotId) {
        final String sql = "select p.photo_id, sp.spot_id, p.photo_url " +
                "from photo p " +
                "inner join spot_photo sp on p.photo_id = sp.photo_id " +
                "where spot_id = ?;";

        return jdbcTemplate.query(sql, new SpotPhotoMapper(), spotId);
    }

    @Override
    public List<TrailPhoto> findByTrailId(int trailId) {
        final String sql = "select p.photo_id, tp.trail_id, p.photo_url " +
                "from photo p " +
                "inner join trail_photo tp on p.photo_id = tp.photo_id " +
                "where trail_id = ?;";

        return jdbcTemplate.query(sql, new TrailPhotoMapper(), trailId);
    }

    // if the spot or trail connected to the photo does not exist, there will be a SQL error
    // add and update methods throw the DataIntegrityViolationException when this occurs
    @Override
    @Transactional
    public SpotPhoto addPhoto(SpotPhoto photo) throws DataIntegrityViolationException {
        final String sql = "insert into photo (photo_url) values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, photo.getPhotoUrl());
            return statement;
        }, keyHolder);

        if (rowsAffected > 0) {
            photo.setPhotoId(keyHolder.getKey().intValue());
            jdbcTemplate.update("insert into spot_photo (photo_id, spot_id) values (?,?);",
                    photo.getPhotoId(), photo.getSpotId());
            return photo;
        }


        return null;
    }

    @Override
    @Transactional
    public TrailPhoto addPhoto(TrailPhoto photo) throws DataIntegrityViolationException {
        final String sql = "insert into photo (photo_url) values (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, photo.getPhotoUrl());
            return statement;
        }, keyHolder);

        if (rowsAffected > 0) {
            photo.setPhotoId(keyHolder.getKey().intValue());
            jdbcTemplate.update("insert into trail_photo (photo_id, trail_id) values (?,?);",
                    photo.getPhotoId(), photo.getTrailId());
            return photo;
        }

        return null;
    }

    @Override
    @Transactional
    public boolean updatePhoto(SpotPhoto photo) throws DataIntegrityViolationException {
        jdbcTemplate.update("delete from spot_photo where photo_id = ?;", photo.getPhotoId());

        int rowsAffected =  jdbcTemplate.update("update photo set photo_url = ? where photo_id = ?;",
                    photo.getPhotoUrl(), photo.getPhotoId());

        if (rowsAffected > 0) {
            return jdbcTemplate.update("insert into spot_photo (photo_id, spot_id) values (?,?);",
                    photo.getPhotoId(), photo.getSpotId()) > 0;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean updatePhoto(TrailPhoto photo) throws DataIntegrityViolationException {
        jdbcTemplate.update("delete from trail_photo where photo_id = ?;", photo.getPhotoId());

        int rowsAffected =  jdbcTemplate.update("update photo set photo_url = ? where photo_id = ?;",
                photo.getPhotoUrl(), photo.getPhotoId());

        if (rowsAffected > 0) {
            return jdbcTemplate.update("insert into trail_photo (photo_id, trail_id) values (?,?);",
                    photo.getPhotoId(), photo.getTrailId()) > 0;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean deleteById(int photoId) {
        jdbcTemplate.update("delete from spot_photo where photo_id = ?;", photoId);
        jdbcTemplate.update("delete from trail_photo where photo_id = ?;", photoId);
        return jdbcTemplate.update("delete from photo where photo_id = ?;", photoId) > 0;
    }

}
