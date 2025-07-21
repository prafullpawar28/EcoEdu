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

        StackPane background = new StackPane();
        background.setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");
        background.setPrefSize(500, 600);

        VBox card = new VBox(24);
        card.getStyleClass().add("eco-card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(380);

        // Animated eco icon
        ImageView ecoAnim = new ImageView(new Image(getClass().getResource("/Assets/Images/loginpage1.jpg").toExternalForm()));
        ecoAnim.setFitWidth(120);
        ecoAnim.setFitHeight(120);
        ecoAnim.setClip(new Circle(60, 60, 60));
        ecoAnim.setStyle("-fx-effect: dropshadow(gaussian, #A8E6CF, 12, 0.2, 0, 4);");
        FadeTransition fade = new FadeTransition(Duration.seconds(2), ecoAnim);
        fade.setFromValue(0.7);
        fade.setToValue(1.0);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();

        Label title = new Label("Welcome to EcoEdu!");
        title.getStyleClass().add("eco-title");
        title.setStyle("-fx-font-family: 'Quicksand', 'Nunito', sans-serif; -fx-font-size: 26px;");
        Label subtitle = new Label("Learn. Play. Grow. Save the Planet!");
        subtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: #388e3c; -fx-font-family: 'Quicksand', 'Nunito', sans-serif;");

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
        forgotBtn.setOnAction(e -> {/* TODO: Add forgot password logic */});
        forgotBtn.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 0;");
        forgotBtn.setTooltip(new Tooltip("Reset your password"));

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
        Hyperlink adminLoginLink = new Hyperlink("Admin Login");
        adminLoginLink.getStyleClass().add("eco-link");
        adminLoginLink.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #388E3C;");
        adminLoginLink.setOnAction(e -> com.ecoedu.dashboard.AdminLoginPage.show(primaryStage));
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("eco-btn");
        backBtn.setStyle("-fx-background-color: #FFD3B6; -fx-text-fill: #388E3C; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 6 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.Home.Home.showHome(primaryStage));
        adminBackBox.getChildren().addAll(adminLoginLink, backBtn);

        card.getChildren().addAll(ecoAnim, title, subtitle, emailBox, passBox, loginBtn, links, messageLabel, adminBackBox);
        background.getChildren().add(card);
        getChildren().add(background);
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Success");
            alert.setHeaderText(null);
            alert.setContentText("Welcome to EcoEdu, " + email + "!");
            alert.showAndWait();
            com.ecoedu.Home.Home.showStudentDashboard(primaryStage);
        } else {
            messageLabel.setText("Invalid email or password.");
        }
    }

    public static void show(Stage primaryStage) {
        StudentLoginPage page = new StudentLoginPage(primaryStage);
        Scene scene = new Scene(page, 500, 500);
         com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Login");
        primaryStage.show();
    }
} 