package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardView extends VBox {
    public DashboardView() {
        setSpacing(24);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");

        Label title = new Label("EcoEdu Admin Dashboard");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#388E3C"));
        getChildren().add(title);

        // Stats
        HBox statsBox = new HBox(32);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(12));
        statsBox.getChildren().addAll(
            statCard("Total Users", "1234", "#43a047"),
            statCard("Total Modules", "24", "#0288d1"),
            statCard("Total Games", "8", "#FFD3B6"),
            statCard("Total Badges", "15", "#FFAAA5")
        );
        getChildren().add(statsBox);

        // Charts
        HBox chartsBox = new HBox(32);
        chartsBox.setAlignment(Pos.CENTER);
        chartsBox.setPadding(new Insets(12));
        chartsBox.getChildren().addAll(createBarChart(), createPieChart());
        getChildren().add(chartsBox);

        // System Status
        getChildren().add(new SystemStatusPanel());
    }

    private VBox statCard(String label, String value, String color) {
        VBox box = new VBox(6);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, " + color + ", 8, 0.2, 0, 2);");
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(color));
        Label labelLabel = new Label(label);
        labelLabel.setFont(Font.font("Quicksand", 16));
        labelLabel.setTextFill(Color.web("#388E3C"));
        box.setPadding(new Insets(18, 32, 18, 32));
        box.getChildren().addAll(valueLabel, labelLabel);
        return box;
    }

    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Active Users");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("User Activity (Weekly)");
        barChart.setLegendVisible(false);
        // loadData() stub
        // Fetch from Firebase here
        return barChart;
    }

    private PieChart createPieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Content Usage");
        // loadData() stub
        // Fetch from Firebase here
        return pieChart;
    }

    // SystemStatusPanel as inner class
    private static class SystemStatusPanel extends VBox {
        public SystemStatusPanel() {
            setSpacing(10);
            setPadding(new Insets(18));
            setAlignment(Pos.CENTER);
            setStyle("-fx-background-color: #fff; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #A8E6CF, 8, 0.2, 0, 2);");
            Label title = new Label("System Status");
            title.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
            title.setTextFill(Color.web("#388E3C"));
            HBox statusBox = new HBox(32);
            statusBox.setAlignment(Pos.CENTER);
            statusBox.getChildren().addAll(
                statusItem("Server", true),
                statusItem("Database", true),
                statusItem("Security", true)
            );
            getChildren().addAll(title, statusBox);
        }
        private HBox statusItem(String label, boolean ok) {
            HBox box = new HBox(8);
            box.setAlignment(Pos.CENTER);
            Label icon = new Label(ok ? "\u2714" : "\u26A0");
            icon.setTextFill(ok ? Color.web("#43a047") : Color.web("#FF5252"));
            icon.setFont(Font.font(18));
            Label labelLabel = new Label(label);
            labelLabel.setFont(Font.font("Quicksand", 15));
            box.getChildren().addAll(icon, labelLabel);
            ProgressBar pb = new ProgressBar(ok ? 1.0 : 0.3);
            pb.setPrefWidth(80);
            pb.setStyle(ok ? "-fx-accent: #43a047;" : "-fx-accent: #FF5252;");
            box.getChildren().add(pb);
            return box;
        }
    }
} 