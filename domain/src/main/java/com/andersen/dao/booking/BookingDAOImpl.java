package com.andersen.dao.booking;
import com.andersen.connection.DatabaseConnectionPool;
import com.andersen.dao.user.UserDAOImpl;
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
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, booking.getCustomer().getId());
            statement.setLong(2, booking.getWorkspace().getId());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, Timestamp.valueOf(booking.getEndTime()));
            statement.executeUpdate();
            logger.info("Booking created successfully: {}", booking.getId());
        } catch (SQLException e) {
            logger.error("Error creating booking: {}", e.getMessage());

        }
    }

    @Override
    public Booking readBooking(Long id) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Customer customer = (Customer) userDAO.readUser(resultSet.getString("customer_id")); // Fetch customer
                Workspace workspace = workspaceDAO.readWorkspace(resultSet.getLong("workspace_id")); // Fetch workspace
                return new Booking(resultSet.getLong("id"),
                        customer,
                        workspace,
                        resultSet.getTimestamp("start_time").toLocalDateTime(),
                        resultSet.getTimestamp("end_time").toLocalDateTime());
            }
        } catch (SQLException e) {
            logger.error("Error reading booking: {}", e.getMessage());

        }
        return null;
    }

    @Override
    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET start_time = ?, end_time = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(2, Timestamp.valueOf(booking.getEndTime()));
            statement.setLong(3, booking.getId());
            statement.executeUpdate();
            logger.info("Booking updated successfully: {}", booking.getId());
        } catch (SQLException e) {
            logger.error("Error updating booking: {}", e.getMessage());

        }
    }

    @Override
    public void deleteBooking(Long id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
            logger.info("Booking deleted successfully: {}", id);
        } catch (SQLException e) {
            logger.error("Error deleting booking: {}", e.getMessage());

        }
    }

    @Override
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Customer customer = (Customer) userDAO.readUser(resultSet.getString("customer_id")); // Fetch customer
                Workspace workspace = workspaceDAO.readWorkspace(resultSet.getLong("workspace_id")); // Fetch workspace
                bookings.add(new Booking(resultSet.getLong("id"),
                        customer,
                        workspace,
                        resultSet.getTimestamp("start_time").toLocalDateTime(),
                        resultSet.getTimestamp("end_time").toLocalDateTime()));
            }
            logger.info("Retrieved {} bookings", bookings.size());
        } catch (SQLException e) {
            logger.error("Error retrieving bookings: {}", e.getMessage());
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByWorkspace(Long workspaceId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE workspace_id = ?";
        try (Connection connection = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, workspaceId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Customer customer = (Customer) userDAO.readUser(resultSet.getString("customer_id")); // Fetch customer
                Workspace workspace = workspaceDAO.readWorkspace(resultSet.getLong("workspace_id")); // Fetch workspace
                bookings.add(new Booking(resultSet.getLong("id"),
                        customer,
                        workspace,
                        resultSet.getTimestamp("start_time").toLocalDateTime(),
                        resultSet.getTimestamp("end_time").toLocalDateTime()));
            }
            logger.info("Retrieved {} bookings for workspace ID: {}", bookings.size(), workspaceId);
        } catch (SQLException e) {
            logger.error("Error retrieving bookings by workspace: {}", e.getMessage());
        }
        return bookings;
    }
}