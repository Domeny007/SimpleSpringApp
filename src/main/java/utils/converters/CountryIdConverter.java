package utils.converters;

import database.entity.Country;
import database.hibernate_repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryIdConverter extends AbstractTwoWayConverter<String, Country> {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryIdConverter(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    protected Country convert(String userId) {
        return countryRepository.findOne(Integer.valueOf(userId));
    }

    @Override
    protected String convertBack(Country target) {
        String result = "null";
        if(target != null) {
            result = String.valueOf(target.getId());
        }
        return result;
    }

}
