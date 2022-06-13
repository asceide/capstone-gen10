package hiking.repository;

import hiking.models.Spot;
import hiking.models.Trail;
import hiking.repository.mappers.SpotMapper;
import hiking.repository.mappers.TrailMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SpotJdbcTemplateRepository implements SpotRepository {

    private final JdbcTemplate jdbcTemplate;

    public SpotJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}


    @Override
    public List<Spot> findAll() {
        final String sql = "select spot_id, " +
                "name, " +
                "gps_lat, " +
                "gps_long, " +
                "rating, " +
                "description, " +
                "app_user_id " +
                "from spot;";

        List<Spot> spots = jdbcTemplate.query(sql, new SpotMapper());
        for (Spot s : spots) {
            addTrails(s);
        }
        return spots;
    }

    @Override
    public Spot findById(int spotId) {
        final String sql = "select spot_id, " +
                "name, " +
                "gps_lat, " +
                "gps_long, " +
                "rating, " +
                "description, " +
                "app_user_id " +
                "from spot " +
                "where spot_id = ?;";
        Spot spot = jdbcTemplate.query(sql, new SpotMapper(), spotId).stream()
                .findFirst().orElse(null);

        if (spot != null) {
            addTrails(spot);
        }

        return spot;
    }

    @Transactional
    @Override
    public Spot add(Spot spot) {

        final String sql = "insert into spot (name, gps_lat, gps_long, rating, description, app_user_id) " +
                "values (?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, spot.getName());
            statement.setDouble(2, spot.getGpsLat());
            statement.setDouble(3, spot.getGpsLong());
            statement.setInt(4, spot.getRating());
            statement.setString(5, spot.getDescription());
            statement.setInt(6, spot.getAppUserId());
            return statement;
        }, keyHolder);

        if (rowsAffected > 0) {
            spot.setSpotId(keyHolder.getKey().intValue());
            updateTrails(spot);
            return spot;
        }
        return null;
    }

    @Transactional
    @Override
    public boolean update(Spot spot) {

        final String sql = "update spot set " +
                "name = ?, " +
                "gps_lat = ?, " +
                "gps_long = ?, " +
                "rating = ?, " +
                "description = ?, " +
                "app_user_id = ? " +
                "where spot_id = ?;";

        int rowsAffected = jdbcTemplate.update(sql,
                spot.getName(),
                spot.getGpsLat(),
                spot.getGpsLong(),
                spot.getRating(),
                spot.getDescription(),
                spot.getAppUserId(),
                spot.getSpotId());

        if (rowsAffected > 0 ) {
            updateTrails(spot);
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public boolean deleteById(int spotId) {
        jdbcTemplate.update("delete from trail_spot where spot_id = ?;", spotId);
        return jdbcTemplate.update("delete from spot where spot_id = ?;", spotId) > 0;
    }

    private void addTrails(Spot spot) {
        final String sql = "select t.trail_id, " +
                "t.name, " +
                "t.city, " +
                "t.state, " +
                "t.trail_length, " +
                "t.rating, " +
                "t.trail_map, " +
                "t.description, " +
                "t.app_user_id " +
                "from trail t " +
                "inner join trail_spot ts on t.trail_id = ts.trail_id " +
                "where ts.spot_id = ?;";
        List<Trail> trails = jdbcTemplate.query(sql, new TrailMapper(), spot.getSpotId());
        spot.setTrails(trails);

    }

    private void updateTrails(Spot spot) {
        jdbcTemplate.update("delete from trail_spot where spot_id = ?;", spot.getSpotId());
        for (Trail t : spot.getTrails()) {
            jdbcTemplate.update("insert into trail_spot (trail_id, spot_id) values (?,?);",
                    t.getTrailId(), spot.getSpotId());
        }
    }
}
