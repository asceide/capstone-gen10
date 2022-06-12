package hiking.security;

import hiking.models.AppUser;
import hiking.repository.AppUserRepository;
import hiking.service.EmailRegex;
import hiking.service.Result;
import hiking.service.ResultType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.List;
import java.util.Set;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();


    public AppUserService(AppUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        AppUser user = repository.findByEmail(email);

        if(user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public Result<AppUser> create(String email, String password){
        Result<AppUser> result = new Result<>();
        validate(email, result);
        validatePassword(password, result);

        if(!result.getMessages().isEmpty()) {
            return result;
        }

        password = encoder.encode(password);

        AppUser appUser = new AppUser(0, email, password, true, List.of("USER"));
        Set<ConstraintViolation<AppUser>> violations = validator.validate(appUser);
        if(!violations.isEmpty()){
            for(ConstraintViolation<AppUser> violation : violations){
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }

        result.setPayload(repository.create(appUser));

        return result;

    }

    private void validate(String email, Result<AppUser> result){
        if(email == null || email.isBlank()){
            result.addMessage("Email is required", ResultType.INVALID);
            return;
        }
        if(!email.matches(EmailRegex.Constants.REGEX)){
            result.addMessage("Email is invalid", ResultType.INVALID);
        }
        if(email.length()>50){
            result.addMessage("Email is too long", ResultType.INVALID);
        }
    }

    private void validatePassword(String password, Result<AppUser> result){
        if(password == null || password.isBlank() || password.length() < 8){
            result.addMessage("Password is too short. It must be atleast 8 characteres in length.", ResultType.INVALID);
            return;
        }

        if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%^*?#&])[A-Za-z\\d@$!%*#?^&]{8,}$")){
            result.addMessage("Password is invalid. It must contain atleast one lowercase letter, one uppercase letter, one number and one special character.", ResultType.INVALID);
        }
    }
}

