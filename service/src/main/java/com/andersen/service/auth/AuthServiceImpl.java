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
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           SessionManager sessionManager,
                           CustomPasswordEncoder customPasswordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
        this.customPasswordEncoder = customPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User login(String username, String password) throws AuthenticationException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.getUserByUsername(username);
            sessionManager.createSession(user); // Create token for tracking if needed
            return user;
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    @Override
    public void logout(String token) {
        sessionManager.invalidateSession(token);
        ConsoleLogger.log("User logged out with token: " + token);
    }

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

    @Override
    public User findById(Long id) throws DataAccessException {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataAccessException("User not found with id: " + id));
    }

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
