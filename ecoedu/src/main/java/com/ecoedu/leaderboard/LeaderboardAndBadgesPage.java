package com.ecoedu.leaderboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.List;
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
        // Tabs for leaderboard views
        HBox tabBar = new HBox(18);
        tabBar.setAlignment(Pos.CENTER);
        tabBar.setPadding(new Insets(0, 0, 18, 0));
        String[] tabNames = {"This Week", "All Time", "Friends Only"};
        List<javafx.scene.control.Label> tabLabels = new java.util.ArrayList<>();
        for (String tab : tabNames) {
            javafx.scene.control.Label tabLabel = new javafx.scene.control.Label(tab);
            tabLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
            tabLabel.setStyle("-fx-background-radius: 16; -fx-padding: 8 28; -fx-cursor: hand; -fx-background-color: #e0f7fa; -fx-text-fill: #388e3c;");
            tabLabels.add(tabLabel);
            tabBar.getChildren().add(tabLabel);
        }
        leaderboardBox.getChildren().add(tabBar);
        // Card pane and scroll
        javafx.scene.layout.FlowPane cardPane = new javafx.scene.layout.FlowPane();
        cardPane.setHgap(32);
        cardPane.setVgap(24);
        cardPane.setAlignment(Pos.CENTER);
        cardPane.setPrefWrapLength(1100);
        javafx.scene.control.ScrollPane scroll = new javafx.scene.control.ScrollPane(cardPane);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(420);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        leaderboardBox.getChildren().add(scroll);
        // Data for tabs
        List<LeaderboardService.UserStats> allTime = LeaderboardService.getInstance().getLeaderboard();
        List<LeaderboardService.UserStats> thisWeek = new java.util.ArrayList<>(allTime);
        java.util.Collections.shuffle(thisWeek); // Demo: shuffle for "This Week"
        List<LeaderboardService.UserStats> friends = new java.util.ArrayList<>();
        for (LeaderboardService.UserStats s : allTime) {
            if (s.name.equals("Eco Kid") || s.name.equals("Green Guru")) friends.add(s);
        }
        String currentUser = LeaderboardService.getInstance().getCurrentUser();
        // Search bar
        HBox searchBar = new HBox(8);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(0, 0, 12, 0));
        Label searchLabel = new Label("🔍");
        searchLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        javafx.scene.control.TextField searchField = new javafx.scene.control.TextField();
        searchField.setPromptText("Search user...");
        searchField.setPrefWidth(220);
        searchBar.getChildren().addAll(searchLabel, searchField);
        leaderboardBox.getChildren().add(searchBar);
        // Helper to simulate achievements and level/xp
        java.util.function.Function<String, Integer> getLevel = (name) -> 1 + (name.hashCode() % 5 + 5) % 5;
        java.util.function.Function<String, Integer> getXP = (name) -> 10 + (name.hashCode() % 100 + 100) % 100;
        java.util.function.Function<String, java.util.List<String>> getAchievements = (name) -> java.util.Arrays.asList(
            "Completed 5 quizzes", "Won 3 minigames", "Planted 10 trees"
        );
        // Helper to render cards
        java.util.function.Consumer<List<LeaderboardService.UserStats>> renderCards = (list) -> {
            cardPane.getChildren().clear();
            String filter = searchField.getText().trim().toLowerCase();
            for (int i = 0, shown = 0; i < list.size(); i++) {
                LeaderboardService.UserStats stats = list.get(i);
                if (!filter.isEmpty() && !stats.name.toLowerCase().contains(filter)) continue;
                int quizzes = (int) stats.badges.stream().filter(b -> b.contains("Quiz Master")).count();
                int minigames = (int) stats.badges.stream().filter(b -> b.contains("Minigame Master")).count();
                AvatarLeaderboardCard card = new AvatarLeaderboardCard(
                    shown + 1,
                    stats.name,
                    stats.score,
                    quizzes,
                    minigames,
                    stats.badges,
                    stats.name.equals(currentUser),
                    "/Assets/Images/avatar.png"
                );
                card.setOpacity(0);
                javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(400 + shown * 80), card);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
                // Profile popover on click
                card.setOnMouseClicked(ev -> {
                    UserProfilePopover pop = new UserProfilePopover(
                        stats.name,
                        "/Assets/Images/avatar.png",
                        getLevel.apply(stats.name),
                        getXP.apply(stats.name),
                        stats.badges,
                        getAchievements.apply(stats.name)
                    );
                    pop.show();
                });
                cardPane.getChildren().add(card);
                shown++;
            }
        };
        // Tab switching logic
        for (int i = 0; i < tabLabels.size(); i++) {
            int idx = i;
            tabLabels.get(i).setOnMouseClicked(e -> {
                for (int j = 0; j < tabLabels.size(); j++) {
                    tabLabels.get(j).setStyle("-fx-background-radius: 16; -fx-padding: 8 28; -fx-cursor: hand; -fx-background-color: #e0f7fa; -fx-text-fill: #388e3c;");
                }
                tabLabels.get(idx).setStyle("-fx-background-radius: 16; -fx-padding: 8 28; -fx-cursor: hand; -fx-background-color: linear-gradient(to right, #43e97b, #b2ff59); -fx-text-fill: #fffde7; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.18, 0, 2);");
                if (idx == 0) renderCards.accept(thisWeek);
                else if (idx == 1) renderCards.accept(allTime);
                else renderCards.accept(friends);
            });
        }
        // Default: All Time selected
        tabLabels.get(1).setStyle("-fx-background-radius: 16; -fx-padding: 8 28; -fx-cursor: hand; -fx-background-color: linear-gradient(to right, #43e97b, #b2ff59); -fx-text-fill: #fffde7; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.18, 0, 2);");
        renderCards.accept(allTime);
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

        // Search field listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            int idx = 1;
            for (int i = 0; i < tabLabels.size(); i++) {
                if (tabLabels.get(i).getStyle().contains("linear-gradient")) idx = i;
            }
            if (idx == 0) renderCards.accept(thisWeek);
            else if (idx == 1) renderCards.accept(allTime);
            else renderCards.accept(friends);
        });
    }

    public static void show(Stage primaryStage) {
        LeaderboardAndBadgesPage page = new LeaderboardAndBadgesPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard & Badges");
        primaryStage.show();
    }
} 