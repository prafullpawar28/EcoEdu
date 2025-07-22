package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.ecoedu.dashboard.ProfileData;

public class ProfilePage extends VBox {
    private Stage primaryStage;
    private TextField nameField;
    private TextField emailField;
    private ImageView avatarView;
    private String avatarPath;
    private Label messageLabel;
    private boolean isAdmin;

    public ProfilePage(Stage primaryStage, boolean isAdmin) {
        this.primaryStage = primaryStage;
        this.isAdmin = isAdmin;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("root");

        Label title = new Label(isAdmin ? "Admin Profile" : "User Profile");
        title.getStyleClass().add("eco-title");

        // Avatar
        avatarView = new ImageView();
        avatarView.setFitWidth(100);
        avatarView.setFitHeight(100);
        avatarView.setStyle("-fx-background-radius: 50; -fx-effect: dropshadow(gaussian, #A8E6CF, 8, 0.2, 0, 2);");
        if (ProfileData.avatarPath != null) {
            avatarView.setImage(new Image(ProfileData.avatarPath));
        } else {
            avatarView.setImage(new Image(getClass().getResource("/Assets/Images/avatar.png").toExternalForm()));
        }
        Button uploadBtn = new Button("Change Avatar");
        uploadBtn.getStyleClass().add("eco-btn");
        uploadBtn.setOnAction(e -> chooseAvatar());

        // Name and Email
        nameField = new TextField(ProfileData.name);
        nameField.setPromptText("Full Name");
        nameField.getStyleClass().add("eco-field");
        emailField = new TextField(ProfileData.email);
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("eco-field");

        // Save button
        Button saveBtn = new Button("Save Changes");
        saveBtn.getStyleClass().add("eco-btn");
        saveBtn.setOnAction(e -> saveProfile());

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");

        getChildren().addAll(title, avatarView, uploadBtn, nameField, emailField, saveBtn, messageLabel);
    }

    private void chooseAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        java.io.File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            avatarPath = file.toURI().toString();
            avatarView.setImage(new Image(avatarPath));
            ProfileData.avatarPath = avatarPath;
        }
    }

    private void saveProfile() {
        String name = nameField.getText();
        String email = emailField.getText();
        if (name.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Name and email cannot be empty.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            messageLabel.setText("Please enter a valid email address.");
            return;
        }
        // TODO: Save to Firebase (stub)
        messageLabel.setText("Profile saved! (Firebase integration coming soon)");
    }

    public static void show(Stage primaryStage, boolean isAdmin) {
        ProfilePage page = new ProfilePage(primaryStage, isAdmin);
        Scene scene = new Scene(page, 1366, 768);
        com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle(isAdmin ? "Admin Profile" : "User Profile");
        primaryStage.show();
    }
} 