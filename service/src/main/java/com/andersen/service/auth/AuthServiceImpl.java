package com.andersen.service.auth;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository.user.UserRepository;
import com.andersen.logger.ConsoleLogger;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import com.andersen.service.security.CustomPasswordEncoder;
import com.andersen.service.security.SessionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AuthService interface, providing authentication and
 * registration functionalities for users in the system.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs an AuthServiceImpl with the provided dependencies.
     *
     * @param userRepository        The repository that manages user data.
     * @param sessionManager        The session manager for handling user sessions.
     * @param customPasswordEncoder The password encoder for encoding passwords.
     * @param authenticationManager The authentication manager for authenticating users.
     */
    public AuthServiceImpl(UserRepository userRepository,
                           SessionManager sessionManager,
                           CustomPasswordEncoder customPasswordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.customPasswordEncoder = customPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username of the user trying to log in.
     * @param password The password of the user trying to log in.
     * @return The authenticated User object.
     * @throws AuthenticationException If the authentication fails due to invalid credentials.
     */
    @Override
    public User login(String username, String password) throws AuthenticationException {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            return userRepository.getUserByUsername(username);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    /**
     * Logs out a user by invalidating their session.
     *
     * @param token The session token of the user to log out.
     */
    @Override
    public void logout(String token) {
        sessionManager.invalidateSession(token);
        ConsoleLogger.log("User logged out");
    }

    /**
     * Registers a new customer in the system.
     *
     * @param username The username of the new customer.
     * @param password The password of the new customer.
     * @param email    The email of the new customer.
     * @param fullName The full name of the new customer.
     * @return The created User object.
     * @throws RegistrationException If registration fails due to invalid data or existing username.
     */
    @Override
    public User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException {
        validateRegistration(username, password, email, fullName);

        if (userRepository.getUserByUsername(username) != null) {
            throw new RegistrationException("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(customPasswordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setRole(UserRole.CUSTOMER);

        User createdUser = userRepository.save(newUser);
        ConsoleLogger.log("New customer registered: " + username);
        return createdUser;
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return The User object.
     * @throws DataAccessException If the user is not found.
     */
    @Override
    public User findById(Long id) throws DataAccessException {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataAccessException("User not found with id: " + id));
    }

    /**
     * Validates the registration data for a new user.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @param email    The email to validate.
     * @param fullName The full name to validate.
     * @throws RegistrationException If any validation rules are violated.
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
}
