package hiking.service;



import hiking.models.Trail;

import hiking.repository.TrailRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TrailServiceTest {

    @Autowired
    TrailService service;

    @MockBean
    TrailRepository repository;

    @MockBean
    PasswordEncoder encoder;


    @Test
    void shouldFindTrails() {
        List<Trail> actual = service.findAll();
        assertTrue(actual.size() >=1);
    }

    @Test
    void shouldFindHazel() {
        // pass-through test, probably not useful
        Trail expected = makeTrail();
        when(repository.findById(5)).thenReturn(expected);
        Trail actual = service.findById(5);
        assertEquals(expected, actual);
    }

    @Test
    void shouldAdd() {
        Trail trail = makeTrail();
        Trail mockOut = makeTrail();
        mockOut.setTrailId(1);

        when(repository.add(trail)).thenReturn(mockOut);

        Result<Trail> actual = service.add(trail);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldUpdate() {
        Trail trail = makeTrail();
        trail.setTrailId(1);

        when(repository.update(trail)).thenReturn(true);

        Result<Trail> actual = service.update(trail);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }


    Trail makeTrail() {

        Trail trail = new Trail();
        trail.setTrailId(5);
        trail.setCity("TCity");
        trail.setState("TState");
        trail.setTrailLength(4);
        trail.setRating("E");
        trail.setTrailMap("");
        trail.setDescription("");
        trail.setAppUserId(2);

        return trail;
    }
}