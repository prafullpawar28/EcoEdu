package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserPanel extends VBox {
    private Stage primaryStage;

    public UserPanel(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(32, 40, 24, 40));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("root");

        // Profile section
        HBox profileBox = new HBox(16);
        profileBox.setAlignment(Pos.CENTER_LEFT);
        ImageView avatar = new ImageView(new Image(getClass().getResource("/Assets/Images/avatar.png").toExternalForm()));
        avatar.setFitWidth(64);
        avatar.setFitHeight(64);
        VBox infoBox = new VBox(4);
        Label name = new Label("Eco Hero Student");
        name.getStyleClass().add("eco-title");
        Label email = new Label("student@ecoedu.com");
        email.setStyle("-fx-text-fill: #388E3C; -fx-font-size: 15px;");
        infoBox.getChildren().addAll(name, email);
        profileBox.getChildren().addAll(avatar, infoBox);

        // Progress bar
        Label progressLabel = new Label("Learning Progress");
        progressLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #388E3C;");
        ProgressBar progressBar = new ProgressBar(0.45);
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: #A8E6CF;");

        // Quick links
        HBox quickLinks = new HBox(24);
        quickLinks.setAlignment(Pos.CENTER);
        Button modulesBtn = new Button("Modules");
        modulesBtn.getStyleClass().add("eco-btn");
        modulesBtn.setOnAction(e -> com.ecoedu.modules.ModulePage.show(primaryStage));
        Button quizzesBtn = new Button("Quizzes");
        quizzesBtn.getStyleClass().add("eco-btn");
        quizzesBtn.setOnAction(e -> com.ecoedu.quiz.QuizPage.show(primaryStage));
        quickLinks.getChildren().addAll(modulesBtn, quizzesBtn);

        // Add all to main panel
        getChildren().addAll(profileBox, progressLabel, progressBar, quickLinks);
    }

    public static void show(Stage primaryStage) {
        UserPanel panel = new UserPanel(primaryStage);
        Scene scene = new Scene(panel, 700, 400);
        // com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - User Panel");
        primaryStage.show();
    }
} 