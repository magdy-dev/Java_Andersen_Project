package org.andersen.service;


import org.andersen.entity.role.User;
import org.andersen.entity.users.Admin;
import org.andersen.entity.users.Customer;

import java.util.List;

public class AuthService {
    private final List<User> users;

    public AuthService(List<User> users) {
        this.users = users;
    }

    public Customer loginCustomer(String username, String password) {
        return users.stream()
                .filter(user -> user instanceof Customer &&
                        user.getUserName().equals(username) &&
                        user.getPassword().equals(password))
                .map(user -> (Customer) user)
                .findFirst()
                .orElse(null);
    }

    public Admin loginAdmin(String username, String password) {
        return users.stream()
                .filter(user -> user instanceof Admin &&
                        user.getUserName().equals(username) &&
                        user.getPassword().equals(password))
                .map(user -> (Admin) user)
                .findFirst()
                .orElse(null);
    }

    public void registerUser(String username, String password) {
        users.add(new Customer(username, password));
    }
}