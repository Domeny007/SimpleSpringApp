package controllers;

import database.entity.*;
import exceptions.SearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import services.SearchService;
import utils.Const;

import java.util.ArrayList;
import java.util.List;

import static utils.Const.*;

@Controller(value = "FindController")
@PreAuthorize("isAuthenticated()")
public class SearchController {

    private static final String SEARCH_BOOKS = "/searchBooks";
    private static final String SEARCH_AUTHORS = "/searchAuthors";
    private static final String SEARCH_COUNTRIES = "/searchCountries";
    private static final String SEARCH_GENRES = "/searchGenres";

    private static final String NAME = "FC#";

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = SEARCH_BOOKS,method = RequestMethod.GET)
    public String searchBooks(Model model,String query,String type,String numPage) {
        String view = "";
        try {
            List<Book> books;
            if (query != null && !query.trim().equals("")) {
                books = searchService.findBookByNameCriteria(query, Integer.valueOf(numPage));
                model.addAttribute(TITLE, "Найденные по данному имени");
            } else {
                books = searchService.findPopularBooksCriteria(Integer.valueOf(numPage));
                model.addAttribute(TITLE, "Самые популярные");
            }

            model.addAttribute(BOOKS_ATTR, books);
            model.addAttribute(SIZE_ATTR, books.size());

            switch (type) {
                case KEY_USER_TYPE:
                    view = "xml_parts/user/user_books";
                    break;

                case KEY_MENU_TYPE:
                    view = "xml_parts/menu_search";
                    break;
            }
        } catch (Exception ex) {
            throw new SearchException(Const.KEY_BOOK_TYPE);
        }
        return view;
    }

    @RequestMapping(value = SEARCH_AUTHORS,method = RequestMethod.GET)
    public String searchAuthors(Model model,@RequestParam String query,
                                 @RequestParam String type,@RequestParam String numPage) {
        String view = "";
        try {
            List<Author> authors;
            if (query != null && !query.trim().equals("")) {
                authors = searchService.findAuthorByName(query, Integer.valueOf(numPage));
                model.addAttribute(TITLE, "Найденные по данному имени");
            } else {
                authors = searchService.findPopularAuthors(Integer.valueOf(numPage));
                model.addAttribute(TITLE, "Самые популярные");
            }
            model.addAttribute(AUTHORS_ATTR, authors);
            model.addAttribute(SIZE_ATTR, authors.size());

            switch (type) {
                case "user":
                    view = "xml_parts/user/user_authors";
                    break;

                case "movie":
                    view = "xml_parts/admin/authors";
                    break;

            }
        } catch(Exception ex) {
            throw new SearchException(Const.KEY_AUTHOR_TYPE);
        }
        return view;
    }

    @RequestMapping(value = SEARCH_COUNTRIES,method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String searchCountries(Model model,@RequestParam String query) {
        try {
            List<Country> countries = searchService.findCountriesByName(query);
            model.addAttribute(COUNTRIES_ATTR, countries);
        }catch (Exception ex){
            throw new SearchException(Const.KEY_COUNTRY_TYPE);
        }
        return "xml_parts/admin/countries";
    }

    @RequestMapping(value = SEARCH_GENRES,method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public String searchGenres(Model model,@RequestParam String query) {
        try {
            List<Genre> genres = searchService.findGenresByName(query);
            model.addAttribute(GENRES_ATTR, genres);
        } catch (Exception e){
            throw new SearchException(Const.KEY_GENRE_TYPE);
        }
        return "xml_parts/admin/genres";
    }
}
