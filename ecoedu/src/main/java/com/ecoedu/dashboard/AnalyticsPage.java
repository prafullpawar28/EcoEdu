package com.ecoedu.dashboard;

import com.ecoedu.adminpanel.AdminDashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AnalyticsPage extends VBox {
    private Stage primaryStage;

    public AnalyticsPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(32);
        setPadding(new Insets(48, 48, 48, 48));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #ede7f6, #e3f2fd);");

        Label title = new Label("📊 Analytics");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#6a1b9a"));
        getChildren().add(title);

        // Example stats (replace with real charts/stats)
        Label stat1 = new Label("Active Students: 120");
        stat1.setFont(Font.font("Comic Sans MS", 20));
        stat1.setTextFill(Color.web("#0288d1"));
        Label stat2 = new Label("Quizzes Completed: 340");
        stat2.setFont(Font.font("Comic Sans MS", 20));
        stat2.setTextFill(Color.web("#43a047"));
        Label stat3 = new Label("Average Score: 82%");
        stat3.setFont(Font.font("Comic Sans MS", 20));
        stat3.setTextFill(Color.web("#ffb300"));
        getChildren().addAll(stat1, stat2, stat3);

        Button backBtn = new Button("Back to Admin Dashboard");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> AdminDashboard.show(primaryStage));
        getChildren().add(backBtn);
    }

    public static void show(Stage primaryStage) {
        AnalyticsPage page = new AnalyticsPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Analytics");
        primaryStage.show();
    }
} 