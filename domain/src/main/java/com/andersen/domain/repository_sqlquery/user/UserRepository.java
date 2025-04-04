package com.andersen.domain.repository_sqlquery.user;

import com.andersen.domain.entity.role.User;
import com.andersen.domain.exception.DataAccessException;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user) throws DataAccessException;

    Optional<User> getUserById(Long id) throws DataAccessException;

    Optional<User> getUserByUsername(String username) throws DataAccessException;

    boolean updateUser(User user) throws DataAccessException;

    boolean deleteUser(Long id) throws DataAccessException;
}