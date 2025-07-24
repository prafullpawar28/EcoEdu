package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class DashboardView extends BorderPane {
    private final StatCard studentCard;
    private final StatCard teacherCard;
    private final StatCard privateTeacherCard;
    private final VBox notificationsBox;
    private final VBox topStudentsBox;
    private final LineChart<Number, Number> lineChart;
    private final PieChart pieChart;
    private final VBox quickActionsBox;
    private final DashboardController controller;

    public DashboardView(Stage primaryStage, DashboardService service) {
        // Stat cards
        studentCard = new StatCard("/Assets/Images/face1.png", "Students", 0, Color.web("#ffe0c6"));
        teacherCard = new StatCard("/Assets/Images/face2.png", "Teachers", 0, Color.web("#e0e7ff"));
        privateTeacherCard = new StatCard("/Assets/Images/face3.png", "Private Teachers", 0, Color.web("#c6f7ff"));
        HBox statRow = new HBox(24, studentCard, teacherCard, privateTeacherCard);
        statRow.setAlignment(Pos.CENTER);
        statRow.setPadding(new Insets(24, 0, 24, 0));
        statRow.setHgrow(studentCard, Priority.ALWAYS);
        statRow.setHgrow(teacherCard, Priority.ALWAYS);
        statRow.setHgrow(privateTeacherCard, Priority.ALWAYS);

        // Notifications
        notificationsBox = new VBox(12);
        notificationsBox.setAlignment(Pos.TOP_LEFT);
        Label notifTitle = new Label("Notifications");
        notifTitle.getStyleClass().add("dashboard-section-title");
        VBox notifSection = new VBox(8, notifTitle, notificationsBox);
        notifSection.setPadding(new Insets(0, 0, 0, 0));
        notifSection.setPrefWidth(320);

        // Top Students
        topStudentsBox = new VBox(12);
        topStudentsBox.setAlignment(Pos.TOP_LEFT);
        Label topTitle = new Label("Top Students");
        topTitle.getStyleClass().add("dashboard-section-title");
        VBox topSection = new VBox(8, topTitle, topStudentsBox);
        topSection.setPadding(new Insets(0, 0, 0, 0));
        topSection.setPrefWidth(320);

        // Charts
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setPrefSize(400, 200);
        pieChart = new PieChart();
        pieChart.setLegendVisible(false);
        pieChart.setPrefSize(200, 200);
        HBox chartsRow = new HBox(24, lineChart, pieChart);
        chartsRow.setAlignment(Pos.CENTER_LEFT);
        chartsRow.setPadding(new Insets(24, 0, 24, 0));

        // Quick Actions
        quickActionsBox = new VBox(16);
        quickActionsBox.setAlignment(Pos.TOP_LEFT);
        quickActionsBox.setPadding(new Insets(16));
        quickActionsBox.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-effect: dropshadow(gaussian, #e0e0e0, 8, 0.10, 0, 2);");
        Label quickTitle = new Label("Quick Actions");
        quickTitle.getStyleClass().add("dashboard-section-title");
        Button addUserBtn = new Button("Add User");
        Button addModuleBtn = new Button("Add Module");
        addUserBtn.setOnAction(e -> showAddUserDialog(primaryStage));
        addModuleBtn.setOnAction(e -> showAddModuleDialog(primaryStage));
        quickActionsBox.getChildren().addAll(quickTitle, addUserBtn, addModuleBtn);
        quickActionsBox.setPrefWidth(220);

        // Main layout
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(32);
        mainGrid.setVgap(32);
        mainGrid.add(statRow, 0, 0, 2, 1);
        mainGrid.add(chartsRow, 0, 1);
        mainGrid.add(notifSection, 1, 1);
        mainGrid.add(topSection, 0, 2);
        mainGrid.add(quickActionsBox, 1, 2);
        mainGrid.setPadding(new Insets(32));
        setCenter(mainGrid);
        // Controller
        controller = new DashboardController(service, this);
        // Apply CSS
        Scene scene = new Scene(this, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/css/dashboard.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Admin Dashboard");
        primaryStage.show();
    }

    // Methods for controller to update UI
    public StatCard getStudentCard() { return studentCard; }
    public StatCard getTeacherCard() { return teacherCard; }
    public StatCard getPrivateTeacherCard() { return privateTeacherCard; }
    public LineChart<Number, Number> getLineChart() { return lineChart; }
    public PieChart getPieChart() { return pieChart; }
    public void updateNotifications(List<String> notifications) {
        notificationsBox.getChildren().clear();
        for (String notif : notifications) {
            notificationsBox.getChildren().add(new NotificationCard("/Assets/Images/face1.png", notif, java.time.LocalDateTime.now(), Color.web("#ffe0c6")));
        }
    }
    public void updateTopStudents(List<DashboardService.TopStudent> students) {
        topStudentsBox.getChildren().clear();
        for (DashboardService.TopStudent s : students) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream(s.avatarPath)));
            avatar.setFitWidth(32);
            avatar.setFitHeight(32);
            Label name = new Label(s.name);
            name.setFont(javafx.scene.text.Font.font("Quicksand", 15));
            row.getChildren().addAll(avatar, name);
            topStudentsBox.getChildren().add(row);
        }
    }
    private void showAddUserDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Add User");
        VBox root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        TextField nameField = new TextField();
        nameField.setPromptText("User Name");
        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            controller.addUser(nameField.getText());
            dialog.close();
        });
        root.getChildren().addAll(new Label("Enter user name:"), nameField, addBtn);
        Scene scene = new Scene(root, 320, 180);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    private void showAddModuleDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Add Module");
        VBox root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        TextField titleField = new TextField();
        titleField.setPromptText("Module Title");
        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            controller.addModule(titleField.getText());
            dialog.close();
        });
        root.getChildren().addAll(new Label("Enter module title:"), titleField, addBtn);
        Scene scene = new Scene(root, 320, 180);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 