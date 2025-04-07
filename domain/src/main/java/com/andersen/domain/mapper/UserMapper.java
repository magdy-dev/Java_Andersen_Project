package com.andersen.domain.mapper;

import com.andersen.domain.dto.userrole.UserDto;
import com.andersen.domain.entity.role.User;

public class UserMapper {

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

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setActive(dto.isActive());
        user.setRole(dto.getRole());
        // password is intentionally excluded here (handled separately)
        return user;
    }
}
