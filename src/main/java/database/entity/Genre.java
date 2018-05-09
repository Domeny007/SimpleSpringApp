package database.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "genres")
public class Genre implements Identified,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "genre_id")
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "{error.not_empty}")
    @Size(min = 1,max = 50,message = "{error.length}")
    private String name;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Genre){
            if(this.getId().equals(((Genre) obj).getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.id;
        hash = 41 * hash + Objects.hashCode(this.name);
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
}
