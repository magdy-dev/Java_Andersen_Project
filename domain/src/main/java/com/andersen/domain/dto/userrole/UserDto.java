package com.andersen.domain.dto.userrole;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.entity.role.UserRole;

public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private boolean isActive;
    private UserRole role;

    public UserDto() {}

    public UserDto(Long id, String username, String email, String fullName, boolean isActive, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.isActive = isActive;
        this.role = role;
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.isActive(),
                user.getRole()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}