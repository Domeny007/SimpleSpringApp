package utils.validators;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Email(message = "{error.invalid_email}")
@NotBlank(message = "{error.not_empty}")
@Size(min = 5,max = 100,message = "{error.length}")
public @interface EmailConstraint {

    String message() default "{error.email_exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
