package com.ticketly.controller;

import com.ticketly.util.AuthService;
import com.ticketly.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller for the signup screen.
 */
public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField fullNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signupButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        signupButton.setOnAction(event -> handleSignup());
        backButton.setOnAction(event -> handleBack());
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String fullName = fullNameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Validation Error", "Passwords do not match.");
            return;
        }

        boolean signedUp = AuthService.signup(username, password, email, fullName);
        if (signedUp) {
            showAlert(AlertType.INFORMATION, "Signup Successful", "Account created successfully! Please login.");
            SceneManager.showLoginScreen();
        } else {
            showAlert(AlertType.ERROR, "Signup Failed", "Username already exists or signup failed.");
        }
    }

    private void handleBack() {
        SceneManager.showLoginScreen();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
