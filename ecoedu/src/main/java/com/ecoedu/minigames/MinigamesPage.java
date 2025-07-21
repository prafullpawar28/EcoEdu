package com.ecoedu.minigames;

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

public class MinigamesPage extends VBox {
    private Stage primaryStage;

    public MinigamesPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #fffde7, #e3f2fd);");

        // Header
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 8 16; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        Label title = new Label("🎮 Minigames");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#ff8a65"));
        header.getChildren().addAll(backBtn, title);
        getChildren().add(header);

        // Cards for minigames
        HBox cardRow = new HBox(36);
        cardRow.setAlignment(Pos.CENTER);
        cardRow.setPadding(new Insets(32, 0, 0, 0));

        cardRow.getChildren().add(makeGameCard(
            "🗑️ Trash Sorting",
            "Sort waste into the right bins!",
            "/Assets/Images/minigames.png",
            "#81c784",
            () -> TrashSortingGame.show(primaryStage)
        ));
        cardRow.getChildren().add(makeGameCard(
            "🌊 Ocean Cleanup",
            "Clean up the ocean and save marine life!",
            "/Assets/Images/minigames.png",
            "#4fc3f7",
            () -> OceanCleanupGame.show(primaryStage)
        ));

        getChildren().add(cardRow);
    }

    private VBox makeGameCard(String title, String subtitle, String iconPath, String color, Runnable onClick) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(240, 200);
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);");
        // Icon
        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
        } catch (Exception e) {
            icon.setImage(null);
        }
        icon.setFitWidth(64);
        icon.setFitHeight(64);
        icon.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 32; -fx-border-radius: 32; -fx-border-color: #fff; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, #fffde7, 8, 0.2, 0, 2);");
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Comic Sans MS", 20));
        titleLabel.setTextFill(Color.web("#263238"));
        titleLabel.setStyle("-fx-font-weight: bold;");
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Comic Sans MS", 14));
        subtitleLabel.setTextFill(Color.web("#424242"));
        Button playBtn = new Button("Play");
        playBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        playBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 32; -fx-cursor: hand;");
        playBtn.setOnAction(e -> onClick.run());
        card.getChildren().addAll(icon, titleLabel, subtitleLabel, playBtn);
        card.setOnMouseClicked(e -> onClick.run());
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-scale-x:1.08;-fx-scale-y:1.08;-fx-effect: dropshadow(gaussian, #0288d1, 32, 0.3, 0, 10);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);"));
        return card;
    }

    public static void show(Stage primaryStage) {
        MinigamesPage page = new MinigamesPage(primaryStage);
        Scene scene = new Scene(page, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Minigames");
        primaryStage.show();
    }
} 