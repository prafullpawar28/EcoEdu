package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsPage extends VBox {
    private Stage primaryStage;
    private boolean isAdmin;
    private Label messageLabel;

    public SettingsPage(Stage primaryStage, boolean isAdmin) {
        this.primaryStage = primaryStage;
        this.isAdmin = isAdmin;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("root");

        Label title = new Label(isAdmin ? "Admin Settings" : "User Settings");
        title.getStyleClass().add("eco-title");

        // Theme toggle
        HBox themeBox = new HBox(10);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        Label themeLabel = new Label("Theme:");
        ToggleGroup themeGroup = new ToggleGroup();
        RadioButton lightBtn = new RadioButton("Light");
        RadioButton darkBtn = new RadioButton("Dark");
        lightBtn.setToggleGroup(themeGroup);
        darkBtn.setToggleGroup(themeGroup);
        lightBtn.setSelected(true);
        themeBox.getChildren().addAll(themeLabel, lightBtn, darkBtn);

        // Notification toggle
        HBox notifBox = new HBox(10);
        notifBox.setAlignment(Pos.CENTER_LEFT);
        Label notifLabel = new Label("Notifications:");
        CheckBox notifToggle = new CheckBox("Enable notifications");
        notifToggle.setSelected(true);
        notifBox.getChildren().addAll(notifLabel, notifToggle);

        // Account management
        HBox accountBox = new HBox(10);
        accountBox.setAlignment(Pos.CENTER_LEFT);
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("eco-btn");
        logoutBtn.setOnAction(e -> handleLogout());
        Button deleteBtn = new Button("Delete Account");
        deleteBtn.getStyleClass().add("eco-btn");
        deleteBtn.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> handleDeleteAccount());
        accountBox.getChildren().addAll(logoutBtn, deleteBtn);

        // Save button
        Button saveBtn = new Button("Save Settings");
        saveBtn.getStyleClass().add("eco-btn");
        saveBtn.setOnAction(e -> saveSettings());

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");

        getChildren().addAll(title, themeBox, notifBox, accountBox, saveBtn, messageLabel);
    }

    private void handleLogout() {
        // TODO: Implement logout logic
        messageLabel.setText("Logged out! (Stub)");
    }

    private void handleDeleteAccount() {
        // TODO: Implement delete logic
        messageLabel.setText("Account deleted! (Stub)");
    }

    private void saveSettings() {
        // TODO: Save settings to Firebase (stub)
        messageLabel.setText("Settings saved! (Firebase integration coming soon)");
    }

    public static void show(Stage primaryStage, boolean isAdmin) {
        SettingsPage page = new SettingsPage(primaryStage, isAdmin);
        Scene scene = new Scene(page, 1366, 768);
        com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle(isAdmin ? "Admin Settings" : "User Settings");
        primaryStage.show();
    }
} 