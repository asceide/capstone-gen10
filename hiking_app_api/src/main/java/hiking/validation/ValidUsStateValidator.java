package hiking.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

public class ValidUsStateValidator implements ConstraintValidator<ValidUsState, String> {
    List<String> USStates = Arrays.asList("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL",
            "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI",
            "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH",
            "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");

    @Override
    public void initialize(ValidUsState constraintAnnotation) {
    }

    @Override
    public boolean isValid(String state, ConstraintValidatorContext context) {
        if(state==null || state.isEmpty()) {
            return true;
        }
        return USStates.contains(state);
    }

}
