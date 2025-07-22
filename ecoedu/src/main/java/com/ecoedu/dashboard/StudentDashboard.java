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
import javafx.scene.control.ProgressBar;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.List;
import java.util.function.Supplier;
import javafx.scene.shape.Circle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import com.ecoedu.dashboard.StudentProfileDialog;
import com.ecoedu.dashboard.ThemeSelectorDialog;
import com.ecoedu.dashboard.HelpDialog;
import com.ecoedu.dashboard.SoundToggleDialog;
import com.ecoedu.dashboard.ParentalControlsDialog;

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

        // --- Top Bar with Settings Icon ---
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(0, 0, 10, 0));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        MenuButton settingsMenu = new MenuButton();
        ImageView gearIcon = new ImageView();
        try {
            gearIcon.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/settings.png")));
        } catch (Exception e) {
            gearIcon.setImage(null);
        }
        gearIcon.setFitWidth(32);
        gearIcon.setFitHeight(32);
        settingsMenu.setGraphic(gearIcon);
        settingsMenu.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0 8 0 8; -fx-background-radius: 16;");
        MenuItem profileItem = new MenuItem("Profile");
        profileItem.setOnAction(e -> StudentProfileDialog.show(primaryStage, profile));
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> com.ecoedu.dashboard.StudentLoginPage.show(primaryStage));
        MenuItem themeItem = new MenuItem("Change Theme");
        themeItem.setOnAction(e -> ThemeSelectorDialog.show(primaryStage));
        MenuItem helpItem = new MenuItem("Help");
        helpItem.setOnAction(e -> HelpDialog.show(primaryStage));
        MenuItem soundItem = new MenuItem("Sound");
        soundItem.setOnAction(e -> SoundToggleDialog.show(primaryStage));
        MenuItem parentItem = new MenuItem("Parental Controls");
        parentItem.setOnAction(e -> ParentalControlsDialog.show(primaryStage));
        settingsMenu.getItems().addAll(profileItem, logoutItem, themeItem, helpItem, soundItem, parentItem);
        topBar.getChildren().addAll(spacer, settingsMenu);
        getChildren().add(0, topBar);

        // --- Simulated Real-Time Data ---
        profile = new StudentProfile("Eco Kid", "/Assets/Images/avatar.png", "Eco Explorer", 0.65, 4);
        cards = new ArrayList<>();
        cards.add(new DashboardCard("\uD83D\uDCDA Modules", "3 modules available!", "#81c784", "/Assets/Images/module.png", () -> openSection(() -> com.ecoedu.modules.ModulePage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83E\uDDE9 Quiz & Puzzles", "2 quizzes pending!", "#ffd54f", "/Assets/Images/quiz.png", () -> openSection(() -> com.ecoedu.quiz.QuizPage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83E\uDDD1\u200D\uD83C\uDFA8 Avatar Customization", "Style your eco hero!", "#4fc3f7", "/Assets/Images/avatar.png", () -> openSection(() -> {
            com.ecoedu.avatar.AvatarCustomizer avatarCustomizer = new com.ecoedu.avatar.AvatarCustomizer();
            Stage avatarStage = new Stage();
            avatarCustomizer.start(avatarStage);
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

        // --- Animated Welcome Message ---
        welcomeLabel = new Label("Welcome, " + profile.getName() + "!");
        welcomeLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.web("#0288d1"));
        welcomeLabel.setPadding(new Insets(0, 0, 18, 0));
        welcomeLabel.setOpacity(0);
        getChildren().add(welcomeLabel);
        FadeTransition fadeInWelcome = new FadeTransition(Duration.seconds(1.2), welcomeLabel);
        fadeInWelcome.setFromValue(0);
        fadeInWelcome.setToValue(1);
        fadeInWelcome.play();

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
                    com.ecoedu.quiz.QuizPage.show(primaryStage);
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
        Scene scene = new Scene(dashboard, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Student Dashboard");
        primaryStage.show();
    }
} 