package controllers.handler;

import controllers.SecurityController;
import exceptions.AddPhotoException;
import exceptions.AddRelatedEntitiesException;
import exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import utils.Const;

@ControllerAdvice
public class ExceptionHandlerController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(AddPhotoException.class)
    public String photoExceptionHandler(AddPhotoException ex, Model model){
        Const.sys("photoExceptionHandler");
        String type = ex.getType();

        String view = "/profile";

        String message = messageSource.getMessage(
                "error.add_photo",null, LocaleContextHolder.getLocale());

        switch (type) {

            case Const.KEY_AUTHOR_TYPE:

                view = "/add-author";
                break;

            case Const.KEY_BOOK_TYPE:

                view = "/add-book";
                break;
        }

        model.addAttribute("photo_error",message);

        return view;
    }

    @ExceptionHandler(AddRelatedEntitiesException.class)
    public String addRelatedEntityHandler(AddRelatedEntitiesException ex, Model model){
        Const.sys("addRelatedEntityHandler");
        String type = ex.getType();

        String view = "/add-book";

        String message = null;

        switch (type) {

            case Const.KEY_AUTHOR_TYPE:

                message = messageSource.getMessage(
                        "error.bind_author",null,LocaleContextHolder.getLocale());
                break;

            case Const.KEY_GENRE_TYPE:

                message = messageSource.getMessage(
                        "error.bind_genre",null,LocaleContextHolder.getLocale());
                break;

            case Const.KEY_COUNTRY_TYPE:

                message = messageSource.getMessage(
                        "error.bind_country",null,LocaleContextHolder.getLocale());
                break;
        }

        model.addAttribute("add_related_error",message);

        return view;
    }

}
