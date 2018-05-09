package controllers.handler;

import database.entity.review.AuthorReview;
import database.entity.review.BookReview;
import exceptions.AddReviewException;
import exceptions.SetMarkException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import services.AddReviewService;
import utils.Const;

import java.util.List;

@ControllerAdvice
public class AddMarkReviewHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AddReviewService addReviewService;

    @Autowired
    private Logger logger;

    @ExceptionHandler(SetMarkException.class)
    public String setMark(Model model, SetMarkException ex) {
        logger.error("Mark wasn't setted");

        String view = "/xml_parts/user/set_mark";

        String message = messageSource.getMessage(
                "error.set_mark_failed",null,LocaleContextHolder.getLocale());

        model.addAttribute("mark_not_set_error",message);
        model.addAttribute("flag",false);

        return view;
    }

    @ExceptionHandler(AddReviewException.class)
    public String addReview(Model model, AddReviewException ex) {
        Const.sys("addReview");

        String type = ex.getType();

        Integer objId = ex.getObjId();

        String messageEx = ex.getMessage();

        String view = "xml_parts/user/add_review";

        String messageOne = messageSource.getMessage("error.not_empty",null,LocaleContextHolder.getLocale());
        String messageTwo = messageSource.getMessage("error.length",null,LocaleContextHolder.getLocale());
        String message;

        Const.sys("ex mess = " + ex.getMessage());
        if(messageOne.equals(messageEx) || messageTwo.equals(messageEx)) {
            message = messageEx;
        } else {
            message = messageSource.getMessage(
                    "error.add_review_failed",null,LocaleContextHolder.getLocale());
        }

        switch (type) {

            case Const.KEY_AUTHOR_TYPE:

                List<AuthorReview> authorReviews = addReviewService.getAuthorReviews(objId);
                model.addAttribute("reviews",authorReviews);
                break;

            case Const.KEY_BOOK_TYPE:
                List<BookReview> bookReviews = addReviewService.getBookReviews(objId);
                model.addAttribute("reviews",bookReviews);
                break;
        }

        model.addAttribute("review_not_added_error",message);

        return view;
    }
}
