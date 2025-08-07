package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;
import java.util.List;

public class AdminAnalyticsPage extends VBox {
    private final AdminDataService dataService;

    public AdminAnalyticsPage() {
        this.dataService = AdminDataService.getInstance();
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("📊 Analytics Dashboard");
        title.getStyleClass().add("label-section");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        // Key metrics cards
        HBox metricsRow = createMetricsCards();
        
        // Charts row
        HBox chartsRow = createChartsRow();
        
        // Detailed statistics
        VBox detailedStats = createDetailedStatistics();

        getChildren().addAll(title, metricsRow, chartsRow, detailedStats);
    }

    private HBox createMetricsCards() {
        HBox metricsRow = new HBox(24);
        metricsRow.setAlignment(Pos.CENTER);
        
        // Total Users
        VBox totalUsersCard = createMetricCard(
            String.valueOf(dataService.getUsers().size()),
            "Total Users",
            "#1976d2",
            "👥"
        );
        
        // Active Users
        VBox activeUsersCard = createMetricCard(
            String.valueOf(dataService.getActiveUsersCount()),
            "Active Users",
            "#2e7d32",
            "🟢"
        );
        
        // Total Modules
        VBox totalModulesCard = createMetricCard(
            String.valueOf(dataService.getModules().size()),
            "Total Modules",
            "#f57c00",
            "📚"
        );
        
        // Average Rating
        VBox avgRatingCard = createMetricCard(
            String.format("%.1f", dataService.getAverageRating()),
            "Avg Rating",
            "#388e3c",
            "⭐"
        );
        
        // Total Sessions
        VBox totalSessionsCard = createMetricCard(
            String.valueOf(dataService.getTotalSessions()),
            "Total Sessions",
            "#7b1fa2",
            "🔄"
        );
        
        // Avg Session Time
        VBox avgSessionCard = createMetricCard(
            String.format("%.1f min", dataService.getAverageSessionTime()),
            "Avg Session",
            "#d32f2f",
            "⏱️"
        );
        
        metricsRow.getChildren().addAll(
            totalUsersCard, activeUsersCard, totalModulesCard, 
            avgRatingCard, totalSessionsCard, avgSessionCard
        );
        
        return metricsRow;
    }

    private VBox createMetricCard(String value, String label, String color, String icon) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(150);
        card.setPrefHeight(100);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", 24));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        
        Label labelText = new Label(label);
        labelText.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");
        
        card.getChildren().addAll(iconLabel, valueLabel, labelText);
        return card;
    }

    private HBox createChartsRow() {
        HBox chartsRow = new HBox(24);
        chartsRow.setAlignment(Pos.TOP_CENTER);
        
        // User Distribution Pie Chart
        VBox userDistributionBox = new VBox(16);
        userDistributionBox.getStyleClass().add("card");
        userDistributionBox.setPadding(new Insets(20));
        userDistributionBox.setPrefWidth(400);
        
        Label userDistTitle = new Label("User Distribution");
        userDistTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        userDistTitle.setStyle("-fx-text-fill: #1976d2;");
        
        PieChart userDistributionChart = createUserDistributionChart();
        userDistributionChart.setPrefSize(350, 250);
        
        userDistributionBox.getChildren().addAll(userDistTitle, userDistributionChart);
        
        // Module Completion Bar Chart
        VBox moduleCompletionBox = new VBox(16);
        moduleCompletionBox.getStyleClass().add("card");
        moduleCompletionBox.setPadding(new Insets(20));
        moduleCompletionBox.setPrefWidth(400);
        
        Label moduleCompTitle = new Label("Module Completion Rates");
        moduleCompTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        moduleCompTitle.setStyle("-fx-text-fill: #1976d2;");
        
        BarChart<String, Number> moduleCompletionChart = createModuleCompletionChart();
        moduleCompletionChart.setPrefSize(350, 250);
        
        moduleCompletionBox.getChildren().addAll(moduleCompTitle, moduleCompletionChart);
        
        // Activity Timeline
        VBox activityTimelineBox = new VBox(16);
        activityTimelineBox.getStyleClass().add("card");
        activityTimelineBox.setPadding(new Insets(20));
        activityTimelineBox.setPrefWidth(400);
        
        Label activityTitle = new Label("User Activity Timeline");
        activityTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        activityTitle.setStyle("-fx-text-fill: #1976d2;");
        
        LineChart<Number, Number> activityChart = createActivityTimelineChart();
        activityChart.setPrefSize(350, 250);
        
        activityTimelineBox.getChildren().addAll(activityTitle, activityChart);
        
        chartsRow.getChildren().addAll(userDistributionBox, moduleCompletionBox, activityTimelineBox);
        return chartsRow;
    }

    private PieChart createUserDistributionChart() {
        Map<String, Integer> analytics = dataService.getAnalytics();
        
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Students", analytics.getOrDefault("Total Students", 0)),
            new PieChart.Data("Teachers", analytics.getOrDefault("Total Teachers", 0)),
            new PieChart.Data("Active Users", dataService.getActiveUsersCount())
        );
        
        PieChart chart = new PieChart(pieData);
        chart.setLegendVisible(true);
        chart.setLabelsVisible(true);
        
        // Customize colors
        pieData.get(0).getNode().setStyle("-fx-pie-color: #388e3c;");
        pieData.get(1).getNode().setStyle("-fx-pie-color: #f57c00;");
        pieData.get(2).getNode().setStyle("-fx-pie-color: #1976d2;");
        
        return chart;
    }

    private BarChart<String, Number> createModuleCompletionChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Completion Rate (%)");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Completion Rate");
        
        List<AdminDataService.Module> modules = dataService.getActiveModules();
        List<AdminDataService.StudentProgress> progress = dataService.getStudentProgress();
        
        for (AdminDataService.Module module : modules) {
            long completedCount = progress.stream()
                .filter(p -> p.moduleName.equals(module.title))
                .count();
            double completionRate = modules.size() > 0 ? (double) completedCount / modules.size() * 100 : 0.0;
            
            String shortTitle = module.title.length() > 15 ? 
                module.title.substring(0, 15) + "..." : module.title;
            
            series.getData().add(new XYChart.Data<>(shortTitle, completionRate));
        }
        
        chart.getData().add(series);
        return chart;
    }

    private LineChart<Number, Number> createActivityTimelineChart() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Session Duration (min)");
        
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Activity");
        
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

    private VBox createDetailedStatistics() {
        VBox detailedStats = new VBox(16);
        detailedStats.getStyleClass().add("card");
        detailedStats.setPadding(new Insets(20));
        
        Label title = new Label("📈 Detailed Statistics");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #1976d2;");
        
        // User statistics
        VBox userStats = createUserStatistics();
        
        // Module statistics
        VBox moduleStats = createModuleStatistics();
        
        // Performance statistics
        VBox performanceStats = createPerformanceStatistics();
        
        HBox statsRow = new HBox(32);
        statsRow.setAlignment(Pos.TOP_CENTER);
        statsRow.getChildren().addAll(userStats, moduleStats, performanceStats);
        
        detailedStats.getChildren().addAll(title, statsRow);
        return detailedStats;
    }

    private VBox createUserStatistics() {
        VBox userStats = new VBox(12);
        userStats.setPrefWidth(250);
        
        Label title = new Label("👥 User Statistics");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: #388e3c;");
        
        List<AdminDataService.User> users = dataService.getUsers();
        long studentCount = users.stream().filter(u -> "Student".equals(u.role)).count();
        long teacherCount = users.stream().filter(u -> "Teacher".equals(u.role)).count();
        long adminCount = users.stream().filter(u -> "Admin".equals(u.role)).count();
        long activeCount = users.stream().filter(u -> "Active".equals(u.status)).count();
        
        userStats.getChildren().addAll(
            title,
            createStatRow("Total Users", String.valueOf(users.size())),
            createStatRow("Students", String.valueOf(studentCount)),
            createStatRow("Teachers", String.valueOf(teacherCount)),
            createStatRow("Admins", String.valueOf(adminCount)),
            createStatRow("Active Users", String.valueOf(activeCount)),
            createStatRow("Inactive Users", String.valueOf(users.size() - activeCount))
        );
        
        return userStats;
    }

    private VBox createModuleStatistics() {
        VBox moduleStats = new VBox(12);
        moduleStats.setPrefWidth(250);
        
        Label title = new Label("📚 Module Statistics");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: #f57c00;");
        
        List<AdminDataService.Module> modules = dataService.getModules();
        long activeModules = modules.stream().filter(m -> "Active".equals(m.status)).count();
        long beginnerModules = modules.stream().filter(m -> "Beginner".equals(m.difficulty)).count();
        long intermediateModules = modules.stream().filter(m -> "Intermediate".equals(m.difficulty)).count();
        long advancedModules = modules.stream().filter(m -> "Advanced".equals(m.difficulty)).count();
        
        double avgDuration = modules.stream()
            .mapToInt(m -> m.durationMinutes)
            .average()
            .orElse(0.0);
        
        moduleStats.getChildren().addAll(
            title,
            createStatRow("Total Modules", String.valueOf(modules.size())),
            createStatRow("Active Modules", String.valueOf(activeModules)),
            createStatRow("Beginner Level", String.valueOf(beginnerModules)),
            createStatRow("Intermediate Level", String.valueOf(intermediateModules)),
            createStatRow("Advanced Level", String.valueOf(advancedModules)),
            createStatRow("Avg Duration", String.format("%.1f min", avgDuration))
        );
        
        return moduleStats;
    }

    private VBox createPerformanceStatistics() {
        VBox performanceStats = new VBox(12);
        performanceStats.setPrefWidth(250);
        
        Label title = new Label("📊 Performance Statistics");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
        title.setStyle("-fx-text-fill: #7b1fa2;");
        
        List<AdminDataService.StudentProgress> progress = dataService.getStudentProgress();
        double avgScore = progress.stream()
            .mapToInt(p -> p.score)
            .average()
            .orElse(0.0);
        
        double avgRating = dataService.getAverageRating();
        int totalSessions = dataService.getTotalSessions();
        double avgSessionTime = dataService.getAverageSessionTime();
        
        performanceStats.getChildren().addAll(
            title,
            createStatRow("Total Sessions", String.valueOf(totalSessions)),
            createStatRow("Avg Session Time", String.format("%.1f min", avgSessionTime)),
            createStatRow("Avg Score", String.format("%.1f%%", avgScore)),
            createStatRow("Avg Rating", String.format("%.1f/5", avgRating)),
            createStatRow("Active Users", String.valueOf(dataService.getActiveUsersCount())),
            createStatRow("Completed Modules", String.valueOf(progress.size()))
        );
        
        return performanceStats;
    }

    private HBox createStatRow(String label, String value) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label labelText = new Label(label + ":");
        labelText.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");
        labelText.setPrefWidth(120);
        
        Label valueText = new Label(value);
        valueText.setStyle("-fx-text-fill: #333; -fx-font-weight: bold; -fx-font-size: 12;");
        
        row.getChildren().addAll(labelText, valueText);
        return row;
    }
} 