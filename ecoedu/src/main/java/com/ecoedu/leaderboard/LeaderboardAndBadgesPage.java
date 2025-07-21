package com.ecoedu.leaderboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import java.util.Arrays;

public class LeaderboardAndBadgesPage extends VBox {
    private Stage primaryStage;
    private TableView<PlayerScore> table;
    private String currentUser = "You";
    private List<Badge> badges;

    public LeaderboardAndBadgesPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #fffde7);");

        Label title = new Label("🏆 Leaderboard & Badges");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // --- Leaderboard Tab ---
        Tab leaderboardTab = new Tab("Leaderboard");
        leaderboardTab.setContent(createLeaderboardContent());

        // --- Badges Tab ---
        Tab badgesTab = new Tab("Badges");
        badgesTab.setContent(createBadgesContent());

        tabPane.getTabs().addAll(leaderboardTab, badgesTab);
        getChildren().add(tabPane);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        getChildren().add(backBtn);
    }

    private VBox createLeaderboardContent() {
        VBox box = new VBox(18);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(24));

        table = new TableView<>();
        table.setPrefWidth(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PlayerScore, Integer> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("rank"));
        TableColumn<PlayerScore, String> nameCol = new TableColumn<>("Player");
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        TableColumn<PlayerScore, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("score"));

        table.getColumns().addAll(rankCol, nameCol, scoreCol);
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(PlayerScore item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getName().equals(currentUser)) {
                    setStyle("-fx-background-color: #fffde7; -fx-border-color: #ffb300; -fx-border-width: 2px;");
                } else if (item.getRank() == 1) {
                    setStyle("-fx-background-color: #ffd54f; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });

        // Example data (replace with real data source)
        List<PlayerScore> scores = Arrays.asList(
            new PlayerScore(1, "Alice", 1200),
            new PlayerScore(2, "Bob", 1100),
            new PlayerScore(3, currentUser, 1050),
            new PlayerScore(4, "Charlie", 900),
            new PlayerScore(5, "Daisy", 850)
        );
        table.getItems().addAll(scores);
        box.getChildren().add(table);
        return box;
    }

    private VBox createBadgesContent() {
        VBox box = new VBox(18);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(24));

        // Example badges (replace with real data)
        badges = Arrays.asList(
            new Badge("Quiz Whiz", true, "Completed 10 quizzes!", "/Assets/Images/badge_quiz.png"),
            new Badge("Eco Hero", true, "Reached Level 5!", "/Assets/Images/badge_ecohero.png"),
            new Badge("Recycler", false, "Sorted 50 items in Trash Sorting!", "/Assets/Images/badge_recycler.png"),
            new Badge("Daily Streak", false, "Completed 7 daily challenges in a row!", "/Assets/Images/badge_streak.png")
        );

        GridPane grid = new GridPane();
        grid.setHgap(32);
        grid.setVgap(32);
        grid.setAlignment(Pos.CENTER);

        int col = 0, row = 0;
        for (Badge badge : badges) {
            VBox badgeBox = new VBox(8);
            badgeBox.setAlignment(Pos.CENTER);
            badgeBox.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #b3e5fc, 8, 0.2, 0, 2);");
            ImageView icon = new ImageView();
            try {
                icon.setImage(new Image(getClass().getResourceAsStream(badge.imagePath)));
            } catch (Exception e) {
                icon.setImage(null);
            }
            icon.setFitWidth(64);
            icon.setFitHeight(64);
            if (!badge.earned) {
                icon.setOpacity(0.3);
            }
            Label name = new Label(badge.name);
            name.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
            name.setTextFill(badge.earned ? Color.web("#43a047") : Color.web("#bdbdbd"));
            Tooltip.install(badgeBox, new Tooltip(badge.description));
            badgeBox.getChildren().addAll(icon, name);
            grid.add(badgeBox, col, row);
            col++;
            if (col > 2) { col = 0; row++; }
        }
        box.getChildren().add(grid);
        return box;
    }

    // PlayerScore class for leaderboard
    public static class PlayerScore {
        private final int rank;
        private final String name;
        private final int score;
        public PlayerScore(int rank, String name, int score) {
            this.rank = rank;
            this.name = name;
            this.score = score;
        }
        public int getRank() { return rank; }
        public String getName() { return name; }
        public int getScore() { return score; }
    }

    // Badge class for badges
    public static class Badge {
        public final String name;
        public final boolean earned;
        public final String description;
        public final String imagePath;
        public Badge(String name, boolean earned, String description, String imagePath) {
            this.name = name;
            this.earned = earned;
            this.description = description;
            this.imagePath = imagePath;
        }
    }

    public static void show(Stage primaryStage) {
        LeaderboardAndBadgesPage page = new LeaderboardAndBadgesPage(primaryStage);
        Scene scene = new Scene(page, 800, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard & Badges");
        primaryStage.show();
    }
} 