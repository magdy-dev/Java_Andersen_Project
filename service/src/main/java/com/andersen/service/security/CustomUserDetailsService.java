package com.andersen.service.security;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the Spring {@link UserDetailsService} interface.
 * <p>
 * This service loads user-specific data from the database based on username
 * and provides a {@link UserDetails} implementation for authentication and
 * authorization purposes.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a CustomUserDetailsService with the specified user repository.
     *
     * @param userRepository the repository for user data access
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by the given username.
     *
     * @param username the username of the user to load
     * @return a UserDetails object containing user information
     * @throws UsernameNotFoundException if the user is not found in the repository
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User  not found");
        }
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }
}