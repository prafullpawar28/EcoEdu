package com.ecoedu.Home;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Dashboard extends VBox {
    private Stage primaryStage;

    public Dashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(30);
        setPadding(new Insets(40, 40, 40, 40));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe, #fffde7);");

        Label title = new Label("EcoEdu Dashboard");
        title.setFont(Font.font("Comic Sans MS", 36));
        title.setTextFill(Color.web("#0288d1"));
        title.setStyle("-fx-font-weight: bold;");
        getChildren().add(title);

        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(30);
        cardGrid.setVgap(30);
        cardGrid.setAlignment(Pos.CENTER);

        // Cards
        cardGrid.add(makeCard("Modules", "Learn eco topics!", "#81c784", "module", () -> openModule()), 0, 0);
        cardGrid.add(makeCard("Quiz & Puzzles", "Test your eco skills!", "#ffd54f", "quiz", () -> openQuiz()), 1, 0);
        cardGrid.add(makeCard("Avatar Customization", "Style your eco hero!", "#4fc3f7", "avatar", () -> openAvatar()), 0, 1);
        cardGrid.add(makeCard("Leaderboard & Badges", "See your rank!", "#ba68c8", "leaderboard", () -> openLeaderboard()), 1, 1);
        cardGrid.add(makeCard("Minigames", "Play & learn!", "#ff8a65", "minigames", () -> openMinigames()), 0, 2);
        cardGrid.add(makeCard("Daily Challenge", "New eco tasks!", "#a1887f", "daily", () -> openDaily()), 1, 2);

        getChildren().add(cardGrid);
    }

    private StackPane makeCard(String title, String subtitle, String color, String assetKey, Runnable onClick) {
        StackPane card = new StackPane();
        card.setPrefSize(220, 180);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 24; -fx-cursor: hand;");
        card.setEffect(new DropShadow(16, Color.web("#bdbdbd")));

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        // --- Asset placeholder ---
        ImageView icon = new ImageView();
        icon.setFitWidth(64);
        icon.setFitHeight(64);
        // icon.setImage(new Image("/assets/" + assetKey + ".png")); // <-- Add your asset here
        icon.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 32; -fx-border-radius: 32; -fx-border-color: #fff; -fx-border-width: 2;");
        // -------------------------

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Comic Sans MS", 22));
        titleLabel.setTextFill(Color.web("#263238"));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Comic Sans MS", 14));
        subtitleLabel.setTextFill(Color.web("#424242"));

        content.getChildren().addAll(icon, titleLabel, subtitleLabel);
        card.getChildren().add(content);

        card.setOnMouseClicked((MouseEvent e) -> onClick.run());
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-scale-x:1.04;-fx-scale-y:1.04;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-scale-x:1.04;-fx-scale-y:1.04;", "")));
        return card;
    }

    // --- Navigation methods ---
    private void openModule() {
        // TODO: Replace with actual module page
        showSection("Modules", "Here you will learn about eco topics!");
    }
    private void openQuiz() {
        com.ecoedu.quiz.QuizPage.show(primaryStage);
    }
    private void openAvatar() {
        // Open the avatar customization page
        com.ecoedu.avatar.AvatarCustomizer avatarCustomizer = new com.ecoedu.avatar.AvatarCustomizer();
        Stage avatarStage = new Stage();
        avatarCustomizer.start(avatarStage);
    }
    private void openLeaderboard() {
        // TODO: Replace with actual leaderboard/badgewall page
        showSection("Leaderboard & Badges", "See your rank and badges earned!");
    }
    private void openMinigames() {
        // Show a dialog to select which minigame to play
        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>("Trash Sorting", "Trash Sorting", "Ocean Cleanup");
        dialog.setTitle("Choose Minigame");
        dialog.setHeaderText("Select a minigame to play:");
        dialog.setContentText("Minigame:");
        java.util.Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> {
            if (choice.equals("Trash Sorting")) {
                com.ecoedu.minigames.TrashSortingGame.show(primaryStage);
            } else if (choice.equals("Ocean Cleanup")) {
                com.ecoedu.minigames.OceanCleanupGame.show(primaryStage);
            }
        });
    }
    private void openDaily() {
        // Open the daily challenge page
        com.ecoedu.dailytasks.DailyChallengePage.show(primaryStage);
    }

    // Simple placeholder for navigation
    private void showSection(String sectionTitle, String message) {
        VBox section = new VBox(30);
        section.setAlignment(Pos.CENTER);
        section.setStyle("-fx-background-color: linear-gradient(to bottom right, #fffde7, #e1f5fe);");
        Label title = new Label(sectionTitle);
        title.setFont(Font.font("Comic Sans MS", 32));
        title.setTextFill(Color.web("#0288d1"));
        Label msg = new Label(message);
        msg.setFont(Font.font("Comic Sans MS", 18));
        msg.setTextFill(Color.web("#388e3c"));
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 8 24;");
        backBtn.setOnAction(e -> primaryStage.setScene(new Scene(this, 900, 700)));
        section.getChildren().addAll(title, msg, backBtn);
        primaryStage.setScene(new Scene(section, 900, 700));
    }

    // --- Utility to launch dashboard ---
    public static void show(Stage primaryStage) {
        Dashboard dashboard = new Dashboard(primaryStage);
        Scene scene = new Scene(dashboard, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Dashboard");
        primaryStage.show();
    }
} 