package com.andersen.repository.user;

import com.andersen.entity.role.User;
import com.andersen.exception.DataAccessException;

import java.util.Optional;

public interface UserRepository {
    User createUser(User user) throws DataAccessException;

    Optional<User> getUserById(Long id) throws DataAccessException;

    Optional<User> getUserByUsername(String username) throws DataAccessException;

    boolean updateUser(User user) throws DataAccessException;

    boolean deleteUser(Long id) throws DataAccessException;


}