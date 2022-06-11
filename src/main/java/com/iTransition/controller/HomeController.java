package com.iTransition.controller;

import com.iTransition.dto.LoginDto;
import com.iTransition.entity.User;
import com.iTransition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class HomeController {
    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/login")
    public String showLoginPage() {
        return "login";
    }


    @GetMapping("/registration")
    public String showFormForAdd(Model model) {
        User user = new User();
        model.addAttribute("myUser", user);
        return "registration-form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("myUser") User user
    ) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.signUp(user);
        return "redirect:/user/list";
    }

    @GetMapping("/home")
    public String homePage() {
        return "index";
    }


}
