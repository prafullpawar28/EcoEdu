package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import java.util.prefs.Preferences;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.collections.ObservableList;

public class AdminDashboard extends HBox {
    private boolean darkMode = false;
    private Preferences prefs = Preferences.userNodeForPackage(AdminDashboard.class);
    private Button darkModeBtn;
    private HBox topBar;
    private VBox sidebar;
    private StackPane mainContent;
    private Stage primaryStage;
    private TextField userSearchField;
    private AdminUsersPage usersPage;
    private List<LogEntry> mockLogs = new ArrayList<>();

    public AdminDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(0);
        loadPrefs();
        setStyle(getBgStyle());

        // Sidebar
        sidebar = new VBox(18);
        sidebar.setPadding(new Insets(36, 18, 36, 18));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle(getSidebarStyle());
        sidebar.setPrefWidth(220);
        sidebar.getChildren().add(makeSidebarButton("👤 Users", "/Assets/Images/avatar.gif", this::showUsers));
        sidebar.getChildren().add(makeSidebarButton("🎮 Games", "/Assets/Images/minigames.jpg", this::showGames));
        sidebar.getChildren().add(makeSidebarButton("🏅 Badges", "/Assets/Images/boat.png", this::showBadges));
        sidebar.getChildren().add(makeSidebarButton("📊 Analytics", "/Assets/Images/ocean.jpg", this::showAnalytics));
        sidebar.getChildren().add(makeSidebarButton("🗒 Logs", "/Assets/Images/trash1.png", this::showLogs));
        sidebar.getChildren().add(makeSidebarButton("📝 Feedback", "/Assets/Images/welcomepage.jpg", this::showFeedback));
        sidebar.getChildren().add(makeSidebarButton("⚙️ Settings", "/Assets/Images/homepagef.jpg", this::showSettings));
        sidebar.getChildren().add(makeSidebarButton("🚪 Logout", "/Assets/Images/trashsorter.jpeg", this::logout));

        // Main content
        mainContent = new StackPane();
        mainContent.setPadding(new Insets(36));
        mainContent.setStyle(getCardStyle());
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        VBox root = new VBox();
        root.setSpacing(0);
        // Top bar
        topBar = new HBox();
        topBar.setPadding(new Insets(0, 0, 0, 0));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(getTopBarStyle());
        topBar.setMinHeight(64);
        Label panelLabel = new Label("Admin Panel");
        panelLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 28));
        panelLabel.setTextFill(darkMode ? Color.web("#b2dfdb") : Color.web("#fffde7"));
        panelLabel.setPadding(new Insets(0, 0, 0, 32));
        // Avatar/profile icon
        ImageView avatar = new ImageView();
        try {
            avatar.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/avatar.png")));
        } catch (Exception e) {
            avatar.setImage(null);
        }
        avatar.setFitHeight(40);
        avatar.setFitWidth(40);
        avatar.setPreserveRatio(true);
        HBox avatarBox = new HBox(avatar);
        avatarBox.setAlignment(Pos.CENTER_RIGHT);
        avatarBox.setPadding(new Insets(0, 32, 0, 0));
        avatarBox.setMinWidth(60);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // Dark mode toggle
        darkModeBtn = new Button(darkMode ? "☀️" : "🌙");
        darkModeBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 22; -fx-cursor: hand;");
        darkModeBtn.setOnAction(e -> toggleDarkMode());
        // Switch to Student Dashboard button (admin only)
        Button switchToStudentBtn = new Button("Switch to Student Dashboard");
        switchToStudentBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24; -fx-font-size: 15; -fx-cursor: hand;");
        switchToStudentBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        // Add hover effect
        switchToStudentBtn.setOnMouseEntered(e -> switchToStudentBtn.setStyle("-fx-background-color: #66bb6a; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24; -fx-font-size: 15; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #43a047, 8, 0.2, 0, 2); -fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        switchToStudentBtn.setOnMouseExited(e -> switchToStudentBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24; -fx-font-size: 15; -fx-cursor: hand;"));
        topBar.getChildren().addAll(panelLabel, spacer, switchToStudentBtn, darkModeBtn, avatarBox);
        // Main content (sidebar + mainContent)
        HBox mainRow = new HBox();
        mainRow.getChildren().addAll(sidebar, mainContent);
        root.getChildren().addAll(topBar, mainRow);
        getChildren().setAll(root);
        showUsers();
    }

    private Button makeSidebarButton(String text, String iconPath, Runnable onClick) {
        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
        } catch (Exception e) {
            icon.setImage(null);
        }
        icon.setFitHeight(28);
        icon.setFitWidth(28);
        icon.setPreserveRatio(true);
        Button btn = new Button(text, icon);
        btn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        btn.setStyle(getSidebarBtnStyle());
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> onClick.run());
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> btn.setStyle(getSidebarBtnHoverStyle()));
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, e -> btn.setStyle(getSidebarBtnStyle()));
        return btn;
    }

    private void showUsers() {
        if (usersPage == null) usersPage = new AdminUsersPage();
        VBox usersBox = new VBox(12);
        usersBox.setAlignment(Pos.TOP_CENTER);
        // Search bar
        userSearchField = new TextField();
        userSearchField.setPromptText("Search users by name or email...");
        userSearchField.setMaxWidth(320);
        userSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
            usersPage.filterUsers(newVal);
        });
        usersBox.getChildren().addAll(userSearchField, usersPage);
        mainContent.getChildren().setAll(usersBox);
    }
    private void showGames() {
        mainContent.getChildren().setAll(new Label("Games page coming soon!"));
    }
    private void showBadges() {
        mainContent.getChildren().setAll(new Label("Badges page coming soon!"));
    }
    private void showAnalytics() {
        mainContent.getChildren().setAll(new Label("Analytics page coming soon!"));
    }
    private void showLogs() {
        VBox logsBox = new VBox(10);
        logsBox.setAlignment(Pos.TOP_LEFT);
        logsBox.setPadding(new Insets(16));
        Label title = new Label("System Logs");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        ListView<String> logList = new ListView<>();
        logList.setPrefHeight(400);
        // Mock logs
        if (mockLogs.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                mockLogs.add(new LogEntry("Admin action " + (i+1), new Date(System.currentTimeMillis() - i*60000)));
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObservableList<String> logItems = FXCollections.observableArrayList();
        for (LogEntry log : mockLogs) {
            logItems.add("[" + sdf.format(log.timestamp) + "] " + log.message);
        }
        logList.setItems(logItems);
        logsBox.getChildren().addAll(title, logList);
        mainContent.getChildren().setAll(logsBox);
    }
    private void showFeedback() {
        mainContent.getChildren().setAll(new Label("Feedback page coming soon!"));
    }
    private void showSettings() {
        mainContent.getChildren().setAll(new Label("Settings page coming soon!"));
    }
    private void logout() {
        AdminLoginPage.show(primaryStage);
    }
    public static void show(Stage primaryStage) {
        AdminDashboard dashboard = new AdminDashboard(primaryStage);
        Scene scene = new Scene(dashboard, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Admin Panel");
        primaryStage.show();
    }
    // --- Theme and Style Helpers ---
    private void toggleDarkMode() {
        darkMode = !darkMode;
        prefs.putBoolean("darkMode", darkMode);
        setStyle(getBgStyle());
        sidebar.setStyle(getSidebarStyle());
        mainContent.setStyle(getCardStyle());
        topBar.setStyle(getTopBarStyle());
    }
    private void loadPrefs() {
        darkMode = prefs.getBoolean("darkMode", false);
    }
    private String getBgStyle() {
        return darkMode ? "-fx-background-color: linear-gradient(to bottom right, #232526, #414345);" : "-fx-background-color: linear-gradient(to bottom right, #b2dfdb, #e0f7fa);";
    }
    private String getSidebarStyle() {
        return darkMode ? "-fx-background-color: #263238; -fx-background-radius: 0 32 32 0;" : "-fx-background-color: #00796b; -fx-background-radius: 0 32 32 0;";
    }
    private String getTopBarStyle() {
        return darkMode ? "-fx-background-color: #37474f; -fx-background-radius: 0 0 32 32; -fx-effect: dropshadow(gaussian, #232526, 8, 0.1, 0, 2);" : "-fx-background-color: #0097a7; -fx-background-radius: 0 0 32 32; -fx-effect: dropshadow(gaussian, #6a1b9a, 8, 0.1, 0, 2);";
    }
    private String getCardStyle() {
        return darkMode ? "-fx-background-color: #263238; -fx-background-radius: 32; -fx-effect: dropshadow(gaussian, #232526, 16, 0.2, 0, 4);" : "-fx-background-color: white; -fx-background-radius: 32; -fx-effect: dropshadow(gaussian, #bdbdbdbd, 16, 0.2, 0, 4);";
    }
    private String getSidebarBtnStyle() {
        return darkMode ? "-fx-background-color: transparent; -fx-text-fill: #b2dfdb; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand;" : "-fx-background-color: transparent; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand;";
    }
    private String getSidebarBtnHoverStyle() {
        return darkMode ? "-fx-background-color: #37474f; -fx-text-fill: #b2dfdb; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #b2dfdb, 8, 0.2, 0, 2); -fx-scale-x: 1.05; -fx-scale-y: 1.05;" : "-fx-background-color: #b2dfdb; -fx-text-fill: #00796b; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #0097a7, 8, 0.2, 0, 2); -fx-scale-x: 1.05; -fx-scale-y: 1.05;";
    }
    // --- Mock Log Entry ---
    private static class LogEntry {
        String message;
        Date timestamp;
        LogEntry(String message, Date timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
    }
} 