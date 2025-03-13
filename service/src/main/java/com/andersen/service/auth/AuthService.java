package com.andersen.service.auth;

import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.exception.UserAuthenticationException;

public interface AuthService {
    Customer loginCustomer(String username, String password) throws UserAuthenticationException;
    Admin loginAdmin(String username, String password) throws UserAuthenticationException;
    void registerUser(String username, String password) throws UserAuthenticationException;
}
