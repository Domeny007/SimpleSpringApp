package database.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class Country implements Identified,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="country_id",unique = true,nullable = false)
    private Integer id;

    @Column
    @NotBlank(message = "{error.not_empty}")
    @Size(min = 1,max = 100,message = "{error.length}")
    private String name;

    public Country(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Country() {}

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Country){
            if(this.getId().equals(((Country) obj).getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 18 * hash + this.id;
        hash = 18 * hash + Objects.hashCode(this.name);
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
