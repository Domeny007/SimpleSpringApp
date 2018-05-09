package services;

import database.entity.User;
import database.entity.forms.RegistrationForm;
import database.entity.forms.UserChangeForm;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface IUserService extends UserDetailsService {

    void registerUser(RegistrationForm registrationForm);

    void deleteUser(int id);

    void updateUser(int id,UserChangeForm userChangeForm);
}
