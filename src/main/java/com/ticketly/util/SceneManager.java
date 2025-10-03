package com.ticketly.util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for managing JavaFX scenes and navigation.
 */
public class SceneManager {
    private static final Logger logger = LoggerFactory.getLogger(SceneManager.class);
    private static Stage primaryStage;

    private static Long loggedInUserId = null;

    public static void initialize(Stage stage) {
        primaryStage = stage;
        logger.info("SceneManager initialized");
    }

    public static void showLoginScreen() {
        try {
            // Root StackPane (for background + overlay)
            StackPane root = new StackPane();

            // Background with gradient
            Region bgRegion = new Region();
            bgRegion.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
            bgRegion.setPrefSize(800, 600);

            // Semi-transparent overlay VBox
            VBox formBox = new VBox(15);
            formBox.setAlignment(Pos.CENTER);
            formBox.setMaxWidth(350);
            formBox.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 30; -fx-background-radius: 15;");

            // Title
            Label title = new Label("🎟 Ticketly 🎬");
            title.setStyle("-fx-font-size: 36px; -fx-font-family: 'Arial Black'; -fx-text-fill: #333;");

            // Username field with icon
            HBox usernameBox = new HBox(10);
            usernameBox.setAlignment(Pos.CENTER_LEFT);
            Label userIcon = new Label("👤");
            userIcon.setStyle("-fx-font-size: 20px;");
            TextField usernameField = new TextField();
            usernameField.setPromptText("Username");
            usernameField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");
            usernameBox.getChildren().addAll(userIcon, usernameField);

            // Password field with icon
            HBox passwordBox = new HBox(10);
            passwordBox.setAlignment(Pos.CENTER_LEFT);
            Label lockIcon = new Label("🔒");
            lockIcon.setStyle("-fx-font-size: 20px;");
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");
            passwordField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");
            passwordBox.getChildren().addAll(lockIcon, passwordField);

            // Buttons
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER);

            Button loginButton = new Button("Login");
            loginButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
            loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;"));
            loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;"));

            Button signupButton = new Button("Sign Up");
            signupButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
            signupButton.setOnMouseEntered(e -> signupButton.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;"));
            signupButton.setOnMouseExited(e -> signupButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;"));

            // Actions
            loginButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username.isEmpty() || password.isEmpty()) {
                    showAlert("Error", "Username and password must not be empty.");
                    return;
                }
                boolean success = AuthService.login(username, password);
                if (success) {
                    loggedInUserId = AuthService.getLoggedInUserId();
                    showMovieSelectionScreen();
                } else {
                    showAlert("Error", "Invalid username or password.");
                }
            });

            signupButton.setOnAction(e -> showSignupScreen());

            buttonBox.getChildren().addAll(loginButton, signupButton);

            // Add everything to formBox
            formBox.getChildren().addAll(title, usernameBox, passwordBox, buttonBox);

            // Overlay
            root.getChildren().addAll(bgRegion, formBox);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Login");
            primaryStage.show();
            logger.info("Interactive login screen displayed");
        } catch (Exception e) {
            logger.error("Failed to load interactive login screen", e);
            throw new RuntimeException("Failed to load interactive login screen", e);
        }
    }

    private static void showAlert(String title, String message) {
        Alert.AlertType type = Alert.AlertType.NONE;
        if (title.equalsIgnoreCase("Error")) type = Alert.AlertType.ERROR;
        else if (title.equalsIgnoreCase("Success")) type = Alert.AlertType.INFORMATION;
        else if (title.equalsIgnoreCase("Warning")) type = Alert.AlertType.WARNING;

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSignupScreen() {
        try {
            // Root StackPane
            StackPane root = new StackPane();

            // Background with gradient
            Region bgRegion = new Region();
            bgRegion.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
            bgRegion.setPrefSize(500, 450);

            // Form VBox
            VBox formBox = new VBox(15);
            formBox.setAlignment(Pos.CENTER);
            formBox.setMaxWidth(400);
            formBox.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 30; -fx-background-radius: 15;");

            // Title
            Label title = new Label("Sign Up for Ticketly");
            title.setStyle("-fx-font-size: 28px; -fx-font-family: 'Arial Black'; -fx-text-fill: #333;");

            // Fields
            TextField usernameField = new TextField();
            usernameField.setPromptText("Username");
            usernameField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");

            TextField emailField = new TextField();
            emailField.setPromptText("Email");
            emailField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");

            TextField fullNameField = new TextField();
            fullNameField.setPromptText("Full Name");
            fullNameField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");
            passwordField.setStyle("-fx-background-radius: 8; -fx-padding: 8;");

            // Buttons
            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER);

            Button signupButton = new Button("Sign Up");
            signupButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
            signupButton.setOnAction(e -> {
                String username = usernameField.getText();
                String email = emailField.getText();
                String fullName = fullNameField.getText();
                String password = passwordField.getText();
                if (username.isEmpty() || email.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                    showAlert("Error", "All fields must be filled.");
                    return;
                }
                boolean success = AuthService.signup(username, password, email, fullName);
                if (success) {
                    showAlert("Success", "Account created successfully! Please login.");
                    showLoginScreen();
                } else {
                    showAlert("Error", "Username already exists.");
                }
            });

            Button backButton = new Button("Back to Login");
            backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
            backButton.setOnAction(e -> showLoginScreen());

            buttonBox.getChildren().addAll(signupButton, backButton);

            formBox.getChildren().addAll(title, usernameField, emailField, fullNameField, passwordField, buttonBox);

            root.getChildren().addAll(bgRegion, formBox);

            Scene scene = new Scene(root, 500, 450);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Sign Up");
        } catch (Exception e) {
            logger.error("Failed to load signup screen", e);
            showAlert("Error", "Failed to load signup screen.");
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private static void showMovieSelectionScreen() {
        try {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(40));
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

            Label title = new Label("🎬 Select a Movie");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            // Movie list
            ListView<HBox> movieList = new ListView<>();
            movieList.setPrefHeight(400);
            movieList.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 10;");

            // Load movies
            com.ticketly.dao.MovieRepository movieRepo = new com.ticketly.dao.MovieRepository();
            java.util.List<com.ticketly.model.Movie> movies = movieRepo.findAll();

            for (com.ticketly.model.Movie movie : movies) {
                HBox movieBox = new HBox(15);
                movieBox.setAlignment(Pos.CENTER_LEFT);
                movieBox.setPadding(new javafx.geometry.Insets(10));

                Label movieTitle = new Label("🎥 " + movie.getTitle());
                movieTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                Label movieInfo = new Label(movie.getGenre() + " | " + movie.getDurationMinutes() + " min");
                movieInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

                Button selectButton = new Button("Select Showtimes");
                selectButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5;");
                selectButton.setOnAction(e -> showShowSelectionScreen(movie.getId()));

                movieBox.getChildren().addAll(movieTitle, movieInfo, selectButton);
                movieList.getItems().add(movieBox);
            }

            Button logoutButton = new Button("Logout");
            logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            logoutButton.setOnAction(e -> showLoginScreen());

            root.getChildren().addAll(title, movieList, logoutButton);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Movies");
        } catch (Exception e) {
            logger.error("Failed to load movie selection screen", e);
            showAlert("Error", "Failed to load movie selection screen.");
        }
    }

    private static void showShowSelectionScreen(long movieId) {
        try {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(40));
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

            Label title = new Label("🕒 Select Showtime");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            ListView<VBox> showList = new ListView<>();
            showList.setPrefHeight(400);
            showList.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-background-radius: 10;");

            com.ticketly.dao.ShowRepository showRepo = new com.ticketly.dao.ShowRepository();
            java.util.List<com.ticketly.model.Show> shows = showRepo.findByMovieId(movieId);

            for (com.ticketly.model.Show show : shows) {
                VBox showBox = new VBox(10);
                showBox.setPadding(new javafx.geometry.Insets(15));
                showBox.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-radius: 5;");

                Label timeLabel = new Label("⏰ " + show.getShowTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                timeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                Label theaterLabel = new Label("🏢 " + show.getTheaterName());
                Label priceLabel = new Label("💰 $" + show.getPrice());
                Label seatsLabel = new Label("💺 Available: " + show.getAvailableSeats());

                Button selectButton = new Button("Select Seats");
                selectButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
                selectButton.setOnAction(e -> showSeatSelectionScreen(show.getId()));

                showBox.getChildren().addAll(timeLabel, theaterLabel, priceLabel, seatsLabel, selectButton);
                showList.getItems().add(showBox);
            }

            Button backButton = new Button("Back to Movies");
            backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            backButton.setOnAction(e -> showMovieSelectionScreen());

            root.getChildren().addAll(title, showList, backButton);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Showtimes");
        } catch (Exception e) {
            logger.error("Failed to load show selection screen", e);
            showAlert("Error", "Failed to load show selection screen.");
        }
    }

    private static void showSeatSelectionScreen(long showId) {
        try {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(40));
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

            Label title = new Label("💺 Select Your Seat");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            GridPane seatGrid = new GridPane();
            seatGrid.setHgap(10);
            seatGrid.setVgap(10);
            seatGrid.setAlignment(Pos.CENTER);
            seatGrid.setStyle("-fx-background-color: rgba(255,255,255,0.9); -fx-padding: 20; -fx-background-radius: 10;");

            com.ticketly.dao.ShowRepository showRepo = new com.ticketly.dao.ShowRepository();
            com.ticketly.model.Show show = showRepo.findById(showId);
            if (show == null) {
                showAlert("Error", "Show not found.");
                return;
            }

            com.ticketly.dao.SeatRepository seatRepo = new com.ticketly.dao.SeatRepository();
            java.util.List<com.ticketly.model.Seat> seats = seatRepo.findByTheaterId(show.getTheaterId());

            java.util.Set<Long> selectedSeatIds = new java.util.HashSet<>();

            int row = 0, col = 0;
            for (com.ticketly.model.Seat seat : seats) {
                Button seatButton = new Button(seat.getSeatRow() + String.valueOf(seat.getSeatNumber()));
                if (!seat.isActive() || seatRepo.isSeatBooked(showId, seat.getId())) {
                    seatButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                    seatButton.setDisable(true);
                } else {
                    seatButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                    seatButton.setOnAction(e -> {
                        if (selectedSeatIds.contains(seat.getId())) {
                            selectedSeatIds.remove(seat.getId());
                            seatButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        } else {
                            selectedSeatIds.add(seat.getId());
                            seatButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
                        }
                    });
                }
                seatGrid.add(seatButton, col, row);
                col++;
                if (col > 9) { // 10 seats per row
                    col = 0;
                    row++;
                }
            }

            Button proceedButton = new Button("Proceed");
            proceedButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            proceedButton.setOnAction(e -> {
                if (selectedSeatIds.isEmpty()) {
                    showAlert("Error", "Please select at least one seat.");
                    return;
                }
                // Use loggedInUserId set at login
                if (loggedInUserId == null) {
                    showAlert("Error", "User not logged in.");
                    return;
                }
                double price = show.getPrice().doubleValue();
                boolean allSuccess = true;
                for (Long seatId : selectedSeatIds) {
                    boolean success = seatRepo.bookSeat(seatId, loggedInUserId, showId, price);
                    if (!success) {
                        allSuccess = false;
                        break;
                    }
                }
                if (allSuccess) {
                    showAlert("Success", "Seats booked successfully!");
                    showMovieSelectionScreen();
                } else {
                    showAlert("Error", "Seat booking failed.");
                }
            });

            Button backButton = new Button("Back to Showtimes");
            backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            backButton.setOnAction(e -> showShowSelectionScreen(show.getMovieId()));

            root.getChildren().addAll(title, seatGrid, proceedButton, backButton);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Seat Selection");
        } catch (Exception e) {
            logger.error("Failed to load seat selection screen", e);
            showAlert("Error", "Failed to load seat selection screen.");
        }
    }
}
