package utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "{error.not_empty}")
public @interface CountryConstraint {

    String message() default "{error.invalid_country}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

