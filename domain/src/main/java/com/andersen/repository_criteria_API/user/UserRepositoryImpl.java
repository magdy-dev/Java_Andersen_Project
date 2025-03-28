package com.andersen.repository_criteria_API.user;

import com.andersen.entity.role.User;
import com.andersen.exception.DataAccessException;
import com.andersen.exception.ErrorCode;
import com.andersen.logger.ConsoleLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User createUser(User user) throws DataAccessException {
        try {
            entityManager.persist(user);
            ConsoleLogger.log("Created user: " + user); // Log user creation
            return user;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to create user:"+e.getMessage()); // Log the error
            throw new DataAccessException("Failed to create user:" + e, ErrorCode.US_002);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) throws DataAccessException {
        try {
            User user = entityManager.find(User.class, id);
            ConsoleLogger.log("Retrieved user by id: " + id); // Log user retrieval
            return Optional.ofNullable(user);
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get user by id:"); // Log the error
            throw new DataAccessException("Failed to get user by id:" + e, ErrorCode.US_007);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws DataAccessException {
        try {
            // Using Criteria API for type-safe query
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> user = cq.from(User.class);

            cq.select(user)
                    .where(cb.equal(user.get("username"), username));

            TypedQuery<User> query = entityManager.createQuery(cq);
            Optional<User> result = query.getResultStream().findFirst();

            ConsoleLogger.log("Retrieved user by username: " + username); // Log user retrieval
            return result;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to get user by username: " + username); // Log the error
            throw new DataAccessException("Failed to get user by username:" + e, ErrorCode.US_007);
        }
    }

    @Override
    public boolean updateUser(User user) throws DataAccessException {
        try {
            if (user == null || user.getId() == null) {
                throw new DataAccessException("Invalid user data for update", ErrorCode.US_003);
            }
            entityManager.merge(user);
            ConsoleLogger.log("Updated user: " + user.getId()); // Log user update
            return true;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to update user: " + (user != null ? user.getId() : "null")); // Log the error
            throw new DataAccessException("Failed to update user: " + e, ErrorCode.US_003);
        }
    }

    @Override
    public boolean deleteUser(Long id) throws DataAccessException {
        try {
            Optional<User> userOptional = getUserById(id);
            if (userOptional.isEmpty()) {
                ConsoleLogger.log("User  not found for deletion: " + id); // Log if user not found
                return false;
            }
            entityManager.remove(userOptional.get());
            ConsoleLogger.log("Deleted user: " + id); // Log user deletion
            return true;
        } catch (Exception e) {
            ConsoleLogger.log("Failed to delete user: " + id); // Log the error
            throw new DataAccessException("Failed to delete user:" + e, ErrorCode.US_004);
        }
    }
}
