package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.Map;

public class AdminAnalyticsPage extends VBox {
    public AdminAnalyticsPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("📊 Analytics");
        title.getStyleClass().add("label-section");
        Map<String, Integer> analytics = AdminDataService.getInstance().getAnalytics();
        PieChart chart = new PieChart();
        chart.getStyleClass().add("chart");
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
        card.getChildren().addAll(title, chart);
        getChildren().add(card);
    }
} 