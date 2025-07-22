package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StudentDashboard extends VBox {
    private Stage primaryStage;
    private StudentProfile profile;
    private List<DashboardCard> cards;
    private List<String> quotes;
    private Label welcomeLabel;
    private Label quoteLabel;
    private GridPane cardGrid;
    private int quoteIndex = 0;
    private ScheduledExecutorService scheduler;

    public StudentDashboard(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(18);
        setPadding(new Insets(32, 40, 24, 40));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // --- Expressive Header (Footer at Top) ---
        HBox header = new HBox();
        header.setMinHeight(80);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(18);
        header.setStyle("-fx-background-radius: 0 0 32 32; " +
            "-fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); " +
            "-fx-effect: dropshadow(gaussian, #43e97b, 12, 0.2, 0, 4);");
        // Animated icon (GIF or PNG)
        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/avatar.png")));
        } catch (Exception e) {
            icon.setImage(null);
        }
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        icon.setPreserveRatio(true);
        // Subtle fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(1200), icon);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
        // Welcome text
        Label welcome = new Label("Welcome to Dashboard");
        welcome.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        welcome.setTextFill(Color.web("#fffde7"));
        welcome.setStyle("-fx-effect: dropshadow(gaussian, #388e3c, 8, 0.2, 0, 2);");
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        // Settings icon
        ImageView settingsIcon = new ImageView();
        try {
            settingsIcon.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/settings.png")));
        } catch (Exception e) {
            settingsIcon.setImage(null);
        }
        settingsIcon.setFitHeight(36);
        settingsIcon.setFitWidth(36);
        settingsIcon.setPreserveRatio(true);
        settingsIcon.setStyle("-fx-cursor: hand;");
        settingsIcon.setOnMouseClicked(e -> showSettingsDialog());
        settingsIcon.setOnMouseEntered(e -> settingsIcon.setOpacity(0.7));
        settingsIcon.setOnMouseExited(e -> settingsIcon.setOpacity(1.0));
        header.getChildren().addAll(icon, welcome, headerSpacer, settingsIcon);
        // Add header as the very first node
        getChildren().add(0, header);

        // --- Top Bar with Settings Icon ---
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(0, 0, 10, 0));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        // MenuButton settingsMenu = new MenuButton(); // This line was removed as per the edit hint
        // ImageView gearIcon = new ImageView(); // This line was removed as per the edit hint
        // try { // This block was removed as per the edit hint
        //     gearIcon.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/settings.png"))); // This line was removed as per the edit hint
        // } catch (Exception e) { // This line was removed as per the edit hint
        //     gearIcon.setImage(null); // This line was removed as per the edit hint
        // } // This block was removed as per the edit hint
        // gearIcon.setFitWidth(32); // This line was removed as per the edit hint
        // gearIcon.setFitHeight(32); // This line was removed as per the edit hint
        // settingsMenu.setGraphic(gearIcon); // This line was removed as per the edit hint
        // settingsMenu.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 8 0 8; -fx-background-radius: 16;"); // This line was removed as per the edit hint
        // MenuItem profileItem = new MenuItem("Profile"); // This line was removed as per the edit hint
        // profileItem.setOnAction(e -> StudentProfileDialog.show(primaryStage, profile)); // This line was removed as per the edit hint
        // MenuItem logoutItem = new MenuItem("Logout"); // This line was removed as per the edit hint
        // logoutItem.setOnAction(e -> com.ecoedu.dashboard.StudentLoginPage.show(primaryStage)); // This line was removed as per the edit hint
        // MenuItem themeItem = new MenuItem("Change Theme"); // This line was removed as per the edit hint
        // themeItem.setOnAction(e -> ThemeSelectorDialog.show(primaryStage)); // This line was removed as per the edit hint
        // MenuItem helpItem = new MenuItem("Help"); // This line was removed as per the edit hint
        // helpItem.setOnAction(e -> HelpDialog.show(primaryStage)); // This line was removed as per the edit hint
        // MenuItem soundItem = new MenuItem("Sound"); // This line was removed as per the edit hint
        // soundItem.setOnAction(e -> SoundToggleDialog.show(primaryStage)); // This line was removed as per the edit hint
        // MenuItem parentItem = new MenuItem("Parental Controls"); // This line was removed as per the edit hint
        // parentItem.setOnAction(e -> ParentalControlsDialog.show(primaryStage)); // This line was removed as per the edit hint
        // settingsMenu.getItems().addAll(profileItem, logoutItem, themeItem, helpItem, soundItem, parentItem); // This line was removed as per the edit hint
        topBar.getChildren().addAll(spacer); // This line was removed as per the edit hint
        getChildren().add(0, topBar);

        // --- Simulated Real-Time Data ---
        profile = new StudentProfile("Eco Kid", "/Assets/Images/avatar.png", "Eco Explorer", 0.65, 4);
        cards = new ArrayList<>();
        cards.add(new DashboardCard("\uD83D\uDCDA Modules", "3 modules available!", "#81c784", "/Assets/Images/module.png", () -> openSection(() -> com.ecoedu.modules.ModulePage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83E\uDDE9 Quiz & Puzzles", "2 quizzes pending!", "#ffd54f", "/Assets/Images/quiz.png", () -> openSection(() -> com.ecoedu.quiz.QuizHomePage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83E\uDDD1\u200D\uD83C\uDFA8 Avatar Customization", "Style your eco hero!", "#4fc3f7", "/Assets/Images/avatar.png", () -> openSection(() -> {
            com.ecoedu.avatar.AvatarCustomizer avatarCustomizer = new com.ecoedu.avatar.AvatarCustomizer();
            Scene scene = new Scene(avatarCustomizer, 1366, 768);
            primaryStage.setScene(scene);
            primaryStage.setTitle("EcoEdu - Avatar Customization");
        })));
        cards.add(new DashboardCard("\uD83C\uDFC6 Leaderboard & Badges", "4 badges earned!", "#ffd54f", "/Assets/Images/leaderboard.png", () -> openSection(() -> com.ecoedu.leaderboard.LeaderboardAndBadgesPage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83C\uDFAE Minigames", "1 new minigame!", "#ff8a65", "/Assets/Images/minigames.png", () -> openSection(() -> com.ecoedu.minigames.MinigamesPage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83C\uDF31 Daily Challenge", "New eco tasks!", "#a1887f", "/Assets/Images/daily.png", () -> openSection(() -> com.ecoedu.dailytasks.DailyChallengePage.show(primaryStage))));
        quotes = Arrays.asList(
            "The Earth is what we all have in common. – Wendell Berry",
            "Small eco-actions can transform the world.",
            "Be the change you wish to see in the world. – Gandhi",
            "Every small eco-action counts!"
        );

        // --- Animated Quote Bar ---
        quoteLabel = new Label(quotes.get(0));
        quoteLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        quoteLabel.setTextFill(Color.web("#388e3c"));
        quoteLabel.setStyle("-fx-background-color: linear-gradient(to right, #b2ff59, #81d4fa); -fx-background-radius: 16; -fx-padding: 10 32; -fx-effect: dropshadow(gaussian, #b2ff59, 8, 0.1, 0, 2);");
        quoteLabel.setOpacity(0);
        getChildren().add(quoteLabel);
        FadeTransition fadeInQuote = new FadeTransition(Duration.seconds(1.2), quoteLabel);
        fadeInQuote.setFromValue(0);
        fadeInQuote.setToValue(1);
        fadeInQuote.play();

        // --- Card Grid with Scroll ---
        cardGrid = new GridPane();
        cardGrid.setHgap(40);
        cardGrid.setVgap(40);
        cardGrid.setAlignment(Pos.TOP_CENTER);
        updateCardGrid();
        ScrollPane scrollPane = new ScrollPane(cardGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        getChildren().add(scrollPane);

        // --- Real-Time Data Simulation ---
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::simulateRealTimeUpdates, 6, 6, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::rotateQuote, 6, 6, TimeUnit.SECONDS);
    }

    private void updateCardGrid() {
        Platform.runLater(() -> {
            cardGrid.getChildren().clear();
            for (int i = 0; i < cards.size(); i++) {
                DashboardCard card = cards.get(i);
                VBox cardBox = makeCard(card);
                cardGrid.add(cardBox, i % 2, i / 2);
            }
        });
    }

    private VBox makeCard(DashboardCard card) {
        VBox cardBox = new VBox(10);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPrefSize(340, 220);
        cardBox.setStyle("-fx-background-color: linear-gradient(to bottom right, " + card.getColor() + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);");
        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(card.getIconPath())));
        } catch (Exception e) {
            icon.setImage(null);
        }
        icon.setFitWidth(72);
        icon.setFitHeight(72);
        icon.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 36; -fx-border-radius: 36; -fx-border-color: #fff; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, #fffde7, 8, 0.2, 0, 2);");
        Label titleLabel = new Label(card.getTitle());
        titleLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#263238"));
        titleLabel.setStyle("-fx-font-weight: bold;");
        Label subtitleLabel = new Label(card.getSubtitle());
        subtitleLabel.setFont(Font.font("Quicksand", 14));
        subtitleLabel.setTextFill(Color.web("#424242"));
        cardBox.getChildren().addAll(icon, titleLabel, subtitleLabel);
        cardBox.setOnMouseClicked(e -> {
            try {
                card.getOnClick().run();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Coming Soon");
                alert.setHeaderText(null);
                alert.setContentText("This section is coming soon!");
                alert.showAndWait();
            }
        });
        cardBox.setOnMouseEntered(e -> cardBox.setStyle("-fx-background-color: linear-gradient(to bottom right, " + card.getColor() + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-scale-x:1.08;-fx-scale-y:1.08;-fx-effect: dropshadow(gaussian, #0288d1, 32, 0.3, 0, 10);"));
        cardBox.setOnMouseExited(e -> cardBox.setStyle("-fx-background-color: linear-gradient(to bottom right, " + card.getColor() + ", #fffde7 80%); -fx-background-radius: 28; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #bdbdbd, 16, 0.2, 0, 4);"));
        return cardBox;
    }

    private void rotateQuote() {
        Platform.runLater(() -> {
            quoteIndex = (quoteIndex + 1) % quotes.size();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), quoteLabel);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                quoteLabel.setText(quotes.get(quoteIndex));
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), quoteLabel);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
            });
            fadeOut.play();
        });
    }

    private void simulateRealTimeUpdates() {
        // Simulate real-time updates (e.g., new modules, quizzes, etc.)
        Platform.runLater(() -> {
            int modules = 3 + (int)(Math.random() * 3);
            int quizzes = 2 + (int)(Math.random() * 2);
            int badges = 4 + (int)(Math.random() * 2);
            cards.get(0).setSubtitle(modules + " modules available!");
            cards.get(1).setSubtitle(quizzes + " quizzes pending!");
            cards.get(3).setSubtitle(badges + " badges earned!");
            updateCardGrid();
        });
    }

    private void openSection(Runnable section) {
        try {
            section.run();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Coming Soon");
            alert.setHeaderText(null);
            alert.setContentText("This section is coming soon!");
            alert.showAndWait();
        }
    }

    // Show settings dialog or message (stub)
    private void showSettingsDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings");
        alert.setHeaderText(null);
        alert.setContentText("Settings dialog coming soon!");
        alert.showAndWait();
    }

    // Data classes
    public static class StudentProfile {
        private String name;
        private String avatarPath;
        private String ecoLevel;
        private double progress;
        private int badges;
        public StudentProfile(String name, String avatarPath, String ecoLevel, double progress, int badges) {
            this.name = name;
            this.avatarPath = avatarPath;
            this.ecoLevel = ecoLevel;
            this.progress = progress;
            this.badges = badges;
        }
        public String getName() { return name; }
        public String getAvatarPath() { return avatarPath; }
        public String getEcoLevel() { return ecoLevel; }
        public double getProgress() { return progress; }
        public int getBadges() { return badges; }
    }
    public static class DashboardCard {
        private String title;
        private String subtitle;
        private String color;
        private String iconPath;
        private Runnable onClick;
        public DashboardCard(String title, String subtitle, String color, String iconPath, Runnable onClick) {
            this.title = title;
            this.subtitle = subtitle;
            this.color = color;
            this.iconPath = iconPath;
            this.onClick = onClick;
        }
        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
        public String getColor() { return color; }
        public String getIconPath() { return iconPath; }
        public Runnable getOnClick() { return onClick; }
    }

    public static void show(Stage primaryStage) {
        // Mock providers for demonstration
        StudentProfile profile = new StudentProfile("Eco Kid", "/Assets/Images/avatar.png", "Eco Explorer", 0.65, 4);
        List<DashboardCard> cards = List.of(
            new DashboardCard("\uD83D\uDCDA Modules", "3 new modules!", "#81c784", "/Assets/Images/module.png", () -> {
                try {
                    com.ecoedu.modules.ModulePage.show(primaryStage);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Coming Soon");
                    alert.setHeaderText(null);
                    alert.setContentText("This section is coming soon!");
                    alert.showAndWait();
                }
            }),
            new DashboardCard("\uD83E\uDDE9 Quiz & Puzzles", "2 quizzes pending!", "#ffd54f", "/Assets/Images/quiz.png", () -> {
                try {
                    com.ecoedu.quiz.QuizPage.show(primaryStage, null);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Coming Soon");
                    alert.setHeaderText(null);
                    alert.setContentText("This section is coming soon!");
                    alert.showAndWait();
                }
            }),
            new DashboardCard("\uD83E\uDDD1\u200D\uD83C\uDFA8 Avatar Customization", "Style your eco hero!", "#4fc3f7", "/Assets/Images/avatar.png", () -> {
                try {
                    com.ecoedu.avatar.AvatarCustomizer avatarCustomizer = new com.ecoedu.avatar.AvatarCustomizer();
                    Stage avatarStage = new Stage();
                    avatarCustomizer.start(avatarStage);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Coming Soon");
                    alert.setHeaderText(null);
                    alert.setContentText("This section is coming soon!");
                    alert.showAndWait();
                }
            }),
            new DashboardCard("\uD83C\uDFC6 Leaderboard & Badges", "4 badges earned!", "#ffd54f", "/Assets/Images/leaderboard.png", () -> {
                try {
                    com.ecoedu.leaderboard.LeaderboardAndBadgesPage.show(primaryStage);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Coming Soon");
                    alert.setHeaderText(null);
                    alert.setContentText("This section is coming soon!");
                    alert.showAndWait();
                }
            }),
            new DashboardCard("\uD83C\uDFAE Minigames", "1 new minigame!", "#ff8a65", "/Assets/Images/minigames.png", () -> {
                try {
                    com.ecoedu.minigames.TrashSortingGame.show(primaryStage);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Coming Soon");
                    alert.setHeaderText(null);
                    alert.setContentText("This section is coming soon!");
                    alert.showAndWait();
                }
            }),
            new DashboardCard("\uD83C\uDF31 Daily Challenge", "New eco tasks!", "#a1887f", "/Assets/Images/daily.png", () -> {
                try {
                    com.ecoedu.dailytasks.DailyChallengePage.show(primaryStage);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Coming Soon");
                    alert.setHeaderText(null);
                    alert.setContentText("This section is coming soon!");
                    alert.showAndWait();
                }
            })
        );
        List<String> quotes = Arrays.asList(
            "The Earth is what we all have in common. – Wendell Berry",
            "Small acts, when multiplied, can transform the world.",
            "Be the change you wish to see in the world. – Gandhi",
            "Every small eco-action counts!"
        );

        StudentDashboard dashboard = new StudentDashboard(primaryStage);
        Scene scene = new Scene(dashboard, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Student Dashboard");
        primaryStage.show();
    }
} 