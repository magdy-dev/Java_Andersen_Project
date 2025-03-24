//package com.andersen.repository.user;
//
//import com.andersen.dao.user.UserDAO;
//import com.andersen.dao.user.UserDAOImpl;
//import com.andersen.entity.role.User;
//import com.andersen.entity.role.UserRole;
//
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * Implementation of the UserRepository interface for user management.
// * This repository handles user registration, login, and retrieval operations.
// */
//public class UserRepositoryEntityImpl implements UserRepository {
//    private final UserDAOImpl userDAO;
//
//    public UserRepositoryEntityImpl( UserDAOImpl userDAO1) {
//        this.userDAO = userDAO1;
//
//    }
//
//    /**
//     * Registers a new customer by creating a new user in the system.
//     *
//     * @param user the user to register
//     * @throws IllegalArgumentException if the user is null
//     * @throws SQLException if there is an error during registration
//     */
//    @Override
//    public void registerCustomer(User user) throws SQLException {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null.");
//        }
//
//        try {
//            userDAO.createUser(user); // Create user using DAO
//        } catch (SQLException e) {
//            throw new SQLException("Error registering customer: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public User userLogin(String username, String password, UserRole role) throws SQLException {
//        if (username == null || password == null || role == null) {
//            throw new IllegalArgumentException("Username, password, and role cannot be null.");
//        }
//
//        User user = userDAO.readUser(username); // Fetch user by username
//        if (user != null && user.getRole() == role && user.getPassword().equals(password)) {
//            return user; // Return user if credentials and role match
//        }
//        return null; // Return null if credentials or role do not match
//    }
//
//
//    /**
//     * Allows a customer to log in by checking their credentials.
//     *
//     * @param username the username of the user
//     * @param password the password of the user
//     * @return the User object if login is successful, otherwise null
//     * @throws SQLException if there is an error during login
//     */
//    public User customerLogin(String username, String password) throws SQLException {
//        User user = userDAO.readUser(username); // Fetch user by username
//        if (user != null && user.getPassword().equals(password)) {
//            return user; // Return user if credentials match
//        }
//        return null; // Return null if credentials do not match
//    }
//
//    /**
//     * Allows an admin to log in by checking their credentials.
//     *
//     * @param username the username of the admin
//     * @param password the password of the admin
//     * @return the User object if login is successful, otherwise null
//     * @throws SQLException if there is an error during login
//     */
//    public User adminLogin(String username, String password) throws SQLException {
//        User user = userDAO.readUser(username); // Fetch user by username
//        if (user != null && user.getRole() == UserRole.ADMIN && user.getPassword().equals(password)) {
//            return user; // Return admin user if credentials match
//        }
//        return null; // Return null if credentials do not match
//    }
//
//    /**
//     * Retrieves all users from the repository.
//     *
//     * @return a list of all users
//     * @throws SQLException if there is an error during retrieval
//     */
//    @Override
//    public List<User> getAllUsers() throws SQLException {
//        return userDAO.getAllUsers(); // Retrieve all users from DAO
//    }
//
//    @Override
//    public User getUserByUsername(String username) throws SQLException {
//        return userDAO.readUser(username); // Fetch user by username
//    }
//}