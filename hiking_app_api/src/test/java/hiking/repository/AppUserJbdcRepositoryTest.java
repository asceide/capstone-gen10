package hiking.repository;

import hiking.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserJbdcRepositoryTest {

    @MockBean
    PasswordEncoder encoder;

    @Autowired
    AppUserJbdcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindThree(){
        List<String> emails = repository.findAllUsers();
        assertEquals(3, emails.size());
        assertTrue(emails.get(2).contains("apple"));
        assertTrue(emails.get(1).contains("dev-10"));;
    }

    @Test
    void shouldFindApple(){
        AppUser user = repository.findByEmail("test@apple.com");

        assertNotNull(user);
        assertEquals(3, user.getAppUserId());
        assertEquals("test@apple.com", user.getUsername());
    }

    @Test
    void shouldFindDev10() {
        AppUser user = repository.findByEmail("test@dev-10.com");

        assertNotNull(user);
        assertEquals(2, user.getAppUserId());
        assertEquals("test@dev-10.com", user.getUsername());
    }

    @Test
    void shouldCreateUser() {
        AppUser user = new AppUser(0, "test@microsoft.com", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", true, List.of("USER"));
        AppUser createdUser = repository.create(user);

        assertNotNull(createdUser);
        assertEquals(4, createdUser.getAppUserId());
        assertEquals("test@microsoft.com", createdUser.getUsername());
    }

    @Test
    void shouldUpdateUser() {
        AppUser user = new AppUser(1, "test@tesla.com", "$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa", true, List.of("ADMIN"));
        assertTrue(repository.update(user));
    }

    @Test
    void shouldDeleteUser(){
        assertTrue(repository.delete(4));
    }

    @Test
    void shouldDisable2And3(){
        assertTrue(repository.disable(2));
        assertTrue(repository.disable(3));
    }

    @Test
    void shouldEnable1(){
        assertTrue(repository.enable(1));
    }
}