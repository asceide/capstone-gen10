package hiking.repository;

import hiking.models.Trail;
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
public class TrailJdbcTemplateRepository implements TrailRepository {
    private final JdbcTemplate jdbcTemplate;

    public TrailJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Trail> findAll() {
        final String sql = "select trail_id, " +
                "name, " +
                "city, " +
                "state, " +
                "trail_length, " +
                "rating, " +
                "trail_map, " +
                "description, " +
                "app_user_id " +
                "from trail;";


        return jdbcTemplate.query(sql, new TrailMapper());

    }

    @Override
    public Trail findById(int trailId) {

        final String sql = "select trail_id, name, city, state, trail_length, rating, trail_map, description, app_user_id "
                + "from trail "
                + "where trail_id = ?;";

        Trail trail = jdbcTemplate.query(sql, new TrailMapper(), trailId).stream()
                .findFirst().orElse(null);

        if (trail!= null) {
        }

        return trail;


    }


    @Override
    public Trail add(Trail trail) {

        final String sql = "insert into trail (trail_id, name, city, state, trail_length, rating, trail_map, description, app_user_id)"
                + " values (?,?,?,?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, trail.getTrailId());
            ps.setString(2, trail.getName());
            ps.setString(3, trail.getCity());
            ps.setString(4, trail.getState());
            ps.setInt(5, trail.getTrailLength());
            ps.setString(6, trail.getRating());
            ps.setString(7, trail.getTrailMap());
            ps.setString(8, trail.getDescription());
            ps.setInt(9, trail.getAppUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        trail.setTrailId(keyHolder.getKey().intValue());
        return trail;
    }

    @Override
    public boolean update(Trail trail) {

        final String sql ="update trail set "

                + "`name` = ?, "
                + "city = ?, "
                + "state = ?, "
                + "trail_length = ?, "
                + "rating = ?, "
                + "trail_map = ?, "
                + "description = ? "
                + "where trail_id = ?;";

        int rowsAffected =  jdbcTemplate.update(sql,
                trail.getName(),
                trail.getCity(),
                trail.getState(),
                trail.getTrailLength(),
                trail.getRating(),
                trail.getTrailMap(),
                trail.getDescription(),
                trail.getTrailId()) ;
        return rowsAffected > 0 ;

    }

    @Transactional
    @Override
    public boolean deleteById(int trailId) {

        jdbcTemplate.update("delete from trail_spot where trail_id = ?;", trailId);
        jdbcTemplate.update("delete from trail_photo where trail_id = ?;", trailId);
        return jdbcTemplate.update("delete from trail where trail_id = ?;", trailId) > 0;
    }


}
