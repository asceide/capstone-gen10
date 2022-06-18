package hiking.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = NullOrNotBlankEmptyValidator.class)
@Documented
public @interface NullOrNotBlankEmpty {
    String message() default "Field can be null or not blank or Empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
