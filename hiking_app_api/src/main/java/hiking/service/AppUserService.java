package hiking.service;

import hiking.models.AppUser;
import hiking.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> findAllUsers() {
        return appUserRepository.findAllUsers();
    }

    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public Result<AppUser> add(AppUser appUser) {
        Result<AppUser> result = new Result<>();

        if(appUser==null) {
            result.addMessage("User is null", ResultType.INVALID);
            return result;
        }

        Set<ConstraintViolation<AppUser>> violations = validator.validate(appUser);
        if(!violations.isEmpty()) {
            for(ConstraintViolation<AppUser> violation : violations) {
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }

        if(appUser.getApp_user_id()!=0){
            result.addMessage("You must not set an ID when adding an affiliation", ResultType.INVALID);
            return result;
        }

        result.setPayload(appUserRepository.add(appUser));
        return result;
    }

    public Result<AppUser> update(AppUser appUser) {
        Result<AppUser> result = new Result<>();

        if (appUser == null) {
            result.addMessage("User must not be null", ResultType.INVALID);
            return result;
        }

        Set<ConstraintViolation<AppUser>> violations = validator.validate(appUser);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<AppUser> violation : violations) {
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }

        if (appUser.getApp_user_id() <= 0) {
            result.addMessage("You must set an ID when updating a User", ResultType.INVALID);
            return result;
        }

        if (!appUserRepository.update(appUser)) {
            result.addMessage("User not found", ResultType.INVALID);
            return result;
        }

        return result;
    }

    public Result<AppUser> delete(int id){
        Result<AppUser> result = new Result<>();

        if(id<=0){
            result.addMessage("You must set an ID when deleting a User", ResultType.INVALID);
            return result;
        }

        if(!appUserRepository.delete(id)){
            result.addMessage("Was not able to delete this User...", ResultType.INVALID);
            return result;
        }

        return result;
    }


}
