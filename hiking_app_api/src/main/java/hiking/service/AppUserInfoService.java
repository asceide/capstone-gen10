package hiking.service;

import hiking.models.AppUser;
import hiking.models.AppUserInfo;
import hiking.repository.AppUserInfoRepository;
import hiking.repository.AppUserJbdcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AppUserInfoService {

    private final AppUserInfoRepository repository;

    private final AppUserJbdcRepository userRepository;
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public AppUserInfoService(@Autowired AppUserInfoRepository repository, AppUserJbdcRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public AppUserInfo findByAppUserId(int appUserId) {
        return repository.findByAppUserId(appUserId);
    }

    public AppUserInfo findByUsername(String username) {
        AppUser user = userRepository.findByEmail(username);
        if(user == null){
            return null;
        }

        return repository.findByAppUserId(user.getAppUserId());
    }

    public int findUserIdByUsername(String username) {
        AppUser user = userRepository.findByEmail(username);
        if(user == null){
            return 0;
        }

        return user.getAppUserId();
    }
    public Result<AppUserInfo> add(AppUserInfo info){
        Result<AppUserInfo> result = new Result<>();

        if(info==null){
            result.addMessage("정보가 없습니다.", ResultType.INVALID);
            return result;
        }

        validate(result, info);
        if(!result.isSuccess()){
            return result;
        }

        result.setPayload(repository.add(info));

        return result;
    }

    public Result<AppUserInfo> update(AppUserInfo info){
        Result<AppUserInfo> result = new Result<>();

        if (info == null) {
            result.addMessage("Info cannot be null", ResultType.INVALID);
            return result;
        }

        validate(result, info);
        if(!result.isSuccess()){
            return result;
        }

        if(!repository.update(info)){
            result.addMessage("User info not found", ResultType.INVALID);
            return result;
        }

        return result;
    }

    public Result<AppUserInfo> delete(int appUserId) {
        Result<AppUserInfo> result = new Result<>();
        if (appUserId <= 0) {
            result.addMessage("User id is invalid", ResultType.INVALID);
            return result;
        }

        if (!repository.delete(appUserId)) {
            result.addMessage("User info not found", ResultType.INVALID);
            return result;
        }

        return result;
    }

    private void validate(Result<AppUserInfo> result, AppUserInfo info){
        if(info==null){
            result.addMessage("User info is null", ResultType.INVALID);
        }

        Set<ConstraintViolation<AppUserInfo>> violations = validator.validate(info);
        if(!violations.isEmpty()){
            for(ConstraintViolation<AppUserInfo> violation: violations){
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
        }
    }

}
