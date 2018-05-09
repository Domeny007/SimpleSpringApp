package services;

import database.entity.User;
import database.entity.review.AuthorReview;
import database.entity.review.BookReview;
import database.hibernate_repository.AuthorRepository;
import database.hibernate_repository.AuthorReviewRepository;
import database.hibernate_repository.BookRepository;
import database.hibernate_repository.BookReviewRepository;
import exceptions.AddReviewException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.Const;

import java.util.List;

@Service
public class AddReviewService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookReviewRepository bookReviewRepository;

    @Autowired
    private AuthorReviewRepository authorReviewRepository;

    @Transactional
    public void addBookReview(String title, String content, String bookId, User writer) {

        try {
            BookReview bookReview = new BookReview();

            bookReview.setWriter(writer);
            bookReview.setBook(bookRepository.findOne(Integer.valueOf(bookId)));
            bookReview.setTitle(title);
            bookReview.setContent(content);

            bookReviewRepository.save(bookReview);
        } catch (Exception ex){
            throw new AddReviewException(ex.getMessage());
        }
    }

    @Transactional
    public void addAuthorReview(String title, String content,String authorId, User writer) {

        try {
            AuthorReview authorReview = new AuthorReview();

            authorReview.setWriter(writer);
            authorReview.setAuthor(authorRepository.findOne(Integer.valueOf(authorId)));
            authorReview.setTitle(title);
            authorReview.setContent(content);

            authorReviewRepository.save(authorReview);

        } catch (Exception ex) {
            throw new AddReviewException(ex.getMessage());
        }
    }

    public List<BookReview> getBookReviews(Integer bookId) {
        return bookRepository.findReviewsById(bookId);
    }

    public List<AuthorReview> getAuthorReviews(Integer authorId) {
        return authorRepository.findReviewsById(authorId);
    }
}
