package database.entity.forms;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;
import utils.validators.FileTypeConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BookForm {

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 100,message = "{error.length}")
    private String name;

    @NotBlank(message = "{error.not_empty}")
    @Size(min = 5,max = 2000,message = "{error.length}")
    private String description;

    @Range(min = 0,max = 10000,message = "{error.year}")
    @NotNull(message = "{error.not_empty}")
    private Integer year;

    @FileTypeConstraint
    private MultipartFile photo;

    @NotBlank(message = "{error.not_empty}")
    private String countriesId;

    @NotBlank(message = "{error.not_empty}")
    private String genresId;

    @NotBlank(message = "{error.not_empty}")
    private String authorsId;

    private String countriesNames;

    private String genresNames;

    private String authorsNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public String getCountriesId() {
        return countriesId;
    }

    public void setCountriesId(String countriesId) {
        this.countriesId = countriesId;
    }

    public String getGenresId() {
        return genresId;
    }

    public void setGenresId(String genresId) {
        this.genresId = genresId;
    }

    public String getAuthorsId() {
        return authorsId;
    }

    public void setAuthorsId(String authorsId) {
        this.authorsId = authorsId;
    }

    public String getCountriesNames() {
        return countriesNames;
    }

    public void setCountriesNames(String countriesNames) {
        this.countriesNames = countriesNames;
    }

    public String getGenresNames() {
        return genresNames;
    }

    public void setGenresNames(String genresNames) {
        this.genresNames = genresNames;
    }

    public String getAuthorsNames() {
        return authorsNames;
    }

    public void setAuthorsNames(String authorsNames) {
        this.authorsNames = authorsNames;
    }
}
