package hiking.repository;


import hiking.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    AppUserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void findAllUsers() {
        List<AppUser> actual =  repository.findAllUsers();

        assertNotNull(actual);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldFindUserByEmail(){
        AppUser actual = repository.findByEmail("test@gmail.com");

        assertNotNull(actual);
        assertTrue(actual.getFirst_name().equalsIgnoreCase("Patrick"));
    }

    @Test
    void shouldAdd(){
        AppUser toAdd = makeUser();
        AppUser actual = repository.add(toAdd);

        assertNotNull(actual);
    }

    @Test
    void shouldUpdate(){
        AppUser toUpdate = makeUser();
        toUpdate.setApp_user_id(1);

        assertTrue(repository.update(toUpdate));
    }

    @Test
    void shouldNotUpdate(){
        AppUser toUpdate = makeUser();
        toUpdate.setApp_user_id(4);

        assertFalse(repository.update(toUpdate));
    }

    @Test
    void shouldDelete(){
        assertTrue(repository.delete(1));
    }

    @Test
    void shouldNotDelete(){
        assertFalse(repository.delete(4));
    }

    private AppUser makeUser(){
        AppUser user = new AppUser();
        user.setFirst_name("Patrick");
        user.setLast_name("Smith");
        user.setEmail("test@microsoft.com");
        user.setPassword_hash("password");
        user.setCity("Seattle");
        user.setState("WA");
        user.setEnabled(true);
        return user;
    }
}