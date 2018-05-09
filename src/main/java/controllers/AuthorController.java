package controllers;

import database.entity.Author;
import database.entity.Country;
import database.entity.User;
import database.entity.forms.AuthorForm;
import exceptions.AddPhotoException;
import exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.AddMainEntityService;
import services.SearchService;
import utils.Const;
import utils.MethodMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static utils.Const.*;

@Controller
@PreAuthorize("isAuthenticated()")
public class AuthorController {

    private static final String AUTHORS_LIST = "/authors";
    private static final String AUTHOR_ITEM = "/author/{id}";
    private static final String ADD_AUTHOR = "/add-author";

    private static final String AUTHOR_FORM = "author";
    private static final String NAME = "AC#";
    private static final String ADD_AUTHOR_GET = "addAuthor";

    @Autowired
    private MethodMap methodMap;

    @Autowired
    private AddMainEntityService addMainEntityService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = AUTHORS_LIST,method = RequestMethod.GET)
    public String showAuthors(Model model) {
        List<Author> authors = searchService.findPopularAuthors(Const.START_PAGE);
        model.addAttribute(AUTHORS_ATTR,authors);
        model.addAttribute(SIZE_ATTR,authors.size());
        model.addAttribute(Const.KEY_NUM_PAGE,Const.START_PAGE);

        return "authors/author_list";
    }

    @RequestMapping(value = AUTHOR_ITEM,method = RequestMethod.GET)
    public String showAuthor(@PathVariable("id") Integer id, Model model,
                             @AuthenticationPrincipal User user) {
        Author author;
        try {
            author = searchService.findAuthor(id,user.getId());
            if(author == null) {
                throw new Exception();
            }
        } catch (Exception ex){
            throw new NotFoundException(Const.KEY_AUTHOR_TYPE);
        }

        model.addAttribute(KEY_AUTHOR_TYPE,author);
        model.addAttribute(KEY_USER_TYPE,user);

        return "authors/author_content";

    }

    @RequestMapping(value = ADD_AUTHOR,method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String addAuthor(Model model) {

        List<Country> countries = searchService.findAllCountries();
        model.addAttribute(COUNTRIES_ATTR,countries);

        if(!model.containsAttribute(AUTHOR_FORM)) {
            AuthorForm author = new AuthorForm();
            model.addAttribute(AUTHOR_FORM, author);
        }

        return "addInstance/addAuthor";

    }

    @RequestMapping(value = ADD_AUTHOR,method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public String addAuthorPost(@ModelAttribute(AUTHOR_FORM) @Valid AuthorForm author,
                                BindingResult bindingResult, HttpServletRequest req,
                                RedirectAttributes attr) throws AddPhotoException {
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute(AUTHOR_FORM, author);
            attr.addFlashAttribute(BINDING_RESULT + AUTHOR_FORM,bindingResult);
            return methodMap.redirectReq(NAME + ADD_AUTHOR_GET);
        } else {
            System.out.println("userForm:\n");
            System.out.println(author.getBirthday());
            System.out.println(author.getDescription());
            System.out.println(author.getMotherland());
            System.out.println(author.getSurname());
            System.out.println(author.getName());
            System.out.println("authPhoto = " + author.getPhoto().getOriginalFilename());

            addMainEntityService.insertAuthor(author,req);
            attr.addFlashAttribute(UPDATE_MESSAGE,messageSource.getMessage(
                    "message.add_author",null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/profile";

    }

}
