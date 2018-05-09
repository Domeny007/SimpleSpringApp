package database.entity.review;

import database.entity.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "book_reviews")
public class BookReview implements Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id",unique = true,nullable = false)
    private Integer id;

    @Column
    @NotBlank(message = "{error.not_empty}")
    @Size(min = 1,max = 100,message = "{error.length}")
    private String title;

    @Column
    @NotBlank(message = "{error.not_empty}")
    @Size(min = 1,max = 2000,message = "{error.length}")
    private String content;

    @ManyToOne
    @JoinColumn(name="writer_id",referencedColumnName = "user_id",nullable=false)
    private User writer;

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
    private Book book;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BookReview){
            if(this.getId().equals(((BookReview) obj).getId())){
                return true;
            }
        }
        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
