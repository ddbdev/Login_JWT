package com.example.springsecurityproject.entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

/**
 * The User Entity is handling the user interface, it implements UserDetails to manage the authentication
 * (More in JwtUsernameAndPasswordAuthenticationFilter.java)
 * The field id is auto incremented for each new user
 * Username is the one that the user set and same with the password, all the other field are set to true except one -
 * isEnabled - that will be set to true once the user will be confirmed in the "confirm?token=".
 * The token is generated immediately after a user is registered as you can see.
 * Every user has to have a Collection of authorities.
 */
@Data
@Entity(name = "users")
@NoArgsConstructor
public class UserEntity implements UserDetails {

    public UserEntity(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isAccountNonExpired = true;
    @Column(nullable = false)
    private boolean isAccountNonLocked = true;
    @Column(nullable = false)
    private boolean isCredentialNonExpired = true;
    @Column(nullable = false)
    private boolean isEnabled = false;
    private String confirmToken = RandomString.make(60);
    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now();
    private LocalDate confirmedAt = null;
    @Transient
    private Collection<SimpleGrantedAuthority> authorities;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
