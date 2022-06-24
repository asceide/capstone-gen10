package hiking.service;

import hiking.models.Trail;
import hiking.repository.TrailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrailService {

    private final TrailRepository repository;

    public TrailService(TrailRepository repository) {
        this.repository = repository;
    }


    public List<Trail> findAll() {
        return repository.findAll();
    }

    public Trail findById(int trailId) {
        return repository.findById(trailId);
    }


    public Result<Trail> add(Trail trail) {
        Result<Trail> result = validate(trail);
        if (!result.isSuccess()) {
            return result;
        }

        if (trail.getTrailId() != 0) {
            result.addMessage("TrailId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        trail = repository.add(trail);
        result.setPayload(trail);
        return result;
    }

    public Result<Trail> update(Trail trail) {
        Result<Trail> result = validate(trail);
        if (!result.isSuccess()) {
            return result;
        }

        if (trail.getTrailId() <= 0) {
            result.addMessage("trailId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(trail)) {
            String msg = String.format("trailId: %s, not found", trail.getTrailId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int agentId) {
        return repository.deleteById(agentId);
    }

    private Result<Trail> validate(Trail trail) {
        Result<Trail> result = new Result<>();
        if (trail == null) {
            result.addMessage("trail cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(trail.getName())) {
            result.addMessage("Name is required", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(trail.getCity())) {
            result.addMessage("City is required", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(trail.getState())) {
            result.addMessage("State is required", ResultType.INVALID);
        }



        return result;
    }


}
