package com.andersen.service.auth;

import com.andersen.entity.role.User;

import com.andersen.service.excption.AuthenticationException;
import com.andersen.service.excption.RegistrationException;

public interface AuthService {

    User login(String username, String password) throws AuthenticationException;

    User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException;

    void logout();


    User getCurrentUser();


    boolean isAdmin();


    boolean isAuthenticated();


}