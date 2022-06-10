package hiking.validation;

import hiking.models.AppUser;
import hiking.repository.AppUserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.stream.Collectors;

public class NoDuplicateUserValidator implements ConstraintValidator<NoDuplicateUser, AppUser> {

    private AppUserRepository repository;

    @Override
    public void initialize(NoDuplicateUser constraintAnnotation) {
        this.repository = (AppUserRepository) ContextProvider.getBean(AppUserRepository.class);
    }

    @Override
    public boolean isValid(AppUser user, ConstraintValidatorContext context) {
        if(user==null || user.getEmail()==null) {
            return true;
        }

        HashSet<String> users = repository.findAllUsers().stream().map(usr ->
                usr.getEmail().toLowerCase()).collect(Collectors.toCollection(HashSet::new));

        return users.add(user.getEmail().toLowerCase());
    }
}
