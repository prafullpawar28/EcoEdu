package com.ecoedu.adminpanel;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;

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
        // Background image
        StackPane root = new StackPane();
        ImageView bgImage = new ImageView();
        try {
            bgImage.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/login_signup.jpg")));
        } catch (Exception e) {
            bgImage.setImage(null);
        }
        bgImage.setFitWidth(1366);
        bgImage.setFitHeight(768);
        bgImage.setPreserveRatio(false);
        bgImage.setOpacity(0.92);

        VBox card = new VBox(24);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setMaxHeight(420);
        card.setMinHeight(380);
        card.setPadding(new Insets(24, 32, 24, 32));
        card.setStyle("-fx-background-color: transparent; " +
            "-fx-background-radius: 28; " +
            "-fx-border-color: rgba(255,255,255,0.7); " +
            "-fx-border-width: 2.5; " +
            "-fx-border-radius: 28; " +
            "-fx-effect: dropshadow(gaussian, #00000055, 32, 0.18, 0, 8);");

        // Animated admin icon
        Circle avatarCircle = new Circle(50, Color.web("#fffde7"));
        Label avatarLabel = new Label("A");
        avatarLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
        avatarLabel.setTextFill(Color.web("#6a1b9a"));
        StackPane avatarPane = new StackPane(avatarCircle, avatarLabel);
        card.getChildren().add(avatarPane);

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

        messageLabel = new Label("");
        messageLabel.setFont(Font.font("Quicksand", 13));
        messageLabel.setTextFill(Color.web("#d32f2f"));

        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(32, 32);

        Button backBtn = new Button("Back");
        backBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 15));
        backBtn.setStyle("-fx-background-color: #FFD3B6; -fx-text-fill: #6a1b9a; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand; margin-top: 16px;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentLoginPage.show(primaryStage));

        card.getChildren().addAll(title, subtitle, formBox, loginBtn, messageLabel, loadingIndicator, backBtn);
        root.getChildren().addAll(bgImage, card);
        getChildren().add(root);
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
        new Thread(() -> {
            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                AdminDataService.User found = null;
                for (AdminDataService.User u : AdminDataService.getInstance().getUsers()) {
                    if (u.email.equalsIgnoreCase(email)) {
                        found = u;
                        break;
                    }
                }
                if (found == null) {
                    messageLabel.setText("No such user.");
                } else if (!found.password.equals(password)) {
                    messageLabel.setText("Incorrect password.");
                } else if (!"Admin".equals(found.role)) {
                    messageLabel.setText("Not an admin user.");
                } else {
                    AdminDashboard.show(primaryStage);
                }
            });
        }).start();
    }

    public static void show(Stage primaryStage) {
        AdminLoginPage page = new AdminLoginPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Login");
        primaryStage.show();
    }
} 