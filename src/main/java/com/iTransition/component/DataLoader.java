package com.iTransition.component;

import com.iTransition.dao.UserRepository;
import com.iTransition.entity.User;
import com.iTransition.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {


    @Value("${spring.sql.init.mode}")
    private String initMode;

    private final UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("always")) {
            User user = new User("star mafia", "star@gmail.com", new BCryptPasswordEncoder().encode("root"), true);
            user.setRegisteredDate(LocalDateTime.now().format(UserServiceImpl.dateTimeFormatter));
            userRepository.save(user);

        }
    }
}