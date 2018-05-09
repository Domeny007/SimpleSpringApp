package controllers;

import database.entity.Country;
import database.entity.Gender;
import database.entity.User;
import database.entity.forms.UserChangeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.IUserService;
import services.SearchService;
import utils.MethodMap;

import javax.validation.Valid;
import java.util.List;

import static utils.Const.*;

@Controller
@PreAuthorize("isAuthenticated()")
public class UserController {

    private static final String DELETE_USER = "/delete-user";
    private static final String CHANGE_PROFILE = "/change-personal";
    private static final String LOGOUT = "/logout";

    private static final String CHANGE_FORM = "userChangeForm";

    private final String CHANGE_PERSONAL_GET = "changePersonalGet";

    private final String NAME = "UC#";

    @Autowired
    private MethodMap methodMap;

    @Autowired
    private IUserService userService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = CHANGE_PROFILE,method = RequestMethod.GET)
    public String changePersonalGet(Model model,@AuthenticationPrincipal User user){
        List<Country> countries = searchService.findAllCountries();

        if(!model.containsAttribute(CHANGE_FORM)) {
            UserChangeForm userChangeForm = new UserChangeForm();
            userChangeForm.setUsernameReal(user.getUsernameReal());
            userChangeForm.setCountry(user.getCountry());
            userChangeForm.setGender(user.getGender().toString());
            model.addAttribute(CHANGE_FORM, userChangeForm);
        }
        model.addAttribute(GENDERS_ATTR, Gender.values());
        model.addAttribute(COUNTRIES_ATTR,countries);

        return "user/change_personal";
    }

    @RequestMapping(value = CHANGE_PROFILE,method = RequestMethod.POST)
    public String changePersonalPost(@ModelAttribute(CHANGE_FORM) @Valid UserChangeForm userChangeForm,
                                    BindingResult bindingResult, RedirectAttributes attr,
                                     @AuthenticationPrincipal User user) {
        if (bindingResult.hasErrors()) {
            attr.addFlashAttribute(CHANGE_FORM, userChangeForm);
            attr.addFlashAttribute(BINDING_RESULT + CHANGE_FORM,bindingResult);
            return methodMap.redirectReq(NAME + CHANGE_PERSONAL_GET);
        } else {
            userService.updateUser(user.getId(), userChangeForm);
            attr.addFlashAttribute(UPDATE_MESSAGE,messageSource.getMessage(
                    "message.edit_user",null, LocaleContextHolder.getLocale()));
        }
        return methodMap.redirectReq(
                SecurityController.NAME + SecurityController.PROFILE_GET);
    }

    @RequestMapping(value = DELETE_USER,method = RequestMethod.GET)
    public String deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getId());
        return "redirect:" + LOGOUT;
    }


}
