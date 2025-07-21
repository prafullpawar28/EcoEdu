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

// Add a static ProfileData class for demo purposes
class ProfileData {
    public static String name = "";
    public static String email = "";
    public static String avatarPath = null;
}

public class StudentSignupPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;
    private TextField nameField; // Make nameField a class field

    public StudentSignupPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(0);
        setAlignment(Pos.CENTER);
        getStyleClass().add("root");

        StackPane background = new StackPane();
        background.setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #FFD3B6);");
        background.setPrefSize(500, 650);

        VBox card = new VBox(24);
        card.getStyleClass().add("eco-card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);

        // Lottie animation placeholder (replace with real Lottie integration if available)
        ImageView ecoAnim = new ImageView(new Image(getClass().getResource("/Assets/Images/welcomepage.jpg").toExternalForm()));
        ecoAnim.setFitWidth(120);
        ecoAnim.setFitHeight(120);
        ecoAnim.setClip(new Circle(60, 60, 60));
        ecoAnim.setStyle("-fx-effect: dropshadow(gaussian, #A8E6CF, 12, 0.2, 0, 4);");

        Label title = new Label("Join EcoEdu");
        title.getStyleClass().add("eco-title");
        title.setStyle("-fx-font-family: 'Quicksand', 'Nunito', sans-serif;");

        HBox nameBox = new HBox(8);
        Label nameIcon = new Label("\uD83C\uDF33"); // Tree emoji as icon
        nameIcon.getStyleClass().add("eco-icon");
        nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.getStyleClass().add("eco-field");
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.getChildren().addAll(nameIcon, nameField);

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

        HBox confirmBox = new HBox(8);
        Label confirmIcon = new Label("\uD83D\uDD12"); // Lock emoji as icon
        confirmIcon.getStyleClass().add("eco-icon");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("eco-field");
        confirmBox.setAlignment(Pos.CENTER_LEFT);
        confirmBox.getChildren().addAll(confirmIcon, confirmPasswordField);

        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Child", "Parent", "Teacher");
        roleDropdown.setPromptText("Select Role");
        roleDropdown.getStyleClass().add("eco-dropdown");
        roleDropdown.setMaxWidth(300);

        Button signupBtn = new Button("Sign Up");
        signupBtn.getStyleClass().add("eco-btn");
        signupBtn.setOnAction(e -> handleSignup());

        HBox links = new HBox(16);
        links.setAlignment(Pos.CENTER);
        Hyperlink loginLink = new Hyperlink("Already have an account? Login");
        loginLink.getStyleClass().add("eco-link");
        loginLink.setOnAction(e -> StudentLoginPage.show(primaryStage));
        links.getChildren().add(loginLink);

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");

        card.getChildren().addAll(ecoAnim, title, nameBox, emailBox, passBox, confirmBox, roleDropdown, signupBtn, links, messageLabel);
        background.getChildren().add(card);
        getChildren().add(background);
    }

    private void handleSignup() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String name = nameField.getText();
        messageLabel.setText("");
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            messageLabel.setText("Please fill all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }
        // Save to ProfileData
        ProfileData.name = name;
        ProfileData.email = email;
        // TODO: Save to Firebase in the future
        boolean signupSuccess = true; // Replace with real check
        if (signupSuccess) {
            com.ecoedu.dashboard.ProfilePage.show(primaryStage, false);
        } else {
            messageLabel.setText("Signup failed. Try a different email.");
        }
    }

    public static void show(Stage primaryStage) {
        StudentSignupPage page = new StudentSignupPage(primaryStage);
        Scene scene = new Scene(page, 500, 550);
        // com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Sign Up");
        primaryStage.show();
    }
} 