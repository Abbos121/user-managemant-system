package com.iTransition.service;

import com.iTransition.dto.LoginDto;
import com.iTransition.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    int login(LoginDto loginDto);
    void signUp(User user);
    int delete(long id);
    boolean blockOrUnblockUser(long id);
    String getNameOfUser(String email);
}
