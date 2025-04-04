package com.andersen.service.auth;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository_Criteria.user.UserRepository;
import com.andersen.service.Security.PasswordEncoder;
import com.andersen.service.Security.SessionManager;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the AuthService interface that handles user authentication and registration functionalities.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, SessionManager sessionManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user by verifying the provided username and password.
     *
     * @param username the user's username
     * @param password the user's password
     * @return the authenticated User object
     * @throws AuthenticationException if the credentials are invalid
     * @throws DataAccessException     if there is an error accessing the user data
     */
    @Override
    public User login(String username, String password) throws AuthenticationException, DataAccessException {
        validateCredentials(username, password);

        Optional<User> userOpt = userRepository.getUserByUsername(username);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        User user = userOpt.get();
        sessionManager.createSession(user);
        logger.info("User logged in: {}", username);
        return user;
    }

    /**
     * Logs out the user by invalidating the session associated with the given token.
     *
     * @param token the session token to be invalidated
     */
    @Override
    public void logout(String token) {
        sessionManager.invalidateSession(token);
        logger.info("User logged out with token: {}", token);
    }

    /**
     * Registers a new customer with the provided username, password, email, and full name.
     *
     * @param username the desired username for the new customer
     * @param password the password for the new customer
     * @param email    the email for the new customer
     * @param fullName the full name of the new customer
     * @return the created User object
     * @throws RegistrationException if registration fails due to validation issues
     * @throws DataAccessException   if there is an error accessing the user data
     */
    @Override
    public User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException, DataAccessException {
        validateRegistration(username, password, email, fullName);

        if (userRepository.getUserByUsername(username).isPresent()) {
            throw new RegistrationException("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Use PasswordEncoder
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setRole(UserRole.CUSTOMER);

        User createdUser = userRepository.createUser(newUser);
        logger.info("New customer registered: {}", username);
        return createdUser;
    }

    /**
     * Validates the provided login credentials.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @throws AuthenticationException if the credentials are invalid
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
     * Validates the registration information for a new user.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @param email    the email to validate
     * @param fullName the full name to validate
     * @throws RegistrationException if
     *                               any of the provided registration information is invalid
     */
    private void validateRegistration(String username, String password, String email, String fullName) throws RegistrationException {
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
}