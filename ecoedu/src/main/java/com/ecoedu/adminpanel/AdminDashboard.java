package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.ecoedu.dashboard.StudentLoginPage;

public class AdminDashboard extends BorderPane {
    private VBox sidebar;
    private HBox header;
    private GridPane cardGrid;
    private VBox mainContent;
    private Stage primaryStage;
    private AdminDataService dataService;
    private Timeline refreshTimeline;
    
    // Keep references to pages for reuse
    private AdminUsersPage usersPage;
    private AdminModulesPage modulesPage;
    private AdminAnalyticsPage analyticsPage;
    private AdminLogsPage logsPage;
    private AdminSettingsPage settingsPage;
    private AdminNotificationsPage notificationsPage;
    private AdminFeedbackPage feedbackPage;
    private AdminParentalControlsPage parentalControlsPage;

    // UI Components for real-time updates
    private Label activeUsersLabel;
    private Label totalStudentsLabel;
    private Label totalTeachersLabel;
    private ListView<String> notificationsList;
    private ListView<String> recentActivityList;
    private LineChart<Number, Number> activityChart;
    private PieChart userDistributionChart;

    private HBox createHeader() {
        HBox bar = new HBox();
        bar.getStyleClass().add("top-bar");
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setSpacing(18);
        Label title = new Label("Dashboard");
        title.getStyleClass().add("header-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // User avatar with null check
        ImageView avatar;
        java.io.InputStream avatarStream = getClass().getResourceAsStream("/Assets/Images/avatar.png");
        if (avatarStream != null) {
            avatar = new ImageView(new Image(avatarStream));
        } else {
            avatar = new ImageView();
            avatar.setFitWidth(36);
            avatar.setFitHeight(36);
            avatar.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 18;");
        }
        avatar.setFitWidth(36);
        avatar.setFitHeight(36);
        avatar.setPreserveRatio(true);
        
        // Switch to Student Dashboard button (admin only)
        Button switchBtn = new Button("Switch to Student Dashboard");
        switchBtn.getStyleClass().add("button");
        switchBtn.setVisible(true);
        switchBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        
        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("button");
        logoutBtn.setOnAction(e -> StudentLoginPage.show(primaryStage));
        
        // Back button
        // Button backBtn = new Button("Back");
        // backBtn.getStyleClass().add("button");
        // backBtn.setOnAction(e ->  StudentLoginPage.show(primaryStage));
        
        bar.getChildren().addAll(title, spacer, switchBtn, logoutBtn, avatar);
        return bar;
    }

    private VBox createFunctionalFields() {
        VBox box = new VBox(18);
        box.setPadding(new Insets(0, 0, 0, 0));
        
        // Admin profile card
        HBox profileCard = new HBox(16);
        profileCard.getStyleClass().add("card");
        profileCard.setPadding(new Insets(18));
        
        ImageView avatar;
        java.io.InputStream avatarStream = getClass().getResourceAsStream("/Assets/Images/avatar.png");
        if (avatarStream != null) {
            avatar = new ImageView(new Image(avatarStream));
        } else {
            avatar = new ImageView();
            avatar.setFitWidth(48);
            avatar.setFitHeight(48);
            avatar.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 24;");
        }
        avatar.setFitWidth(48);
        avatar.setFitHeight(48);
        avatar.setPreserveRatio(true);
        
        VBox info = new VBox(4);
        info.setSpacing(2);
        Label nameLabel = new Label("Admin User");
        nameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        Label roleLabel = new Label("System Administrator");
        roleLabel.setStyle("-fx-text-fill: #666;");
        Label lastLoginLabel = new Label("Last login: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")));
        lastLoginLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
        info.getChildren().addAll(nameLabel, roleLabel, lastLoginLabel);
        
        profileCard.getChildren().addAll(avatar, info);
        
        // Notifications
        VBox notifications = new VBox(8);
        notifications.getStyleClass().add("card");
        notifications.setPadding(new Insets(18));
        Label notifTitle = new Label("Recent Notifications");
        notifTitle.getStyleClass().add("label-section");
        
        notificationsList = new ListView<>();
        notificationsList.setPrefHeight(120);
        notificationsList.setItems(FXCollections.observableArrayList());
        updateNotificationsList();
        
        notifications.getChildren().addAll(notifTitle, notificationsList);
        
        // Quick actions
        VBox quickActions = new VBox(8);
        quickActions.getStyleClass().add("card");
        quickActions.setPadding(new Insets(18));
        Label quickTitle = new Label("Quick Actions");
        quickTitle.getStyleClass().add("label-section");
        
        Button addUserBtn = new Button("Add User");
        addUserBtn.getStyleClass().add("button");
        addUserBtn.setOnAction(e -> {
            AddUserDialog dialog = new AddUserDialog();
            dialog.showAndWait().ifPresent(user -> {
                dataService.addUser(user);
                updateNotificationsList();
                refreshDashboard();
            });
        });
        
        Button addModuleBtn = new Button("Add Module");
        addModuleBtn.getStyleClass().add("button");
        addModuleBtn.setOnAction(e -> {
            AddModuleDialog dialog = new AddModuleDialog();
            dialog.showAndWait().ifPresent(module -> {
                dataService.addModule(module);
                updateNotificationsList();
                refreshDashboard();
            });
        });
        
        Button viewAnalyticsBtn = new Button("View Analytics");
        viewAnalyticsBtn.getStyleClass().add("button");
        viewAnalyticsBtn.setOnAction(e -> setMainContent(getAnalyticsPage()));
        
        quickActions.getChildren().addAll(quickTitle, addUserBtn, addModuleBtn, viewAnalyticsBtn);
        
        // Recent activity
        VBox recentActivity = new VBox(8);
        recentActivity.getStyleClass().add("card");
        recentActivity.setPadding(new Insets(18));
        Label recentTitle = new Label("Recent Activity");
        recentTitle.getStyleClass().add("label-section");
        
        recentActivityList = new ListView<>();
        recentActivityList.setPrefHeight(120);
        recentActivityList.setItems(FXCollections.observableArrayList());
        updateRecentActivityList();
        
        recentActivity.getChildren().addAll(recentTitle, recentActivityList);
        
        // Layout
        HBox row = new HBox(18, profileCard, notifications, quickActions, recentActivity);
        box.getChildren().add(row);
        return box;
    }

    private void updateNotificationsList() {
        List<String> notificationStrings = new ArrayList<>();
        List<AdminDataService.Notification> notifications = dataService.getNotifications();
        
        for (int i = 0; i < Math.min(5, notifications.size()); i++) {
            AdminDataService.Notification notif = notifications.get(i);
            String timeAgo = formatTimeAgo(notif.timestamp);
            notificationStrings.add(String.format("[%s] %s", timeAgo, notif.title));
        }
        
        notificationsList.setItems(FXCollections.observableArrayList(notificationStrings));
    }

    private void updateRecentActivityList() {
        List<String> activityStrings = new ArrayList<>();
        List<AdminDataService.LogEntry> logs = dataService.getLogs();
        
        for (int i = 0; i < Math.min(5, logs.size()); i++) {
            AdminDataService.LogEntry log = logs.get(i);
            String timeAgo = formatTimeAgo(log.timestamp);
            activityStrings.add(String.format("[%s] %s", timeAgo, log.message));
        }
        
        recentActivityList.setItems(FXCollections.observableArrayList(activityStrings));
    }

    private String formatTimeAgo(LocalDateTime timestamp) {
        long minutes = java.time.Duration.between(timestamp, LocalDateTime.now()).toMinutes();
        if (minutes < 60) return minutes + "m ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + "h ago";
        long days = hours / 24;
        return days + "d ago";
    }

    private void setMainContent(Pane page) {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(page);
    }

    // Refresh dashboard data in real time
    private void refreshDashboard() {
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(createFunctionalFields(), createCardGrid(), createChartsRow());
        updateNotificationsList();
        updateRecentActivityList();
    }

    public AdminDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.dataService = AdminDataService.getInstance();
        
        getStyleClass().add("root");
        setPadding(new Insets(0));
        
        // Sidebar
        sidebar = createSidebar();
        setLeft(sidebar);
        
        // Header
        header = createHeader();
        setTop(header);
        
        // Main content
        mainContent = new VBox(24);
        mainContent.setPadding(new Insets(32, 32, 32, 32));
        refreshDashboard();
        setCenter(mainContent);
        
        // Start real-time updates
        startRealTimeUpdates();
    }

    private void startRealTimeUpdates() {
        refreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(300), e -> {
                refreshDashboard();
            })
        );
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private VBox createSidebar() {
        VBox box = new VBox(8);
        box.getStyleClass().add("sidebar");
        box.setAlignment(Pos.TOP_CENTER);
        box.setPrefWidth(90);
        
        // Logo with null check
        ImageView logo;
        java.io.InputStream avatarStream = getClass().getResourceAsStream("/Assets/Images/avatar.png");
        if (avatarStream != null) {
            logo = new ImageView(new Image(avatarStream));
        } else {
            logo = new ImageView();
            logo.setFitWidth(48);
            logo.setFitHeight(48);
            logo.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 24;");
        }
        logo.setFitWidth(48);
        logo.setFitHeight(48);
        logo.setPreserveRatio(true);
        box.getChildren().add(logo);
        
        // Sidebar buttons with navigation
        Button dashboardBtn = makeSidebarButton("\uD83D\uDCCB", "Dashboard");
        Button usersBtn = makeSidebarButton("\uD83D\uDC64", "Users");
        Button modulesBtn = makeSidebarButton("\uD83D\uDCDA", "Modules");
        Button analyticsBtn = makeSidebarButton("\uD83D\uDCCA", "Analytics");
        Button logsBtn = makeSidebarButton("\uD83D\uDDD2", "Logs");
        Button notificationsBtn = makeSidebarButton("\uD83D\uDD14", "Notifications");
        Button feedbackBtn = makeSidebarButton("\uD83D\uDCEC", "Feedback");
        Button parentalBtn = makeSidebarButton("\uD83D\uDC6A", "Parental Controls");
        Button settingsBtn = makeSidebarButton("\u2699\uFE0F", "Settings");
        
        dashboardBtn.setOnAction(e -> refreshDashboard());
        usersBtn.setOnAction(e -> setMainContent(getUsersPage()));
        modulesBtn.setOnAction(e -> setMainContent(getModulesPage()));
        analyticsBtn.setOnAction(e -> setMainContent(getAnalyticsPage()));
        logsBtn.setOnAction(e -> setMainContent(getLogsPage()));
        notificationsBtn.setOnAction(e -> setMainContent(getNotificationsPage()));
        feedbackBtn.setOnAction(e -> setMainContent(getFeedbackPage()));
        parentalBtn.setOnAction(e -> setMainContent(getParentalControlsPage()));
        settingsBtn.setOnAction(e -> setMainContent(getSettingsPage()));
        
        box.getChildren().addAll(
            dashboardBtn, usersBtn, modulesBtn, analyticsBtn, logsBtn, notificationsBtn, feedbackBtn, parentalBtn, settingsBtn
        );
        return box;
    }

    private AdminUsersPage getUsersPage() {
        if (usersPage == null) usersPage = new AdminUsersPage();
        return usersPage;
    }
    private AdminModulesPage getModulesPage() {
        if (modulesPage == null) modulesPage = new AdminModulesPage();
        return modulesPage;
    }
    private AdminAnalyticsPage getAnalyticsPage() {
        if (analyticsPage == null) analyticsPage = new AdminAnalyticsPage();
        return analyticsPage;
    }
    private AdminLogsPage getLogsPage() {
        if (logsPage == null) logsPage = new AdminLogsPage();
        return logsPage;
    }
    private AdminSettingsPage getSettingsPage() {
        if (settingsPage == null) settingsPage = new AdminSettingsPage();
        return settingsPage;
    }

    private AdminNotificationsPage getNotificationsPage() {
        if (notificationsPage == null) notificationsPage = new AdminNotificationsPage();
        return notificationsPage;
    }
    private AdminFeedbackPage getFeedbackPage() {
        if (feedbackPage == null) feedbackPage = new AdminFeedbackPage();
        return feedbackPage;
    }
    private AdminParentalControlsPage getParentalControlsPage() {
        if (parentalControlsPage == null) parentalControlsPage = new AdminParentalControlsPage();
        return parentalControlsPage;
    }

    private Button makeSidebarButton(String icon, String text) {
        Button btn = new Button(icon + "  " + text);
        btn.getStyleClass().add("button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    private GridPane createCardGrid() {
        cardGrid = new GridPane();
        cardGrid.getStyleClass().add("card-grid");
        cardGrid.setHgap(24);
        cardGrid.setVgap(24);
        
        // Get real data from AdminDataService
        Map<String, Integer> analytics = dataService.getAnalytics();
        
        totalStudentsLabel = new Label(String.valueOf(analytics.getOrDefault("Total Students", 0)));
        totalTeachersLabel = new Label(String.valueOf(analytics.getOrDefault("Total Teachers", 0)));
        activeUsersLabel = new Label(String.valueOf(dataService.getActiveUsersCount()));
        
        cardGrid.add(createStatCard(totalStudentsLabel, "Students", "stat-card students"), 0, 0);
        cardGrid.add(createStatCard(totalTeachersLabel, "Teachers", "stat-card teachers"), 1, 0);
        cardGrid.add(createStatCard(activeUsersLabel, "Active Users", "stat-card active"), 2, 0);
        
        return cardGrid;
    }

    private VBox createStatCard(Label numberLabel, String label, String styleClass) {
        VBox card = new VBox(8);
        card.getStyleClass().addAll(styleClass.split(" "));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16));
        
        numberLabel.getStyleClass().add("stat-number");
        numberLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        
        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");
        lbl.setStyle("-fx-text-fill: #666;");
        
        card.getChildren().addAll(numberLabel, lbl);
        return card;
    }

    private HBox createChartsRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);
        
        // Activity chart (Management Value)
        activityChart = createActivityChart();
        activityChart.setPrefSize(400, 200);
        
        // User distribution chart
        userDistributionChart = createUserDistributionChart();
        userDistributionChart.setPrefSize(200, 200);
        
        // Subject Task (Bar chart style)
        VBox subjectTask = createSubjectTask();
        
        // Top Students list
        VBox topList = createTopList();
        
        row.getChildren().addAll(activityChart, userDistributionChart, subjectTask, topList);
        return row;
    }

    private LineChart<Number, Number> createActivityChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.getStyleClass().add("chart");
        chart.setLegendVisible(false);
        chart.setTitle("User Activity (Last 7 Days)");
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        
        // Generate realistic activity data
        List<AdminDataService.SessionData> sessionHistory = dataService.getSessionHistory();
        if (!sessionHistory.isEmpty()) {
            for (int i = 0; i < Math.min(7, sessionHistory.size()); i++) {
                AdminDataService.SessionData session = sessionHistory.get(i);
                series.getData().add(new XYChart.Data<>(i + 1, session.durationMinutes));
            }
        } else {
            // Fallback data
            series.getData().add(new XYChart.Data<>(1, 15));
            series.getData().add(new XYChart.Data<>(2, 22));
            series.getData().add(new XYChart.Data<>(3, 18));
            series.getData().add(new XYChart.Data<>(4, 25));
            series.getData().add(new XYChart.Data<>(5, 20));
        }
        
        chart.getData().add(series);
        return chart;
    }

    private PieChart createUserDistributionChart() {
        Map<String, Integer> analytics = dataService.getAnalytics();
        
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Students", analytics.getOrDefault("Total Students", 0)),
            new PieChart.Data("Teachers", analytics.getOrDefault("Total Teachers", 0)),
            new PieChart.Data("Active", dataService.getActiveUsersCount())
        );
        
        PieChart chart = new PieChart(pieData);
        chart.getStyleClass().add("chart");
        chart.setLegendVisible(true);
        chart.setTitle("User Distribution");
        return chart;
    }

    private VBox createSubjectTask() {
        VBox box = new VBox(8);
        box.getStyleClass().add("card");
        box.setPadding(new Insets(16));
        Label label = new Label("Module Completion");
        label.getStyleClass().add("label-section");
        box.getChildren().add(label);
        
        // Get real module data
        List<AdminDataService.Module> modules = dataService.getActiveModules();
        List<AdminDataService.StudentProgress> progress = dataService.getStudentProgress();
        
        for (int i = 0; i < Math.min(4, modules.size()); i++) {
            AdminDataService.Module module = modules.get(i);
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            
            Label moduleLabel = new Label(module.title.substring(0, Math.min(15, module.title.length())) + "...");
            moduleLabel.setPrefWidth(120);
            moduleLabel.setStyle("-fx-font-size: 12;");
            
            // Calculate completion percentage
            long completedCount = progress.stream()
                .filter(p -> p.moduleName.equals(module.title))
                .count();
            double completionRate = modules.size() > 0 ? (double) completedCount / modules.size() : 0.0;
            
            javafx.scene.control.ProgressBar bar = new javafx.scene.control.ProgressBar(completionRate);
            bar.getStyleClass().add("progress-bar");
            bar.setPrefWidth(100);
            
            row.getChildren().addAll(moduleLabel, bar);
            box.getChildren().add(row);
        }
        
        return box;
    }

    private VBox createTopList() {
        VBox box = new VBox(8);
        box.getStyleClass().add("top-list");
        box.setPadding(new Insets(16));
        Label label = new Label("Top Performers");
        label.getStyleClass().add("label-section");
        
        ListView<String> list = new ListView<>();
        list.setPrefHeight(120);
        
        // Get real student progress data
        List<AdminDataService.StudentProgress> progress = dataService.getStudentProgress();
        List<String> topStudents = progress.stream()
            .sorted((p1, p2) -> Integer.compare(p2.score, p1.score))
            .limit(4)
            .map(p -> p.studentName + " (" + p.score + "%)")
            .collect(java.util.stream.Collectors.toList());
        
        if (topStudents.isEmpty()) {
            topStudents.add("No data available");
        }
        
        list.setItems(FXCollections.observableArrayList(topStudents));
        box.getChildren().addAll(label, list);
        return box;
    }

    public static void show(Stage primaryStage) {
        AdminDashboard dashboard = new AdminDashboard(primaryStage);
        Scene scene = new Scene(dashboard, 1366, 768);
        scene.getStylesheets().add(AdminDashboard.class.getResource("/css/adminpanel-theme.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Admin Panel");
        primaryStage.show();
    }
    
    public void cleanup() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
        dataService.shutdown();
    }
} 