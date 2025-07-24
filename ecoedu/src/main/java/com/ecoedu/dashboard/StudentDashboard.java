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
import com.ecoedu.modules.ModulePage;
import com.ecoedu.quiz.QuizPage;
import javafx.stage.PopupWindow;
import javafx.util.Pair;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import java.io.InputStream;
import javafx.scene.effect.GaussianBlur;

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
        // Remove background color from dashboard VBox
        setStyle("-fx-background-color: transparent;");

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
            settingsIcon.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/settings (1).png")));
        } catch (Exception e) {
            settingsIcon.setImage(null);
        }
        settingsIcon.setFitHeight(24);
        settingsIcon.setFitWidth(24);
        settingsIcon.setPreserveRatio(true);
        settingsIcon.setStyle("-fx-cursor: hand; -fx-padding: 10; -fx-background-radius: 18; -fx-background-color: rgba(255,255,255,0.08);");
        settingsIcon.setOnMouseEntered(e -> settingsIcon.setOpacity(0.7));
        settingsIcon.setOnMouseExited(e -> settingsIcon.setOpacity(1.0));
        // --- Custom Dropdown Menu using Popup ---
        javafx.stage.Popup settingsMenu = new javafx.stage.Popup();
        VBox menuBox = new VBox(0);
        menuBox.setStyle("-fx-background-radius: 18; -fx-background-color: linear-gradient(to bottom, #e0f7fa 60%, #b2dfdb 100%); -fx-effect: dropshadow(gaussian, #43e97b, 16, 0.18, 0, 4); -fx-padding: 10 0 10 0; -fx-border-radius: 18; -fx-border-color: #43e97b; -fx-border-width: 2;");
        menuBox.setPrefWidth(220);
        String menuItemStyle = "-fx-font-size: 17; -fx-font-family: 'Quicksand'; -fx-background-radius: 14; -fx-padding: 10 24 10 18; -fx-cursor: hand; -fx-text-fill: #388e3c; -fx-alignment: center-left;";
        String menuItemHover = "-fx-background-color: linear-gradient(to right, #b2ff59 0%, #81d4fa 100%); -fx-text-fill: #0288d1;";
        java.util.List<javafx.util.Pair<String, Runnable>> items = java.util.Arrays.asList(
            new javafx.util.Pair<>("\uD83D\uDC64  Profile", () -> StudentProfileDialog.show(primaryStage, new StudentProfileDialog.StudentProfile(
                profile.getName(), profile.getAvatarPath(), profile.getEcoLevel(), profile.getBadges()
            ))),
            new javafx.util.Pair<>("\uD83D\uDD13  Logout", () -> StudentLoginPage.show(primaryStage)),
            new javafx.util.Pair<>("\uD83C\uDF08  Theme", () -> ThemeSelectorDialog.show(primaryStage)),
            new javafx.util.Pair<>("\u2753  Help", () -> HelpDialog.show(primaryStage)),
            new javafx.util.Pair<>("\uD83D\uDD0A  Sound", () -> SoundToggleDialog.show(primaryStage)),
            new javafx.util.Pair<>("\uD83D\uDD12  Parental Controls", () -> ParentalControlsDialog.show(primaryStage)),
            new javafx.util.Pair<>("\u2139\uFE0F  About Us", () -> AboutUs.show(primaryStage))
        );
        for (javafx.util.Pair<String, Runnable> item : items) {
            Label label = new Label(item.getKey());
            label.setStyle(menuItemStyle);
            label.setMinWidth(200);
            label.setOnMouseEntered(ev -> label.setStyle(menuItemStyle + menuItemHover));
            label.setOnMouseExited(ev -> label.setStyle(menuItemStyle));
            label.setOnMouseClicked(ev -> {
                settingsMenu.hide();
                item.getValue().run();
            });
            menuBox.getChildren().add(label);
        }
        javafx.scene.layout.StackPane menuRoot = new javafx.scene.layout.StackPane(menuBox);
        menuRoot.setStyle("-fx-background-radius: 18;");
        menuRoot.setOpacity(0);
        settingsMenu.getContent().clear();
        settingsMenu.getContent().add(menuRoot);
        settingsMenu.setAutoHide(true);
        settingsMenu.setHideOnEscape(true);
        settingsIcon.setOnMouseClicked(e -> {
            if (settingsMenu.isShowing()) {
                settingsMenu.hide();
            } else {
                javafx.geometry.Point2D iconPos = settingsIcon.localToScreen(settingsIcon.getBoundsInLocal().getMaxX(), settingsIcon.getBoundsInLocal().getMaxY());
                settingsMenu.show(settingsIcon, iconPos.getX() - 210, iconPos.getY() + 8);
                // Fade-in animation
                menuRoot.setOpacity(0);
                javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(180), menuRoot);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
        });
        // Restore header to previous state: only add settingsIcon to header (no settingsBox, no aboutUsLabel)
        header.getChildren().addAll(icon, welcome, headerSpacer, settingsIcon);
        // Add header as the very first node
        getChildren().add(0, header);

        // --- Top Bar with Settings Icon ---
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(0, 0, 10, 0));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(spacer); // This line was removed as per the edit hint
        getChildren().add(0, topBar);

        // --- Simulated Real-Time Data ---
        //profile = new StudentProfile("Eco Kid", "/Assets/Images/avatar.png", "Eco Explorer", 0.65, 4);
        cards = new ArrayList<>();
        cards.add(new DashboardCard("\uD83D\uDCDA Modules", "Learn eco topics!", " #81c784", "/Assets/Images/module.png", () -> openSection(() -> com.ecoedu.modules.ModulePage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83E\uDDE9 Quiz & Puzzles", " Test your eco skills!", "rgb(242, 86, 174)", "/Assets/Images/quiz.png", () -> openSection(() -> com.ecoedu.quiz.QuizHomePage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83E\uDDD1\u200D\uD83C\uDFA8 Avatar Customization", "Style your eco hero!", "#4fc3f7", "/Assets/Images/avatar.png", () -> openSection(() -> com.ecoedu.avatar.AvatarCustomizer.show(primaryStage))));
        cards.add(new DashboardCard("\uD83C\uDFC6 Leaderboard & Badges", "See your rank!", "#ffd54f", "/Assets/Images/leaderboard.png", () -> openSection(() -> showLeaderboardWithBack(primaryStage))));
        cards.add(new DashboardCard("\uD83C\uDFAE Minigames", "Play & learn!", "#ff8a65", "/Assets/Images/minigames.png", () -> openSection(() -> com.ecoedu.minigames.MinigamesPage.show(primaryStage))));
        cards.add(new DashboardCard("\uD83C\uDF31 Daily Challenge", "New eco tasks!", "#a1887f", "/Assets/Images/daily.png", () -> openSection(() -> com.ecoedu.dailytasks.DailyChallengePage.show(primaryStage))));
        quotes = Arrays.asList(
            "The Earth is what we all have in common. – Wendell Berry",
            "Small eco-actions can transform the world.",
            "Be the change you wish to see in the world. ",
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
        cardGrid.setHgap(60); // horizontal gap between cards
        cardGrid.setVgap(35); // vertical gap between cards
        cardGrid.setAlignment(Pos.CENTER);
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
        // Gradient background for the card
        String gradient = "linear-gradient(to bottom right, " + card.getColor() + ", #fffde7 90%)";
        cardBox.setStyle("-fx-background-radius: 18; -fx-background-color: " + gradient + "; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.12, 0, 2);");
        cardBox.setPadding(new Insets(18, 12, 18, 12));
        cardBox.setMinWidth(350);
        cardBox.setMaxWidth(350);
        cardBox.setMinHeight(180);
        cardBox.setMaxHeight(180);
        // Animated gradient effect inside card
        javafx.scene.shape.Rectangle effectRect = new javafx.scene.shape.Rectangle();
        effectRect.setWidth(200);
        effectRect.setHeight(110);
        effectRect.setArcWidth(28);
        effectRect.setArcHeight(28);
        effectRect.setMouseTransparent(true);
        effectRect.setManaged(false);
        effectRect.setFill(new javafx.scene.paint.LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
            new javafx.scene.paint.Stop(0, javafx.scene.paint.Color.web("#ffffff33")),
            new javafx.scene.paint.Stop(1, javafx.scene.paint.Color.TRANSPARENT)));
        // Subtle animation: fade in and out
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(2.5), effectRect);
        fade.setFromValue(0.18);
        fade.setToValue(0.38);
        fade.setAutoReverse(true);
        fade.setCycleCount(javafx.animation.Animation.INDEFINITE);
        fade.play();
        // Icon
        ImageView icon = new ImageView();
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(card.getIconPath())));
        } catch (Exception e) {
            icon.setImage(null);
        }
        icon.setFitHeight(36);
        icon.setFitWidth(36);
        icon.setPreserveRatio(true);
        // Title
        Label title = new Label(card.getTitle());
        title.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 32));
        title.setTextFill(Color.web("#22223b"));
        title.setStyle("-fx-effect: dropshadow(gaussian,rgb(35, 35, 34), 8, 0.25, 0, 2);");
        title.setWrapText(true);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(320);
        // Subtitle
        Label subtitle = new Label(card.getSubtitle());
        subtitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        subtitle.setTextFill(Color.web("#3a86ff"));
        subtitle.setStyle("-fx-effect: dropshadow(gaussian,rgb(31, 31, 29), 6, 0.18, 0, 1);");
        subtitle.setWrapText(true);
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setMaxWidth(320);
        VBox textBox = new VBox(8, title, subtitle);
        textBox.setAlignment(Pos.CENTER);
        textBox.setFillWidth(true);
        javafx.scene.layout.StackPane cardContent = new javafx.scene.layout.StackPane(effectRect, new VBox(18, icon, textBox));
        cardContent.setAlignment(Pos.CENTER);
        cardBox.getChildren().clear();
        cardBox.getChildren().add(cardContent);
        cardBox.setOnMouseClicked(e -> card.getOnClick().run());
        cardBox.setOnMouseEntered(e -> {
            cardBox.setStyle("-fx-background-radius: 18; -fx-background-color: linear-gradient(to bottom right, derive(" + card.getColor() + ", 20%), #fffde7 90%); -fx-effect: dropshadow(gaussian, #43e97b, 32, 0.28, 0, 8); -fx-scale-x:1.06;-fx-scale-y:1.06;");
            // Animate the effectRect to shimmer/brighten on hover
            javafx.animation.FadeTransition hoverFade = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(0.3), effectRect);
            hoverFade.setFromValue(effectRect.getOpacity());
            hoverFade.setToValue(0.7);
            hoverFade.setAutoReverse(false);
            hoverFade.setCycleCount(1);
            hoverFade.play();
        });
        cardBox.setOnMouseExited(e -> {
            cardBox.setStyle("-fx-background-radius: 18; -fx-background-color: " + gradient + "; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.12, 0, 2);");
            // Restore the effectRect to normal fade
            javafx.animation.FadeTransition exitFade = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(0.3), effectRect);
            exitFade.setFromValue(effectRect.getOpacity());
            exitFade.setToValue(0.28);
            exitFade.setAutoReverse(false);
            exitFade.setCycleCount(1);
            exitFade.play();
        });
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

    // Helper method to show leaderboard with back navigation
    private void showLeaderboardWithBack(Stage primaryStage) {
        com.ecoedu.leaderboard.LeaderboardAndBadgesPage leaderboardPage = new com.ecoedu.leaderboard.LeaderboardAndBadgesPage();
        // Breadcrumb bar
        HBox breadcrumb = new HBox();
        breadcrumb.setAlignment(Pos.CENTER_LEFT);
        breadcrumb.setSpacing(8);
        Label dash = new Label("Dashboard");
        Label sep = new Label("> ");
        Label lead = new Label("Leaderboard & Badges");
        breadcrumb.getChildren().addAll(dash, sep, lead);
        breadcrumb.setStyle("-fx-font-size: 15; -fx-text-fill: #388e3c; -fx-padding: 0 0 8 0;");
        leaderboardPage.getChildren().add(0, breadcrumb);
        // Back button
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-radius: 16; -fx-background-color: #43e97b; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 32;");
        backBtn.setOnAction(e -> StudentDashboard.show(primaryStage));
        leaderboardPage.getChildren().add(1, backBtn);
        Scene scene = new Scene(leaderboardPage, 1366, 768);
        // Keyboard shortcut for back
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                case B:
                    StudentDashboard.show(primaryStage);
                    break;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard & Badges");
        // Fade-in animation
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(500), leaderboardPage);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
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
        StudentDashboard dashboard = new StudentDashboard(primaryStage);
        // Add background image as the bottom-most node
        ImageView bgImage = new ImageView();
        InputStream is = dashboard.getClass().getResourceAsStream("/Assets/Images/dashboard.jpg");
        System.out.println("StudentDashboard BG image found? " + (is != null));
        try {
            bgImage.setImage(is != null ? new Image(is) : null);
        } catch (Exception e) {
            bgImage.setImage(null);
        }
        bgImage.setOpacity(1.0);
        bgImage.fitWidthProperty().bind(primaryStage.widthProperty());
        bgImage.fitHeightProperty().bind(primaryStage.heightProperty());
        bgImage.setEffect(new GaussianBlur(12));
        StackPane root = new StackPane(bgImage);
        root.setPrefSize(1366, 768);
        root.getChildren().add(dashboard);
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Student Dashboard");
        primaryStage.show();
    }
} 