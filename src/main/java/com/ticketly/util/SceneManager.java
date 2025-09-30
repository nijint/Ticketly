package com.ticketly.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Utility class for managing JavaFX scenes and navigation.
 */
public class SceneManager {
    private static final Logger logger = LoggerFactory.getLogger(SceneManager.class);
    private static Stage primaryStage;

    /**
     * Initialize the scene manager with the primary stage.
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
        logger.info("SceneManager initialized");
    }

    /**
     * Show the login screen.
     */
    public static void showLoginScreen() {
        try {
            // For now, create a simple scene without FXML
            // TODO: Implement proper FXML loading when FXML files are created
            javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(10);
            root.setAlignment(javafx.geometry.Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(20));

            javafx.scene.control.Label title = new javafx.scene.control.Label("Welcome to Ticketly");
            title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            javafx.scene.control.TextField usernameField = new javafx.scene.control.TextField();
            usernameField.setPromptText("Username");

            javafx.scene.control.PasswordField passwordField = new javafx.scene.control.PasswordField();
            passwordField.setPromptText("Password");

            javafx.scene.control.Button loginButton = new javafx.scene.control.Button("Login");

            root.getChildren().addAll(title, usernameField, passwordField, loginButton);

            Scene scene = new Scene(root, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Login");
            primaryStage.show();
            logger.info("Login screen displayed");
        } catch (Exception e) {
            logger.error("Failed to load login screen", e);
            throw new RuntimeException("Failed to load login screen", e);
        }
    }

    /**
     * Get the primary stage.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
