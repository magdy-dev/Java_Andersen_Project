package com.andersen.service.auth;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository.user.UserRepository;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import com.andersen.service.security.CustomPasswordEncoder;
import com.andersen.service.security.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private UserRepository userRepository;
    private SessionManager sessionManager;
    private CustomPasswordEncoder customPasswordEncoder;
    private AuthenticationManager authenticationManager;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        sessionManager = mock(SessionManager.class);
        customPasswordEncoder = mock(CustomPasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);

        authService = new AuthServiceImpl(userRepository, sessionManager, customPasswordEncoder, authenticationManager);
    }

    @Test
    void login_ShouldSucceed_WhenCredentialsAreValid() throws AuthenticationException {
        String username = "user1";
        String password = "password";

        User mockUser = new User();
        mockUser.setUsername(username);


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(org.springframework.security.core.Authentication.class));

        when(userRepository.getUserByUsername(username)).thenReturn(mockUser);

        User result = authService.login(username, password);

        assertEquals(mockUser, result);
        verify(sessionManager).createSession(mockUser);
    }

    @Test
    void login_ShouldFail_WhenCredentialsAreInvalid() {
        String username = "user1";
        String password = "wrongpass";

        doThrow(new BadCredentialsException("Invalid")).when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        assertThrows(AuthenticationException.class, () -> authService.login(username, password));
    }

    @Test
    void logout_ShouldInvalidateSession() {
        String token = "session-token";

        authService.logout(token);

        verify(sessionManager).invalidateSession(token);
    }

    @Test
    void registerCustomer_ShouldSucceed_WhenValidInput() throws RegistrationException {
        String username = "newUser";
        String password = "securePass123";
        String email = "email@example.com";
        String fullName = "Test User";

        when(userRepository.getUserByUsername(username)).thenReturn(null);
        when(customPasswordEncoder.encode(password)).thenReturn("encodedPass");

        User savedUser = new User();
        savedUser.setUsername(username);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.registerCustomer(username, password, email, fullName);

        assertEquals(username, result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerCustomer_ShouldThrow_WhenUsernameExists() {
        String username = "existingUser";
        when(userRepository.getUserByUsername(username)).thenReturn(new User());

        assertThrows(RegistrationException.class, () ->
                authService.registerCustomer(username, "pass12345", "email", "Full Name"));
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() throws DataAccessException {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = authService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void findById_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DataAccessException.class, () -> authService.findById(99L));
    }
}
