package controllers;

import database.entity.Book;
import database.entity.User;
import exceptions.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.SearchService;
import utils.Const;

import java.util.List;

import static utils.Const.KEY_BOOK_TYPE;
import static utils.Const.KEY_USER_TYPE;

@Controller
@PreAuthorize("isAuthenticated()")
public class BookController {

    private static final String BOOKS_LIST = "/books";
    private static final String BOOK_ITEM = "/book/{id}";

    private static final String NAME = "BC#";

    @Autowired
    private SearchService searchService;

    @Autowired
    private Logger logger;

    @RequestMapping(value = BOOKS_LIST,method = RequestMethod.GET)
    public String showBooks(Model model) {
        System.out.println("showBookListGET");

        List<Book> bookList = searchService.findPopularBooksCriteria(Const.START_PAGE);

        if(logger.isDebugEnabled()){
            logger.debug("found bookList size = " + bookList.size());
        }

        model.addAttribute("books",bookList);
        model.addAttribute("size",bookList.size());
        model.addAttribute(Const.KEY_NUM_PAGE,Const.START_PAGE);

        return "books/book_list";

    }

    @RequestMapping(value = BOOK_ITEM ,method = RequestMethod.GET)
    public String showBook(@PathVariable("id") Integer id,Model model,@AuthenticationPrincipal User user) {
        Book book;
        try {
            book = searchService.findBook(id, user.getId());
            if(book == null) {
                throw new Exception();
            }
        } catch (Exception ex){
            throw new NotFoundException(KEY_BOOK_TYPE);
        }

        System.out.println("authors book = " + book.getAuthors().get(0).getName());

        model.addAttribute(KEY_BOOK_TYPE,book);
        model.addAttribute(KEY_USER_TYPE,user);

        return "books/book_content";

    }









}
