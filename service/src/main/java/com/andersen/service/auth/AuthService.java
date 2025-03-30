package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;

public interface AuthService {
    User login(String username, String password) throws AuthenticationException;

    void logout(String token);

    User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException;
}