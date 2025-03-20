package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.exception.UserAuthenticationException;
import com.andersen.repository.user.UserRepositoryEntityImpl;


import java.sql.SQLException;

/**
 * Implementation of the {@link AuthService} interface for user authentication.
 * This service provides methods for logging in customers and admins, as well as registering new users.
 */
public class AuthServiceImp implements AuthService {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private final UserRepositoryEntityImpl userRepository;

    /**
     * Constructs a new AuthServiceImp with the specified UserRepository.
     *
     * @param userRepository the repository to manage user data
     * @throws IllegalArgumentException if userRepository is null
     */
    public AuthServiceImp(UserRepositoryEntityImpl userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null.");
        }
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a customer using their username and password.
     *
     * @param username the username of the customer
     * @param password the password of the customer
     * @return the authenticated Customer object
     * @throws UserAuthenticationException if the credentials are invalid or the customer is not found
     * @throws IllegalArgumentException if the username or password is null
     */
    @Override
    public Customer loginCustomer(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        try {
            User user = userRepository.getUserByUsername(username); // Retrieve user from repo

            // Verify password
            if (user != null && user.getPassword().equals(password)) {
                return (Customer) user; // Return customer if credentials match
            }
        } catch (SQLException e) {
            throw new UserAuthenticationException("Error during customer authentication: " + e.getMessage());
        }

        throw new UserAuthenticationException("Customer not found or invalid credentials.");
    }

    /**
     * Authenticates an admin using their username and password.
     *
     * @param username the username of the admin
     * @param password the password of the admin
     * @return the authenticated Admin object
     * @throws UserAuthenticationException if the credentials are invalid
     * @throws IllegalArgumentException if the username or password is null
     */
    @Override
    public Admin loginAdmin(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        // Check admin credentials
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            return new Admin(username, password); // Return new Admin object
        }

        throw new UserAuthenticationException("Admin not found or invalid credentials.");
    }

    /**
     * Registers a new user with the specified username and password.
     *
     * @param username the username for the new user
     * @param password the password for the new user
     * @throws UserAuthenticationException if the username already exists
     * @throws IllegalArgumentException if the username or password is null
     */
    @Override
    public void registerUser(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        try {
            User existingUser = userRepository.getUserByUsername(username);
            if (existingUser != null) {
                throw new UserAuthenticationException("Username already exists.");
            }

            // Create and register new customer
            Customer newUser = new Customer(username, password); // Assuming you have a constructor for Customer
            userRepository.registerCustomer(newUser);
        } catch (SQLException e) {
            throw new UserAuthenticationException("Error registering user: " + e.getMessage());
        }
    }
}