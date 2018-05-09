package utils.validators;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileTypeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotNull(message = "{error.not_empty}")
public @interface FileTypeConstraint {

    String message() default "{error.invalid_file}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
