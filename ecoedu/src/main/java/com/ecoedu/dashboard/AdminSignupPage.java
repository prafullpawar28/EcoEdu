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

public class AdminSignupPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;

    public AdminSignupPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(48, 48, 48, 48));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #fffde7, #ede7f6);");

        Label title = new Label("🧑‍💼 Admin Sign Up");
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

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setFont(Font.font("Comic Sans MS", 16));
        confirmPasswordField.setMaxWidth(300);

        Button signupBtn = new Button("Sign Up");
        signupBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        signupBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        signupBtn.setOnAction(e -> handleSignup());

        Hyperlink loginLink = new Hyperlink("Already have an account? Login");
        loginLink.setOnAction(e -> AdminLoginPage.show(primaryStage));

        messageLabel = new Label("");
        messageLabel.setTextFill(Color.web("#d32f2f"));
        messageLabel.setFont(Font.font("Comic Sans MS", 14));

        getChildren().addAll(emailField, passwordField, confirmPasswordField, signupBtn, loginLink, messageLabel);
    }

    private void handleSignup() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        messageLabel.setText("");
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Please fill all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }
        // TODO: Add real signup logic here
        boolean signupSuccess = true; // Replace with real check
        if (signupSuccess) {
            AdminDashboard.show(primaryStage);
        } else {
            messageLabel.setText("Signup failed. Try a different email.");
        }
    }

    public static void show(Stage primaryStage) {
        AdminSignupPage page = new AdminSignupPage(primaryStage);
        Scene scene = new Scene(page, 500, 550);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Sign Up");
        primaryStage.show();
    }
} 