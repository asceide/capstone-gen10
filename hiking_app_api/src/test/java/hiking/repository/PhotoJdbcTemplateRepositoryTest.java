package hiking.repository;

import hiking.models.SpotPhoto;
import hiking.models.Trail;
import hiking.models.TrailPhoto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PhotoJdbcTemplateRepositoryTest {

    @Autowired
    PhotoJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @MockBean
    PasswordEncoder encoder;

    @BeforeEach
    void setUp() {knownGoodState.set();}

    @Test
    void shouldFind2PhotosPerSpot() {
        List<SpotPhoto> photos1 = repository.findBySpotId(1);
        List<SpotPhoto> photos2 = repository.findBySpotId(2);

        assertTrue(photos1.size() >= 2);
        assertTrue(photos2.size() >= 2);
    }

    @Test
    void shouldFind2PhotosPerTrail() {
        List<TrailPhoto> photos1 = repository.findByTrailId(1);
        List<TrailPhoto> photos2 = repository.findByTrailId(2);

        assertTrue(photos1.size() >= 2);
        assertTrue(photos2.size() >= 2);
    }

    @Test
    void shouldAddSpotPhoto() {
        List<SpotPhoto> before = repository.findBySpotId(1);
        SpotPhoto photo = new SpotPhoto();
        photo.setSpotId(1);
        photo.setPhotoUrl("repoTestUrl");

        SpotPhoto actual = repository.addPhoto(photo);

        List<SpotPhoto> after = repository.findBySpotId(1);

        assertEquals(before.size() + 1, after.size());
        assertFalse(actual.getPhotoId() == 0);
    }

    @Test
    void shouldAddTrailPhoto() {
        List<TrailPhoto> before = repository.findByTrailId(1);
        TrailPhoto photo = new TrailPhoto();
        photo.setTrailId(1);
        photo.setPhotoUrl("repoTestUrl");

        TrailPhoto actual = repository.addPhoto(photo);

        List<TrailPhoto> after = repository.findByTrailId(1);

        assertEquals(before.size() + 1, after.size());
        assertFalse(actual.getPhotoId() == 0);
    }

    @Test
    void shouldUpdateSpotPhoto() {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoId(2);
        photo.setPhotoUrl("updatedPhotoUrl");
        photo.setSpotId(1);

        repository.updatePhoto(photo);
        List<SpotPhoto> photos = repository.findBySpotId(1);

        assertTrue(photos.contains(photo));

        // changing the spot associated with the photo
        photo.setSpotId(2);
        repository.updatePhoto(photo);
        photos = repository.findBySpotId(2);
        assertTrue(photos.contains(photo));
    }

    @Test
    void shouldUpdateTrailPhoto() {
        TrailPhoto photo = new TrailPhoto();
        photo.setPhotoId(2);
        photo.setPhotoUrl("updatedPhotoUrl");
        photo.setTrailId(1);

        repository.updatePhoto(photo);
        List<TrailPhoto> photos = repository.findByTrailId(1);

        assertTrue(photos.contains(photo));

        // changing the trail associated with the photo
        photo.setTrailId(2);
        repository.updatePhoto(photo);
        photos = repository.findByTrailId(2);
        assertTrue(photos.contains(photo));
    }

    @Test
    void shouldDeleteSpotPhoto() {
        // replication of the photo created in the stored procedure
        SpotPhoto target = new SpotPhoto();
        target.setSpotId(1);
        target.setPhotoId(5);
        target.setPhotoUrl("deletethisspot");

        List<SpotPhoto> before = repository.findBySpotId(1);
        repository.deleteById(5);
        List<SpotPhoto> after = repository.findBySpotId(1);

        assertEquals(before.size() -1, after.size());
        assertFalse(after.contains(target));
    }

    void shouldDeleteTrailPhoto() {
        // replication of the photo created in the stored procedure
        TrailPhoto target = new TrailPhoto();
        target.setTrailId(1);
        target.setPhotoId(10);
        target.setPhotoUrl("deletethistrail");

        List<TrailPhoto> before = repository.findByTrailId(1);
        repository.deleteById(10);
        List<TrailPhoto> after = repository.findByTrailId(1);

        assertEquals(before.size() -1, after.size());
        assertFalse(after.contains(target));
    }
}