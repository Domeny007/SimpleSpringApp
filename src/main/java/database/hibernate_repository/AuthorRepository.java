package database.hibernate_repository;

import database.entity.Author;
import database.entity.review.AuthorReview;
import database.entity.review.Review;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<Author,Integer>,JpaSpecificationExecutor<Author> {

    @Query(nativeQuery = true,
            value = "select * from spring_database.public.authors as b " +
                    "order by b.mark desc ,b.author_id desc " +
                    "limit 5 offset :num*5")
    List<Author> findPopular(@Param("num")Integer numPage);

    @Query(nativeQuery = true,
            value = "SELECT * FROM " +
                    "spring_database.public.getselectauthorbyname(:name,:surname,:num)")
    List<Author> findByName(@Param("name") String name,
                            @Param("surname") String surname,
                            @Param("num") Integer numPage);

    @Query(nativeQuery = true,
            value = "SELECT mark FROM spring_database.public.authors_users_votes " +
                    "WHERE author_id =:authorId AND user_id =:userId")
    Integer findUserMark(@Param("authorId") Integer bookId,@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO spring_database.public.authors_users_votes(mark,author_id,user_id) VALUES(:mark,:authorId,:userId)")
    void addUserMark(@Param("mark") Integer mark,@Param("authorId") Integer authorId,@Param("userId") Integer userId);


    @Query(nativeQuery = true,
            value = "SELECT mark FROM spring_database.public.authors " +
                    "WHERE author_id =:authorId")
    Integer getCurrentMark(@Param("authorId") Integer authorId);

    @Query("select u.reviews from Author u where u.id =:authorId")
    List<AuthorReview> findReviewsById(@Param("authorId") Integer authorId);


    /*@PostFilter("hasPermission(filterObject, 'READ')")
    List<Author> findAll();

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    Author findById(Integer id);*/

    /*@SuppressWarnings("unchecked")
    @PreAuthorize("hasPermission(#author, 'WRITE')")
    Author save(@Param("author")Author author);*/



}
