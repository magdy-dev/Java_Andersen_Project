package com.andersen.service.mapper;


import com.andersen.domain.entity.role.User;
import com.andersen.service.dto.userrole.UserDto;

/**
 * Mapper for converting between User entities and User DTOs.
 * <p>
 * This class contains methods to facilitate the conversion between
 * {@link User} entities and {@link UserDto} data transfer objects.
 * It provides static methods to handle the mapping, ensuring a
 * clean separation between the data representation used for
 * persistence and the one used for transporting data across layers.
 * </p>
 */
public class UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDto}.
     *
     * @param user the User entity to convert
     * @return the converted UserDto, or null if the provided User is null
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
     * Converts a {@link UserDto} to a {@link User} entity.
     *
     * @param dto the UserDto to convert
     * @return the converted User entity, or null if the provided UserDto is null
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