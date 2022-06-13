package hiking.repository.mappers;

import hiking.models.AppUserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppUserInfoMapper implements RowMapper<AppUserInfo> {

    @Override
    public AppUserInfo mapRow(ResultSet rs, int i) throws SQLException{
        return new AppUserInfo(rs.getInt("app_user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("city"),
                rs.getString("state"));
    }
}
