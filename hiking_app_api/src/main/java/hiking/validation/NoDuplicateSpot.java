package hiking.validation;


import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
//@Constraint(validatedBy = NoDuplicateUserValidator.class)
@Documented
public @interface NoDuplicateSpot {
    String message() default "Spot already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
