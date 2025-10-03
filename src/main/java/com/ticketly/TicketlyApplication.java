package com.ticketly;

import com.ticketly.util.DatabaseUtil;
import com.ticketly.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class for Ticketly Movie Ticket Booking System.
 * Entry point for the JavaFX application.
 */
public class TicketlyApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(TicketlyApplication.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Starting Ticketly Application...");

            // Test database connection
            if (DatabaseUtil.testConnection()) {
                logger.info("Database connection is working");
            } else {
                logger.error("Database connection failed!");
            }

            // Initialize scene manager
            SceneManager.initialize(primaryStage);

            // Show login screen
            SceneManager.showLoginScreen();

            logger.info("Ticketly Application started successfully");

        } catch (Exception e) {
            logger.error("Failed to start Ticketly Application", e);
            throw new RuntimeException("Application startup failed", e);
        }
    }

    @Override
    public void stop() {
        logger.info("Shutting down Ticketly Application...");
        DatabaseUtil.closeDataSource();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
