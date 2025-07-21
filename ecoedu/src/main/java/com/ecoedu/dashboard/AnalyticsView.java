package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AnalyticsView extends VBox {
    public AnalyticsView() {
        setSpacing(18);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");
        Label title = new Label("Analytics");
        title.getStyleClass().add("eco-title");
        // Filters
        HBox filterBox = new HBox(12);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        ComboBox<String> userTypeBox = new ComboBox<>();
        userTypeBox.getItems().addAll("All", "Student", "Teacher", "Parent");
        userTypeBox.setValue("All");
        DatePicker fromDate = new DatePicker();
        DatePicker toDate = new DatePicker();
        filterBox.getChildren().addAll(new Label("User Type:"), userTypeBox, new Label("From:"), fromDate, new Label("To:"), toDate);
        // Charts
        HBox chartsBox = new HBox(32);
        chartsBox.setAlignment(Pos.CENTER);
        // LineChart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Engagement");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Weekly User Engagement");
        // PieChart
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Most Used Modules");
        // loadData() stubs
        // Fetch from Firebase here
        chartsBox.getChildren().addAll(lineChart, pieChart);
        // Export button
        Button exportBtn = new Button("Export Report");
        exportBtn.getStyleClass().add("eco-btn");
        exportBtn.setOnAction(e -> {/* TODO: Export to CSV/PDF */});
        getChildren().addAll(title, filterBox, chartsBox, exportBtn);
    }
} 