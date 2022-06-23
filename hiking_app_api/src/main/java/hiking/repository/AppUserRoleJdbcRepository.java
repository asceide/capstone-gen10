package hiking.repository;

import hiking.models.AppUserRole;
import hiking.repository.mappers.AppUserRoleMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppUserRoleJdbcRepository implements AppUserRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserRoleJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AppUserRole> findAll() {
        final String sql = "select au.app_user_id, email, role_name "+
                "from app_user_role aur "+
                "inner join app_user au on au.app_user_id = aur.app_user_id "+
                "inner join app_role r on r.app_role_id = aur.app_role_id";

        return jdbcTemplate.query(sql, new AppUserRoleMapper());
    }

    @Override
    public boolean update(AppUserRole user) {
        final String sql = "update app_user_role set app_role_id = (select app_role_id from app_role where role_name = ?) where app_user_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, user.getRole(), user.getAppUserId());
        return rowsAffected>0;
    }

}

