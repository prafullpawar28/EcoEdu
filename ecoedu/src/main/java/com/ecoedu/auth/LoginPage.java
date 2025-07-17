package com.ecoedu.auth;

import com.ecoedu.Home.Dashboard;
//import com.ecoedu.auth.SignupPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(20);
        setPadding(new Insets(40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #fffde7);");

        Label title = new Label("Login to EcoEdu");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1976d2;");

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-font-size: 16px; -fx-background-radius: 12;");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-background-radius: 12;");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 20; -fx-padding: 10 30;");
        loginBtn.setOnAction(e -> handleLogin());

        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign Up");
        signupLink.setOnAction(e -> SignupPage.show(primaryStage));

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> com.ecoedu.Home.Dashboard.show(primaryStage));

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 14px;");

        getChildren().addAll(title, emailField, passwordField, loginBtn, signupLink, messageLabel);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        messageLabel.setText("");
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password.");
            return;
        }
        // TODO: Add Firebase Authentication logic here
        // Example: if (FirebaseAuth.signIn(email, password)) { ... }
        boolean loginSuccess = true; // Replace with actual Firebase result
        if (loginSuccess) {
            Dashboard.show(primaryStage);
        } else {
            messageLabel.setText("Invalid email or password.");
        }
    }

    public static void show(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene scene = new Scene(loginPage, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Login");
        primaryStage.show();
    }
} 