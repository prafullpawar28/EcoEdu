package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Map;

public class AdminAnalyticsPage extends VBox {
    public AdminAnalyticsPage() {
        setSpacing(24);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("📊 Analytics");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#4fc3f7"));

        Map<String, Integer> analytics = AdminDataService.getInstance().getAnalytics();
        PieChart chart = new PieChart();
        chart.setTitle("User Activity");
        chart.setLabelsVisible(true);
        chart.setLegendVisible(true);
        if (analytics.isEmpty()) {
            chart.getData().add(new PieChart.Data("No Data", 1));
        } else {
            for (Map.Entry<String, Integer> entry : analytics.entrySet()) {
                chart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
        getChildren().addAll(title, chart);
    }
} 