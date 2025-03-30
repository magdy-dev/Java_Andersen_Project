package com.andersen.service.auth;

import com.andersen.entity.role.User;
import com.andersen.entity.role.UserRole;
import com.andersen.exception.DataAccessException;
import com.andersen.repository.user.UserRepository;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import com.andersen.logger.ConsoleLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Provides implementation for user authentication and registration services.
 * This service handles user login, logout, and registration of new customers.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    /**
     * Constructs a new AuthServiceImpl with the specified userRepository
     * and sessionManager.
     *
     * @param userRepository the repository for user data access
     * @param sessionManager the session manager for handling user sessions
     */
    @Autowired
    public AuthServiceImpl(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * Authenticates a user using the provided username and password.
     *
     * @param username the username of the user trying to log in
     * @param password the password of the user trying to log in
     * @return the authenticated User object
     * @throws AuthenticationException if authentication fails due to invalid credentials or if the service is unavailable
     */
    @Override
    public User login(String username, String password) throws AuthenticationException {
        try {
            validateCredentials(username, password);

            User user = userRepository.getUserByUsername(username)
                    .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

            String hashedInput = hashPassword(password);
            if (!user.getPassword().equals(hashedInput)) {
                throw new AuthenticationException("Invalid credentials");
            }

            sessionManager.createSession(user);
            ConsoleLogger.log("User logged in: " + username);
            return user;
        } catch (DataAccessException e) {
            ConsoleLogger.log("Login failed: " + e.getMessage());
            throw new AuthenticationException("Authentication service unavailable");
        }
    }

    /**
     * Logs out the user associated with the given session token.
     *
     * @param token the session token of the user to log out
     */
    @Override
    public void logout(String token) {
        sessionManager.invalidateSession(token);
        ConsoleLogger.log("User logged out");
    }

    /**
     * Registers a new customer with the specified user details.
     *
     * @param username the desired username of the new customer
     * @param password the desired password of the new customer
     * @param email the email address of the new customer
     * @param fullName the full name of the new customer
     * @return the created User object
     * @throws RegistrationException if registration fails due to validation errors or if the username already exists
     */
    @Override
    public User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException {
        try {
            validateRegistration(username, password, email, fullName);

            if (userRepository.getUserByUsername(username).isPresent()) {
                throw new RegistrationException("Username already exists");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(hashPassword(password));
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setRole(UserRole.CUSTOMER);

            User createdUser = userRepository.createUser(newUser);
            ConsoleLogger.log("New customer registered: " + username);
            return createdUser;
        } catch (DataAccessException | AuthenticationException e) {
            ConsoleLogger.log("Registration failed: " + e.getMessage());
            throw new RegistrationException("Registration service unavailable");
        }
    }

    /**
     * Validates the provided username and password for login.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @throws AuthenticationException if validation fails
     */
    private void validateCredentials(String username, String password) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password is required");
        }
    }

    /**
     * Validates the provided details for user registration.
     *
     * @param username the desired username to validate
     * @param password the desired password to validate
     * @param email the email address to validate
     * @param fullName the full name to validate
     * @throws RegistrationException if any of the validation checks fail
     */
    private void validateRegistration(String username, String password,
                                      String email, String fullName) throws RegistrationException {
        if (username == null || username.trim().isEmpty()) {
            throw new RegistrationException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RegistrationException("Password is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new RegistrationException("Email is required");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new RegistrationException("Full name is required");
        }
        if (password.length() < 8) {
            throw new RegistrationException("Password must be at least 8 characters");
        }
    }

    /**
     * Hashes the provided password using SHA-256 algorithm and encodes it in Base64.
     *
     * @param password the plain text password to hash
     * @return the hashed password as a Base64-encoded string
     * @throws AuthenticationException if hashing fails due to an unsupported algorithm
     */
    private String hashPassword(String password) throws AuthenticationException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            ConsoleLogger.log("Password hashing failed");
            throw new AuthenticationException("Authentication system error");
        }
    }
}