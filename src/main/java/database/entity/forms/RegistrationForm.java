package database.entity.forms;

import database.entity.Country;
import org.hibernate.validator.constraints.NotBlank;
import utils.validators.CountryConstraint;
import utils.validators.EmailConstraint;
import utils.validators.GenderConstraint;
import utils.validators.PasswordMatches;

import javax.validation.constraints.Size;


@PasswordMatches
public class RegistrationForm {

    @EmailConstraint
    private String username;

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 100,message = "{error.length}")
    private String password;

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 100,message = "{error.length}")
    private String passwordRepeat;

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 100,message = "{error.length}")
    private String usernameReal;

    @GenderConstraint
    private String gender;

    @CountryConstraint
    private Country country;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getUsernameReal() {
        return usernameReal;
    }

    public void setUsernameReal(String usernameReal) {
        this.usernameReal = usernameReal;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
