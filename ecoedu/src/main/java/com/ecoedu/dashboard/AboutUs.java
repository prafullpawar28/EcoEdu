package com.ecoedu.dashboard;

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

public class AboutUs extends VBox {
    public AboutUs(Stage primaryStage) {
        setAlignment(Pos.TOP_CENTER);
        setSpacing(32);
        setPadding(new Insets(48, 48, 48, 48));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa, #fffde7);");

        // Title
        Label title = new Label("🌱 About EcoEdu");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#0288d1"));
        title.setPadding(new Insets(0, 0, 12, 0));

        // Project summary
        Label summary = new Label(
            "EcoEdu is an interactive, gamified learning platform designed to inspire eco-friendly habits in students. " +
            "Through daily challenges, quizzes, minigames, and a dynamic dashboard, EcoEdu empowers young learners to make a positive impact on the environment.\n\n" +
            "Key Features:\n" +
            "• Daily eco challenges and streaks\n" +
            "• Real-time leaderboard and badges\n" +
            "• Fun minigames and quizzes\n" +
            "• Customizable avatars and profiles\n" +
            "• Admin and parental controls\n\n" +
            "Our mission: To make environmental education engaging, practical, and fun for the next generation!"
        );
        summary.setFont(Font.font("Quicksand", 18));
        summary.setTextFill(Color.web("#22223b"));
        summary.setWrapText(true);
        summary.setStyle("-fx-background-color: #e0f7fa; -fx-background-radius: 18; -fx-padding: 24; -fx-effect: dropshadow(gaussian, #b2ebf2, 8, 0.10, 0, 2);");

        // Team info
        Label team = new Label("Developed by the Team Bytecode\n Under the guidance of Mr.Shashikant Bagal Sir\n(Founder of Core2Web)\nContact: www.ecoedu.com");
        team.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        team.setTextFill(Color.web("#388e3c"));
        team.setPadding(new Insets(12, 0, 0, 0));

        // Logo or icon (optional)
        ImageView logo = null;
        try {
            logo = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/ecoedu-logo.png")));
            logo.setFitWidth(120);
            logo.setFitHeight(120);
            logo.setPreserveRatio(true);
            logo.setSmooth(true);
        } catch (Exception e) {
            logo = null;
        }
        StackPane logoPane = null;
        if (logo != null && logo.getImage() != null) {
            logoPane = new StackPane(logo);
            logoPane.setStyle("-fx-background-radius: 60; -fx-background-color: #fffde7; -fx-padding: 18; -fx-effect: dropshadow(gaussian, #e0f7fa, 8, 0.10, 0, 2);");
        }

        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 10 32; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));

        // Add children, skipping logo if not available
        if (logoPane != null) {
            getChildren().addAll(title, logoPane, summary, team, backBtn);
        } else {
            getChildren().addAll(title, summary, team, backBtn);
        }
    }

    public static void show(Stage primaryStage) {
        AboutUs page = new AboutUs(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("About EcoEdu");
        primaryStage.show();
    }
} 