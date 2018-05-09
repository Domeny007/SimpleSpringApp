package database.entity;

import database.entity.review.AuthorReview;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author implements Identified,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "author_id",unique = true,nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Temporal(value = TemporalType.DATE)
    private Date birthday;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name="motherland",referencedColumnName = "country_id")
    private Country motherland;

    @Column
    private String photo;

    @Generated(GenerationTime.ALWAYS)
    @Column(insertable = false, updatable = false)
    private Integer mark;

    @Transient
    private Integer userMark;

    @Column
    private String description;

    @OneToMany(mappedBy = "author",fetch = FetchType.LAZY)
    private List<AuthorReview> reviews;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Author){
            if(this.getId().equals(((Author) obj).getId())){
                return true;
            }
        }
        return false;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Country getMotherland() {
        return motherland;
    }

    public void setMotherland(Country motherland) {
        this.motherland = motherland;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AuthorReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<AuthorReview> reviews) {
        this.reviews = reviews;
    }

    public Integer getUserMark() {
        return userMark;
    }

    public void setUserMark(Integer userMark) {
        this.userMark = userMark;
    }
}