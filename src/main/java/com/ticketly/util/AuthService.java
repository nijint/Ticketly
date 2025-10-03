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
}
