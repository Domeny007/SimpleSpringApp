package controllers;

import database.entity.Country;
import database.entity.User;
import database.entity.Gender;
import database.entity.forms.LoginForm;
import database.entity.forms.RegistrationForm;
import org.apache.log4j.Logger;
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
import services.IUserService;
import services.SearchService;
import utils.MethodMap;

import javax.validation.Valid;
import java.util.List;

import static utils.Const.*;


@Controller
public class SecurityController {

    private static final String FORBIDDEN = "/403";
    private static final String REGISTRATION = "/registration";

    public static final String LOGIN = "/login";
    public static final String PROFILE = "/profile" ;

    private static final String REGISTRATION_FORM = "registrationForm";
    private static final String LOGIN_FORM = "loginForm";

    public static final String NAME = "SC#";

    public static final String LOGIN_GET = "loginGet";
    public static final String REGISTRATION_GET = "registration";
    public static final String PROFILE_GET = "profileGet";
    public static final String LOGOUT_GET = "logout";

    @Autowired
    private MethodMap methodMap;

    @Autowired
    private IUserService userService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private Logger logger;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/")
    public String index(){
        return methodMap.redirectWithNullArgs(NAME + LOGIN_GET);
    }

    @RequestMapping(LOGIN)
    @PreAuthorize("isAnonymous()")
    public String loginGet(@RequestParam(name = "error", required = false) String error,
                           @RequestParam(name = "logout", required = false) String logout,
                           Model model){

        if(!model.containsAttribute(LOGIN_FORM)) {
            model.addAttribute(LOGIN_FORM, new LoginForm());
        }
        System.out.println("error " +  error + " logout = " + logout);
        if (error != null) {
            model.addAttribute("error", messageSource.getMessage(
                    "error.invalid_password",null, LocaleContextHolder.getLocale()));
        }
        if (logout != null) {
            model.addAttribute("msg", messageSource.getMessage(
                    "message.logout",null, LocaleContextHolder.getLocale()));
        }
        return "index";
    }

    @RequestMapping(value = PROFILE,method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String profileGet(Model model,@AuthenticationPrincipal User user) {
        User currentUser = (User) userService.loadUserByUsername(user.getUsername());
        model.addAttribute("user",currentUser);
        return "user/personal_page";
    }

    @RequestMapping(value = REGISTRATION,method = RequestMethod.GET)
    @PreAuthorize("isAnonymous()")
    public String registration(Model model) {

        if(!model.containsAttribute(REGISTRATION_FORM)) {
            model.addAttribute(REGISTRATION_FORM,new RegistrationForm());
        }
        List<Country> countries = searchService.findAllCountries();

        model.addAttribute(GENDERS_ATTR, Gender.values());
        model.addAttribute(COUNTRIES_ATTR,countries);

        return "main/registration";
    }

    @RequestMapping(value = REGISTRATION,method = RequestMethod.POST)
    @PreAuthorize("isAnonymous()")
    public String registrationPost(@ModelAttribute(REGISTRATION_FORM) @Valid RegistrationForm registrationForm,
                                   BindingResult bindingResult, RedirectAttributes attr) {

        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute(REGISTRATION_FORM, registrationForm);
            attr.addFlashAttribute(BINDING_RESULT + REGISTRATION_FORM,bindingResult);
            return methodMap.redirectReq(NAME + REGISTRATION_GET);
        } else {
            userService.registerUser(registrationForm);
            attr.addFlashAttribute(UPDATE_MESSAGE,messageSource.getMessage(
                    "message.registration",null, LocaleContextHolder.getLocale()));
        }
        return methodMap.redirectWithNullArgs(NAME + LOGIN_GET);
    }

    @RequestMapping(FORBIDDEN)
    public String forbidden() {
        logger.info("access denied");
        return "403";
    }

}
