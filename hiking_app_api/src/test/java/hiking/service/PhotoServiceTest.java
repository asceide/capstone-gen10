package hiking.service;

import hiking.models.SpotPhoto;
import hiking.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PhotoServiceTest {

    @Autowired
    PhotoService service;

    @MockBean
    PhotoRepository repository;

    @MockBean
    PasswordEncoder encoder;

    @Test
    void shouldRejectNullPhoto() {
        SpotPhoto photo = new SpotPhoto();
        System.out.println(photo);
        photo = null;

        Result<Object> result = service.addPhoto(photo);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Photo cannot be null");
    }

    @Test
    void shouldRejectBadUrl() {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoId(1);
        photo.setPhotoUrl("notValid");
        photo.setSpotId(1);

        Result<Object> result = service.addPhoto(photo);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Photo url must be a url");

        photo.setPhotoUrl("");
        result = service.addPhoto(photo);
        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Photo url is required");

    }

    @Test
    void shouldRejectNonZeroIdForAdd() {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoId(1);
        photo.setPhotoUrl("https://somefakeurl.com");
        photo.setSpotId(1);

        Result<Object> result = service.addPhoto(photo);

        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "ID must not be set for add");
    }

    @Test
    void shouldRejectBadParentId() {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoUrl("https://somefakeurl.com");
        photo.setSpotId(-5);

        Result<Object> result = service.addPhoto(photo);

        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Id must be greater than or equal to 0");
    }


    @Test
    void shouldAddValid() throws Exception {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoUrl("https://somefakeurl.com");
        photo.setSpotId(1);

        SpotPhoto expected = new SpotPhoto();
        expected.setPhotoId(1);
        expected.setPhotoUrl("https://somefakeurl.com");
        expected.setSpotId(1);

        when(repository.addPhoto(photo)).thenReturn(expected);

        Result<Object> result = service.addPhoto(photo);
        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), expected);
    }

    @Test
    void shouldRejectBadPhotoId() {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoId(-3);
        photo.setPhotoUrl("https://somefakeurl.com");
        photo.setSpotId(1);

        Result<Object> result = service.updatePhoto(photo);

        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Id must be greater than or equal to 0");
    }

    @Test
    void shouldUpdateValid() throws Exception {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoId(1);
        photo.setPhotoUrl("https://somefakeurl.com");
        photo.setSpotId(1);

        when(repository.updatePhoto(photo)).thenReturn(true);

        Result<Object> result = service.updatePhoto(photo);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldBeNotFoundForMissingUpdate() throws Exception {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoId(1);
        photo.setPhotoUrl("https://somefakeurl.com");
        photo.setSpotId(1);

        when(repository.updatePhoto(photo)).thenReturn(false);

        Result<Object> result = service.updatePhoto(photo);

        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Photo with ID 1 not found");
    }

    @Test
    void shouldRejectZeroIdForUpdate() {
        SpotPhoto photo = new SpotPhoto();
        photo.setPhotoUrl("https://somefakeurl.com");
        photo.setSpotId(1);

        Result<Object> result = service.updatePhoto(photo);

        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "ID is required for update");
    }

    @Test
    void shouldDelete() {
        when(repository.deleteById(1)).thenReturn(true);
        Result<Object> result = service.deleteById(1);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldBeNotFoundForMissingDelete() {
        when(repository.deleteById(1)).thenReturn(false);
        Result<Object> result = service.deleteById(1);

        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "Photo with ID 1 not found");
    }

    @Test
    void shouldRejectBadIdForDelete() {
        Result<Object> result = service.deleteById(-5);

        assertEquals(ResultType.INVALID, result.getType());
        assertEquals(result.getMessages().stream().findFirst().orElse(null),
                "PhotoId is required for delete");
    }
}