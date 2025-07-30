package com.ecoedu.leaderboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.ecoedu.auth.FirebaseAuthService;
import com.google.firebase.FirebaseApp;

import javafx.animation.FadeTransition;
import javafx.collections.ListChangeListener;

public class LeaderboardAndBadgesPage extends VBox {
    private FlowPane leaderboardFlow;
    private ScrollPane leaderboardScroll;
    private TextField searchField;
    private ComboBox<String> sortBox;
    private Button refreshBtn;
    private Label lastUpdatedLabel;
    private ProgressIndicator loadingSpinner;
    private VBox myRankBox;

    public LeaderboardAndBadgesPage() {
        FirebaseAuthService firebaseAuthService = new FirebaseAuthService();
                String [][]list=firebaseAuthService.getAllStudents("Student");  
                System.out.println("Fetching...");  
                 Arrays.sort(list, (a, b) -> b[5].compareTo(a[5]));
        setSpacing(24);
        setPadding(new Insets(32, 40, 24, 40));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa 60%, #fffde7 100%);");

        // Header
        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);
        Label trophy = new Label("Trophy:");
        trophy.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 38));
        Label title = new Label("Leaderboard & Badges");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        refreshBtn = new Button("⟳ Refresh");
        refreshBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        refreshBtn.setStyle("-fx-background-radius: 16; -fx-background-color: linear-gradient(to right, #b2ff59, #81d4fa); -fx-text-fill: #0288d1; -fx-cursor: hand;");
        refreshBtn.setOnAction(e -> {
            refreshLeaderboard();
        });
        lastUpdatedLabel = new Label();
        lastUpdatedLabel.setFont(Font.font("Quicksand", 13));
        lastUpdatedLabel.setTextFill(Color.web("#388e3c"));
        HBox.setMargin(refreshBtn, new Insets(0, 0, 0, 24));
        HBox.setMargin(lastUpdatedLabel, new Insets(0, 0, 0, 18));
        header.getChildren().addAll(trophy, title, refreshBtn, lastUpdatedLabel);
        getChildren().add(header);

        // Controls
        HBox controls = new HBox(16);
        controls.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("Search user...");
        searchField.setFont(Font.font("Quicksand", 15));
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshLeaderboard());
        sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Score", "Quizzes", "Minigames");
        sortBox.setValue("Score");
        sortBox.setOnAction(e -> refreshLeaderboard());
        controls.getChildren().addAll(new Label("Sort by:"), sortBox, searchField);
        getChildren().add(controls);

        // Leaderboard section
        leaderboardFlow = new FlowPane();
        leaderboardFlow.setHgap(32);
        leaderboardFlow.setVgap(32);
        leaderboardFlow.setPadding(new Insets(16, 0, 16, 0));
        leaderboardFlow.setAlignment(Pos.TOP_CENTER);
        leaderboardFlow.setPrefWrapLength(900);
        leaderboardScroll = new ScrollPane(leaderboardFlow);
        leaderboardScroll.setFitToWidth(true);
        leaderboardScroll.setPrefHeight(420);
        leaderboardScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-radius: 18;");
        StackPane leaderboardSection = new StackPane(leaderboardScroll);
        leaderboardSection.setStyle("-fx-background-radius: 24; -fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%); -fx-padding: 18 0 18 0;");
        // Loading spinner
        loadingSpinner = new ProgressIndicator();
        loadingSpinner.setMaxSize(60, 60);
        leaderboardSection.getChildren().add(loadingSpinner);
        getChildren().add(leaderboardSection);
        // Sticky My Rank card
        myRankBox = new VBox();
        myRankBox.setAlignment(Pos.CENTER);
        myRankBox.setPadding(new Insets(8));
        getChildren().add(2, myRankBox);
        // Listen for real-time updates
        LeaderboardService.getInstance().getLeaderboardUsers().addListener((ListChangeListener<LeaderboardService.LeaderboardUserStats>) c -> {
            refreshLeaderboard();
        });
        refreshLeaderboard();
    }

    public void refreshLeaderboard() {
        leaderboardFlow.getChildren().clear();
        myRankBox.getChildren().clear();
        List<LeaderboardService.LeaderboardUserStats> users = LeaderboardService.getInstance().getLeaderboardUsers();
        if (users.isEmpty()) {
            loadingSpinner.setVisible(true);
            return;
        } else {
            loadingSpinner.setVisible(false);
        }
        String search = searchField.getText() == null ? "" : searchField.getText().toLowerCase(Locale.ROOT);
        String sort = sortBox.getValue();
        // Filter
        users = users.stream().filter(u -> u.username != null && u.username.toLowerCase(Locale.ROOT).contains(search)).collect(Collectors.toList());
        // Sort
        if ("Quizzes".equals(sort)) {
            users.sort(Comparator.comparingInt((LeaderboardService.LeaderboardUserStats u) -> u.quizzesCompleted).reversed());
        } else if ("Minigames".equals(sort)) {
            users.sort(Comparator.comparingInt((LeaderboardService.LeaderboardUserStats u) -> u.minigamesPlayed).reversed());
        } else {
            users.sort(Comparator.comparingInt((LeaderboardService.LeaderboardUserStats u) -> u.score).reversed());
        }
        lastUpdatedLabel.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
        // Sticky My Rank card
        LeaderboardService.LeaderboardUserStats me = users.stream().filter(u -> u.isCurrentUser).findFirst().orElse(null);
        if (me != null) {
            VBox myCard = makeCard(me, me.rank - 1);
            myCard.setStyle(myCard.getStyle() + "-fx-border-color: #ffb300; -fx-border-width: 4; -fx-border-radius: 22; -fx-effect: dropshadow(gaussian, #ffb300, 32, 0.38, 0, 8);");
            Label sticky = new Label("My Rank");
            sticky.setFont(Font.font("Quicksand", FontWeight.BOLD, 15));
            sticky.setTextFill(Color.web("#ffb300"));
            myRankBox.getChildren().addAll(sticky, myCard);
        }
        // Animate cards
        for (int i = 0; i < users.size(); i++) {
            LeaderboardService.LeaderboardUserStats user = users.get(i);
            if (me != null && user.username.equals(me.username)) continue; // Already shown as sticky
            VBox card = makeCard(user, i);
            FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(500), card);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            leaderboardFlow.getChildren().add(card);
        }
    }

    private VBox makeCard(LeaderboardService.LeaderboardUserStats user, int index) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        String gradient = index == 0 ? "linear-gradient(to bottom right, #fffde7, #ffd700 90%)" :
                index == 1 ? "linear-gradient(to bottom right, #fffde7, #b0bec5 90%)" :
                index == 2 ? "linear-gradient(to bottom right, #fffde7, #cd7f32 90%)" :
                "linear-gradient(to bottom right, #e0f7fa, #b2dfdb 90%)";
        card.setStyle("-fx-background-radius: 22; -fx-background-color: " + gradient + "; -fx-effect: dropshadow(gaussian, #43e97b, 16, 0.18, 0, 4);");
        if (user.isCurrentUser) {
            card.setStyle(card.getStyle() + "-fx-border-color: #43e97b; -fx-border-width: 3; -fx-border-radius: 22; -fx-effect: dropshadow(gaussian, #43e97b, 32, 0.38, 0, 8);");
        }
        card.setPadding(new Insets(16));
        card.setMinWidth(260);
        card.setMaxWidth(260);
        card.setMinHeight(220);
        card.setMaxHeight(220);
        // Rank
        Label rankLabel = new Label("#" + (index + 1));
        rankLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        rankLabel.setTextFill(Color.web("#388e3c"));
        // Avatar (use initials)
        Circle avatarCircle = new Circle(27, Color.web("#b2dfdb"));
        Label initials = new Label(user.username.substring(0, 1).toUpperCase());
        initials.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        initials.setTextFill(Color.web("#0288d1"));
        StackPane avatar = new StackPane(avatarCircle, initials);
        // Name
        Label nameLabel = new Label(user.username);
        nameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        nameLabel.setTextFill(Color.web("#22223b"));
        // Score
        HBox scoreBox = new HBox(6);
        scoreBox.setAlignment(Pos.CENTER);
        Label scoreIcon = new Label("XP");
        scoreIcon.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        Label scoreLabel = new Label(user.score + " XP");
        scoreLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        scoreLabel.setTextFill(Color.web("#3a86ff"));
        scoreBox.getChildren().addAll(scoreIcon, scoreLabel);
        // Badges
        HBox badgesBox = new HBox(10);
        badgesBox.setAlignment(Pos.CENTER);
        for (String badge : user.badges) {
            Label badgeIcon = new Label(badge.equals("Quiz Master") ? "📝" : badge.equals("Minigame Master") ? "🎮" : "🏅");
            badgeIcon.setFont(Font.font(20));
            badgeIcon.setTooltip(new Tooltip(badge));
            badgesBox.getChildren().add(badgeIcon);
        }
        // Progress bar
        ProgressBar xpBar = new ProgressBar(user.progress);
        xpBar.setPrefWidth(160);
        xpBar.setStyle("-fx-accent: #43e97b; -fx-background-radius: 8; -fx-border-radius: 8;");
        Label xpLabel = new Label((int)(user.progress * 100) + "% XP");
        xpLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 13));
        xpLabel.setTextFill(Color.web("#388e3c"));
        HBox xpBox = new HBox(8, xpBar, xpLabel);
        xpBox.setAlignment(Pos.CENTER);
        // Quizzes/Minigames
        HBox statsBox = new HBox(16);
        statsBox.setAlignment(Pos.CENTER);
        Label quizLabel = new Label("📝 " + user.quizzesCompleted);
        quizLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 15));
        quizLabel.setTooltip(new Tooltip("Quizzes Completed"));
        Label minigameLabel = new Label("🎮 " + user.minigamesPlayed);
        minigameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 15));
        minigameLabel.setTooltip(new Tooltip("Minigames Played"));
        statsBox.getChildren().addAll(quizLabel, minigameLabel);
        // View Profile button
        Button profileBtn = new Button("View Profile");
        profileBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
        profileBtn.setStyle("-fx-background-radius: 12; -fx-background-color: linear-gradient(to right, #b2ff59, #81d4fa); -fx-text-fill: #0288d1; -fx-cursor: hand;");
        profileBtn.setOnAction(e -> showProfileDialog(user));
        VBox content = new VBox(8, rankLabel, avatar, nameLabel, scoreBox, badgesBox, statsBox, xpBox, profileBtn);
        content.setAlignment(Pos.CENTER);
        card.getChildren().add(content);
        // Hover animation
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-scale-x:1.06;-fx-scale-y:1.06;-fx-effect: dropshadow(gaussian, #43e97b, 32, 0.28, 0, 8);"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replaceAll("-fx-scale-x:1.06;-fx-scale-y:1.06;", "")));
        return card;
    }

    private void showProfileDialog(LeaderboardService.LeaderboardUserStats user) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Profile");
        alert.setHeaderText(user.username + "'s Profile");
        alert.setContentText("Rank: #" + user.rank + "\nScore: " + user.score + " XP\nQuizzes: " + user.quizzesCompleted + "\nMinigames: " + user.minigamesPlayed + "\nBadges: " + String.join(", ", user.badges));
        alert.showAndWait();
    }

    // If you have a static show method, set the scene size to 1366x768
    public static void show(javafx.stage.Stage primaryStage) {
        LeaderboardAndBadgesPage page = new LeaderboardAndBadgesPage();
        javafx.scene.Scene scene = new javafx.scene.Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard & Badges");
        primaryStage.show();
    }
} 