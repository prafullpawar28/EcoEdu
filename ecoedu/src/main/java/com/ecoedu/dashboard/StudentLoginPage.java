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

        // Lottie animation placeholder (replace with real Lottie integration if available)
        ImageView ecoAnim = new ImageView(new Image(getClass().getResource("/Assets/Images/loginpage1.jpg").toExternalForm()));
        ecoAnim.setFitWidth(120);
        ecoAnim.setFitHeight(120);
        ecoAnim.setClip(new Circle(60, 60, 60));
        ecoAnim.setStyle("-fx-effect: dropshadow(gaussian, #A8E6CF, 12, 0.2, 0, 4);");

        Label title = new Label("EcoEdu – Learn. Play. Grow.");
        title.getStyleClass().add("eco-title");
        title.setStyle("-fx-font-family: 'Quicksand', 'Nunito', sans-serif;");

        HBox emailBox = new HBox(8);
        Label emailIcon = new Label("\uD83D\uDCE7"); // Envelope emoji as icon
        emailIcon.getStyleClass().add("eco-icon");
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("eco-field");
        emailBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.getChildren().addAll(emailIcon, emailField);

        HBox passBox = new HBox(8);
        Label passIcon = new Label("\uD83D\uDD12"); // Lock emoji as icon
        passIcon.getStyleClass().add("eco-icon");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("eco-field");
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.getChildren().addAll(passIcon, passwordField);

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("eco-btn");
        loginBtn.setOnAction(e -> handleLogin());

        Button forgotBtn = new Button("Forgot Password?");
        forgotBtn.getStyleClass().add("eco-link");
        forgotBtn.setOnAction(e -> {/* TODO: Add forgot password logic */});
        forgotBtn.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 0 0;");

        HBox links = new HBox(16);
        links.setAlignment(Pos.CENTER);
        Hyperlink signupLink = new Hyperlink("Sign Up");
        signupLink.getStyleClass().add("eco-link");
        signupLink.setOnAction(e -> StudentSignupPage.show(primaryStage));
        links.getChildren().addAll(forgotBtn, signupLink);

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");

        card.getChildren().addAll(ecoAnim, title, emailBox, passBox, loginBtn, links, messageLabel);
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
            StudentDashboard.show(primaryStage);
        } else {
            messageLabel.setText("Invalid email or password.");
        }
    }

    public static void show(Stage primaryStage) {
        StudentLoginPage page = new StudentLoginPage(primaryStage);
        Scene scene = new Scene(page, 500, 500);
        // com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Login");
        primaryStage.show();
    }
} 