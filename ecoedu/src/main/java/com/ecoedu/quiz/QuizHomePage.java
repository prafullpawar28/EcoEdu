package com.ecoedu.quiz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import com.ecoedu.leaderboard.LeaderboardService;

public class QuizHomePage extends VBox {
    public QuizHomePage(Stage primaryStage) {
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

        Label title = new Label("Quizzes & Puzzles");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        // Quiz category cards
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(40);
        grid.setAlignment(Pos.CENTER);
        List<String> categories = QuizData.getQuizCategories();
        for (int i = 0; i < categories.size(); i++) {
            String cat = categories.get(i);
            VBox card = makeQuizCard(cat, () -> QuizPage.show(primaryStage, cat));
            grid.add(card, i % 2, i / 2);
        }
        getChildren().add(grid);

        // My Progress area
        VBox progressBox = new VBox(8);
        progressBox.setAlignment(Pos.CENTER);
        Label progressTitle = new Label("My Progress");
        progressTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        progressTitle.setTextFill(Color.web("#43a047"));
        progressBox.getChildren().add(progressTitle);
        // Real progress tracking
        LeaderboardService.UserStats me = LeaderboardService.getInstance().getCurrentUserStats();
        int completed = 0;
        for (String cat : QuizData.getQuizCategories()) {
            if (me.badges.contains(cat + " Quiz Master")) completed++;
        }
        Label progress = new Label("Quizzes completed: " + completed + " / " + QuizData.getQuizCategories().size() + " | Badges: " + me.badges.size());
        progress.setFont(Font.font("Quicksand", 16));
        progressBox.getChildren().add(progress);
        HBox badgeBox = new HBox(4);
        for (String badge : me.badges) {
            Label badgeIcon = new Label("🏅 " + badge);
            badgeIcon.setFont(Font.font(18));
            badgeBox.getChildren().add(badgeIcon);
        }
        progressBox.getChildren().add(badgeBox);
        getChildren().add(progressBox);
    }

    private VBox makeQuizCard(String title, Runnable onClick) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(340, 180);
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, #b2ff59, #81d4fa 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);");
        Label icon = new Label("❓");
        icon.setFont(Font.font("Quicksand", FontWeight.BOLD, 44));
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#263238"));
        card.getChildren().addAll(icon, titleLabel);
        card.setOnMouseClicked(e -> onClick.run());
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, #b2ff59, #81d4fa 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-scale-x:1.08;-fx-scale-y:1.08;-fx-effect: dropshadow(gaussian, #0288d1, 32, 0.3, 0, 10);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, #b2ff59, #81d4fa 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);"));
        return card;
    }

    public static void show(Stage primaryStage) {
        QuizHomePage page = new QuizHomePage(primaryStage);
        Scene scene = new Scene(page, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Quizzes & Puzzles");
        primaryStage.show();
    }
} 