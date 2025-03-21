package com.andersen.dao.booking;

import com.andersen.connection.DatabaseConnection;
import com.andersen.dao.user.UserDAO;
import com.andersen.dao.user.UserDAOImpl;
import com.andersen.dao.workspace.WorkspaceDAO;
import com.andersen.dao.workspace.WorkspaceDAOImpl;
import com.andersen.entity.booking.Booking;
import com.andersen.entity.users.Customer;
import com.andersen.entity.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    private static final Logger logger = LoggerFactory.getLogger(BookingDAOImpl.class);
    private final UserDAOImpl userDAO; // Injected UserDAO
    private final WorkspaceDAOImpl workspaceDAO; // Injected WorkspaceDAO


    public BookingDAOImpl(UserDAOImpl userDAO, WorkspaceDAOImpl workspaceDAO) {
        this.userDAO = userDAO;
        this.workspaceDAO = workspaceDAO;
    }

    @Override
    public void createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (customer_id, workspace_id, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, booking.getCustomer().getId());
            statement.setLong(2, booking.getWorkspace().getId());
            statement.setTime(3, Time.valueOf(booking.getStartTime()));
            statement.setTime(4, Time.valueOf(booking.getEndTime()));
            statement.executeUpdate();
            logger.info("Booking created successfully: {}", booking.getId());
        } catch (SQLException e) {
            logger.error("Error creating booking: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Booking readBooking(Long id) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Customer customer = (Customer) userDAO.readUser(resultSet.getString("customer_id")); // Fetch customer
                Workspace workspace = workspaceDAO.readWorkspace(resultSet.getLong("workspace_id")); // Fetch workspace
                return new Booking(resultSet.getLong("id"),
                        customer,
                        workspace,
                        resultSet.getTime("start_time").toLocalTime(),
                        resultSet.getTime("end_time").toLocalTime());
            }
        } catch (SQLException e) {
            logger.error("Error reading booking: {}", e.getMessage());
            throw e;
        }
        return null;
    }

    @Override
    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET start_time = ?, end_time = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTime(1, Time.valueOf(booking.getStartTime()));
            statement.setTime(2, Time.valueOf(booking.getEndTime()));
            statement.setLong(3, booking.getId());
            statement.executeUpdate();
            logger.info("Booking updated successfully: {}", booking.getId());
        } catch (SQLException e) {
            logger.error("Error updating booking: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteBooking(Long id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
            logger.info("Booking deleted successfully: {}", id);
        } catch (SQLException e) {
            logger.error("Error deleting booking: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection connection = DatabaseConnection.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Customer customer = (Customer) userDAO.readUser(resultSet.getString("customer_id")); // Fetch customer
                Workspace workspace = workspaceDAO.readWorkspace(resultSet.getLong("workspace_id")); // Fetch workspace
                bookings.add(new Booking(resultSet.getLong("id"),
                        customer,
                        workspace,
                        resultSet.getTime("start_time").toLocalTime(),
                        resultSet.getTime("end_time").toLocalTime()));
            }
            logger.info("Retrieved {} bookings", bookings.size());
        } catch (SQLException e) {
            logger.error("Error retrieving bookings: {}", e.getMessage());
            throw e;
        }
        return bookings;
    }

    @Override
    public Long generateId() {
        return System.currentTimeMillis();
    }
}