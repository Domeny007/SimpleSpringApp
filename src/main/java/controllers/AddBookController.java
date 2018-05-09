package controllers;

import database.entity.Country;
import database.entity.forms.BookForm;
import exceptions.AddPhotoException;
import exceptions.AddRelatedEntitiesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.AddMainEntityService;
import services.SearchService;
import utils.MethodMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static utils.Const.BINDING_RESULT;
import static utils.Const.COUNTRIES_ATTR;
import static utils.Const.UPDATE_MESSAGE;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AddBookController {

    private static final String ADD_BOOK = "/add-book";
    private static final String BIND_AUTHORS = "/bind-authors";
    private static final String BIND_COUNTRIES = "/bind-countries";
    private static final String BIND_GENRES = "/bind-genres";
    private static final String SAVE_BOOK_DATA = "/saveBookData";

    private final String BOOK_FORM = "bookForm";
    private final String NAME = "ABC#";
    private final String ADD_BOOK_GET = "addBook";

    @Autowired
    private AddMainEntityService addMainEntityService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private HttpSession session;

    @Autowired
    private MethodMap methodMap;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = ADD_BOOK,method = RequestMethod.GET)
    public String addBook(Model model) {
        if(!model.containsAttribute(BOOK_FORM)) {
            Object bookObject = session.getAttribute(BOOK_FORM);
            BookForm bookForm;
            if (bookObject != null) {
                bookForm = (BookForm) bookObject;
            } else {
                bookForm = new BookForm();
                session.setAttribute(BOOK_FORM, bookForm);
            }
            model.addAttribute(BOOK_FORM, bookForm);
        }

        return "addInstance/addBook";

    }

    @RequestMapping(value = ADD_BOOK,method = RequestMethod.POST)
    public String addBookPost(@ModelAttribute(BOOK_FORM) @Valid BookForm bookForm,
                              BindingResult bindingResult, HttpServletRequest req,
                              RedirectAttributes attr)
                                                throws AddPhotoException, AddRelatedEntitiesException {
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute(BOOK_FORM, bookForm);
            attr.addFlashAttribute(BINDING_RESULT + BOOK_FORM,bindingResult);
            return methodMap.redirectReq(NAME + ADD_BOOK_GET);
        } else {
            addMainEntityService.insertBook(bookForm,req);
            session.removeAttribute(BOOK_FORM);
            attr.addFlashAttribute(UPDATE_MESSAGE,messageSource.getMessage(
                    "message.add_book",null, LocaleContextHolder.getLocale()));
        }
        return methodMap.redirectReq(
                SecurityController.NAME + SecurityController.PROFILE_GET);

    }

    @RequestMapping(value = BIND_AUTHORS,method = RequestMethod.GET)
    public String bindAuthors(Model model) {
        List<Country> countries = searchService.findAllCountries();
        model.addAttribute(COUNTRIES_ATTR,countries);

        return "adminSearch/search-authors";

    }

    @RequestMapping(value = BIND_GENRES,method = RequestMethod.GET)
    public String bindGenres() {
        return "adminSearch/search-genres";

    }

    @RequestMapping(value = BIND_COUNTRIES,method = RequestMethod.GET)
    public String bindCountries() {
        return "adminSearch/search-countries";

    }

    @RequestMapping(value = BIND_AUTHORS,method = RequestMethod.POST)
    public String bindAuthorsPost(Model model,String authorsId, String authorsNames) {

        BookForm bookForm = (BookForm) session.getAttribute(BOOK_FORM);

        bookForm.setAuthorsId(authorsId);
        bookForm.setAuthorsNames(authorsNames);

        model.addAttribute(BOOK_FORM,bookForm);
        session.setAttribute(BOOK_FORM,bookForm);

        return methodMap.redirectReq(NAME + ADD_BOOK_GET);

    }

    @RequestMapping(value = BIND_GENRES,method = RequestMethod.POST)
    public String bindGenresPost(Model model, String genresId, String genresNames) {

        BookForm bookForm = (BookForm) session.getAttribute(BOOK_FORM);

        bookForm.setGenresId(genresId);
        bookForm.setGenresNames(genresNames);

        model.addAttribute(BOOK_FORM,bookForm);
        session.setAttribute(BOOK_FORM,bookForm);

        return methodMap.redirectReq(NAME + ADD_BOOK_GET);

    }

    @RequestMapping(value = BIND_COUNTRIES,method = RequestMethod.POST)
    public String bindCountriesPost(Model model, String countriesId, String countriesNames) {

        BookForm bookForm = (BookForm) session.getAttribute(BOOK_FORM);

        bookForm.setCountriesId(countriesId);
        bookForm.setCountriesNames(countriesNames);

        model.addAttribute(BOOK_FORM,bookForm);
        session.setAttribute(BOOK_FORM,bookForm);

        return methodMap.redirectReq(NAME + ADD_BOOK_GET);

    }

    @RequestMapping(value = SAVE_BOOK_DATA,method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveBookData(@RequestParam String name,
                                       @RequestParam String description,
                                       @RequestParam String authorsId,
                                       @RequestParam String genresId,
                                       @RequestParam String countriesId) {

        BookForm bookForm = (BookForm) session.getAttribute(BOOK_FORM);

        bookForm.setName(name);
        bookForm.setDescription(description);
        bookForm.setAuthorsId(authorsId);
        bookForm.setGenresId(genresId);
        bookForm.setCountriesId(countriesId);

        System.out.println("bookName = " + bookForm.getName());
        System.out.println("description = " + description);
        System.out.println("authorsId = " + authorsId);
        System.out.println("genresId = " + genresId);
        System.out.println("countriesId = " + countriesId);

        session.setAttribute(BOOK_FORM,bookForm);

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }


}
