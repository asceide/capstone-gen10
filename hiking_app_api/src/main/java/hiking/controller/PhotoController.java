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

    @GetMapping
    public Object findBySpotId(@RequestParam(value = "spot-id", required = false, defaultValue = "0") int spotId,
                                @RequestParam(value = "trail-id", required = false, defaultValue = "0") int trailId) {
        if(spotId != 0) {
            List<SpotPhoto> photos = service.findBySpotId(spotId);
            if (photos.size() == 0) {
                String msg = String.format("Spot #%s not found.", spotId);
                return new ResponseEntity<>(List.of(msg), HttpStatus.NOT_FOUND);
            }
            return photos;
        } else if (trailId != 0) {
            List<TrailPhoto> photos = service.findByTrailId(trailId);
            if(photos.size() == 0) {
                String msg = String.format("Trail #%s not found.", trailId);
                return new ResponseEntity<>(List.of(msg), HttpStatus.NOT_FOUND);
            }
            return photos;
        } else {
            return new ResponseEntity<>(List.of("Spot or trail id must be provided"), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping
    public ResponseEntity<Object> add(@RequestParam(value = "spot-id", required = false, defaultValue = "0") int spotId,
                                               @RequestParam(value = "trail-id", required = false, defaultValue = "0") int trailId,
                                               @RequestBody Map<String, String> map) {
        String photoUrl = map.get("photoUrl");
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
            return new ResponseEntity<>(List.of("Spot or trail id must be provided"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{photoId}")
    public ResponseEntity<Object> update(@PathVariable int photoId, @RequestBody Map<String, String> map) {

        if(map.containsKey("spotId")) {
            try {
                int mapPhotoId = Integer.parseInt(map.get("photoId"));
                int spotId = Integer.parseInt(map.get("spotId"));
                SpotPhoto photo = new SpotPhoto();
                photo.setPhotoId(mapPhotoId);
                photo.setPhotoUrl(map.get("photoUrl"));
                photo.setSpotId(spotId);
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
                return new ResponseEntity<>(List.of("Photo id and spot id must be positive integers"), HttpStatus.BAD_REQUEST);
            }
        } else if (map.containsKey("trailId")) {
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
