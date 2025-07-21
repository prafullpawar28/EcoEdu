package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class StudentDashboard extends VBox {
    private Stage primaryStage;

    public StudentDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(18);
        setPadding(new Insets(32, 40, 24, 40));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // --- Header with Settings Icon ---
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setPadding(new Insets(0, 0, 10, 0));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button settingsBtn = new Button();
        ImageView settingsIcon = new ImageView(new Image(getClass().getResource("").toExternalForm()));
        settingsIcon.setFitWidth(32);
        settingsIcon.setFitHeight(32);
        settingsBtn.setGraphic(settingsIcon);
        settingsBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        settingsBtn.setOnAction(e -> StudentSettingsView.show(primaryStage));
        header.getChildren().addAll(spacer, settingsBtn);
        getChildren().add(header);

        // --- Playful Animated Eco-Themed Header ---
        HBox playfulHeader = new HBox(18);
        playfulHeader.setAlignment(Pos.CENTER_LEFT);
        playfulHeader.setPadding(new Insets(0, 0, 0, 0));
        Label mascot = new Label("\uD83C\uDF0E");
        mascot.setFont(Font.font("Quicksand", FontWeight.BOLD, 44));
        Label sun = new Label("\u2600\uFE0F");
        sun.setFont(Font.font("Quicksand", 32));
        Label greeting = new Label("Welcome back, Eco Hero!");
        greeting.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        greeting.setTextFill(Color.web("#0288d1"));
        playfulHeader.getChildren().addAll(mascot, greeting, sun);
        getChildren().add(playfulHeader);

        // --- Notification/Info Bar ---
        HBox infoBar = new HBox();
        infoBar.setAlignment(Pos.CENTER_LEFT);
        infoBar.setPadding(new Insets(10, 0, 10, 0));
        infoBar.setStyle("-fx-background-color: linear-gradient(to right, #b2ff59, #81d4fa); -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #b2ff59, 8, 0.1, 0, 2);");
        Label infoLabel = new Label("\uD83C\uDF31 Tip: Every small eco-action counts! Try a new module today.");
        infoLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        infoLabel.setTextFill(Color.web("#388e3c"));
        infoBar.getChildren().add(infoLabel);
        getChildren().add(infoBar);

        // --- Dashboard Title ---
        Label title = new Label("\uD83D\uDC66 Student Dashboard");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#0288d1"));
        title.setStyle("-fx-font-weight: bold;");
        getChildren().add(title);

        // --- Card Grid with Scroll ---
        GridPane cardGrid = new GridPane();
        cardGrid.setHgap(40);
        cardGrid.setVgap(40);
        cardGrid.setAlignment(Pos.TOP_CENTER);
        cardGrid.add(makeCard("\uD83D\uDCDA Modules", "Learn eco topics!", "#81c784", "/Assets/Images/module.png", true, () -> openModules()), 0, 0);
        cardGrid.add(makeCard("\uD83E\uDDE9 Quiz & Puzzles", "Test your eco skills!", "#ffd54f", "/Assets/Images/quiz.png", false, () -> openQuiz()), 1, 0);
        cardGrid.add(makeCard("\uD83E\uDDD1\u200D\uD83C\uDFA8 Avatar Customization", "Style your eco hero!", "#4fc3f7", "/Assets/Images/avatar.png", false, () -> openAvatar()), 0, 1);
        cardGrid.add(makeCard("\uD83C\uDFC6 Leaderboard & Badges", "See your rank and achievements!", "#ffd54f", "/Assets/Images/leaderboard.png", true, () -> openLeaderboardAndBadges()), 1, 1);
        cardGrid.add(makeCard("\uD83C\uDFAE Minigames", "Play & learn!", "#ff8a65", "/Assets/Images/minigames.png", false, () -> openMinigamesPage()), 0, 2);
        cardGrid.add(makeCard("\uD83C\uDF31 Daily Challenge", "New eco tasks!", "#a1887f", "/Assets/Images/daily.png", false, () -> openDaily()), 1, 2);
        ScrollPane scrollPane = new ScrollPane(cardGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        getChildren().add(scrollPane);

        // --- Footer with Motivational Quote ---
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(18, 0, 0, 0));
        footer.setStyle("-fx-background-color: transparent;");
        Label quote = new Label("\"The Earth is what we all have in common.\" – Wendell Berry");
        quote.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        quote.setTextFill(Color.web("#388e3c"));
        footer.getChildren().add(quote);
        getChildren().add(footer);
    }

    // Enhanced makeCard with increased width, spacing, and expressive hover
    private VBox makeCard(String title, String subtitle, String color, String iconPath, boolean isNew, Runnable onClick) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(340, 220);
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);");
        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(iconPath)));
        } catch (Exception e) {
            icon.setImage(null);
        }
        icon.setFitWidth(72);
        icon.setFitHeight(72);
        icon.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 36; -fx-border-radius: 36; -fx-border-color: #fff; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, #fffde7, 8, 0.2, 0, 2);");
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Quicksand", 22));
        titleLabel.setTextFill(Color.web("#263238"));
        titleLabel.setStyle("-fx-font-weight: bold;");
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Quicksand", 14));
        subtitleLabel.setTextFill(Color.web("#424242"));
        HBox badgeBox = new HBox();
        badgeBox.setAlignment(Pos.CENTER);
        if (isNew) {
            Label badge = new Label("NEW");
            badge.setFont(Font.font("Quicksand", FontWeight.BOLD, 12));
            badge.setTextFill(Color.WHITE);
            badge.setStyle("-fx-background-color: #ff5252; -fx-background-radius: 10; -fx-padding: 2 10; -fx-effect: dropshadow(gaussian, #ff5252, 4, 0.2, 0, 1);");
            badgeBox.getChildren().add(badge);
        }
        card.getChildren().addAll(icon, titleLabel, subtitleLabel, badgeBox);
        card.setOnMouseClicked(e -> onClick.run());
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-scale-x:1.08;-fx-scale-y:1.08;-fx-effect: dropshadow(gaussian, #0288d1, 32, 0.3, 0, 10);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + color + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);"));
        return card;
    }

    // Navigation for each card
    private void openModules() {
        // Navigate to the main modules page
        try {
            com.ecoedu.modules.ModulePage.show(primaryStage);
        } catch (Exception e) {
            showComingSoon("Modules");
        }
    }
    private void openQuiz() {
        try {
            com.ecoedu.quiz.QuizPage.show(primaryStage);
        } catch (Exception e) {
            showComingSoon("Quiz & Puzzles");
        }
    }
    private void openAvatar() {
        try {
            com.ecoedu.avatar.AvatarCustomizer avatarCustomizer = new com.ecoedu.avatar.AvatarCustomizer();
            Stage avatarStage = new Stage();
            avatarCustomizer.start(avatarStage);
        } catch (Exception e) {
            showComingSoon("Avatar Customization");
        }
    }
    private void openLeaderboardAndBadges() {
        try {
            com.ecoedu.leaderboard.LeaderboardAndBadgesPage.show(primaryStage);
        } catch (Exception e) {
            showComingSoon("Leaderboard & Badges");
        }
    }
    private void openMinigames() {
        // Navigate to the minigames section (default to TrashSortingGame if no MinigamesPage)
        try {
            com.ecoedu.minigames.TrashSortingGame.show(primaryStage);
        } catch (Exception e) {
            showComingSoon("Minigames");
        }
    }
    private void openMinigamesPage() {
        try {
            com.ecoedu.minigames.MinigamesPage.show(primaryStage);
        } catch (Exception e) {
            showComingSoon("Minigames");
        }
    }
    private void openDaily() {
        try {
            com.ecoedu.dailytasks.DailyChallengePage.show(primaryStage);
        } catch (Exception e) {
            showComingSoon("Daily Challenge");
        }
    }
    // Show a playful alert if a section is not implemented
    private void showComingSoon(String section) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText(null);
        alert.setContentText(section + " section is coming soon!");
        alert.showAndWait();
    }

    public static void show(Stage primaryStage) {
        StudentDashboard dashboard = new StudentDashboard(primaryStage);
        Scene scene = new Scene(dashboard, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Student Dashboard");
        primaryStage.show();
    }
} 