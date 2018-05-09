package database.entity;

import database.entity.review.BookReview;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book implements Identified,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id",unique = true,nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private Integer year;

    @Generated(GenerationTime.ALWAYS)
    @Column(insertable = false,updatable = false)
    private Integer mark;

    @Transient
    private Integer userMark;

    @Column
    private String photo;

    @ManyToMany(cascade = { CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_countries",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "country_id") }
    )
    private List<Country> countries;

    @ManyToMany(cascade = { CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_genres",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "genre_id") }
    )
    private List<Genre> genres;

    @ManyToMany(cascade = { CascadeType.MERGE },fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "author_id") }
    )
    private List<Author> authors;

    @OneToMany(mappedBy = "book",fetch = FetchType.LAZY)
    private List<BookReview> reviews;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Book){
            if(this.getId().equals(((Book) obj).getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.id;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.description);
        hash = 23 * hash + Objects.hashCode(this.year);
        hash = 23 * hash + Objects.hashCode(this.mark);
        hash = 23 * hash + Objects.hashCode(this.photo);
        hash = 23 * hash + Objects.hashCode(this.countries);
        hash = 23 * hash + Objects.hashCode(this.genres);
        return hash;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<BookReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<BookReview> reviews) {
        this.reviews = reviews;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Integer getUserMark() {
        return userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }
}

