package hiking.repository.mappers;

import hiking.models.AppUserRole;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class AppUserRoleMapper implements RowMapper<AppUserRole> {

    @Override
    public AppUserRole mapRow(ResultSet rs, int i) throws SQLException {
        return new AppUserRole(
                rs.getInt("app_user_id"),
                rs.getString("email"),
                rs.getString("role_name"));
    }
}
