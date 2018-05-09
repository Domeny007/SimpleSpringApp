package database.entity.forms;

import database.entity.Country;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import utils.validators.CountryConstraint;
import utils.validators.FileTypeConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

public class AuthorForm {

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 1,max = 50,message = "{error.length}")
    private String name;

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 1,max = 50,message = "{error.length}")
    private String surname;

    @NotNull(message = "{error.not_empty}")
    @Past(message = "{error.incorrect_past}")
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date birthday;

    @CountryConstraint
    private Country motherland;

    @FileTypeConstraint
    private MultipartFile photo;

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 2000,message = "{error.length}")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Country getMotherland() {
        return motherland;
    }

    public void setMotherland(Country motherland) {
        this.motherland = motherland;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
