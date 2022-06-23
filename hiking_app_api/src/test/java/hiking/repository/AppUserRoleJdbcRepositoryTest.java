package hiking.repository;

import hiking.models.AppUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserRoleJdbcRepositoryTest {

    @MockBean
    PasswordEncoder encoder;

    @Autowired
    AppUserRoleJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldNotBeNull() {
        assertNotNull(repository.findAll());
        repository.findAll().stream().forEach(item -> {
            assertNotNull(item.getAppUserId());
            assertNotNull(item.getUsername());
            assertNotNull(item.getRole());
        });
    }

    @Test
    void shouldUpdate() {
        AppUserRole user = createAppUserRole();
        assertTrue(repository.update(user));
    }

    private AppUserRole createAppUserRole() {
        AppUserRole appUserRole = new AppUserRole();
        appUserRole.setAppUserId(2);
        appUserRole.setUsername("test");
        appUserRole.setRole("ADMIN");
        return appUserRole;
    }


}