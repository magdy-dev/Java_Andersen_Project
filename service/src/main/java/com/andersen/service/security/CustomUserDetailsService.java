package com.andersen.service.security;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService is an implementation of the UserDetailsService interface,
 * which is used to load user-specific data during the authentication process.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    /**
     * Constructs a CustomUserDetailsService with the provided user repository.
     *
     * @param userRepository The repository that manages user data.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their username. If the user is found, their details are returned.
     * Otherwise, a UsernameNotFoundException is thrown.
     *
     * @param username The username of the user to retrieve.
     * @return UserDetails containing the user's data.
     * @throws UsernameNotFoundException If the user is not found in the repository.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }

}