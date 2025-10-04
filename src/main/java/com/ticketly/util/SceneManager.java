package com.ticketly.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

            // Background with professional gradient
            Region bgRegion = new Region();
            bgRegion.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72 0%, #2a5298 50%, #3a7bd5 100%);");
            bgRegion.setPrefSize(800, 600);

            // Semi-transparent overlay VBox with enhanced styling
            VBox formBox = new VBox(20);
            formBox.setAlignment(Pos.CENTER);
            formBox.setMaxWidth(380);
            formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-padding: 40; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

            // Title
            Label title = new Label("🎬Ticketly🎬");
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

            // Background with professional gradient
            Region bgRegion = new Region();
            bgRegion.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72 0%, #2a5298 50%, #3a7bd5 100%);");
            bgRegion.setPrefSize(500, 450);

            // Form VBox with enhanced styling
            VBox formBox = new VBox(20);
            formBox.setAlignment(Pos.CENTER);
            formBox.setMaxWidth(420);
            formBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-padding: 40; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

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
            root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72 0%, #2a5298 50%, #3a7bd5 100%);");

            Label title = new Label("🎬 Select a Movie");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            // Movie list with enhanced styling
            javafx.scene.layout.FlowPane movieFlowPane = new javafx.scene.layout.FlowPane();
            movieFlowPane.setHgap(20);
            movieFlowPane.setVgap(20);
            movieFlowPane.setPrefWrapLength(760); // width at which wrapping occurs
            movieFlowPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
            movieFlowPane.setAlignment(Pos.CENTER);

            // Load movies
            com.ticketly.dao.MovieRepository movieRepo = new com.ticketly.dao.MovieRepository();
            java.util.List<com.ticketly.model.Movie> movies = movieRepo.findAll();

            for (com.ticketly.model.Movie movie : movies) {
                VBox movieBox = new VBox(10);
                movieBox.setAlignment(Pos.CENTER);
                movieBox.setPadding(new javafx.geometry.Insets(10));
                movieBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
                movieBox.setPrefWidth(150);

                // Load image for movie from resources
                ImageView movieImageView;
                try {
                    String posterUrl = movie.getPosterUrl();
                    Image movieImage = new Image(SceneManager.class.getResourceAsStream("/" + posterUrl));
                    movieImageView = new ImageView(movieImage);
                    movieImageView.setFitWidth(120);
                    movieImageView.setFitHeight(180);
                    movieImageView.setPreserveRatio(true);
                } catch (Exception ex) {
                    System.err.println("Failed to load movie image: " + movie.getPosterUrl() + " - " + ex.getMessage());
                    // If image not found, use a placeholder or no image
                    movieImageView = new ImageView();
                    movieImageView.setFitWidth(120);
                    movieImageView.setFitHeight(180);
                }

                Label movieTitle = new Label("🎥 " + movie.getTitle());
                movieTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;");
                movieTitle.setWrapText(true);

                Label movieInfo = new Label(movie.getGenre() + " | " + movie.getDurationMinutes() + " min");
                movieInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-text-alignment: center;");
                movieInfo.setWrapText(true);

                Button selectButton = new Button("Select Theater");
                selectButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5;");
                selectButton.setOnAction(e -> showTheaterSelectionScreen(movie.getId()));

                movieBox.getChildren().addAll(movieImageView, movieTitle, movieInfo, selectButton);
                movieFlowPane.getChildren().add(movieBox);
            }

            Button logoutButton = new Button("Logout");
            logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            logoutButton.setOnAction(e -> showLoginScreen());

            root.getChildren().addAll(title, movieFlowPane, logoutButton);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Movies");
        } catch (Exception e) {
            logger.error("Failed to load movie selection screen", e);
            showAlert("Error", "Failed to load movie selection screen.");
        }
    }

    private static void showTheaterSelectionScreen(long movieId) {
        try {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(40));
            root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72 0%, #2a5298 50%, #3a7bd5 100%);");

            Label title = new Label("🏢 Select a Theater");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            ListView<HBox> theaterList = new ListView<>();
            theaterList.setPrefHeight(400);
            theaterList.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");

            // Load all theaters regardless of movie
            com.ticketly.dao.TheaterRepository theaterRepo = new com.ticketly.dao.TheaterRepository();
            java.util.List<com.ticketly.model.Theater> theaters = theaterRepo.findAll();

            for (com.ticketly.model.Theater theater : theaters) {
                HBox theaterBox = new HBox(15);
                theaterBox.setAlignment(Pos.CENTER_LEFT);
                theaterBox.setPadding(new javafx.geometry.Insets(10));

                Label theaterName = new Label("🏢 " + theater.getName());
                theaterName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                Label theaterLocation = new Label(theater.getLocation() + " | " + theater.getTotalSeats() + " seats");
                theaterLocation.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

                Button selectButton = new Button("Select Showtimes");
                selectButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5;");
                selectButton.setOnAction(e -> showShowSelectionScreen(movieId, theater.getId()));

                theaterBox.getChildren().addAll(theaterName, theaterLocation, selectButton);
                theaterList.getItems().add(theaterBox);
            }

            Button backButton = new Button("Back to Movies");
            backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            backButton.setOnAction(e -> showMovieSelectionScreen());

            root.getChildren().addAll(title, theaterList, backButton);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Theaters");
        } catch (Exception e) {
            logger.error("Failed to load theater selection screen", e);
            showAlert("Error", "Failed to load theater selection screen.");
        }
    }

    private static void showShowSelectionScreen(long movieId, long theaterId) {
        try {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(40));
            root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72 0%, #2a5298 50%, #3a7bd5 100%);");

            Label title = new Label("🕒 Select Showtime");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            ListView<VBox> showList = new ListView<>();
            showList.setPrefHeight(400);
            showList.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");

            com.ticketly.dao.ShowRepository showRepo = new com.ticketly.dao.ShowRepository();
            java.util.List<com.ticketly.model.Show> shows = showRepo.findByMovieIdAndTheaterId(movieId, theaterId);

            if (shows.isEmpty()) {
                Label noShowsLabel = new Label("No shows available for this movie at the selected theater.");
                noShowsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
                root.getChildren().add(noShowsLabel);
            } else {
                for (com.ticketly.model.Show show : shows) {
                    VBox showBox = new VBox(10);
                    showBox.setPadding(new javafx.geometry.Insets(15));
                    showBox.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-radius: 5;");

                    Label timeLabel = new Label("⏰ " + show.getShowTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    timeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                    Label theaterLabel = new Label("🏢 " + show.getTheaterName());
                    Label priceLabel = new Label("💰 ₹" + show.getPrice());
                    Label seatsLabel = new Label("💺 Available: " + show.getAvailableSeats());

                    Button selectButton = new Button("Select Seats");
                    selectButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
                    selectButton.setOnAction(e -> showSeatSelectionScreen(show.getId()));

                    showBox.getChildren().addAll(timeLabel, theaterLabel, priceLabel, seatsLabel, selectButton);
                    showList.getItems().add(showBox);
                }
                root.getChildren().add(showList);
            }

            Button backButton = new Button("Back to Theaters");
            backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            backButton.setOnAction(e -> showTheaterSelectionScreen(movieId));

            root.getChildren().add(backButton);

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
            seatGrid.setStyle("-fx-background-color: rgba(56, 148, 247, 0.74); -fx-padding: 20; -fx-background-radius: 10;");

            com.ticketly.dao.ShowRepository showRepo = new com.ticketly.dao.ShowRepository();
            com.ticketly.model.Show show = showRepo.findById(showId);
            if (show == null) {
                showAlert("Error", "Show not found.");
                return;
            }

            com.ticketly.dao.SeatRepository seatRepo = new com.ticketly.dao.SeatRepository();
            java.util.List<com.ticketly.model.Seat> seats = seatRepo.findByTheaterId(show.getTheaterId());

            java.util.Set<Long> selectedSeatIds = new java.util.HashSet<>();

            // Limit seats to max 20 and arrange in rows A, B, C, D
            int maxSeats = Math.min(seats.size(), 20);
            char[] rows = {'A', 'B', 'C', 'D'};
            int seatsPerRow = 5; // 4 rows * 5 seats = 20 seats max

            // Add column labels (1, 2, 3, 4, 5)
            for (int col = 0; col < seatsPerRow; col++) {
                Label colLabel = new Label(String.valueOf(col + 1));
                colLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                seatGrid.add(colLabel, col + 1, 0);
            }

            // Add row labels (A, B, C, D)
            for (int row = 0; row < rows.length; row++) {
                Label rowLabel = new Label(String.valueOf(rows[row]));
                rowLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
                seatGrid.add(rowLabel, 0, row + 1);
            }

            for (int i = 0; i < maxSeats; i++) {
                com.ticketly.model.Seat seat = seats.get(i);
                int rowIndex = i / seatsPerRow;
                int colIndex = i % seatsPerRow;
                String seatLabel = rows[rowIndex] + String.valueOf(colIndex + 1);

                Button seatButton = new Button(seatLabel);
                if (!seat.isActive() || seatRepo.isSeatBooked(showId, seat.getId())) {
                    seatButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;"); // grey color for booked seats
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
                seatGrid.add(seatButton, colIndex + 1, rowIndex + 1);
            }

            Button proceedButton = new Button("Proceed to Payment");
            proceedButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            proceedButton.setOnAction(e -> {
                if (selectedSeatIds.isEmpty()) {
                    showAlert("Error", "Please select at least one seat.");
                    return;
                }
                if (loggedInUserId == null) {
                    showAlert("Error", "User not logged in.");
                    return;
                }
                showPaymentScreen(showId, selectedSeatIds);
            });

            Button backButton = new Button("Back to Showtimes");
            backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            backButton.setOnAction(e -> showShowSelectionScreen(show.getMovieId(), show.getTheaterId()));

            root.getChildren().addAll(title, seatGrid, proceedButton, backButton);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Seat Selection");
        } catch (Exception e) {
            logger.error("Failed to load seat selection screen", e);
            showAlert("Error", "Failed to load seat selection screen.");
        }
    }

    private static void showPaymentScreen(long showId, java.util.Set<Long> selectedSeatIds) {
        try {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(40));
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

            Label title = new Label("💳 Payment");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label seatsLabel = new Label("Selected Seats: " + selectedSeatIds.size());
            seatsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

            ToggleGroup paymentGroup = new ToggleGroup();

            RadioButton cardOption = new RadioButton("Card");
            cardOption.setToggleGroup(paymentGroup);
            cardOption.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            RadioButton upiOption = new RadioButton("UPI");
            upiOption.setToggleGroup(paymentGroup);
            upiOption.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            RadioButton debitOption = new RadioButton("Debit");
            debitOption.setToggleGroup(paymentGroup);
            debitOption.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            RadioButton creditOption = new RadioButton("Credit");
            creditOption.setToggleGroup(paymentGroup);
            creditOption.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            VBox paymentOptions = new VBox(10, cardOption, upiOption, debitOption, creditOption);
            paymentOptions.setAlignment(Pos.CENTER_LEFT);

            Button confirmButton = new Button("Confirm Booking");
            confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            confirmButton.setOnAction(e -> {
                if (paymentGroup.getSelectedToggle() == null) {
                    showAlert("Error", "Please select a payment method.");
                    return;
                }
                com.ticketly.dao.SeatRepository seatRepo = new com.ticketly.dao.SeatRepository();
                com.ticketly.dao.ShowRepository showRepo = new com.ticketly.dao.ShowRepository();
                com.ticketly.model.Show show = showRepo.findById(showId);
                if (show == null) {
                    showAlert("Error", "Show not found.");
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
                    // Calculate total amount as price * number of seats
                    double totalAmount = price * selectedSeatIds.size();
                    // Fetch the latest booking for e-ticket
                    com.ticketly.dao.BookingRepository bookingRepo = new com.ticketly.dao.BookingRepository();
                    java.util.List<com.ticketly.model.Booking> userBookings = bookingRepo.findByUserId(loggedInUserId);
                    if (!userBookings.isEmpty()) {
                        com.ticketly.model.Booking latestBooking = userBookings.get(0); // Most recent
                        // Update total amount in booking object for display
                        latestBooking.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));
                        showETicketScreen(latestBooking, selectedSeatIds, (RadioButton) paymentGroup.getSelectedToggle());
                    } else {
                        showAlert("Error", "Failed to retrieve booking details.");
                        showSeatSelectionScreen(showId);
                    }
                } else {
                    showAlert("Error", "Seat booking failed.");
                    showSeatSelectionScreen(showId);
                }
            });

            Button backButton = new Button("Back to Seat Selection");
            backButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            backButton.setOnAction(e -> showSeatSelectionScreen(showId));

            root.getChildren().addAll(title, seatsLabel, paymentOptions, confirmButton, backButton);

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - Payment");
        } catch (Exception e) {
            logger.error("Failed to load payment screen", e);
            showAlert("Error", "Failed to load payment screen.");
        }
    }

    private static void showETicketScreen(com.ticketly.model.Booking booking, java.util.Set<Long> selectedSeatIds, RadioButton paymentMethod) {
        try {
            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new javafx.geometry.Insets(40));
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #cbcedbff 0%, #764ba2 100%);");

            Label title = new Label("🎫 E-Ticket");
            title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

            // Fetch user details
            com.ticketly.model.User user = com.ticketly.util.AuthService.getLoggedInUser(loggedInUserId);
            if (user == null) {
                showAlert("Error", "User not found.");
                return;
            }

            // Fetch show and movie details
            com.ticketly.dao.ShowRepository showRepo = new com.ticketly.dao.ShowRepository();
            com.ticketly.model.Show show = showRepo.findById(booking.getShowId());
            if (show == null) {
                showAlert("Error", "Show not found.");
                return;
            }
            com.ticketly.dao.MovieRepository movieRepo = new com.ticketly.dao.MovieRepository();
            com.ticketly.model.Movie movie = movieRepo.findById((int)show.getMovieId());
            if (movie == null) {
                showAlert("Error", "Movie not found.");
                return;
            }

            // E-ticket details
            VBox ticketBox = new VBox(10);
            ticketBox.setStyle("-fx-background-color: rgba(56, 148, 247, 0.74); -fx-padding: 20; -fx-background-radius: 10;");
            ticketBox.setAlignment(Pos.CENTER_LEFT);

            Label ticketIdLabel = new Label("Ticket ID: " + booking.getId());
            ticketIdLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label userNameLabel = new Label("Name: " + user.getFullName());
            userNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label userEmailLabel = new Label("Email: " + user.getEmail());
            userEmailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label movieLabel = new Label("Movie: " + movie.getTitle());
            movieLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label showTimeLabel = new Label("Show Time: " + show.getShowTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            showTimeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label theaterLabel = new Label("Theater: " + show.getTheaterName());
            theaterLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            // Seat labels
            StringBuilder seatsStr = new StringBuilder("Seats: ");
            for (Long seatId : selectedSeatIds) {
                // Find seat label
                com.ticketly.dao.SeatRepository seatRepo = new com.ticketly.dao.SeatRepository();
                java.util.List<com.ticketly.model.Seat> seats = seatRepo.findByTheaterId(show.getTheaterId());
                for (com.ticketly.model.Seat seat : seats) {
                    if (seat.getId() == seatId) {
                        int index = seats.indexOf(seat);
                        if (index < 20) {
                            int rowIndex = index / 5;
                            int colIndex = index % 5;
                            char row = (char) ('A' + rowIndex);
                            seatsStr.append(row).append(colIndex + 1).append(" ");
                        }
                        break;
                    }
                }
            }
            Label seatsLabel = new Label(seatsStr.toString());
            seatsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label paymentLabel = new Label("Payment Method: " + paymentMethod.getText());
            paymentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            Label amountLabel = new Label("Total Amount: ₹" + booking.getTotalAmount());
            amountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

            // Generate QR code with booking details
            String qrText = "Ticket ID: " + booking.getId() + "\n" +
                            "Name: " + user.getFullName() + "\n" +
                            "Email: " + user.getEmail() + "\n" +
                            "Movie: " + movie.getTitle() + "\n" +
                            "Show Time: " + show.getShowTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n" +
                            "Theater: " + show.getTheaterName() + "\n" +
                            seatsStr.toString() + "\n" +
                            "Payment Method: " + paymentMethod.getText() + "\n" +
                            "Total Amount: ₹" + booking.getTotalAmount();

            ImageView qrCodeImageView = new ImageView();
            final java.awt.image.BufferedImage[] bufferedImageHolder = new java.awt.image.BufferedImage[1];
            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);
                bufferedImageHolder[0] = MatrixToImageWriter.toBufferedImage(bitMatrix);
                java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
                javax.imageio.ImageIO.write(bufferedImageHolder[0], "png", outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                Image qrImage = new Image(new java.io.ByteArrayInputStream(imageBytes));
                qrCodeImageView.setImage(qrImage);
                qrCodeImageView.setFitWidth(150);
                qrCodeImageView.setFitHeight(150);
                qrCodeImageView.setPreserveRatio(true);
            } catch (WriterException | java.io.IOException ex) {
                logger.error("Failed to generate QR code", ex);
                showAlert("Error", "Failed to generate QR code.");
            }

            ticketBox.getChildren().addAll(ticketIdLabel, userNameLabel, userEmailLabel, movieLabel, showTimeLabel, theaterLabel, seatsLabel, paymentLabel, amountLabel, qrCodeImageView);

            HBox buttonBox = new HBox(15);
            buttonBox.setAlignment(Pos.CENTER);

            Button downloadTextButton = new Button("Download E-Ticket Text");
            downloadTextButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            downloadTextButton.setOnAction(e -> {
                javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                fileChooser.setTitle("Save E-Ticket Text");
                fileChooser.setInitialFileName("e_ticket_" + booking.getId() + ".txt");
                java.io.File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("E-Ticket\n");
                        sb.append("Ticket ID: ").append(booking.getId()).append("\n");
                        sb.append("Name: ").append(user.getFullName()).append("\n");
                        sb.append("Email: ").append(user.getEmail()).append("\n");
                        sb.append("Movie: ").append(movie.getTitle()).append("\n");
                        sb.append("Show Time: ").append(show.getShowTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
                        sb.append("Theater: ").append(show.getTheaterName()).append("\n");
                        sb.append(seatsStr.toString()).append("\n");
                        sb.append("Payment Method: ").append(paymentMethod.getText()).append("\n");
                        sb.append("Total Amount: ₹").append(booking.getTotalAmount()).append("\n");
                        java.nio.file.Files.write(file.toPath(), sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
                        showAlert("Success", "E-Ticket text file downloaded successfully!");
                    } catch (Exception ex) {
                        showAlert("Error", "Failed to download e-ticket text file.");
                        logger.error("Error downloading e-ticket text file", ex);
                    }
                }
            });

            Button bookMoreButton = new Button("Book More");
            bookMoreButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            bookMoreButton.setOnAction(e -> showMovieSelectionScreen());

            Button logoutButton = new Button("Logout");
            logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20;");
            logoutButton.setOnAction(e -> showLoginScreen());

            buttonBox.getChildren().addAll(downloadTextButton, bookMoreButton, logoutButton);

            root.getChildren().addAll(title, ticketBox, buttonBox);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ticketly - E-Ticket");
        } catch (Exception e) {
            logger.error("Failed to load e-ticket screen", e);
            showAlert("Error", "Failed to load e-ticket screen.");
        }
    }
}
