package com.ecoedu.leaderboard;

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
import java.text.SimpleDateFormat;
import java.util.List;
import com.ecoedu.leaderboard.LeaderboardService;
import javafx.scene.layout.Region;

public class LeaderboardAndBadgesPage extends VBox {
    public LeaderboardAndBadgesPage(Stage primaryStage) {
        setSpacing(32);
        setPadding(new Insets(40, 60, 40, 60));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // Modern Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(18);
        header.setStyle("-fx-background-radius: 0 0 32 32; -fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); -fx-effect: dropshadow(gaussian, #43e97b, 12, 0.2, 0, 4);");
        Label icon = new Label("🏆");
        icon.setFont(Font.font("Quicksand", FontWeight.BOLD, 40));
        Label title = new Label("Leaderboard & Badges");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#fffde7"));
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, javafx.scene.layout.Priority.ALWAYS);
        Button refreshBtn = new Button("⟳ Refresh");
        refreshBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24; -fx-cursor: hand;");
        refreshBtn.setOnAction(e -> com.ecoedu.leaderboard.LeaderboardAndBadgesPage.show(primaryStage));
        header.getChildren().addAll(icon, title, headerSpacer, refreshBtn, backBtn);
        getChildren().add(header);

        // Leaderboard
        VBox leaderboardBox = new VBox(10);
        leaderboardBox.setAlignment(Pos.CENTER);
        leaderboardBox.setPadding(new Insets(16));
        Label lbTitle = new Label("🏅 Top Eco Heroes");
        lbTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        lbTitle.setTextFill(Color.web("#43a047"));
        leaderboardBox.getChildren().add(lbTitle);
        List<LeaderboardService.UserStats> leaderboard = LeaderboardService.getInstance().getLeaderboard();
        String currentUser = LeaderboardService.getInstance().getCurrentUser();
        for (int i = 0; i < leaderboard.size(); i++) {
            LeaderboardService.UserStats stats = leaderboard.get(i);
            HBox row = new HBox(18);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(8));
            Label rank = new Label("#" + (i + 1));
            rank.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
            Label name = new Label(stats.name);
            name.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
            name.setTextFill(stats.name.equals(currentUser) ? Color.web("#ffb300") : Color.web("#263238"));
            Label score = new Label("Score: " + stats.score);
            score.setFont(Font.font("Quicksand", 17));
            // Show quizzes and minigames stats
            Label quizStat = new Label("📝 Quizzes: " + stats.badges.stream().filter(b -> b.contains("Quiz Master")).count());
            quizStat.setFont(Font.font("Quicksand", 15));
            Label gameStat = new Label("🎮 Minigames: " + stats.badges.stream().filter(b -> b.contains("Minigame Master")).count());
            gameStat.setFont(Font.font("Quicksand", 15));
            HBox badges = new HBox(4);
            for (String badge : stats.badges) {
                Label badgeIcon = new Label(badge.contains("Quiz") ? "🏅" : "🎖");
                badgeIcon.setFont(Font.font(22));
                badges.getChildren().add(badgeIcon);
            }
            row.getChildren().addAll(rank, name, score, quizStat, gameStat, badges);
            row.setStyle(stats.name.equals(currentUser) ? "-fx-background-color: #fffde7; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #ffd54f, 10, 0.15, 0, 3);" : "-fx-background-radius: 18;");
            leaderboardBox.getChildren().add(row);
        }
        getChildren().add(leaderboardBox);

        // Activity Feed
        VBox activityBox = new VBox(10);
        activityBox.setAlignment(Pos.CENTER);
        activityBox.setPadding(new Insets(16));
        Label actTitle = new Label("Recent Activity");
        actTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        actTitle.setTextFill(Color.web("#0288d1"));
        activityBox.getChildren().add(actTitle);
        List<LeaderboardService.Activity> activityFeed = LeaderboardService.getInstance().getActivityFeed();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i < Math.min(10, activityFeed.size()); i++) {
            LeaderboardService.Activity act = activityFeed.get(i);
            String iconStr = act.action.contains("quiz") ? "📝" : (act.action.contains("minigame") ? "🎮" : "🏅");
            Label actLabel = new Label(iconStr + " " + sdf.format(act.date) + " - " + act.user + " " + act.action);
            actLabel.setFont(Font.font("Quicksand", 15));
            actLabel.setTextFill(Color.web("#388e3c"));
            activityBox.getChildren().add(actLabel);
        }
        getChildren().add(activityBox);

        // My Stats
        LeaderboardService.UserStats me = LeaderboardService.getInstance().getCurrentUserStats();
        VBox myBox = new VBox(10);
        myBox.setAlignment(Pos.CENTER);
        myBox.setPadding(new Insets(16));
        Label myTitle = new Label("My Stats");
        myTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        myTitle.setTextFill(Color.web("#0288d1"));
        myBox.getChildren().add(myTitle);
        Label myScore = new Label("Score: " + me.score);
        myScore.setFont(Font.font("Quicksand", 16));
        Label myQuiz = new Label("📝 Quizzes: " + me.badges.stream().filter(b -> b.contains("Quiz Master")).count());
        myQuiz.setFont(Font.font("Quicksand", 15));
        Label myGame = new Label("🎮 Minigames: " + me.badges.stream().filter(b -> b.contains("Minigame Master")).count());
        myGame.setFont(Font.font("Quicksand", 15));
        myBox.getChildren().addAll(myQuiz, myGame);
        HBox myBadges = new HBox(4);
        for (String badge : me.badges) {
            Label badgeIcon = new Label(badge.contains("Quiz") ? "🏅" : "🎖");
            badgeIcon.setFont(Font.font(22));
            myBadges.getChildren().add(badgeIcon);
        }
        myBox.getChildren().add(myBadges);
        getChildren().add(myBox);
    }

    public static void show(Stage primaryStage) {
        LeaderboardAndBadgesPage page = new LeaderboardAndBadgesPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard & Badges");
        primaryStage.show();
    }
} 