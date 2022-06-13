package hiking.repository;

import hiking.models.AppUser;
import hiking.models.AppUserInfo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.transaction.BeforeTransaction;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserInfoJdbcRepositoryTest {

    @Autowired
    AppUserInfoJdbcRepository repository;

    @Autowired
    AppUserJbdcRepository userRepository;

    @MockBean
    PasswordEncoder encoder;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }


    @Test
    void shouldFindSteve() {
        AppUser user = userRepository.findByEmail("test@apple.com");

        assertNotNull(user);

        AppUserInfo userInfo = repository.findByAppUserId(user.getAppUserId());

        assertNotNull(userInfo);
        assertEquals("Steve", userInfo.getFirstName());
        assertEquals("CA", userInfo.getState());

    }

    @Test
    void shouldNotFindAnyone(){
        AppUserInfo userInfo = repository.findByAppUserId(55);
        assertNull(userInfo);
    }

    @Test
    void shouldCreateUser() {
         userRepository.create(new AppUser(0, "test@microsoft.com", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", true, List.of("USER")));

        int userId  = userRepository.findByEmail("test@microsoft.com").getAppUserId();

        assertTrue(userId>0);

        AppUserInfo userInfo = new AppUserInfo(userId, "Bill", "Gates", "Seattle", "WA");

        assertNotNull(repository.add(userInfo));
    }

    @Test
    void shouldNotCreateUser() {
        AppUserInfo userInfo = new AppUserInfo(0, "Bill", "Gates", "Seattle", "WA");

        // This should fail and the DataIntegrityViolationException should be thrown because the is no user with an
        // unknown ID in the UserInfo table. This is a test for the foreign key constraint.
        assertNull(repository.add(userInfo));
        userInfo.setAppUserId(55);
        // Same here. We are catching it in the repository and just returning a null.
        assertNull(repository.add(userInfo));
    }


    @Test
    void shouldUpdatePatrick(){
        AppUser user = userRepository.findByEmail("test@gmail.com");

        assertNotNull(user);

        AppUserInfo userInfo = repository.findByAppUserId(user.getAppUserId());

        assertEquals("Patrick", userInfo.getFirstName());

        userInfo.setCity("Charlotte");
        userInfo.setState("NC");

        assertTrue(repository.update(userInfo));
    }

    @Test
    void shouldNotUpdate(){
        AppUserInfo userInfo = new AppUserInfo(0, "Patrick", "Gates", "Seattle", "WA");

        assertFalse(repository.update(userInfo));
    }


    @Test
    void shouldDeleteUser() {
        AppUser user = userRepository.findByEmail("test@microsoft.com");

        assertNotNull(user);

        assertTrue(repository.delete(user.getAppUserId()));
        assertTrue(userRepository.delete(user.getAppUserId()));

    }

    @Test
    void shouldNotDeleteUser() {
        assertFalse(repository.delete(55));
    }



}