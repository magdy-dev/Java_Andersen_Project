package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.exception.UserAuthenticationException;

import com.andersen.logger.UserOutputLogger;
import org.slf4j.Logger;

import java.util.List;

public class AuthServiceImp implements AuthService {
    private static final Logger logger = UserOutputLogger.getLogger(AuthServiceImp.class); // For internal logs
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private final List<User> users;

    public AuthServiceImp(List<User> users) {
        if (users == null) {
            logger.error("User list cannot be null.");
            throw new IllegalArgumentException("User list cannot be null.");
        }
        this.users = users;
        logger.info("AuthServiceImp initialized with {} users.", users.size());
    }

    @Override
    public Customer loginCustomer(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            logger.error("Username or password cannot be null.");
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        logger.debug("Attempting to log in customer with username: {}", username);

        return users.stream()
                .filter(user -> user instanceof Customer &&
                        user.getUserName().equals(username) &&
                        user.getPassword().equals(password))
                .map(user -> (Customer) user)
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Customer login failed for username: {}", username);
                    UserOutputLogger.log("Invalid username or password. Please try again."); // User-facing message
                    return new UserAuthenticationException("Customer not found or invalid credentials.");
                });
    }

    @Override
    public Admin loginAdmin(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            logger.error("Username or password cannot be null.");
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        logger.debug("Attempting to log in admin with username: {}", username);

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            UserOutputLogger.log("Admin logged in successfully."); // User-facing message
            return new Admin(username, password);
        }

        logger.error("Admin login failed for username: {}", username);
        UserOutputLogger.log("Invalid admin credentials. Please try again."); // User-facing message
        throw new UserAuthenticationException("Admin not found or invalid credentials.");
    }

    @Override
    public void registerUser(String username, String password) throws UserAuthenticationException {
        if (username == null || password == null) {
            logger.error("Username or password cannot be null.");
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        logger.debug("Attempting to register user with username: {}", username);

        if (users.stream().anyMatch(user -> user.getUserName().equals(username))) {
            logger.error("Username already exists: {}", username);
            UserOutputLogger.log("Username already exists. Please choose a different username."); // User-facing message
            throw new UserAuthenticationException("Username already exists.");
        }

        users.add(new Customer(username, password));
        logger.info("User registered successfully: {}", username);
        UserOutputLogger.log("User registered successfully: " + username); // User-facing message
    }
}