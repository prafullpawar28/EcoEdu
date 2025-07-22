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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.ecoedu.dashboard.AdminHelpDialog;

public class AdminLoginPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField passwordVisibleField;
    private Label messageLabel;
    private ProgressIndicator loadingIndicator;
    private boolean isPasswordVisible = false;

    public AdminLoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(0);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #ede7f6, #e1f5fe);");

        StackPane background = new StackPane();
        background.setStyle("-fx-background-color: transparent;");
        background.setPrefSize(500, 600);

        VBox card = new VBox(24);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 24; -fx-effect: dropshadow(gaussian, #6a1b9a, 16, 0.2, 0, 4);");

        // Animated admin icon
        ImageView adminAnim = new ImageView();
        try {
            adminAnim.setImage(new Image(getClass().getResource("/Assets/Images/avatar.png").toExternalForm()));
        } catch (Exception e) {
            adminAnim.setImage(null);
        }
        adminAnim.setFitWidth(100);
        adminAnim.setFitHeight(100);
        adminAnim.setClip(new Circle(50, 50, 50));
        adminAnim.setStyle("-fx-effect: dropshadow(gaussian, #6a1b9a, 12, 0.2, 0, 4);");

        Label title = new Label("Welcome, Admin!");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#6a1b9a"));
        Label subtitle = new Label("Manage EcoEdu with confidence.");
        subtitle.setFont(Font.font("Quicksand", 16));
        subtitle.setTextFill(Color.web("#388e3c"));

        HBox emailBox = new HBox(8);
        Label emailIcon = new Label("\uD83D\uDCE7");
        emailIcon.setFont(Font.font(20));
        emailBox.setAlignment(Pos.CENTER);
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setFont(Font.font("Quicksand", 16));
        emailField.setPrefWidth(220);
        emailBox.getChildren().addAll(emailIcon, emailField);

        HBox passBox = new HBox(8);
        Label passIcon = new Label("\uD83D\uDD12");
        passIcon.setFont(Font.font(20));
        passBox.setAlignment(Pos.CENTER);
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setFont(Font.font("Quicksand", 16));
        passwordField.setPrefWidth(180);
        passwordVisibleField = new TextField();
        passwordVisibleField.setFont(Font.font("Quicksand", 16));
        passwordVisibleField.setPrefWidth(180);
        passwordVisibleField.setManaged(false);
        passwordVisibleField.setVisible(false);
        Button showHideBtn = new Button("👁");
        showHideBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 16; -fx-cursor: hand;");
        showHideBtn.setOnAction(e -> togglePasswordVisibility());
        passBox.getChildren().addAll(passIcon, passwordField, passwordVisibleField, showHideBtn);

        // Sync password fields
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> passwordVisibleField.setText(newVal));
        passwordVisibleField.textProperty().addListener((obs, oldVal, newVal) -> passwordField.setText(newVal));

        VBox formBox = new VBox(18, emailBox, passBox);
        formBox.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        loginBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        loginBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> handleLogin());
        loginBtn.setDefaultButton(true);

        // Keyboard accessibility
        emailField.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleLogin(); });
        passwordField.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleLogin(); });
        passwordVisibleField.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleLogin(); });

        Button helpBtn = new Button("Help");
        helpBtn.setFont(Font.font("Quicksand", 14));
        helpBtn.setStyle("-fx-background-color: #81c784; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 6 24; -fx-cursor: hand;");
        helpBtn.setOnAction(e -> AdminHelpDialog.show(primaryStage));

        messageLabel = new Label("");
        messageLabel.setFont(Font.font("Quicksand", 13));
        messageLabel.setTextFill(Color.web("#d32f2f"));

        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(32, 32);

        card.getChildren().setAll(adminAnim, title, subtitle, formBox, loginBtn, helpBtn, messageLabel, loadingIndicator);
        background.getChildren().setAll(card);
        getChildren().setAll(background);
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        passwordVisibleField.setManaged(isPasswordVisible);
        passwordVisibleField.setVisible(isPasswordVisible);
        passwordField.setManaged(!isPasswordVisible);
        passwordField.setVisible(!isPasswordVisible);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = isPasswordVisible ? passwordVisibleField.getText() : passwordField.getText();
        messageLabel.setText("");
        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both email and password.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            messageLabel.setText("Please enter a valid email address.");
            return;
        }
        loadingIndicator.setVisible(true);
        // Simulate loading and call real authentication service here
        new Thread(() -> {
            try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                // TODO: Replace with real authentication logic, e.g.:
                // boolean loginSuccess = AuthService.login(email, password);
                boolean loginSuccess = true; // TEMP: allows any login for testing
                if (loginSuccess) {
                    AdminDashboard.show(primaryStage);
                } else {
                    messageLabel.setText("Invalid email or password.");
                }
            });
        }).start();
    }

    public static void show(Stage primaryStage) {
        AdminLoginPage page = new AdminLoginPage(primaryStage);
        Scene scene = new Scene(page, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Login");
        primaryStage.show();
    }
} 