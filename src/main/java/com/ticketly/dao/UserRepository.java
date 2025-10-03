package com.ticketly.dao;

import com.ticketly.model.User;
import com.ticketly.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

/**
 * Data Access Object for User operations.
 */
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Find user by username.
     */
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, email, password_hash, full_name, phone, role, is_active, created_at, updated_at FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password_hash"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                user.setIsActive(rs.getBoolean("is_active"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                return Optional.of(user);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username: {}", username, e);
        }
        return Optional.empty();
    }

    /**
     * Save a new user.
     */
    public User save(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, full_name, phone, role, is_active, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        logger.debug("Attempting to save user: {}", user.getUsername());
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getPhone() != null ? user.getPhone() : null);
            stmt.setString(6, user.getRole());
            stmt.setBoolean(7, user.getIsActive() != null ? user.getIsActive() : true);
            stmt.setTimestamp(8, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(9, Timestamp.valueOf(user.getUpdatedAt()));

            logger.debug("Executing INSERT statement for user: {}", user.getUsername());
            int affectedRows = stmt.executeUpdate();
            logger.debug("INSERT affected {} rows", affectedRows);

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    logger.debug("Generated user ID: {}", user.getId());
                }
            }
            logger.info("User saved successfully: {}", user.getUsername());
        } catch (SQLException e) {
            logger.error("Error saving user: {}", user.getUsername(), e);
            throw new RuntimeException("Failed to save user", e);
        }
        return user;
    }

    /**
     * Check if username exists.
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            logger.error("Error checking if username exists: {}", username, e);
        }
        return false;
    }
}
