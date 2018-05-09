package database.hibernate_repository;

import database.entity.Author;
import database.entity.review.AuthorReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorReviewRepository extends CrudRepository<AuthorReview,Integer> {
}
