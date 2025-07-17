package com.ecoedu.auth;

import com.ecoedu.Home.Dashboard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;

    public SignupPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(20);
        setPadding(new Insets(40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #fffde7, #e3f2fd);");

        Label title = new Label("Sign Up for EcoEdu");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #388e3c;");

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-font-size: 16px; -fx-background-radius: 12;");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-background-radius: 12;");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setStyle("-fx-font-size: 16px; -fx-background-radius: 12;");

        Button signupBtn = new Button("Sign Up");
        signupBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 20; -fx-padding: 10 30;");
        signupBtn.setOnAction(e -> handleSignup());

        Hyperlink loginLink = new Hyperlink("Already have an account? Login");
        loginLink.setOnAction(e -> LoginPage.show(primaryStage));

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> com.ecoedu.Home.Dashboard.show(primaryStage));

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 14px;");

        getChildren().addAll(title, emailField, passwordField, confirmPasswordField, signupBtn, loginLink, messageLabel);
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
        // TODO: Add Firebase Authentication logic here
        // Example: if (FirebaseAuth.signUp(email, password)) { ... }
        boolean signupSuccess = true; // Replace with actual Firebase result
        if (signupSuccess) {
            Dashboard.show(primaryStage);
        } else {
            messageLabel.setText("Signup failed. Try a different email.");
        }
    }

    public static void show(Stage primaryStage) {
        SignupPage signupPage = new SignupPage(primaryStage);
        Scene scene = new Scene(signupPage, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Sign Up");
        primaryStage.show();
    }
} 