package com.andersen.service.security;

import com.andersen.domain.entity.role.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of the Spring {@link UserDetails} interface.
 * <p>
 * This class represents the user details for Spring Security, encapsulating
 * user-specific information such as ID, username, password, and role.
 * </p>
 */
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final UserRole role;

    /**
     * Constructs a CustomUserDetails object with the provided user information.
     *
     * @param id       the unique identifier of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param role     the role of the user
     */
    public CustomUserDetails(Long id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the user's ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the role of the user.
     *
     * @return the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + role.name());
    }

    /**
     * Returns the user's password.
     *
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username of the user.
     *
     * @return the user's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the account is non-expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return true if the account is non-locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     *
     * @return true if the credentials are non-expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}