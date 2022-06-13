package hiking.repository;

import hiking.models.AppUser;

import java.util.List;

public interface AppUserRepository {

    List<String> findAllUsers();
    AppUser findByEmail(String email);
    AppUser create(AppUser user);
    boolean update(AppUser user);
    boolean delete(int id);
    boolean disable(int id);
    boolean enable(int id);
}
