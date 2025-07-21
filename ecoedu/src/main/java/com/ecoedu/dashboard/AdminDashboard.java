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

public class AdminDashboard extends BorderPane {
    private Stage primaryStage;
    private VBox sidebar;
    private VBox notificationsBox;
    private Label headerTitle;
    private StackPane mainContent;

    public AdminDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
        Label bell = new Label("🔔");
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
        // Admin avatar
        Circle avatarCircle = new Circle(20, Color.web("#fffde7"));
        Label avatarLabel = new Label("A");
        avatarLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        avatarLabel.setTextFill(Color.web("#6a1b9a"));
        StackPane avatarPane = new StackPane(avatarCircle, avatarLabel);
        avatarPane.setPadding(new Insets(0, 8, 0, 8));
        // Admin profile dropdown
        MenuButton profileMenu = new MenuButton("Admin");
        profileMenu.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #6a1b9a; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 6 18;");
        MenuItem logout = new MenuItem("Logout");
        logout.setOnAction(e -> com.ecoedu.Home.Home.showStudentDashboard(primaryStage));
        profileMenu.getItems().addAll(logout);
        header.getChildren().addAll(headerTitle, spacer, bellPane, avatarPane, profileMenu);
        setTop(header);

        // --- Sidebar Navigation ---
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
        setLeft(sidebar);

        // --- Notifications Area ---
        notificationsBox = new VBox(8);
        notificationsBox.setPadding(new Insets(18));
        notificationsBox.setAlignment(Pos.TOP_RIGHT);
        notificationsBox.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #bdbdbd, 8, 0.2, 0, 2);");
        notificationsBox.getChildren().add(new Label("🔔 No new notifications"));

        // --- Main Content Area ---
        mainContent = new StackPane();
        mainContent.setPadding(new Insets(36));
        mainContent.setStyle("-fx-background-color: white; -fx-background-radius: 32; -fx-effect: dropshadow(gaussian, #bdbdbdbd, 16, 0.2, 0, 4);");
        setCenter(mainContent);
        setRight(notificationsBox);

        // Show default section
        showAnalytics();
    }

    private Button makeSidebarButton(String text, Runnable onClick) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        btn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #6a1b9a; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand; -fx-transition: background-color 0.2s;");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            for (javafx.scene.Node node : sidebar.getChildren()) {
                node.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #6a1b9a; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand;");
            }
            btn.setStyle("-fx-background-color: #d1c4e9; -fx-text-fill: #4a148c; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand; -fx-font-weight: bold;");
            onClick.run();
        });
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-background-color: #d1c4e9;"));
        btn.setOnMouseExited(e -> {
            if (!mainContent.getChildren().isEmpty() && ((Label)((VBox)mainContent.getChildren().get(0)).getChildren().get(0)).getText().equals(text)) {
                btn.setStyle("-fx-background-color: #d1c4e9; -fx-text-fill: #4a148c; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand; -fx-font-weight: bold;");
            } else {
                btn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #6a1b9a; -fx-background-radius: 16; -fx-padding: 10 24; -fx-cursor: hand;");
            }
        });
        return btn;
    }

    // Show a notification in the notifications area
    private void showNotification(String message) {
        notificationsBox.getChildren().clear();
        Label notif = new Label(message);
        notif.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        notif.setTextFill(Color.web("#ff5252"));
        notificationsBox.getChildren().add(notif);
        FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(800), notif);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
    }

    // --- Section Navigation ---
    private void showManageStudents() {
        headerTitle.setText("Manage Students");
        mainContent.getChildren().setAll(new ManageStudentsPage(primaryStage));
    }
    private void showAnalytics() {
        headerTitle.setText("Analytics");
        mainContent.getChildren().setAll(new AnalyticsPage(primaryStage));
    }
    private void showManageModules() {
        headerTitle.setText("Manage Modules");
        mainContent.getChildren().setAll(new ManageModulesPage(primaryStage));
    }
    private void showReviewQuizzes() {
        headerTitle.setText("Review Quizzes");
        mainContent.getChildren().setAll(new ReviewQuizzesPage(primaryStage));
    }
    private void showLeaderboardAndBadges() {
        headerTitle.setText("Leaderboard & Badges");
        mainContent.getChildren().setAll(new com.ecoedu.leaderboard.LeaderboardAndBadgesPage(primaryStage));
    }

    public static void show(Stage primaryStage) {
        AdminDashboard dashboard = new AdminDashboard(primaryStage);
        Scene scene = new Scene(dashboard, 1100, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Admin Dashboard");
        primaryStage.show();
    }
} 