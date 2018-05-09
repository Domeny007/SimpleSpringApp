package controllers.handler;

import controllers.SecurityController;
import exceptions.NotFoundException;
import exceptions.SearchException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import utils.Const;

@ControllerAdvice
public class FindEntityException {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Logger logger;

    @ExceptionHandler(NotFoundException.class)
    public String notFoundItem(Model model, NotFoundException ex) {
        Const.sys("notFoundItem");
        String type = ex.getType();

        String view = SecurityController.PROFILE;

        String message = null;

        switch (type) {

            case Const.KEY_AUTHOR_TYPE:

                message = messageSource.getMessage(
                        "error.author_not_found",null, LocaleContextHolder.getLocale());
                break;

            case Const.KEY_BOOK_TYPE:

                message = messageSource.getMessage(
                        "error.book_not_found",null,LocaleContextHolder.getLocale());
                break;

            case Const.KEY_USER_TYPE:
                logger.error("User wasn't found");
                message = messageSource.getMessage(
                        "error.user_not_found",null,LocaleContextHolder.getLocale());

                break;
        }

        model.addAttribute("item_not_found_error",message);

        return view;
    }

    @ExceptionHandler(SearchException.class)
    public String notFoundList(Model model, SearchException ex) {

        Const.sys("searchEx");
        String typeFirst = ex.getTypeFirst();


        String view = "/xml_parts/search_error.html";

        String message = null;

        switch (typeFirst) {

            case Const.KEY_AUTHOR_TYPE:

                message = messageSource.getMessage(
                        "error.authors_not_found",null, LocaleContextHolder.getLocale());
                break;

            case Const.KEY_BOOK_TYPE:

                message = messageSource.getMessage(
                        "error.books_not_found",null,LocaleContextHolder.getLocale());
                break;

            case Const.KEY_COUNTRY_TYPE:

                message = messageSource.getMessage(
                        "error.countries_not_found",null,LocaleContextHolder.getLocale());
                break;

            case Const.KEY_GENRE_TYPE:

                message = messageSource.getMessage(
                        "error.genres_not_found",null,LocaleContextHolder.getLocale());
                break;
        }

        model.addAttribute("list_not_found_error",message);

        return view;
    }
}
