package database.entity.forms;

import database.entity.Country;
import org.hibernate.validator.constraints.NotBlank;
import utils.validators.CountryConstraint;
import utils.validators.GenderConstraint;

import javax.validation.constraints.Size;

public class UserChangeForm {

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 100,message = "{error.length}")
    private String usernameReal;

    @GenderConstraint
    private String gender;

    @CountryConstraint
    private Country country;

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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
