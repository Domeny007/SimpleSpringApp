package services;

import database.entity.Author;
import database.entity.Book;
import database.entity.Country;
import database.entity.Genre;
import database.hibernate_repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utils.RepoSpecification;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RepoSpecification repoSpecification;

    public List<Country> findAllCountries() {
        return (List<Country>) countryRepository.findAll();
    }

    public Country findCountry(Integer id)  {
        return countryRepository.findOne(id);
    }

    public Genre findGenre(Integer id)  {
        return genreRepository.findOne(id);
    }

    public Book findBook(Integer bookId, Integer userId) {
        Book book =  bookRepository.findOne(bookId);
        Integer userMark = bookRepository.findUserMark(bookId,userId);
        book.setUserMark(userMark);

        return book;
    }

    public List<Book> findPopularBooks(Integer numPage) {
        return bookRepository.findPopular(numPage);
    }

    public List<Book> findBookByName(String bookName,Integer numPage) {
        return bookRepository.findByName(bookName,numPage);
    }

    public List<Book> findBookByNameCriteria(String bookName,Integer numPage) {
        Page<Book> bookPage = bookRepository.findAll(repoSpecification.getBookSpecByName(bookName),
                new PageRequest(numPage,5,new Sort(Sort.Direction.DESC, "name")
                ));

        return bookPage.getContent();
    }


    public List<Book> findPopularBooksCriteria(Integer numPage) {
        System.out.println("numbPage = " + numPage);
        Page<Book> bookPage = bookRepository.findAll(
                new PageRequest(numPage,5,new Sort(Sort.Direction.DESC, "mark")
                        .and(new Sort(Sort.Direction.DESC, "id"))
                ));

        return bookPage.getContent();
    }


    public Author findAuthor(Integer id) {
        return authorRepository.findOne(id);
    }

    public Author findAuthor(Integer authorId, Integer userId) {
        Author author =  authorRepository.findOne(authorId);
        Integer userMark = authorRepository.findUserMark(authorId,userId);
        author.setUserMark(userMark);

        return author;
    }


    public List<Author> findPopularAuthors(Integer numPage){
        return authorRepository.findPopular(numPage);
    }


    public List<Author> findAuthorByName(String queryAuthor,Integer numPage) {
        System.out.println("query = " + queryAuthor);
        String [] queryParts = divideQueryOnParts(queryAuthor);
        String authorName = queryParts[0];
        String authorSurname = queryParts[1];
        List<Author> authors = authorRepository.findByName(authorName,authorSurname,numPage);

        return authors;
    }

    public List<Country> findCountriesByName(String countryName) {
        System.out.println("query = " + countryName);
        List<Country> authors = countryRepository.findByName(countryName);

        for(Country author : authors){
            System.out.println("countryName =" + author.getName());
        }
        return authors;

    }

    public List<Genre> findGenresByName(String genreName) {
        List<Genre> authors = genreRepository.findByName(genreName);

        for(Genre author : authors){
            System.out.println("genreName =" + author.getName());
        }
        return authors;
    }

    private String[] divideQueryOnParts(String queryAuthor){
        String [] searchParts = queryAuthor.split(" ");
        String [] realParts = new String[2];
        int partsCount = 0;
        for(int i = 0; i < searchParts.length && partsCount < 2; i++){
            System.out.println(searchParts[i]);
            if(!searchParts[i].trim().equals("")){
                realParts[partsCount] = searchParts[i].trim();
                partsCount++;
            }
        }
        if(realParts[1] == null){
            realParts[1] = realParts[0];
        }
        System.out.println(realParts[0] + " : " + realParts[1]);
        return realParts;
    }
}
