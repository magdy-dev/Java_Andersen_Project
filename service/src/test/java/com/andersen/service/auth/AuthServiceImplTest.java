package com.andersen.service.auth;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;
import com.andersen.domain.exception.DataAccessException;
import com.andersen.domain.repository.user.UserRepository;
import com.andersen.service.exception.AuthenticationException;
import com.andersen.service.exception.RegistrationException;
import com.andersen.service.security.CustomPasswordEncoder;
import com.andersen.service.security.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    private UserRepository userRepository;
    private CustomPasswordEncoder customPasswordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        customPasswordEncoder = mock(CustomPasswordEncoder.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);

        authService = new AuthServiceImpl(jwtTokenProvider, customPasswordEncoder, userRepository);
    }

    @Test
    void login_ShouldSucceed_WhenCredentialsAreValid() throws AuthenticationException, DataAccessException {
        String username = "user1";
        String password = "password";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword("hashedPassword");
        mockUser.setRole(UserRole.CUSTOMER);
        mockUser.setId(1L);

        when(userRepository.getByUsername(username)).thenReturn(mockUser);
        when(customPasswordEncoder.matches(password, "hashedPassword")).thenReturn(true);
        when(jwtTokenProvider.createToken(username, UserRole.CUSTOMER)).thenReturn("jwt-token");

        var result = authService.login(username, password);

        assertEquals("jwt-token", result.toString());
        assertEquals("user1", result.getUsername());
        assertEquals(1L, result.getId());
    }

    @Test
    void login_ShouldThrow_WhenPasswordDoesNotMatch() {
        String username = "user1";
        String password = "wrongpass";

        User user = new User();
        user.setUsername(username);
        user.setPassword("hashed");

        when(userRepository.getByUsername(username)).thenReturn(user);
        when(customPasswordEncoder.matches(password, "hashed")).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authService.login(username, password));
    }

    @Test
    void registerCustomer_ShouldSucceed_WhenValidInput() throws RegistrationException, AuthenticationException, DataAccessException {
        String username = "newUser";
        String password = "securePass123";
        String email = "email@example.com";
        String fullName = "Test User";

        when(userRepository.getByUsername(username)).thenReturn(null);
        when(customPasswordEncoder.encode(password)).thenReturn("encodedPass");

        User savedUser = new User();
        savedUser.setUsername(username);
        savedUser.setPassword("encodedPass");
        savedUser.setEmail(email);
        savedUser.setFullName(fullName);
        savedUser.setRole(UserRole.CUSTOMER);
        savedUser.setId(10L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.registerCustomer(username, password, email, fullName);

        assertEquals(username, result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertEquals(UserRole.CUSTOMER, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerCustomer_ShouldThrow_WhenUsernameExists() {
        String username = "existingUser";
        when(userRepository.getByUsername(username)).thenReturn(new User());

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
