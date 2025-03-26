package com.andersen.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class handles the database connection for the application.
 * It provides methods to establish a connection to the PostgreSQL database
 * and to close an existing connection.
 */
public class DatabaseConnection {

    // Database connection details
    private static final String URL = "jdbc:postgresql://localhost:5432/mydatabase";
    private static final String USER = "postgres";
    private static final String PASSWORD = "magdy";

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private DatabaseConnection() {
    }

    /**
     * Establishes and returns a new connection to the database.
     *
     * @return a Connection object connected to the specified database.
     * @throws SQLException if a database access error occurs,
     *         or the URL is null, or the connection cannot be established.
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Throw a detailed SQLException if connection fails
            throw new SQLException("Failed to connect to database", e);
        }
    }

    /**
     * Closes the provided database connection, if it is not null.
     *
     * @param connection the Connection object to be closed.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}