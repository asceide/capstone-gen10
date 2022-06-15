package hiking.service;

import hiking.models.Spot;
import hiking.models.Trail;
import hiking.repository.SpotRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpotServiceTest {

    @Autowired
    SpotService service;

    @MockBean
    SpotRepository repository;

    @MockBean
    PasswordEncoder encoder;

    @Test
    void shouldFindSpots() {
        Spot expected = makeSpot();
        when(repository.findAll()).thenReturn(List.of(expected));
        List<Spot> actual = service.findAll();
        assertEquals(expected, actual.stream().findFirst().orElse(null));
    }

    @Test
    void shouldFindById() {
        Spot expected = makeSpot();
        when(repository.findById(1)).thenReturn(expected);
        Spot actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldAddWhenValid() {
        Spot expected = makeSpot();
        Spot arg = makeSpot();
        arg.setSpotId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Spot> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddSetId() {
        Spot spot = makeSpot();

        Result<Spot> result = service.add(spot);
        assertEquals(ResultType.INVALID, result.getType());

        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Spot id must not be set for add");
    }

    @Test
    void shouldNotAddInvalid() {
        Spot spot = makeSpot();
        spot.setGpsLong(300.50);

        Result<Spot> result = service.add(spot);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Longitude must be between -180.0 and 180.0");

        spot = makeSpot();

        spot.setDescription("");
        result = service.add(spot);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Description cannot be blank");
    }

    @Test
    void shouldUpdateWhenValid() {
        Spot spot = makeSpot();

        when(repository.update(spot)).thenReturn(true);

        Result<Spot> result = service.update(spot);

        assertEquals(ResultType.SUCCESS, result.getType());

    }

    @Test
    void shouldNotUpdate0Id() {
        Spot spot = makeSpot();
        spot.setSpotId(0);

        Result<Spot> result = service.update(spot);

        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Spot id must be set for update");
    }

    @Test
    void resultShouldBeNotFoundForMissingUpdate() {
        Spot spot = makeSpot();

        when(repository.update(spot)).thenReturn(false);

        Result<Spot> result = service.update(spot);

        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Spot with id 1 not found");
    }

    @Test
    void shouldUpdateRating() {
        Spot spot = makeSpot();

        when(repository.findById(1)).thenReturn(spot);

        Spot returned = makeSpot();
        returned.setRating(2);
        returned.setRatingCount(2);

        when(repository.update(ArgumentMatchers.any())).thenReturn(true);

        Result<Spot> result = service.rate(1,1);

        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(2, result.getPayload().getRating());
        assertEquals(2, result.getPayload().getRatingCount());
    }

    @Test
    void shouldDeleteExisting() {
        when(repository.deleteById(1)).thenReturn(true);

        Result<Spot> result = service.deleteById(1);

        assertTrue(result.isSuccess());
    }

    @Test
    void correctMessageForMissingDelete() {
        when(repository.deleteById(1)).thenReturn(false);

        Result<Spot> result = service.deleteById(1);

        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Spot with id 1 not found");
    }



    Spot makeSpot() {
        Trail trail = new Trail();
        trail.setTrailId(1);
        Spot spot = new Spot();
        spot.setSpotId(1);
        spot.setName("Test");
        spot.setGpsLat(45.00);
        spot.setGpsLong(50.00);
        spot.setRating(4);
        spot.setRatingCount(1);
        spot.setDescription("A test spot for service tests");
        spot.setTrails(List.of(trail));
        spot.setAppUserId(1);
        return spot;
    }
}