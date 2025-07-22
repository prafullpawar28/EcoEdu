package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.scene.shape.Circle;
import com.ecoedu.dashboard.FeedbackSupportPage;
import com.ecoedu.dashboard.SystemLogsPage;
import com.ecoedu.dashboard.RealTimeUserManagementPage;
import com.ecoedu.dashboard.StudentDashboardWithBack;
import javafx.scene.control.ProgressBar;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class AdminDashboard extends BorderPane {
    private Stage primaryStage;
    private VBox sidebar;
    private VBox notificationsBox;
    private Label headerTitle;
    private StackPane mainContent;
    private String adminName;

    private String[] quotes = {
        "Leadership is not a position or a title, it is action and example.",
        "Empower others to act for the planet.",
        "Great leaders inspire greatness in others.",
        "Every action counts in sustainability."
    };
    private int currentQuoteIndex = 0;

    // Mock admin data (replace with real data integration)
    private String mockName = "A. Verma";
    private String mockRole = "System Admin";
    private String mockAvatar = null; // Use initials if null

    public AdminDashboard(Stage primaryStage) {
        this(primaryStage, null);
    }

    public AdminDashboard(Stage primaryStage, String adminName) {
        this.primaryStage = primaryStage;
        this.adminName = adminName != null ? adminName : mockName;
        setStyle("-fx-background-color: linear-gradient(to bottom right, #f3e5f5, #e1f5fe);");

        // --- Header ---
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 32, 18, 32));
        header.setSpacing(24);
        header.setStyle("-fx-background-color: linear-gradient(to right, #6a1b9a 60%, #43a047 100%); -fx-background-radius: 0 0 32 32; -fx-effect: dropshadow(gaussian, #b39ddb, 12, 0.2, 0, 4);");
        headerTitle = new Label("Admin Dashboard");
        headerTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        headerTitle.setTextFill(Color.WHITE);
        headerTitle.setStyle("-fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #fffde7, 2, 0.2, 0, 1);");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // Notification bell with badge
        StackPane bellPane = new StackPane();
        Label bell = new Label("\uD83D\uDD14");
        bell.setFont(Font.font(26));
        Circle badge = new Circle(7, Color.web("#ff5252"));
        Label badgeNum = new Label("1");
        badgeNum.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        badgeNum.setTextFill(Color.WHITE);
        StackPane badgeStack = new StackPane(badge, badgeNum);
        badgeStack.setTranslateX(10);
        badgeStack.setTranslateY(-10);
        bellPane.getChildren().addAll(bell, badgeStack);
        bellPane.setOnMouseClicked(e -> showNotification("System update scheduled for tonight."));
        // Admin avatar/profile
        StackPane avatarPane = new StackPane();
        if (mockAvatar != null) {
            ImageView avatarImg = new ImageView(new Image(getClass().getResourceAsStream(mockAvatar)));
            avatarImg.setFitWidth(40);
            avatarImg.setFitHeight(40);
            avatarImg.setClip(new Circle(20, 20, 20));
            avatarPane.getChildren().add(avatarImg);
        } else {
            Circle avatarCircle = new Circle(20, Color.web("#fffde7"));
            Label avatarLabel = new Label(this.adminName.substring(0, 1));
            avatarLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
            avatarLabel.setTextFill(Color.web("#6a1b9a"));
            avatarPane.getChildren().addAll(avatarCircle, avatarLabel);
        }
        avatarPane.setPadding(new Insets(0, 8, 0, 8));
        // Admin profile dropdown
        Button settingsBtn = new Button("\u2699\uFE0F"); // Unicode gear icon
        settingsBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        settingsBtn.setTextFill(Color.web("#fffde7"));
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 8 0 8;");
        settingsBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings");
            alert.setHeaderText(null);
            alert.setContentText("Settings page coming soon!");
            alert.showAndWait();
        });
        MenuButton profileMenu = new MenuButton(this.adminName);
        profileMenu.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #6a1b9a; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 6 18;");
        MenuItem logout = new MenuItem("Logout");
        logout.setOnAction(e -> com.ecoedu.Home.Home.showStudentDashboard(primaryStage));
        profileMenu.getItems().addAll(logout);
        header.getChildren().addAll(headerTitle, spacer, bellPane, avatarPane, settingsBtn, profileMenu);
        setTop(header);

        // --- Dynamic Profile Section ---
        HBox profileSection = new HBox(18);
        profileSection.setAlignment(Pos.CENTER_LEFT);
        profileSection.setPadding(new Insets(0, 0, 18, 32));
        profileSection.setStyle("-fx-background-color: linear-gradient(to right, #ede7f6, #fffde7 80%); -fx-background-radius: 22; -fx-effect: dropshadow(gaussian, #b2ff59, 8, 0.1, 0, 2);");
        VBox profileInfo = new VBox(6);
        Label nameLabel = new Label(this.adminName);
        nameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        nameLabel.setTextFill(Color.web("#6a1b9a"));
        Label roleLabel = new Label(mockRole);
        roleLabel.setFont(Font.font("Quicksand", 14));
        roleLabel.setTextFill(Color.web("#388e3c"));
        profileInfo.getChildren().addAll(nameLabel, roleLabel);
        profileSection.getChildren().addAll(profileInfo);
        sidebar = new VBox(18);
        sidebar.setPadding(new Insets(36, 18, 36, 18));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #ede7f6; -fx-background-radius: 0 32 32 0;");
        sidebar.setPrefWidth(220);
        sidebar.getChildren().add(makeSidebarButton("👥 Manage Students", this::showManageStudents));
        sidebar.getChildren().add(makeSidebarButton("📊 Analytics", this::showAnalytics));
        sidebar.getChildren().add(makeSidebarButton("📚 Manage Modules", this::showManageModules));
        sidebar.getChildren().add(makeSidebarButton("📝 Review Quizzes", this::showReviewQuizzes));
        sidebar.getChildren().add(makeSidebarButton("🏆 Leaderboard & Badges", this::showLeaderboardAndBadges));
        sidebar.getChildren().add(makeSidebarButton("🔄 Real-Time User Management", this::showRealTimeUserManagement));
        sidebar.getChildren().add(makeSidebarButton("📝 System Logs", this::showSystemLogs));
        sidebar.getChildren().add(makeSidebarButton("💬 Feedback & Support", this::showFeedbackSupport));
        sidebar.getChildren().add(makeSidebarButton("🔀 Switch to Student Dashboard", this::switchToStudentDashboard));
        VBox leftBox = new VBox();
        if (profileSection != null) leftBox.getChildren().add(profileSection);
        if (sidebar != null) leftBox.getChildren().add(sidebar);
        setLeft(leftBox);

        // --- Notifications Area ---
        notificationsBox = new VBox(8);
        notificationsBox.setPadding(new Insets(18));
        notificationsBox.setAlignment(Pos.TOP_RIGHT);
        notificationsBox.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #bdbdbd, 8, 0.2, 0, 2);");
        notificationsBox.getChildren().add(new Label("🔔 No new notifications"));
        setRight(notificationsBox);

        // --- Main Content Area ---
        mainContent = new StackPane();
        mainContent.setPadding(new Insets(36));
        mainContent.setStyle("-fx-background-color: white; -fx-background-radius: 32; -fx-effect: dropshadow(gaussian, #bdbdbdbd, 16, 0.2, 0, 4);");
        setCenter(mainContent);

        // Show default section
        showAnalytics();
    }

    private Button makeSidebarButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        button.setTextFill(Color.web("#6a1b9a"));
        button.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 16; -fx-padding: 12 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #b2ff59, 4, 0.1, 0, 1);");
        button.setOnAction(e -> {
            action.run();
            showNotification("Navigating to " + text);
        });
        return button;
    }

    private void showNotification(String message) {
        Label notificationLabel = new Label(message);
        notificationLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
        notificationLabel.setTextFill(Color.web("#388e3c"));
        notificationLabel.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 12; -fx-padding: 10 15; -fx-effect: dropshadow(gaussian, #b2ff59, 4, 0.1, 0, 1);");
        notificationsBox.getChildren().add(notificationLabel);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> notificationsBox.getChildren().remove(notificationLabel));
        delay.play();
    }

    private void showManageStudents() {
        Label label = new Label("Manage Students Page Coming Soon!");
        label.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        label.setTextFill(Color.web("#6a1b9a"));
        label.setPadding(new Insets(20));
        label.setAlignment(Pos.CENTER);
        mainContent.getChildren().clear();
        mainContent.getChildren().add(label);
    }

    private void showAnalytics() {
        Label label = new Label("Analytics Page Coming Soon!");
        label.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        label.setTextFill(Color.web("#6a1b9a"));
        label.setPadding(new Insets(20));
        label.setAlignment(Pos.CENTER);
        mainContent.getChildren().clear();
        mainContent.getChildren().add(label);
    }

    private void showManageModules() {
        Label label = new Label("Manage Modules Page Coming Soon!");
        label.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        label.setTextFill(Color.web("#6a1b9a"));
        label.setPadding(new Insets(20));
        label.setAlignment(Pos.CENTER);
        mainContent.getChildren().clear();
        mainContent.getChildren().add(label);
    }

    private void showReviewQuizzes() {
        Label label = new Label("Review Quizzes Page Coming Soon!");
        label.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        label.setTextFill(Color.web("#6a1b9a"));
        label.setPadding(new Insets(20));
        label.setAlignment(Pos.CENTER);
        mainContent.getChildren().clear();
        mainContent.getChildren().add(label);
    }

    private void showLeaderboardAndBadges() {
        Label label = new Label("Leaderboard & Badges Page Coming Soon!");
        label.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        label.setTextFill(Color.web("#6a1b9a"));
        label.setPadding(new Insets(20));
        label.setAlignment(Pos.CENTER);
        mainContent.getChildren().clear();
        mainContent.getChildren().add(label);
    }

    private void showRealTimeUserManagement() {
        RealTimeUserManagementPage.show(primaryStage);
    }

    private void showSystemLogs() {
        SystemLogsPage.show(primaryStage);
    }

    private void showFeedbackSupport() {
        FeedbackSupportPage.show(primaryStage);
    }

    private void switchToStudentDashboard() {
        com.ecoedu.Home.Home.showStudentDashboard(primaryStage);
    }

    public static void show(Stage primaryStage) {
        AdminDashboard dashboard = new AdminDashboard(primaryStage);
        Scene scene = new Scene(dashboard, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Admin Dashboard");
        primaryStage.show();
    }
} 