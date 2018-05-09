package utils.validators;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static database.entity.Gender.FEMALE;
import static database.entity.Gender.MALE;

public class GenderConstraintValidator implements
        ConstraintValidator<GenderConstraint, String> {

    @Override
    public void initialize(GenderConstraint genderConstraint) {
    }

    @Override
    public boolean isValid(String gender,
                           ConstraintValidatorContext cxt) {

        return FEMALE.toString().equals(gender) ||
                MALE.toString().equals(gender);
    }

}
