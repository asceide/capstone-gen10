package hiking.repository;

import hiking.models.AppUser;
import hiking.repository.mappers.AppUserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository{

    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public List<AppUser> findAllUsers(){
        String sql = "select app_user_id, email, password_hash, first_name, last_name, city, state, enabled "
                + "from app_user ";
        return jdbcTemplate.query(sql, new AppUserMapper());
    }

    @Override
    public AppUser findByEmail(String email){
        String sql = "select app_user_id, email, password_hash, first_name, last_name, city, state, enabled "
                + "from app_user "
                + "where email = ? ";
        return jdbcTemplate.query(sql, new AppUserMapper(), email).stream().findFirst().orElse(null);
    }

    @Override
    public AppUser add(AppUser user) {
        String sql = "insert into app_user (email, password_hash, first_name, last_name, city, state, enabled) "
                + "values (?, ?, ?, ?, ?, ?, ?) ";

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            java.sql.PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword_hash());
            ps.setString(3, user.getFirst_name());
            ps.setString(4, user.getLast_name());
            ps.setString(5, user.getCity());
            ps.setString(6, user.getState());
            ps.setBoolean(7, user.isEnabled());
            return ps;
        }, holder);

        if (rowsAffected > 0){
            user.setApp_user_id(holder.getKey().intValue());
            return user;
        }

        return null;
    }

    @Override
    public boolean update(AppUser user){
        String sql = "update app_user set email = ?, password_hash = ?, first_name = ?, last_name = ?, city = ?, state = ?, enabled = ? "
                + "where app_user_id = ? ";
        int rowsAffected = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword_hash(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getCity(),
                user.getState(),
                user.isEnabled(),
                user.getApp_user_id());
        return rowsAffected > 0;
    }

    @Override
    public boolean delete(int id){
        String sql = "delete from app_user where app_user_id = ? ";
        return jdbcTemplate.update(sql,id) > 0;
    }
}


