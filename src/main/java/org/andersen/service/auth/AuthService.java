package org.andersen.service.auth;

import org.andersen.entity.users.Admin;
import org.andersen.entity.users.Customer;
import org.andersen.exception.UserAuthenticationException;

public interface AuthService {
    Customer loginCustomer(String username, String password) throws UserAuthenticationException;
    Admin loginAdmin(String username, String password) throws UserAuthenticationException;
    void registerUser(String username, String password) throws UserAuthenticationException;
}
