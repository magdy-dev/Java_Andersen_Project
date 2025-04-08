package com.andersen.domain.mapper;

import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.entity.role.User;

/**
 * Mapper class for converting between User entities and User Data Transfer Objects (DTOs).
 * This class provides methods to translate User data to and from its DTO representation
 * to facilitate data transfer across different layers of the application.
 */
public class UserMapper {

    /**
     * Converts a User entity to a UserDto.
     *
     * @param user the User entity to be converted
     * @return a UserDto representing the provided User entity, or null if the user is null
     */
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

    /**
     * Converts a UserDto to a User entity.
     *
     * @param dto the UserDto to be converted
     * @return a User entity representing the provided UserDto, or null if the dto is null
     */
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setActive(dto.isActive());
        user.setRole(dto.getRole());

        return user;
    }
}