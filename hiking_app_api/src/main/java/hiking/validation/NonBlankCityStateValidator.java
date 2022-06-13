package hiking.validation;

import hiking.models.AppUserInfo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NonBlankCityStateValidator implements ConstraintValidator<NonBlankCityState, AppUserInfo> {

        @Override
        public void initialize(NonBlankCityState constraintAnnotation) {
            // Nothing to do
        }

        @Override
         public boolean isValid(AppUserInfo info, ConstraintValidatorContext context) {
            if(info== null || info.getCity()==null && info.getState()==null) {
                return true;
            }
            // If one of them is not null, then both must be not null
            if(info.getCity()!=null || info.getState()!=null) {
                return info.getCity() != null && info.getState() != null;
            }

            return false;
        }
}
