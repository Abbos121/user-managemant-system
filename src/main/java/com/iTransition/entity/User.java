package com.iTransition.entity;

import com.iTransition.dao.UserRepository;
import com.iTransition.service.UserServiceImpl;
import lombok.*;

import org.springframework.context.ApplicationListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.iTransition.service.UserServiceImpl.dateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "name")
    private String firstName;

    @NotBlank
    @Email(message = "Please enter valid email e.g. someone@gmail.com")
    private String email;

//    @NotBlank
//    @Size(min = 6, max = 24, message = "The length of password must be between 6 and 24")
    private String password;

    @Column(name = "last_login_date")
    private String lastLoginDate;

    @Column(name = "registered_date", updatable = false)
    @CreatedDate
    private String registeredDate;

    @Column(name = "is_active")
    private boolean isActive;

    public User(String name, String email, String password) {
        this.firstName=name;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, boolean isActive) {
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    public User(String firstName, String email, String password, boolean isActive) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
