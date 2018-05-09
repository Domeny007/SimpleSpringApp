package utils.validators;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GenderConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "{error.not_empty}")
public @interface GenderConstraint {

    String message() default "{error.invalid_gender}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
