package utils.validators;

import database.entity.User;
import database.hibernate_repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint,String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(EmailConstraint emailConstraint) {}


    @Override
    public boolean isValid(String email,ConstraintValidatorContext cxt) {
        boolean flag = false;

        if(email != null){
            User user = userRepository.findByUsername(email);

            if(user == null){
                flag = true;
            }
        }

        return flag;
    }
}
