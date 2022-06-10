package hiking.repository.mappers;

import hiking.models.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppUserMapper implements RowMapper<AppUser> {

    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException{
        AppUser appUser = new AppUser();
        appUser.setApp_user_id(rs.getInt("app_user_id"));
        appUser.setEmail(rs.getString("email"));
        appUser.setPassword_hash(rs.getString("password_hash"));
        appUser.setFirst_name(rs.getString("first_name"));
        appUser.setLast_name(rs.getString("last_name"));
        appUser.setCity(rs.getString("city"));
        appUser.setState(rs.getString("state"));
        appUser.setEnabled(rs.getBoolean("enabled"));

        return appUser;
    }
}
