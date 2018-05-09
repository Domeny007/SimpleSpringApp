package database.hibernate_repository;

import database.entity.Book;
import database.entity.review.BookReview;
import database.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer>,JpaSpecificationExecutor<Book> {

    @Query(nativeQuery = true,
            value = "select * from spring_database.public.books as b " +
                    "order by b.mark desc ,b.book_id desc " +
                    "limit 5 offset :num*5")
    List<Book> findPopular(@Param("num")Integer numPage);

    @Query(nativeQuery = true,
            value = "SELECT * FROM spring_database.public.getselectbookbyname(:name,:num)")
    List<Book> findByName(@Param("name") String bookName,@Param("num") Integer numPage);

    @Query(nativeQuery = true,
            value = "SELECT mark FROM spring_database.public.books_users_votes " +
                    "WHERE book_id =:bookId AND user_id =:userId")
    Integer findUserMark(@Param("bookId") Integer bookId,@Param("userId") Integer userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO spring_database.public.books_users_votes(mark,book_id,user_id) VALUES(:mark,:bookId,:userId)")
    void addUserMark(@Param("mark") Integer mark,@Param("bookId") Integer bookId,@Param("userId") Integer userId);


    @Query(nativeQuery = true,
            value = "SELECT mark FROM spring_database.public.books " +
                    "WHERE book_id =:bookId")
    Integer getCurrentMark(@Param("bookId") Integer bookId);


    @Query("select u.reviews from Book u where u.id =:bookId")
    List<BookReview> findReviewsById(@Param("bookId") Integer bookId);

}
