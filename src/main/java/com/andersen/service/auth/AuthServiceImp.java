package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.users.Admin;
import com.andersen.entity.users.Customer;
import com.andersen.exception.UserAuthenticationException;
import com.andersen.logger.Loge;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link AuthService} interface for user authentication.
 * This service provides methods for logging in customers and admins, as well as registering new users.
 */
public class AuthServiceImp implements AuthService {
    private static final Logger logger = Loge.getLogger(AuthServiceImp.class); // For internal logs
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private final List<User> users;

    /**
     * Constructs a new AuthServiceImp with the specified list of users.
     *
     * @param users the list of users for authentication
     * @throws IllegalArgumentException if the provided user list is null
     */
    public AuthServiceImp(List<User> users) {
        if (users == null) {
            logger.error("User list cannot be null.");
            throw new IllegalArgumentException("User list cannot be null.");
        }
        this.users = users;
        logger.info("AuthServiceImp initialized with {} users.", users.size());
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
            logger.error("Username or password cannot be null.");
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        logger.debug("Attempting to log in customer with username: {}", username);

        // Optional to find the customer
        Optional<Customer> customerOptional = users.stream()
                .filter(user -> user instanceof Customer &&
                        user.getUserName().equals(username) &&
                        user.getPassword().equals(password))
                .map(user -> (Customer) user)
                .findFirst();

        return customerOptional.orElseThrow(() -> {
            logger.error("Customer login failed for username: {}", username);
            logger.info("Invalid username or password. Please try again.");
            return new UserAuthenticationException("Customer not found or invalid credentials.");
        });
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
            logger.error("Username or password cannot be null.");
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        logger.debug("Attempting to log in admin with username: {}", username);

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            logger.info("Admin logged in successfully.");
            return new Admin(username, password);
        }

        logger.error("Admin login failed for username: {}", username);
        logger.info("Invalid admin credentials. Please try again.");
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
            logger.error("Username or password cannot be null.");
            throw new IllegalArgumentException("Username or password cannot be null.");
        }

        logger.debug("Attempting to register user with username: {}", username);

        if (users.stream().anyMatch(user -> user.getUserName().equals(username))) {
            logger.error("Username already exists: {}", username);
            logger.info("Username already exists. Please choose a different username.");
            throw new UserAuthenticationException("Username already exists.");
        }

        users.add(new Customer(username, password));
        logger.info("User registered successfully: {}", username);
    }
}