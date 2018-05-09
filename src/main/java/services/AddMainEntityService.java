package services;

import database.entity.*;
import database.entity.forms.AuthorForm;
import database.entity.forms.BookForm;
import database.hibernate_repository.*;
import exceptions.AddPhotoException;
import exceptions.AddRelatedEntitiesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utils.Const;
import utils.SavePhotoUtil;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class AddMainEntityService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private SavePhotoUtil savePhotoUtil;

    @Transactional
    public void insertAuthor(AuthorForm authorForm, HttpServletRequest req) throws AddPhotoException {
        Author author = new Author();
        author.setName(authorForm.getName());
        author.setSurname(authorForm.getSurname());
        author.setBirthday(authorForm.getBirthday());
        author.setDescription(authorForm.getDescription());
        author.setMotherland(authorForm.getMotherland());

        String phPath = savePhotoUtil.savePhoto(req,authorForm.getPhoto(),Author.class);
        author.setPhoto(phPath);
        authorRepository.save(author);
    }

    @Transactional
    public void insertBook(BookForm bookForm, HttpServletRequest req) throws AddPhotoException, AddRelatedEntitiesException {
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setDescription(bookForm.getDescription());
        book.setYear(bookForm.getYear());
        book.setAuthors(getAuthors(bookForm.getAuthorsId()));
        book.setCountries(getCountries(bookForm.getCountriesId()));
        book.setGenres(getGenres(bookForm.getGenresId()));

        String phPath = savePhotoUtil.savePhoto(req,bookForm.getPhoto(),Book.class);
        book.setPhoto(phPath);

        System.out.println("user:\n");

        bookRepository.save(book);
    }

    private List<Country> getCountries(String countriesStr) throws AddRelatedEntitiesException {
        List<Integer> ids = getBindList(countriesStr);
        List<Country> countries = new ArrayList<>();
        for(Integer i : ids){
            Country country = searchService.findCountry(i);
            if(country == null){
                throw new AddRelatedEntitiesException(Const.KEY_COUNTRY_TYPE);
            }
            countries.add(searchService.findCountry(i));
        }

        for(Country country : countries){
            System.out.println("country = " + country.getId() + " " + country.getName());
        }
        return countries;
    }

    private List<Genre> getGenres(String genresStr) throws AddRelatedEntitiesException {
        List<Integer> ids = getBindList(genresStr);
        List<Genre> genres = new ArrayList<>();
        for(Integer i : ids){
            Genre genre =  searchService.findGenre(i);
            if(genre == null){
                throw new AddRelatedEntitiesException(Const.KEY_GENRE_TYPE);
            }
            genres.add(genre);
        }

        for(Genre genre : genres){
            System.out.println("country = " + genre.getId() + " " + genre.getName());
        }
        return genres;
    }

    private List<Author> getAuthors(String authorsStr) throws AddRelatedEntitiesException {
        List<Integer> ids = getBindList(authorsStr);
        List<Author> authors = new ArrayList<>();
        for(Integer i : ids){
            Author author = searchService.findAuthor(i);
            if(author == null){
                throw new AddRelatedEntitiesException(Const.KEY_AUTHOR_TYPE);
            }
            authors.add(author);
        }

        for(Author author : authors){
            System.out.println("country = " + author.getId() + " " + author.getName());
        }
        return authors;
    }

    private List<Integer> getBindList(String list){
        List<Integer> bindList = new ArrayList<>();
        String[] listStr = list.split(Const.SEPARATOR);
        for(String item : listStr){
            if(!item.equals("")){
                bindList.add(Integer.valueOf(item));
            }
        }
        return bindList;
    }
}

