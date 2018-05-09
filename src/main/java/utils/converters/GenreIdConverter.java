package utils.converters;

import database.entity.Genre;
import database.hibernate_repository.GenreRepository;
import database.hibernate_repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenreIdConverter extends AbstractTwoWayConverter<String, Genre> {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreIdConverter(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    protected Genre convert(String userId) {
        return genreRepository.findOne(Integer.valueOf(userId));
    }

    @Override
    protected String convertBack(Genre target) {
        return String.valueOf(target.getId());
    }
}
