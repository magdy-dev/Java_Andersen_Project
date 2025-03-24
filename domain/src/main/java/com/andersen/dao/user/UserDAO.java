package com.andersen.dao.user;

import com.andersen.entity.role.User;
import com.andersen.exception.UserAuthenticationException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    void  createUser (User user) throws UserAuthenticationException;
    User readUser (String username)  throws UserAuthenticationException;

    Optional<User> findByUsername(String username)  throws UserAuthenticationException;

    void updateUser (User user) throws UserAuthenticationException;
    void deleteUser (String username) throws UserAuthenticationException;
    List<User> getAllUsers() throws UserAuthenticationException;
}