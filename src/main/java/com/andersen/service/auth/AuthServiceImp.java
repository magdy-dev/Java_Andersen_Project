package com.andersen.service.auth;


import com.andersen.entity.role.User;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.exception.UserAuthenticationException;

import java.util.List;

public class AuthServiceImp implements AuthService {
    private final List<User> users;

    public AuthServiceImp(List<User> users) {
        this.users = users;
    }

    public Customer loginCustomer(String username, String password) throws UserAuthenticationException {
        return users.stream()
                .filter(user -> user instanceof Customer &&
                        user.getUserName().equals(username) &&
                        user.getPassword().equals(password))
                .map(user -> (Customer) user)
                .findFirst()
                .orElseThrow(() -> new UserAuthenticationException("Customer not found or invalid credentials."));
    }

    public Admin loginAdmin(String username, String password) throws UserAuthenticationException {
        return users.stream()
                .filter(user -> user instanceof Admin &&
                        user.getUserName().equals("admin") &&
                        user.getPassword().equals("admin"))
                .map(user -> (Admin) user)
                .findFirst()
                .orElseThrow(() -> new UserAuthenticationException("Admin not found or invalid credentials."));
    }

    public void registerUser(String username, String password) throws UserAuthenticationException {

        if (users.stream().anyMatch(user -> user.getUserName().equals(username))) {
            throw new UserAuthenticationException("Username already exists.");
        }

        users.add(new Customer(username, password));
    }




}