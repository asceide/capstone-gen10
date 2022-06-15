package hiking.service;

import hiking.models.SpotPhoto;
import hiking.models.TrailPhoto;
import hiking.repository.PhotoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class PhotoService {

    private final PhotoRepository repository;
    private final Validator validator;

    public PhotoService(PhotoRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<SpotPhoto> findBySpotId(int spotId) {return repository.findBySpotId(spotId);}
    public List<TrailPhoto> findByTrailId(int trailId) {return repository.findByTrailId(trailId);}

    public Result<Object> addPhoto(SpotPhoto photo) {
        Result<Object> result = validate(photo);

        if (!result.isSuccess()) {
            return result;
        }

        if(photo.getPhotoId() != 0) {
            result.addMessage("ID must not be set for add", ResultType.INVALID);
            return result;
        }

        try {
            SpotPhoto returned = repository.addPhoto(photo);
            result.setPayload(returned);
            return result;
        } catch (DataIntegrityViolationException ex) {
            String msg = String.format("Spot with ID %s not found", photo.getSpotId());
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }


    }

    public Result<Object> addPhoto(TrailPhoto photo) {
        Result<Object> result = validate(photo);
        if (!result.isSuccess()) {
            return result;
        }

        if(photo.getPhotoId() != 0) {
            result.addMessage("ID must not be set for add", ResultType.INVALID);
            return result;
        }

        try{
            TrailPhoto returned = repository.addPhoto(photo);
            result.setPayload(returned);
            return result;
        } catch (DataIntegrityViolationException ex) {
            String msg = String.format("Trail with ID %s not found", photo.getTrailId());
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }

    }

    public Result<Object> updatePhoto(SpotPhoto photo) {
        Result<Object> result = validate(photo);
        if (!result.isSuccess()) {
            return result;
        }

        if(photo.getPhotoId() <= 0) {
            result.addMessage("ID is required for update", ResultType.INVALID);
            return result;
        }

        try {
            if(!repository.updatePhoto(photo)) {
                String msg = String.format("Photo with ID %s not found", photo.getPhotoId());
                result.addMessage(msg, ResultType.NOT_FOUND);
            }
            return result;
        } catch (DataIntegrityViolationException ex) {
            String msg = String.format("Spot with ID %s not found", photo.getSpotId());
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }

    }

    public Result<Object> updatePhoto(TrailPhoto photo) {
        Result<Object> result = validate(photo);
        if (!result.isSuccess()) {
            return result;
        }

        if(photo.getPhotoId() <= 0) {
            result.addMessage("ID is required for update", ResultType.INVALID);
            return result;
        }

        try {
            if(!repository.updatePhoto(photo)) {
                String msg = String.format("Photo with ID %s not found", photo.getPhotoId());
                result.addMessage(msg, ResultType.NOT_FOUND);
            }
            return result;
        } catch (DataIntegrityViolationException ex) {
            String msg = String.format("Trail with ID %s not found", photo.getTrailId());
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }

    }

    public Result<Object> deleteById(int photoId) {
        Result<Object> result = new Result<>();

        if (photoId <= 0) {
            result.addMessage("PhotoId is required for delete", ResultType.INVALID);
            return result;
        }

        if(!repository.deleteById(photoId)) {
            String msg = String.format("Photo with ID %s not found", photoId);
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<Object> validate(Object photo) {
        Result<Object> result = new Result<>();

        if(photo == null) {
            result.addMessage("Photo cannot be null", ResultType.INVALID);
            return result;
        }

        Set<ConstraintViolation<Object>> violations = validator.validate(photo);
        for (var violation : violations) {
            result.addMessage(violation.getMessage(), ResultType.INVALID);
        }

        return result;
    }
}
