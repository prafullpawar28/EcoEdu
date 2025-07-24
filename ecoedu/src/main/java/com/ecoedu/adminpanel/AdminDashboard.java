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
import java.util.List;
import java.util.ArrayList;

public class AdminDashboard extends BorderPane {
    private VBox sidebar;
    private HBox header;
    private GridPane cardGrid;
    private VBox mainContent;
    private Stage primaryStage;
    // Keep references to pages for reuse
    private AdminUsersPage usersPage;
    private AdminModulesPage modulesPage;
    private AdminAnalyticsPage analyticsPage;
    private AdminLogsPage logsPage;
    private AdminSettingsPage settingsPage;
    private AdminNotificationsPage notificationsPage;
    private AdminFeedbackPage feedbackPage;
    private AdminParentalControlsPage parentalControlsPage;

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
        // Only show for admin role (stubbed for now)
        boolean isAdmin = true; // TODO: Replace with real user role check
        switchBtn.setVisible(isAdmin);
        switchBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("button");
        logoutBtn.setOnAction(e -> com.ecoedu.adminpanel.AdminLoginPage.show(primaryStage));
        // Back button
        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("button");
        backBtn.setOnAction(e -> com.ecoedu.adminpanel.AdminLoginPage.show(primaryStage));
        bar.getChildren().addAll(backBtn, title, spacer, switchBtn, logoutBtn, avatar);
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
        VBox info = new VBox(
            new Label("Admin Name: John Doe"),
            new Label("Role: Admin"),
            new Label("Email: admin@ecoedu.com")
        );
        info.setSpacing(2);
        profileCard.getChildren().addAll(avatar, info);
        // Notifications
        VBox notifications = new VBox(8);
        notifications.getStyleClass().add("card");
        notifications.setPadding(new Insets(18));
        Label notifTitle = new Label("Notifications");
        notifTitle.getStyleClass().add("label-section");
        ListView<String> notifList = new ListView<>();
        notifList.setPrefHeight(80);
        notifList.setItems(javafx.collections.FXCollections.observableArrayList(
            "New user registered", "Quiz submitted for review"
        ));
        notifications.getChildren().addAll(notifTitle, notifList);
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
                AdminDataService.getInstance().addUser(user);
                notifList.getItems().add(0, "User added: " + user.name);
                refreshDashboard();
            });
        });
        Button addModuleBtn = new Button("Add Module");
        addModuleBtn.getStyleClass().add("button");
        addModuleBtn.setOnAction(e -> {
            AddModuleDialog dialog = new AddModuleDialog();
            dialog.showAndWait().ifPresent(module -> {
                AdminDataService.getInstance().addModule(module);
                notifList.getItems().add(0, "Module added: " + module.title);
                refreshDashboard();
            });
        });
        quickActions.getChildren().addAll(quickTitle, addUserBtn, addModuleBtn);
        // Recent activity
        VBox recentActivity = new VBox(8);
        recentActivity.getStyleClass().add("card");
        recentActivity.setPadding(new Insets(18));
        Label recentTitle = new Label("Recent Activity");
        recentTitle.getStyleClass().add("label-section");
        ListView<String> recentList = new ListView<>();
        recentList.setPrefHeight(80);
        recentList.setItems(javafx.collections.FXCollections.observableArrayList(
            "Module updated: Recycling", "Badge awarded: Eco Star"
        ));
        recentActivity.getChildren().addAll(recentTitle, recentList);
        // Layout
        HBox row = new HBox(18, profileCard, notifications, quickActions, recentActivity);
        box.getChildren().add(row);
        return box;
    }

    private void setMainContent(Pane page) {
        mainContent.getChildren().clear();
        mainContent.getChildren().add(page);
    }

    // Refresh dashboard data in real time
    private void refreshDashboard() {
        // Stat cards and charts now use AdminDataService for real data
        mainContent.getChildren().clear();
        mainContent.getChildren().addAll(createFunctionalFields(), createCardGrid(), createChartsRow());
    }

    public AdminDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
        // Stat cards
        cardGrid.add(createStatCard("1256", "Students", "stat-card students"), 0, 0);
        cardGrid.add(createStatCard("102", "Teachers", "stat-card teachers"), 1, 0);
        cardGrid.add(createStatCard("102", "Private Teachers", "stat-card private"), 2, 0);
        return cardGrid;
    }

    private VBox createStatCard(String number, String label, String styleClass) {
        VBox card = new VBox(8);
        card.getStyleClass().addAll(styleClass.split(" "));
        card.setAlignment(Pos.CENTER_LEFT);
        Label num = new Label(number);
        num.getStyleClass().add("stat-number");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");
        card.getChildren().addAll(num, lbl);
        return card;
    }

    private HBox createChartsRow() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);
        // Line chart (Management Value)
        LineChart<Number, Number> lineChart = createLineChart();
        lineChart.setPrefSize(400, 200);
        // Pie chart (Breakdown)
        PieChart pieChart = createPieChart();
        pieChart.setPrefSize(200, 200);
        // Subject Task (Bar chart style)
        VBox subjectTask = createSubjectTask();
        // Top Students list
        VBox topList = createTopList();
        row.getChildren().addAll(lineChart, pieChart, subjectTask, topList);
        return row;
    }

    private LineChart<Number, Number> createLineChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.getStyleClass().add("chart");
        chart.setLegendVisible(false);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 10));
        series.getData().add(new XYChart.Data<>(2, 15));
        series.getData().add(new XYChart.Data<>(3, 12));
        series.getData().add(new XYChart.Data<>(4, 18));
        series.getData().add(new XYChart.Data<>(5, 14));
        chart.getData().add(series);
        return chart;
    }

    private PieChart createPieChart() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Students", 60),
            new PieChart.Data("Teachers", 25),
            new PieChart.Data("Private Teachers", 15)
        );
        PieChart chart = new PieChart(pieData);
        chart.getStyleClass().add("chart");
        chart.setLegendVisible(false);
        return chart;
    }

    private VBox createSubjectTask() {
        VBox box = new VBox(8);
        box.getStyleClass().add("card");
        Label label = new Label("Subject Task");
        label.getStyleClass().add("label-section");
        box.getChildren().add(label);
        // Simulate horizontal bars
        for (int i = 1; i <= 4; i++) {
            HBox row = new HBox(8);
            Label subject = new Label("Week " + i);
            subject.setPrefWidth(60);
            javafx.scene.control.ProgressBar bar = new javafx.scene.control.ProgressBar(i * 0.2 + 0.2);
            bar.getStyleClass().add("progress-bar");
            bar.setPrefWidth(120);
            row.getChildren().addAll(subject, bar);
            box.getChildren().add(row);
        }
        return box;
    }

    private VBox createTopList() {
        VBox box = new VBox(8);
        box.getStyleClass().add("top-list");
        Label label = new Label("Top Students");
        label.getStyleClass().add("label-section");
        ListView<String> list = new ListView<>();
        list.setPrefHeight(120);
        list.setItems(FXCollections.observableArrayList(
            "Lucas Jones", "Emma Smith", "Olivia Brown", "Noah Lee"
        ));
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
} 