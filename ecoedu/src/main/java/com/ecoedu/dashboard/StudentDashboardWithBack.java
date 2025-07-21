package com.ecoedu.dashboard;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class StudentDashboardWithBack extends StudentDashboard {
    public StudentDashboardWithBack(Stage primaryStage) {
        super(primaryStage);
        HBox backBox = new HBox();
        backBox.setAlignment(Pos.TOP_LEFT);
        backBox.setPadding(new Insets(10, 0, 0, 10));
        Button backBtn = new Button("← Back to Admin Dashboard");
        backBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 8 16; -fx-cursor: hand;");
        backBtn.setOnAction(e -> AdminDashboard.show(primaryStage));
        if (!getChildren().isEmpty() && getChildren().get(0) instanceof javafx.scene.layout.HBox) {
            getChildren().add(0, backBox);
        } else {
            getChildren().add(0, backBox);
        }
        backBox.getChildren().add(backBtn);
    }
} 