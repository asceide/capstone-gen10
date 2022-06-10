package hiking.repository;

import hiking.models.AppUser;

import java.util.List;

public interface AppUserRepository {

    List<AppUser> findAllUsers();
    AppUser findByEmail(String email);
    AppUser add(AppUser user);
    boolean update(AppUser user);
    boolean delete(int id);

}
