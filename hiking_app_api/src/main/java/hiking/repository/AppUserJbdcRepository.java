package hiking.repository;

import hiking.models.AppUser;
import hiking.repository.mappers.AppUserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
public class AppUserJbdcRepository implements AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserJbdcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> findAllUsers(){
        final String sql = "select email from app_user";

        List<String> finalList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("email"));

        Collections.reverse(finalList);
        return finalList;
    }

    @Override
    @Transactional
    public AppUser findByEmail(String email) {
        List<String> roles = getRolesByUsername(email);
        final String sql = "select app_user_id, email, password_hash, enabled "
                + "from app_user where email = ?";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), email).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional
    public AppUser create (AppUser user) {
        final String sql = "insert into app_user (email, password_hash) " +
                "values (?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getPassword());
                    return ps;
                }, keyHolder);
        if (rowsAffected <= 0) {
            return null;
        }

        user.setAppUserId(keyHolder.getKey().intValue());

        updateRoles(user);

        return user;
    }

    @Override
    @Transactional
    public boolean update (AppUser user){
        final String sql = "update app_user set "
                + "email = ?, enabled = ? "
                + "where app_user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, user.getUsername(), !user.isEnabled(), user.getAppUserId());

        if(rowsAffected > 0){
            updateRoles(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete (int id) {
        final String sql = "delete from app_user_role where app_user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected > 0) {
            final String sql2 = "delete from app_user where app_user_id = ?";
            rowsAffected = jdbcTemplate.update(sql2, id);
            return rowsAffected > 0;
        }
        return false;
    }

    @Override
    public boolean disable (int id) {
        final String sql = "update app_user set enabled = false where app_user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    @Override
    public boolean enable (int id) {
        final String sql = "update app_user set enabled = true where app_user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    private void updateRoles(AppUser user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getAppUserId());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (String role : AppUser.convertAuthoritiesToRoles(authorities)) {
            String sql = "insert into app_user_role (app_user_id, app_role_id) "
                    + "select ?, app_role_id from app_role where role_name = ?;";
            jdbcTemplate.update(sql, user.getAppUserId(), role);
        }
    }


    private List<String> getRolesByUsername(String email) {
        final String sql = "select r.role_name "
                + "from app_user_role ur "
                + "inner join app_role r on r.app_role_id = ur.app_role_id "
                + "inner join app_user u on u.app_user_id = ur.app_user_id "
                + "where u.email = ?";

        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("role_name"), email);
    }
}

