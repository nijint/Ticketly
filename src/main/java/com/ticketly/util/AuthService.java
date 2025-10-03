package com.ticketly.util;

import com.ticketly.dao.UserRepository;
import com.ticketly.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Authentication service for login and signup without bcrypt.
 */
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final UserRepository userRepository = new UserRepository();

    // Store currently logged-in user ID
    private static Long loggedInUserId = null;

    /**
     * Login user by username and password.
     */
    public static boolean login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password.equals(user.getPassword())) { // plain-text comparison
                loggedInUserId = user.getId(); // store logged-in user ID
                logger.info("User logged in: {}", username);
                return true;
            }
        }
        logger.warn("Failed login attempt for user: {}", username);
        return false;
    }

    /**
     * Signup new user.
     */
    public static boolean signup(String username, String password, String email, String fullName) {
        logger.debug("Attempting signup for user: {}", username);
        if (userRepository.existsByUsername(username)) {
            logger.warn("Signup failed: username already exists: {}", username);
            return false;
        }
        User user = new User(username, email, password, fullName, "USER");
        userRepository.save(user);
        logger.info("User signed up successfully: {}", username);
        return true;
    }

    /**
     * Get currently logged-in user ID.
     */
    public static Long getLoggedInUserId() {
        return loggedInUserId;
    }

    /**
     * Get logged-in user by ID.
     */
    public static User getLoggedInUser(Long userId) {
        if (userId == null) return null;
        // UserRepository does not have findById(Long), so implement here
        String sql = "SELECT id, username, email, password_hash, full_name, phone, role, is_active, created_at, updated_at FROM users WHERE id = ?";
        try (java.sql.Connection conn = com.ticketly.util.DatabaseUtil.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            java.sql.ResultSet rs = stmt.executeQuery();
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
                return user;
            }
        } catch (java.sql.SQLException e) {
            logger.error("Error finding user by ID: {}", userId, e);
        }
        return null;
    }
}
