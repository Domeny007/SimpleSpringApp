package utils;

import database.entity.Book;
import database.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class RepoSpecification {

    public Specification<Book> getBookSpecByName(final String bookName) {
        return (r, cq, cb) -> cb.like(cb.lower(r.get("name")), bookName.toLowerCase() +"%");
    }
}
