package hiking.controller;

import hiking.models.SpotPhoto;
import hiking.models.TrailPhoto;
import hiking.service.PhotoService;
import hiking.service.Result;
import hiking.service.ResultType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {

    private final PhotoService service;

    public PhotoController(PhotoService service) {this.service = service;}

    // use optional request parameters so there is one endpoint for both spots and trails
    @GetMapping
    public Object findBySpotId(@RequestParam(value = "spot-id", required = false, defaultValue = "0") int spotId,
                                @RequestParam(value = "trail-id", required = false, defaultValue = "0") int trailId) {

        // default value is 0, which will never by a valid ID
        if(spotId != 0) {
            // get the photos for a spot if ID is provided
            List<SpotPhoto> photos = service.findBySpotId(spotId);
            if (photos.size() == 0) {
                String msg = String.format("Spot #%s not found.", spotId);
                return new ResponseEntity<>(List.of(msg), HttpStatus.NOT_FOUND);
            }
            return photos;
        } else if (trailId != 0) {
            // get photos for trail if ID is provided
            List<TrailPhoto> photos = service.findByTrailId(trailId);
            if(photos.size() == 0) {
                String msg = String.format("Trail #%s not found.", trailId);
                return new ResponseEntity<>(List.of(msg), HttpStatus.NOT_FOUND);
            }
            return photos;
        } else {
            // no ids provided
            return new ResponseEntity<>(List.of("Spot or trail id must be provided"), HttpStatus.BAD_REQUEST);
        }
    }


    // request parameters again so that either spot or trail photo are accepted
    @PostMapping
    public ResponseEntity<Object> add(@RequestParam(value = "spot-id", required = false, defaultValue = "0") int spotId,
                                               @RequestParam(value = "trail-id", required = false, defaultValue = "0") int trailId,
                                               @RequestBody Map<String, String> map) {

        // get the photo url out of the json object
        String photoUrl = map.get("photoUrl");

        // same pattern for handling optional parameters
        if(spotId != 0) {
            SpotPhoto photo = new SpotPhoto();
            photo.setSpotId(spotId);
            photo.setPhotoUrl(photoUrl);
            Result<Object> result = service.addPhoto(photo);
            if(result.isSuccess()) {
                return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
            } else if (result.getType() == ResultType.NOT_FOUND) {
                return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        } else if (trailId != 0) {
            TrailPhoto photo = new TrailPhoto();
            photo.setTrailId(trailId);
            photo.setPhotoUrl(photoUrl);
            Result<Object> result = service.addPhoto(photo);
            if(result.isSuccess()) {
                return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
            } else if (result.getType() == ResultType.NOT_FOUND) {
                return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        } else {
            // no id provided in parameters
            return new ResponseEntity<>(List.of("Spot or trail id must be provided"), HttpStatus.BAD_REQUEST);
        }
    }

    // take in the photo as a map to allow either trailId or spotId
    @PutMapping("/{photoId}")
    public ResponseEntity<Object> update(@PathVariable int photoId, @RequestBody Map<String, String> map) {

        if(map.containsKey("spotId")) {
            // if a spotId is passed in the json object, proceed with updating a spot photo
            try {
                // we have to parse ids from strings, so wrapped in try/catch
                int mapPhotoId = Integer.parseInt(map.get("photoId"));
                int spotId = Integer.parseInt(map.get("spotId"));
                SpotPhoto photo = new SpotPhoto();
                photo.setPhotoId(mapPhotoId);
                photo.setPhotoUrl(map.get("photoUrl"));
                photo.setSpotId(spotId);
                if(photo.getPhotoId() != photoId) {
                    // check that id in path matches the id in the object to double check we are updating the right photo
                    return new ResponseEntity<>(List.of("Photo id must match path"), HttpStatus.BAD_REQUEST);
                }
                Result<Object> result = service.updatePhoto(photo);

                if(result.getType() == ResultType.NOT_FOUND) {
                    return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
                } else if (!result.isSuccess()) {
                    return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (NumberFormatException ex) {
                return new ResponseEntity<>(List.of("Photo id and spot id must be positive integers"), HttpStatus.BAD_REQUEST);
            }
        } else if (map.containsKey("trailId")) {
            // if a trailId is provided, proceed with updating a trail photo
            try {
                int mapPhotoId = Integer.parseInt(map.get("photoId"));
                int trailId = Integer.parseInt(map.get("spotId"));
                TrailPhoto photo = new TrailPhoto();
                photo.setPhotoId(mapPhotoId);
                photo.setPhotoUrl(map.get("photoUrl"));
                photo.setTrailId(trailId);
                if(photo.getPhotoId() != photoId) {
                    return new ResponseEntity<>(List.of("Photo id must match path"), HttpStatus.BAD_REQUEST);
                }
                Result<Object> result = service.updatePhoto(photo);

                if(result.getType() == ResultType.NOT_FOUND) {
                    return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
                } else if (!result.isSuccess()) {
                    return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (NumberFormatException ex) {
                return new ResponseEntity<>(List.of("Photo id and trail id must be positive integers"), HttpStatus.BAD_REQUEST);
            }
        } else {
            // no id provided
            return new ResponseEntity<>(List.of("Spot or trail id is required"), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Object> delete(@PathVariable int photoId) {
        Result<Object> result = service.deleteById(photoId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getMessages(), HttpStatus.NOT_FOUND);
    }

}
