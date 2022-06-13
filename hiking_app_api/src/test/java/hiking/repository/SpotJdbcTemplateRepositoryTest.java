package hiking.repository;

import hiking.models.Spot;
import hiking.models.Trail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpotJdbcTemplateRepositoryTest {

   @Autowired
   SpotJdbcTemplateRepository repository;

   @Autowired
   KnownGoodState knownGoodState;

   @BeforeEach
   void setUp() {knownGoodState.set();}

   @Test
    void shouldFind2Spots() {
       List<Spot> spots = repository.findAll();

       assertTrue(spots.size() >= 2);
   }

   @Test
   void shouldFindCoolSpot() {
       Spot actual = repository.findById(1);
       assertEquals("Super cool test spot", actual.getName());
   }

   @Test
   void shouldAdd() {
       int before = repository.findAll().size();
       Spot spot = new Spot();
       spot.setName("Repo spot");
       spot.setGpsLat(36.987);
       spot.setGpsLong(86.326);
       spot.setRating(3);
       spot.setDescription("A test spot added with the repository");
       spot.setAppUserId(1);
       Trail trail = new Trail();
       trail.setTrailId(1);
       List<Trail> trails = new ArrayList<>();
       trails.add(trail);
       spot.setTrails(trails);

       Spot actual = repository.add(spot);

       int after = repository.findAll().size();

       assertEquals(before + 1, after);
       assertEquals(4, actual.getSpotId());
   }

   @Test
   void shouldUpdate() {
       Spot spot = new Spot();
       spot.setSpotId(2);
       spot.setName("Updated spot name");
       spot.setGpsLat(45.378);
       spot.setGpsLong(26.942);
       spot.setDescription("This has been updated");
       spot.setAppUserId(3);
       Trail trail = new Trail();
       trail.setTrailId(1);
       Trail trail2 = new Trail();
       trail2.setTrailId(2);
       List<Trail> trails = new ArrayList<>();
       trails.add(trail);
       trails.add(trail2);
       spot.setTrails(trails);
       spot.setRating(3);

       repository.update(spot);

       Spot actual = repository.findById(2);

       assertTrue(actual.getName().contains("Updated"));
       assertTrue(actual.getDescription().contains("updated"));
   }

   @Test
   void shouldDelete() {
       int before = repository.findAll().size();

       repository.deleteById(3);

       int after = repository.findAll().size();

       assertEquals(before - 1, after);
       assertNull(repository.findById(3));
   }
}