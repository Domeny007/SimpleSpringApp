package database.hibernate_repository;

import database.entity.Author;
import database.entity.review.BookReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepository extends CrudRepository<BookReview,Integer> {
}
