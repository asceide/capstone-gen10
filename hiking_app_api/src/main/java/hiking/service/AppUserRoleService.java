package hiking.service;

import hiking.models.AppUserRole;
import hiking.repository.AppUserRoleRepository;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class AppUserRoleService {
    private final AppUserRoleRepository repository;

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public AppUserRoleService(AppUserRoleRepository repository) {
        this.repository = repository;
    }

    public List<AppUserRole> findAll() {
        return repository.findAll();
    }

    public Result<AppUserRole> update(AppUserRole user) {
        Result<AppUserRole> result = new Result<>();

        if(user==null){
            result.addMessage("정보가 없습니다.", ResultType.INVALID);
            return result;
        }

        Set<ConstraintViolation<AppUserRole>> violations = validator.validate(user);
        if(!violations.isEmpty()){
            for(ConstraintViolation<AppUserRole> violation: violations){
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
        }

        if(!result.isSuccess()){
            return result;
        }

        if(!repository.update(user)){
            result.addMessage("정보를 수정하는데 실패했습니다.", ResultType.INVALID);
            return result;
        }

        return result;
    }
}
