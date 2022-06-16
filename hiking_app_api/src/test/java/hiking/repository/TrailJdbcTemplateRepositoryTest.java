package hiking.repository;

import hiking.models.Spot;
import hiking.models.Trail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TrailJdbcTemplateRepositoryTest {

    @Autowired
    TrailJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @MockBean
    PasswordEncoder encoder;

    @BeforeEach
    void setUp() {knownGoodState.set();}

    @Test
    void shouldfindAll() {
        List<Trail> trails = repository.findAll();

        assertNotNull(trails);

        assertTrue(trails.size() == 2);
    }

    @Test
    void shouldFindByID(){
        Trail actual = repository.findById(2);
        assertEquals("Cool test trail", actual.getName());
    }


    @Test
    void shouldAddTrail(){
        Trail trail = new Trail();
        trail.setTrailId(3);
        trail.setName("Dish Hike");
        trail.setCity("Stanford");
        trail.setState("CA");
        trail.setTrailLength(4);
        trail.setRating("Easy");
        trail.setTrailMap("");
        trail.setDescription("Cool hike");
        trail.setAppUserId(2);

        Trail actual = repository.add(trail);
        assertNotNull(actual);
        assertEquals(3, actual.getTrailId());

    }

    @Test
    void shouldUpdate() {
        Trail trail = new Trail();
        trail.setTrailId(2);
        trail.setName("Cooler Name");
        trail.setCity("Palo Alto");
        trail.setState("CA");
        trail.setTrailLength(4);
        trail.setRating("M");
        trail.setTrailMap("tt");
        trail.setDescription("description updated");
        trail.setAppUserId(2);

        repository.update(trail);

        Trail actual = repository.findById(2);
        assertTrue(actual.getName().contains("Cooler"));

    }



    @Test
    void shouldDeleteAgency() {
        repository.deleteById(3);
        assertNull(repository.findById(3));
    }
}