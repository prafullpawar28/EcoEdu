package com.ecoedu.Home;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Map;

public class SignupPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label errorLabel;

    public SignupPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(18);
        setPadding(new Insets(40, 40, 40, 40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #fffde7, #e1f5fe);");

        Label title = new Label("Sign Up for EcoEdu");
        title.setFont(Font.font("Comic Sans MS", 28));
        title.setTextFill(Color.web("#388e3c"));

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setFont(Font.font(16));
        emailField.setMaxWidth(260);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setFont(Font.font(16));
        passwordField.setMaxWidth(260);

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setFont(Font.font(16));
        confirmPasswordField.setMaxWidth(260);

        Button signupBtn = new Button("Sign Up");
        signupBtn.setFont(Font.font(18));
        signupBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 24;");
        signupBtn.setOnAction(e -> handleSignup());

        Hyperlink loginLink = new Hyperlink("Already have an account? Login!");
        loginLink.setFont(Font.font(14));
        loginLink.setOnAction(e -> LoginPage.show(primaryStage));

        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font(14));

        getChildren().addAll(title, emailField, passwordField, confirmPasswordField, signupBtn, loginLink, errorLabel);
    }

    private void handleSignup() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        errorLabel.setText("");
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }
        try {
            Map<String, String> result = AuthService.signup(email, password);
            // On success, navigate to Dashboard
            Dashboard.show(primaryStage);
        } catch (Exception ex) {
            errorLabel.setText("Signup failed: " + ex.getMessage());
        }
    }

    // Utility to show this page
    public static void show(Stage primaryStage) {
        SignupPage signupPage = new SignupPage(primaryStage);
        Scene scene = new Scene(signupPage, 420, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Sign Up");
        primaryStage.show();
    }
} 