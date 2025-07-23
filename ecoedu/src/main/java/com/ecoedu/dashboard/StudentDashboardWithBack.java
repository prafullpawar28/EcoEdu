package com.ecoedu.dashboard;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import com.ecoedu.adminpanel.AdminPanelMain;

public class StudentDashboardWithBack extends StudentDashboard {
    public StudentDashboardWithBack(Stage primaryStage) {
        super(primaryStage);
        // Create a container for back button and banner
        VBox topBox = new VBox();
        topBox.setSpacing(8);
        // Back button
        HBox backBox = new HBox();
        backBox.setAlignment(Pos.TOP_LEFT);
        backBox.setPadding(new Insets(10, 0, 0, 10));
        Button backBtn = new Button("\u2190 Back to Admin Panel");
        backBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 8 16; -fx-cursor: hand;");
        backBtn.setOnAction(e -> {
            try {
                AdminPanelMain.main(new String[]{});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        backBox.getChildren().add(backBtn);
        // Admin banner
        Label adminBanner = new Label("Welcome, Administration!");
        adminBanner.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        adminBanner.setTextFill(Color.web("#fffde7"));
        adminBanner.setStyle("-fx-background-color: linear-gradient(to right, #43a047, #6a1b9a); -fx-background-radius: 18; -fx-padding: 16 40; -fx-effect: dropshadow(gaussian, #388e3c, 12, 0.2, 0, 4); -fx-font-weight: bold;");
        adminBanner.setAlignment(Pos.CENTER);
        adminBanner.setMaxWidth(Double.MAX_VALUE);
        topBox.getChildren().addAll(backBox, adminBanner);
        getChildren().add(0, topBox);
    }
} 