package hiking.service;

import hiking.models.Spot;
import hiking.repository.SpotRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class SpotService {

    private final SpotRepository repository;
    private final Validator validator;

    public SpotService(SpotRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<Spot> findAll() {return repository.findAll();}

    public Spot findById(int spotId) {return repository.findById(spotId);}

    public Result<Spot> add(Spot spot) {
        Result<Spot> result = validate(spot);
        if(!result.isSuccess()) {
            return result;
        }

        if(spot.getSpotId() != 0) {
            result.addMessage("Spot id must not be set for add", ResultType.INVALID);
            return result;
        }

        Spot returned = repository.add(spot);

        result.setPayload(returned);

        return result;
    }

    public Result<Spot> update(Spot spot) {
        Result<Spot> result = validate(spot);
        if(!result.isSuccess()) {
            return result;
        }

        if(spot.getSpotId() <= 0) {
            result.addMessage("Spot id must be set for update", ResultType.INVALID);
            return result;
        }

        if(!repository.update(spot)) {
            String msg = String.format("Spot with id %s not found.", spot.getSpotId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<Spot> deleteById(int spotId) {
        Result<Spot> result = new Result<>();

        if(!repository.deleteById(spotId)) {
            String msg = String.format("Spot with id %s not found", spotId);
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<Spot> rate(int spotId, int rating) {
        Spot spot = repository.findById(spotId);

        int total = spot.getRating() * spot.getRatingCount() + rating;

        int newCount = spot.getRatingCount() + 1;

        int newRating = Math.round(total / newCount);

        spot.setRating(newRating);
        spot.setRatingCount(newCount);

        return update(spot);
    }

    private Result<Spot> validate(Spot spot) {
        Result<Spot> result = new Result<>();
        if(spot == null) {
            result.addMessage("Spot cannot be null", ResultType.INVALID);
            return result;
        }

        Set<ConstraintViolation<Spot>> violations = validator.validate(spot);
        for (var violation : violations) {
            result.addMessage(violation.getMessage(), ResultType.INVALID);
        }

        return result;
    }
}
