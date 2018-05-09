package utils.converters;

import database.entity.Author;
import database.hibernate_repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorIdConverter extends AbstractTwoWayConverter<String, Author> {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorIdConverter(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    protected Author convert(String userId) {
        return authorRepository.findOne(Integer.valueOf(userId));
    }

    @Override
    protected String convertBack(Author target) {
        return String.valueOf(target.getId());
    }
}
