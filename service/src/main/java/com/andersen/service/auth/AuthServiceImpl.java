package com.andersen.service.auth;


import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;
import com.andersen.domain.repository.user.UserRepository;
import com.andersen.service.dto.userrole.AuthResponseDto;
import com.andersen.service.security.JwtTokenProvider;
import com.andersen.service.security.CustomPasswordEncoder;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AuthService for user authentication and registration.
 * <p>
 * This service handles user login, registration, and retrieval of user details.
 * It utilizes JWT for token generation and secures passwords using a custom
 * password encoder. It interacts with the user repository to perform
 * data access operations.
 * </p>
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Constructs an AuthServiceImpl with the specified JWT token provider,
     * password encoder, and user repository.
     *
     * @param jwtTokenProvider the JWT token provider for generating tokens
     * @param passwordEncoder  the custom password encoder for encoding passwords
     * @param userRepository   the user repository for accessing user data
     */
    @Autowired
    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider,
                           CustomPasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username the username of the user trying to log in
     * @param password the password of the user trying to log in
     * @return an AuthResponseDto containing the authentication token and user role
     * @throws AuthenticationException if the username or password is invalid
     * @throws DataAccessException     if there is an error accessing user data
     */
    @Override
    public AuthResponseDto login(String username, String password)
            throws AuthenticationException {
        User user = userRepository.getByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
            return new AuthResponseDto(user.getId(), user.getUsername(), token);
        } else {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    /**
     * Registers a new customer with the provided details.
     *
     * @param username the desired username for the new customer
     * @param password the desired password for the new customer
     * @param email    the email address for the new customer
     * @param fullName the full name of the new customer
     * @return the newly registered User object
     * @throws RegistrationException   if the username is already taken
     * @throws AuthenticationException if the registration fails for some reason
     * @throws DataAccessException     if there is an error accessing user data
     */
    @Override
    public User registerCustomer(String username, String password, String email, String fullName)
            throws RegistrationException, AuthenticationException, DataAccessException {

        User existingUser = userRepository.getByUsername(username);
        if (existingUser != null) {
            throw new RegistrationException("Username already taken");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Hash the password
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setRole(UserRole.CUSTOMER); // Default role

        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            throw new DataAccessException("Error saving the user to the database");
        }

        return newUser;
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the User object associated with the given identifier
     * @throws DataAccessException if the user is not found or there is an error accessing user data
     */
    @Override
    public User findById(Long id) throws DataAccessException {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataAccessException("User not found"));
    }
}