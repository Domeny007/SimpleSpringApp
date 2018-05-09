package utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Const {

    public final static String DATABASE = "spring_database";

    public final static String SCHEMA = "public";

    public static final Integer START_PAGE = 0;

    public static final String KEY_NUM_PAGE = "numPage";

    public static final String SEPARATOR = ",";

    public static final String KEY_BOOK_AUTHORS = "book_authors";
    public static final String KEY_BOOK_COUNTRIES = "book_countries";
    public static final String KEY_BOOK_GENRES = "book_genres";

    public static final String KEY_BOOK_AUTHORS_NAMES = "actorsNames";
    public static final String KEY_BOOK_COUNTRIES_NAMES = "countriesNames";
    public static final String KEY_BOOK_GENRES_NAMES = "genresNames";

    //images
    public static final String AUTHOR_IMAGES = "/images/authors";
    public static final String BOOK_IMAGES = "/images/books";

    //константы для people
    public static final String KEY_AUTHOR_TYPE = "author";
    public static final String KEY_GENRE_TYPE = "genre";
    public static final String KEY_COUNTRY_TYPE = "country";
    public static final String KEY_BOOK_TYPE = "book";
    public static final String KEY_USER_TYPE = "user";
    public static final String KEY_MENU_TYPE = "menu";

    public static final String GENDERS_ATTR = "genders";
    public static final String COUNTRIES_ATTR = "countries";
    public static final String GENRES_ATTR = "genres";
    public static final String BOOKS_ATTR = "books";
    public static final String AUTHORS_ATTR = "authors";
    public static final String REVIEWS_ATTR = "reviews";

    public static final String SIZE_ATTR = "size";
    public static final String TITLE = "title";
    public static final String UPDATE_MESSAGE = "update_message";

    public static final String USER_SESSION = "user";

    public static final String USUAL_AUTHORITY = "ROLE_USER";

    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";


    public static final String BINDING_RESULT = "org.springframework.validation.BindingResult.";


    public static void sys(String str){
        System.out.println(str);
    }
}
