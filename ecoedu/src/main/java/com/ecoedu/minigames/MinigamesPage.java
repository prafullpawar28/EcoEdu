package com.ecoedu.minigames;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MinigamesPage extends VBox {
    public MinigamesPage(Stage primaryStage) {
        setSpacing(32);
        setPadding(new Insets(40, 60, 40, 60));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // Back button
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        topBar.getChildren().add(backBtn);
        getChildren().add(topBar);

        Label title = new Label("Minigames");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        HBox cardRow = new HBox(40);
        cardRow.setAlignment(Pos.CENTER);

        VBox trashCard = makeGameCard("Trash Sorting Game", "Sort the trash correctly!", "/Assets/Images/trashsorter.jpeg", () -> TrashSortingGame.show(primaryStage));
        VBox oceanCard = makeGameCard("Ocean Cleaner Game", "Clean up the ocean!", "/Assets/Images/ocean.jpg", () -> OceanCleanupGame.show(primaryStage));
        VBox pollutionCard = makeGameCard(
            "Pollution Patrol",
            "Catch falling trash, steer your boat,\n and save the sea!",
            "/Assets/Images/boat.png",
            () -> PollutionPatrolGame.show(primaryStage)
        );
        // Enhance Pollution Patrol card with a unique style and animation
        pollutionCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #00c6ff, #43e97b 80%); -fx-background-radius: 32; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #00c6ff, 24, 0.3, 0, 8);");
        pollutionCard.setOnMouseEntered(e -> pollutionCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #43e97b, #00c6ff 80%); -fx-background-radius: 32; -fx-cursor: hand; -fx-scale-x:1.10;-fx-scale-y:1.10;-fx-effect: dropshadow(gaussian, #43e97b, 36, 0.4, 0, 14);"));
        pollutionCard.setOnMouseExited(e -> pollutionCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #00c6ff, #43e97b 80%); -fx-background-radius: 32; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #00c6ff, 24, 0.3, 0, 8);"));
        cardRow.getChildren().addAll(trashCard, oceanCard, pollutionCard);
        getChildren().add(cardRow);
    }

    private VBox makeGameCard(String title, String subtitle, String imagePath, Runnable onClick) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(320, 200);
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, #b2ff59, #81d4fa 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);");
        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            icon.setImage(null);
        }
        icon.setFitWidth(80);
        icon.setFitHeight(80);
        icon.setStyle("-fx-background-radius: 40; -fx-border-radius: 40; -fx-border-color: #fff; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, #fffde7, 8, 0.2, 0, 2);");
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#263238"));
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Quicksand", 14));
        subtitleLabel.setTextFill(Color.web("#424242"));
        card.getChildren().addAll(icon, titleLabel, subtitleLabel);
        card.setOnMouseClicked(e -> onClick.run());
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, #b2ff59, #81d4fa 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-scale-x:1.08;-fx-scale-y:1.08;-fx-effect: dropshadow(gaussian, #0288d1, 32, 0.3, 0, 10);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, #b2ff59, #81d4fa 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);"));
        return card;
    }

    public static void show(Stage primaryStage) {
        MinigamesPage page = new MinigamesPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Minigames");
        primaryStage.show();
    }
} 