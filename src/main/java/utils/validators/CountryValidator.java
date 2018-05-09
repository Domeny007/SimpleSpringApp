package utils.validators;

import database.entity.Country;
import database.hibernate_repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryValidator
        implements ConstraintValidator<CountryConstraint, Country> {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public void initialize(CountryConstraint genderConstraint) {}


    @Override
    public boolean isValid(Country country,ConstraintValidatorContext cxt) {
        boolean flag = false;

        if(country != null){
            Country countryDb = countryRepository.findOne(country.getId());

            if(countryDb != null){
                flag = true;
            }
        }

        return flag;
    }
}
