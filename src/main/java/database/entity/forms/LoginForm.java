package database.entity.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import utils.validators.EmailConstraint;

import javax.validation.constraints.Size;

public class LoginForm {

    @Email(message = "{error.invalid_email}")
    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 100,message = "{error.length}")
    private String username;

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 100,message = "{error.length}")
    private String password;

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
}
