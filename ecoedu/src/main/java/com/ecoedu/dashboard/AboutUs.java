package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
        setSpacing(24);
        setPadding(new Insets(32));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa, #fffde7);");

        // Title
        Label title = new Label("🌱 About EcoEdu");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#0288d1"));

        // Summary
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

        // Logo
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
        if (logo != null) {
            logoPane = new StackPane(logo);
            logoPane.setStyle("-fx-background-radius: 60; -fx-background-color: #fffde7; -fx-padding: 18; -fx-effect: dropshadow(gaussian, #e0f7fa, 8, 0.10, 0, 2);");
        }

        // Mentors Section
        VBox mentorSection = new VBox(24);
        mentorSection.setPadding(new Insets(24));
        mentorSection.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, #cfd8dc, 6, 0.2, 0, 2);");

        Label mentorTitle = new Label("👨‍🏫 Mentors & Guides");
        mentorTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 28));
        mentorTitle.setTextFill(Color.web("#00796b"));

        mentorSection.getChildren().add(mentorTitle);

        // Shashi Sir with Photo and description (ADDED photo)
        HBox shashiBox = new HBox(16);
        shashiBox.setAlignment(Pos.CENTER_LEFT);

        ImageView shashiImage = null;
        try {
            shashiImage = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/shashisir.jpg")));
            shashiImage.setFitWidth(120);
            shashiImage.setFitHeight(120);
            shashiImage.setPreserveRatio(true);
            shashiImage.setSmooth(true);
        } catch (Exception e) {
            // fallback: no image
        }

        VBox shashiInfo = new VBox(6);
        Label shashiName = new Label("Mr. Shashikant Bagal (Founder of Core2Web)");
        shashiName.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        shashiName.setTextFill(Color.web("#388e3c"));

        Label shashiDesc = new Label(
            "Shashikant Bagal Sir is a visionary educator and the founder of Core2Web. With years of experience in training and mentoring aspiring developers, " +
            "he has empowered countless students to build strong technical foundations. His guidance throughout the EcoEdu project played a crucial role in shaping " +
            "its educational and interactive approach. His passion for teaching and dedication to student success is truly inspiring."
        );
        shashiDesc.setFont(Font.font("Quicksand", 16));
        shashiDesc.setWrapText(true);

        shashiInfo.getChildren().addAll(shashiName, shashiDesc);

        if (shashiImage != null) {
            shashiBox.getChildren().addAll(shashiImage, shashiInfo);
        } else {
            shashiBox.getChildren().add(shashiInfo);
        }

        mentorSection.getChildren().add(shashiBox);

        // Other Mentors
        mentorSection.getChildren().addAll(
            createMentorCard("Mr. Sachin Sir",
                "Sachin Sir is known for his expertise in Android and front-end technologies. His sessions helped the team understand practical aspects of UI development."),
            createMentorCard("Mr. Pramod Sir",
                "Pramod Sir provided deep insights into backend systems, databases, and logical structures. He played a key role in mentoring us on architectural design."),
            createMentorCard("Mr. Akshay Sir",
                "Akshay Sir is a skilled developer with experience in full-stack projects. He helped the team overcome technical hurdles and debug critical issues.")
        );

        // Team Section with members (ADDED photos and descriptions)
        VBox teamSection = new VBox(24);
        teamSection.setPadding(new Insets(24));
        teamSection.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 12;");

        Label teamTitle = new Label("👨‍💻 Our Team – Team Bytecode");
        teamTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        teamTitle.setTextFill(Color.web("#1b5e20"));

        Label teamDesc = new Label(
           "We are a dedicated team of students committed to promoting environmental awareness among children through innovative tech platforms."+
           " Each member has brought their unique skills—from UI design to database management—to help bring EcoEdu to life."
        );
        teamDesc.setFont(Font.font("Quicksand", 16));
        teamDesc.setWrapText(true);

        teamSection.getChildren().addAll(teamTitle, teamDesc);

        // Team Members Data - name, role, image path, description
        String[][] members = {
            {"Prafull Pawar", "Team Leader", "/Assets/Images/prafull.jpg", "Lead developer and project coordinator, passionate about eco-friendly tech."},
            {"Rushi Pawar", "Frontend Developer", "/Assets/Images/rushiphoto.jpg", "Expert in UI design and creating interactive user experiences."},
            {"Shivling Biradar", "UI Designer", "/Assets/Images/shivlingphoto.jpg", "Focused on visual aesthetics and user interface design."}
        };

        HBox membersBox = new HBox(24);
        membersBox.setAlignment(Pos.CENTER);

        for (String[] member : members) {
            VBox card = new VBox(8);
            card.setAlignment(Pos.CENTER);
            card.setPadding(new Insets(16));
            card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, #bbb, 4, 0.2, 0, 2);");
            card.setMaxWidth(200);

            ImageView avatar = null;
            try {
                avatar = new ImageView(new Image(getClass().getResourceAsStream(member[2])));
                avatar.setFitHeight(100);
                avatar.setFitWidth(100);
                avatar.setPreserveRatio(true);
                avatar.setSmooth(true);
            } catch (Exception e) {
                // no image fallback
            }

            Label name = new Label(member[0]);
            name.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));

            Label role = new Label(member[1]);
            role.setFont(Font.font("Quicksand", 14));
            role.setTextFill(Color.GRAY);

            Label desc = new Label(member[3]);
            desc.setFont(Font.font("Quicksand", 13));
            desc.setWrapText(true);
            desc.setTextFill(Color.DARKGRAY);

            if (avatar != null) {
                card.getChildren().addAll(avatar, name, role, desc);
            } else {
                card.getChildren().addAll(name, role, desc);
            }

            membersBox.getChildren().add(card);
        }

        teamSection.getChildren().add(membersBox);

        // Back Button
        Button backBtn = new Button("← Back");
        backBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 10 32; -fx-cursor: hand;");
        backBtn.setOnAction(e -> StudentDashboard.show(primaryStage));

        // Main content VBox (inside ScrollPane)
        VBox content = new VBox(32);
        content.setPadding(new Insets(24));
        content.setAlignment(Pos.TOP_CENTER);
        if (logoPane != null) {
            content.getChildren().addAll(title, logoPane, summary, mentorSection, teamSection, backBtn);
        } else {
            content.getChildren().addAll(title, summary, mentorSection, teamSection, backBtn);
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(12));
        scrollPane.setStyle("-fx-background-color: transparent;");

        getChildren().add(scrollPane);
    }

    private VBox createMentorCard(String name, String description) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: #f1f8e9; -fx-background-radius: 8;");
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.web("#33691e"));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Quicksand", 15));
        descLabel.setWrapText(true);
        descLabel.setTextFill(Color.web("#4e342e"));

        card.getChildren().addAll(nameLabel, descLabel);
        return card;
    }

    public static void show(Stage primaryStage) {
        AboutUs page = new AboutUs(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("About EcoEdu");
        primaryStage.show();
    }
}
