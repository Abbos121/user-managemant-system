package com.iTransition.service;

import com.iTransition.dao.UserRepository;
import com.iTransition.dto.LoginDto;
import com.iTransition.entity.User;
import com.iTransition.exceptions.UserAlreadyExistException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService, ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm  dd.MM.yyyy");


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    // user found => 1 || user not found with this email => 0
    // password doesn't match => -1   || blocked user => -2
    @Override
    public int login(LoginDto loginDto) {
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        if (user.isPresent()) {
            if (user.get().getPassword().equals(loginDto.getPassword())) {
                if (!user.get().isActive()) return -2;
                System.out.println("logged in -------------------> ");
                updateLastLoginTime(user.get());
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void signUp(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User with this email already exist!", 40001);
        } else {
            user.setActive(true);
            user.setLastLoginDate(LocalDateTime.now().format(dateTimeFormatter));
            user.setRegisteredDate(LocalDateTime.now().format(dateTimeFormatter));
            userRepository.save(user);
        }
    }

    @Override
    public int delete(long id) {
        if (userRepository.findById(id).isEmpty()) return 0;
        userRepository.delete(userRepository.findById(id).get());
        return 1;
    }


    @Override
    public boolean blockOrUnblockUser(long id) {
        User user = userRepository.findById(id).get();
        if (user.isActive())
            user.setActive(false);
        else
            user.setActive(true);

        userRepository.save(user);
        return true;
    }

    public void updateLastLoginTime(User user) {
        user.setLastLoginDate(LocalDateTime.now().format(dateTimeFormatter));
        userRepository.save(user);
    }

    @Override
    public String getNameOfUser(String email) {
        return userRepository.findByEmail(email).get().getFirstName();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (userRepository.findByEmail(username).isPresent())
            return userRepository.findByEmail(username).get();
        else {
            System.out.println("Inside load user");
            return new User();
        }

    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();
        user.setLastLoginDate(LocalDateTime.now().format(dateTimeFormatter));
        userRepository.save(user);
    }
}
