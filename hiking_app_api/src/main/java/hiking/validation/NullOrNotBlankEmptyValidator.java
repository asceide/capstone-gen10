package hiking.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrNotBlankEmptyValidator implements ConstraintValidator<NullOrNotBlankEmpty, String> {

    @Override
    public void initialize(NullOrNotBlankEmpty constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.trim().length() > 0;
    }
}
