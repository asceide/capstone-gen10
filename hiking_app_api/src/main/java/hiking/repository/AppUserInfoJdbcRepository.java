package hiking.repository;

import hiking.models.AppUserInfo;
import hiking.repository.mappers.AppUserInfoMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class AppUserInfoJdbcRepository implements AppUserInfoRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserInfoJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public AppUserInfo findByAppUserId(int appUserId) {
        final String sql = "Select app_user_id, first_name, last_name, city, state "
                + "from app_user_info where app_user_id = ?";
        return jdbcTemplate.query(sql, new AppUserInfoMapper(), appUserId).stream().findFirst().orElse(null);
    }

    @Override
    public AppUserInfo add(AppUserInfo info){
        final String sql = "insert into app_user_info (app_user_id, first_name, last_name, city, state) " +
                "values (?, ?, ?, ?, ?)";
        int rowsAffected=0;

        // If user info is added and the ID is not found in the app_user database,
        // then an exception is thrown because of foreign key constraints - Its not found.
        try {
            rowsAffected = jdbcTemplate.update(sql, info.getAppUserId(), info.getFirstName(), info.getLastName(),
                    info.getCity(), info.getState());
        }catch(DataIntegrityViolationException e){
            System.out.println("The user ID was not found in the app_user table");
            return null;
        }

        if (rowsAffected <= 0) {
            return null;
        }

        return info;
    }

    @Override
    public boolean update(AppUserInfo info) {
        final String sql = "update app_user_info set first_name = ?, last_name = ?, city = ?, state = ? "
                + "where app_user_id = ?";

        return jdbcTemplate.update(sql, info.getFirstName(), info.getLastName(), info.getCity(), info.getState(), info.getAppUserId()) > 0;
    }

    @Override
    public boolean delete(int appUserId) {
        final String sql = "delete from app_user_info where app_user_id = ?";
        return jdbcTemplate.update(sql, appUserId) > 0;
    }
}
