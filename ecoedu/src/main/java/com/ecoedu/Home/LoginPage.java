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
// Add this import so SignupPage is recognized
import com.ecoedu.Home.SignupPage;

public class LoginPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private Label errorLabel;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(18);
        setPadding(new Insets(40, 40, 40, 40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe, #fffde7);");

        Label title = new Label("Login to EcoEdu");
        title.setFont(Font.font("Comic Sans MS", 28));
        title.setTextFill(Color.web("#0288d1"));

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setFont(Font.font(16));
        emailField.setMaxWidth(260);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setFont(Font.font(16));
        passwordField.setMaxWidth(260);

        Button loginBtn = new Button("Login");
        loginBtn.setFont(Font.font(18));
        loginBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 24;");
        loginBtn.setOnAction(e -> handleLogin());

        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign up!");
        signupLink.setFont(Font.font(14));
        signupLink.setOnAction(e -> SignupPage.show(primaryStage));

        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font(14));

        getChildren().addAll(title, emailField, passwordField, loginBtn, signupLink, errorLabel);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        errorLabel.setText("");
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password.");
            return;
        }
        try {
            Map<String, String> result = AuthService.login(email, password);
            // On success, navigate to Dashboard
            Dashboard.show(primaryStage);
        } catch (Exception ex) {
            errorLabel.setText("Login failed: " + ex.getMessage());
        }
    }

    // Utility to show this page
    public static void show(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene scene = new Scene(loginPage, 420, 420);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Login");
        primaryStage.show();
    }
} 