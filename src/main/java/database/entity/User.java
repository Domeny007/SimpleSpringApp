package database.entity;


import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements Identified,CredentialsContainer, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "email",unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = "username")
    private String usernameReal;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JoinColumn(name="country_id")
    private Country country;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.MERGE})
    @JoinTable(
            name = "users_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    private List<UserAuthority> authorities = new ArrayList<>();

    public User() {
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User){
            if(this.getId().equals(((User) obj).getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 78 * hash + this.id;
        hash = 78 * hash + Objects.hashCode(this.usernameReal);
        hash = 78 * hash + Objects.hashCode(this.username);
        hash = 78 * hash + Objects.hashCode(this.password);
        hash = 78 * hash + Objects.hashCode(this.country);
        hash = 78 * hash + Objects.hashCode(this.gender);
        return hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsernameReal(String username) {
        this.usernameReal = username;
    }

    public String getUsernameReal() {
        return usernameReal;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addAuthority(UserAuthority authority) {
        this.authorities.add(authority);
    }

    @Override
    public List<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

}

