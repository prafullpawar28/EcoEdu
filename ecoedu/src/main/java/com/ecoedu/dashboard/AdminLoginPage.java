package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;

public class AdminLoginPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private Label messageLabel;

    public AdminLoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(48, 48, 48, 48));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #ede7f6, #e3f2fd);");

        // Animated admin icon
        ImageView adminAnim = new ImageView(new Image(getClass().getResource("/Assets/Images/avatar.png").toExternalForm()));
        adminAnim.setFitWidth(100);
        adminAnim.setFitHeight(100);
        adminAnim.setClip(new Circle(50, 50, 50));
        adminAnim.setStyle("-fx-effect: dropshadow(gaussian, #6a1b9a, 12, 0.2, 0, 4);");
        FadeTransition fade = new FadeTransition(Duration.seconds(2), adminAnim);
        fade.setFromValue(0.7);
        fade.setToValue(1.0);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();

        Label title = new Label("Welcome, Admin!");
        title.getStyleClass().add("eco-title");
        title.setStyle("-fx-font-family: 'Quicksand', 'Nunito', sans-serif; -fx-font-size: 24px;");
        Label subtitle = new Label("Manage EcoEdu with confidence.");
        subtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: #6a1b9a; -fx-font-family: 'Quicksand', 'Nunito', sans-serif;");

        HBox emailBox = new HBox(8);
        Label emailIcon = new Label("\uD83D\uDCE7");
        emailIcon.getStyleClass().add("eco-icon");
        emailIcon.setTooltip(new Tooltip("Enter your admin email"));
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
        loginBtn.setTooltip(new Tooltip("Login as admin"));

        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign Up");
        signupLink.setOnAction(e -> AdminSignupPage.show(primaryStage));
        signupLink.setTooltip(new Tooltip("Create a new admin account"));

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");

        // --- Back link at the bottom ---
        Hyperlink backLink = new Hyperlink("Back");
        backLink.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #388E3C; -fx-underline: true; -fx-cursor: hand; -fx-padding: 12 0 0 0;");
        backLink.setOnAction(e -> com.ecoedu.Home.Home.showHome(primaryStage));
        backLink.setTooltip(new Tooltip("Return to home page"));

        getChildren().addAll(adminAnim, title, subtitle, emailBox, passBox, loginBtn, signupLink, messageLabel, backLink);
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
            System.out.println("Admin login successful. Navigating to dashboard on stage: " + primaryStage);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Success");
            alert.setHeaderText(null);
            alert.setContentText("Welcome, Admin " + email + "!");
            alert.showAndWait();
            com.ecoedu.Home.Home.showAdminDashboard(primaryStage);
        } else {
            messageLabel.setText("Invalid email or password.");
        }
    }

    public static void show(Stage primaryStage) {
        AdminLoginPage page = new AdminLoginPage(primaryStage);
        Scene scene = new Scene(page, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Login");
        primaryStage.show();
    }
} 