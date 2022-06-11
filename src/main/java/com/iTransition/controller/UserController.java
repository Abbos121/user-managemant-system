package com.iTransition.controller;

import com.iTransition.entity.User;
import com.iTransition.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String userList(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("userList", users);
        return "users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, @AuthenticationPrincipal User user) {
        userService.delete(id);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (user.getId() == id) {
//            authentication.setAuthenticated(false);
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }
        return "redirect:/user/list";
    }

    @GetMapping("/block/{id}")
    public String blockOrUnblockUser(@PathVariable("id") long id,
                                     @AuthenticationPrincipal User user) {
        userService.blockOrUnblockUser(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (user.getId() == id) {
            authentication.setAuthenticated(false);
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }

        return "redirect:/user/list";
    }


}
