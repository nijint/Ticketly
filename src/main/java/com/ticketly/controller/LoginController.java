package com.ticketly.controller;

import com.ticketly.util.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller for the login screen.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        signupButton.setOnAction(event -> handleSignup());
    }

    private void handleSignup() {
        // Navigate to signup screen
        com.ticketly.util.SceneManager.showSignupScreen();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Username and password must not be empty.");
            return;
        }

        boolean authenticated = AuthService.login(username, password);
        if (authenticated) {
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
            // TODO: Navigate to main application screen
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
