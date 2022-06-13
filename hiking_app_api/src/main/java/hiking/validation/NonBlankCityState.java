package hiking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NonBlankCityStateValidator.class)
@Documented
public @interface NonBlankCityState {
    String message() default "If either is used, City and State must not be blank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
