package hiking.repository;

import hiking.models.AppUserRole;

import java.util.List;

public interface AppUserRoleRepository {
    List<AppUserRole> findAll();
    boolean update(AppUserRole user);
}
