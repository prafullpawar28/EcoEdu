package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import com.ecoedu.adminpanel.AdminPanelMain;

public class StudentLoginPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private Label messageLabel;

    public StudentLoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(0);
        setAlignment(Pos.CENTER);
        getStyleClass().add("root");

        // Background image
        StackPane root = new StackPane();
        ImageView bgImage = new ImageView();
        try {
            bgImage.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/login_signup.jpg")));
        } catch (Exception e) {
            bgImage.setImage(null);
        }
        bgImage.setFitWidth(1366);
        bgImage.setFitHeight(768);
        bgImage.setPreserveRatio(false);
        bgImage.setOpacity(0.92);

        VBox card = new VBox(24);
        card.getStyleClass().add("eco-card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(380);
        card.setMaxHeight(420);
        card.setMinHeight(380);
        card.setPadding(new Insets(24, 32, 24, 32));
        card.setStyle("-fx-background-color: transparent; " +
            "-fx-background-radius: 28; " +
            "-fx-border-color: rgba(255,255,255,0.7); " +
            "-fx-border-width: 2.5; " +
            "-fx-border-radius: 28; " +
            "-fx-effect: dropshadow(gaussian, #00000055, 32, 0.18, 0, 8);");
        // Lottie animation placeholder (replace with real Lottie integration if available)

        Label welcome = new Label("Welcome, Student");
        welcome.setFont(Font.font("Quicksand", FontWeight.BOLD, 35));
        welcome.setTextFill(Color.web("rgb(37, 9, 54)"));
        welcome.setStyle("-fx-effect: dropshadow(gaussian, #fff, 8, 0.2, 0, 2); -fx-padding: 0 0 18 0;");

        HBox emailBox = new HBox(8);
        Label emailIcon = new Label("\uD83D\uDCE7");
        emailIcon.getStyleClass().add("eco-icon");
        emailIcon.setTooltip(new Tooltip("Enter your email address"));
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("eco-field");
        emailBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.getChildren().addAll(emailIcon, emailField);

        HBox passBox = new HBox(8);
        Label passIcon = new Label("\uD83D\uDD12");
        passIcon.getStyleClass().add("eco-icon");
        passIcon.setTooltip(new Tooltip("Enter your password"));
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("eco-field");
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.getChildren().addAll(passIcon, passwordField);

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("eco-btn");
        loginBtn.setOnAction(e -> handleLogin());
        loginBtn.setTooltip(new Tooltip("Login to your EcoEdu account"));

        Button forgotBtn = new Button("Forgot Password?");
        forgotBtn.getStyleClass().add("eco-link");
        forgotBtn.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 0; -fx-text-fill: #388e3c; -fx-underline: true; -fx-cursor: hand;");
        forgotBtn.setTooltip(new Tooltip("Reset your password"));
        forgotBtn.setOnAction(e -> showForgotPasswordDialog());

        HBox links = new HBox(16);
        links.setAlignment(Pos.CENTER);
        Hyperlink signupLink = new Hyperlink("Sign Up");
        signupLink.getStyleClass().add("eco-link");
        signupLink.setOnAction(e -> StudentSignupPage.show(primaryStage));
        signupLink.setTooltip(new Tooltip("Create a new EcoEdu account"));
        links.getChildren().addAll(forgotBtn, signupLink);

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");

        // --- Admin Login and Back ---
        HBox adminBackBox = new HBox(24);
        adminBackBox.setAlignment(Pos.CENTER);
        Button adminLoginBtn = new Button("Admin Login");
        adminLoginBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 15));
        adminLoginBtn.setStyle("-fx-background-color: #FFD3B6; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand; margin-top: 8px;");
        adminLoginBtn.setOnAction(e -> com.ecoedu.adminpanel.AdminLoginPage.show(primaryStage));
        adminLoginBtn.setOnMouseEntered(e -> adminLoginBtn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill:  #388E3C; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand; margin-top: 8px; -fx-scale-x:1.07;-fx-scale-y:1.07; -fx-effect: dropshadow(gaussian , 12, 0.3, 0, 4);"));
        adminLoginBtn.setOnMouseExited(e -> adminLoginBtn.setStyle("-fx-background-color:  #FFD3B6; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand; margin-top: 8px;"));
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("eco-btn");
        backBtn.setStyle("-fx-background-color: #FFD3B6; -fx-text-fill: #388E3C; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 6 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.Home.Home.showHome(primaryStage));
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #ffb74d; -fx-text-fill: #fff; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 6 24; -fx-cursor: hand; -fx-scale-x:1.07;-fx-scale-y:1.07; -fx-effect: dropshadow(gaussian, #ffb74d, 12, 0.3, 0, 4);"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #FFD3B6; -fx-text-fill: #388E3C; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 6 24; -fx-cursor: hand;"));
        adminBackBox.getChildren().addAll(adminLoginBtn, backBtn);

        card.getChildren().addAll(welcome, emailBox, passBox, loginBtn, links, messageLabel, adminBackBox);
        root.getChildren().addAll(bgImage, card);
        getChildren().add(root);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        messageLabel.setText("");
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password.");
            return;
        }
        // TODO: Add real authentication logic here
        boolean loginSuccess = true; // Replace with real check
        if (loginSuccess) {
            System.out.println("Student login successful. Navigating to dashboard on stage: " + primaryStage);
            com.ecoedu.Home.Home.showStudentDashboard(primaryStage);
        } else {
            messageLabel.setText("Invalid email or password.");
        }
    }

    // --- Forgot Password Logic ---
    private void showForgotPasswordDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Forgot Password");
        dialog.setHeaderText("Reset your password");
        dialog.setContentText("Enter your registered email:");
        dialog.getDialogPane().setStyle("-fx-font-family: 'Quicksand', 'Nunito', sans-serif;");
        dialog.showAndWait().ifPresent(email -> {
            if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid email address.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            // Simulate sending a reset link
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "If an account with this email exists, a password reset link has been sent.", ButtonType.OK);
            alert.showAndWait();
        });
    }

    public static void show(Stage primaryStage) {
        StudentLoginPage page = new StudentLoginPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
         com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Login");
        primaryStage.show();
    }
} 