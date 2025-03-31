package com.andersen.repository_Criteria.user;

import com.andersen.entity.role.User;
import com.andersen.exception.DataAccessException;
import com.andersen.exception.errorCode.ErrorCode;
import com.andersen.logger.logger.Console_Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User createUser(User user) throws DataAccessException {
        try {
            // No need to set isActive here if it's already defaulted in the User entity
            entityManager.persist(user);
            Console_Logger.log("Created user: " + user);
            return user;
        } catch (Exception e) {
            Console_Logger.log("Failed to create user: " + e.getMessage());
            throw new DataAccessException("Failed to create user", ErrorCode.US_002);
        }
    }
    @Override
    public Optional<User> getUserById(Long id) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            // Only fetch active users
            Predicate idPredicate = cb.equal(root.get("id"), id);
            Predicate activePredicate = cb.isTrue(root.get("isActive"));
            query.where(cb.and(idPredicate, activePredicate));

            User user = entityManager.createQuery(query).getSingleResult();
            Console_Logger.log("Retrieved user by id: " + id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            Console_Logger.log("Failed to get user by id: " + id);
            throw new DataAccessException("Failed to get user by id", ErrorCode.US_007);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            // Only fetch active users with matching username
            Predicate usernamePredicate = cb.equal(root.get("username"), username);
            Predicate activePredicate = cb.isTrue(root.get("isActive"));
            query.where(cb.and(usernamePredicate, activePredicate));

            TypedQuery<User> typedQuery = entityManager.createQuery(query);
            return typedQuery.getResultStream().findFirst();
        } catch (Exception e) {
            Console_Logger.log("Failed to get user by username: " + username);
            throw new DataAccessException("Failed to get user by username", ErrorCode.US_007);
        }
    }
    @Override
    public boolean updateUser(User user) throws DataAccessException {
        try {
            if (user == null || user.getId() == null) {
                throw new DataAccessException("Invalid user data for update", ErrorCode.US_003);
            }

            // Get the existing user without merging
            User existing = entityManager.find(User.class, user.getId());
            if (existing == null) {
                throw new DataAccessException("User not found", ErrorCode.US_007);
            }

            // Copy all fields except isActive from the input user to the existing one
            existing.setUsername(user.getUsername());
            existing.setPassword(user.getPassword());
            existing.setEmail(user.getEmail());
            existing.setFullName(user.getFullName());
            existing.setRole(user.getRole());
            // isActive remains unchanged

            // No merge needed since we're working with the managed entity
            Console_Logger.log("Updated user: " + user.getId());
            return true;
        } catch (Exception e) {
            Console_Logger.log("Failed to update user: " + e.getMessage());
            throw new DataAccessException("Failed to update user", ErrorCode.US_003);
        }
    }

    @Override
    public boolean deleteUser(Long id) throws DataAccessException {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);
            Root<User> root = update.from(User.class);

            // Soft delete: set isActive = false
            update.set("isActive", false)
                    .where(cb.equal(root.get("id"), id));

            int updated = entityManager.createQuery(update).executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            Console_Logger.log("Failed to delete user: " + id + ", Error: " + e.getMessage());
            throw new DataAccessException("Failed to delete user", ErrorCode.US_004);
        }
    }
}