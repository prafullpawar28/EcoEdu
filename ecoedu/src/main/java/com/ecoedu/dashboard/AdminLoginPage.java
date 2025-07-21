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

        Label title = new Label("🧑‍💼 Admin Login");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#6a1b9a"));
        getChildren().add(title);

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setFont(Font.font("Comic Sans MS", 16));
        emailField.setMaxWidth(300);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setFont(Font.font("Comic Sans MS", 16));
        passwordField.setMaxWidth(300);

        Button loginBtn = new Button("Login");
        loginBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        loginBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        loginBtn.setOnAction(e -> handleLogin());

        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign Up");
        signupLink.setOnAction(e -> AdminSignupPage.show(primaryStage));

        messageLabel = new Label("");
        messageLabel.setTextFill(Color.web("#d32f2f"));
        messageLabel.setFont(Font.font("Comic Sans MS", 14));

        getChildren().addAll(emailField, passwordField, loginBtn, signupLink, messageLabel);
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
            AdminDashboard.show(primaryStage);
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