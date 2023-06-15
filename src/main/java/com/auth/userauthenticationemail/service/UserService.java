package com.auth.userauthenticationemail.service;

import com.auth.userauthenticationemail.model.User;

public interface UserService {

    User getUserByEmail(String email);

    void saveUser(User user);
}
