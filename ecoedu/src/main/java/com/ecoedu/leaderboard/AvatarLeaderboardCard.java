package com.ecoedu.leaderboard;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.List;

public class AvatarLeaderboardCard extends StackPane {
    public AvatarLeaderboardCard(int rank, String name, int score, int quizzes, int minigames, List<String> badges, boolean isCurrentUser, String avatarPath) {
        setPadding(new Insets(0));
        setAlignment(Pos.CENTER);
        // Card background with gradient and glow for top 3
        String bg;
        if (rank == 1) bg = "linear-gradient(to bottom, #ffe082 60%, #fffde7 100%)";
        else if (rank == 2) bg = "linear-gradient(to bottom, #b0bec5 60%, #eceff1 100%)";
        else if (rank == 3) bg = "linear-gradient(to bottom, #ffb300 60%, #fffde7 100%)";
        else bg = "linear-gradient(to bottom, #e0f7fa 60%, #b2dfdb 100%)";
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18, 24, 18, 24));
        card.setMinWidth(260);
        card.setMaxWidth(320);
        card.setMinHeight(180);
        card.setMaxHeight(220);
        card.setStyle("-fx-background-radius: 22; -fx-background-color: " + bg + "; -fx-effect: dropshadow(gaussian, #43e97b, 12, 0.18, 0, 4);");
        if (isCurrentUser) {
            card.setStyle(card.getStyle() + "-fx-border-color: #43e97b; -fx-border-width: 3; -fx-border-radius: 22; ");
            DropShadow glow = new DropShadow(18, Color.web("#43e97b", 0.5));
            card.setEffect(glow);
        }
        // Rank
        Label rankLabel = new Label("#" + rank);
        rankLabel.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 26));
        rankLabel.setTextFill(Color.web("#ffd600"));
        // Avatar
        ImageView avatar;
        if (avatarPath != null && !avatarPath.isEmpty()) {
            avatar = new ImageView(new Image(getClass().getResourceAsStream(avatarPath)));
        } else {
            avatar = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/avatar.png")));
        }
        avatar.setFitHeight(54);
        avatar.setFitWidth(54);
        avatar.setClip(new Circle(27, 27, 27));
        // Name
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        nameLabel.setTextFill(Color.web("#263238"));
        // Score
        Label scoreLabel = new Label("Score: " + score);
        scoreLabel.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 24));
        scoreLabel.setTextFill(Color.web("#43a047"));
        // XP/Progress bar (simulate progress)
        ProgressBar xpBar = new ProgressBar(Math.min(1.0, (score % 100) / 100.0 + 0.1));
        xpBar.setPrefWidth(160);
        xpBar.setStyle("-fx-accent: #43e97b; -fx-background-radius: 8; -fx-border-radius: 8;");
        // Badges and stats
        HBox statsBox = new HBox(12);
        statsBox.setAlignment(Pos.CENTER);
        Label quizLabel = new Label("📝 " + quizzes);
        quizLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        quizLabel.setTextFill(Color.web("#00bcd4"));
        Label minigameLabel = new Label("🎮 " + minigames);
        minigameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        minigameLabel.setTextFill(Color.web("#ffd54f"));
        HBox badgeIcons = new HBox(2);
        for (String badge : badges) {
            ImageView badgeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/badge.png")));
            badgeIcon.setFitHeight(22);
            badgeIcon.setFitWidth(22);
            badgeIcons.getChildren().add(badgeIcon);
        }
        statsBox.getChildren().addAll(quizLabel, minigameLabel, badgeIcons);
        // Challenge button
        Button challengeBtn = new Button("Challenge");
        challengeBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
        challengeBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-background-radius: 14; -fx-padding: 4 18; -fx-cursor: hand;");
        // Tooltip for badges
        if (!badges.isEmpty()) {
            challengeBtn.setTooltip(new javafx.scene.control.Tooltip("Challenge this user!"));
            badgeIcons.setOnMouseEntered(e -> badgeIcons.setOpacity(0.7));
            badgeIcons.setOnMouseExited(e -> badgeIcons.setOpacity(1.0));
        }
        // Hover effect
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.06);
            st.setToY(1.06);
            st.play();
            card.setStyle(card.getStyle() + "-fx-effect: dropshadow(gaussian, #43e97b, 32, 0.28, 0, 8);");
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            card.setStyle("-fx-background-radius: 22; -fx-background-color: " + bg + "; -fx-effect: dropshadow(gaussian, #43e97b, 12, 0.18, 0, 4);" + (isCurrentUser ? "-fx-border-color: #43e97b; -fx-border-width: 3; -fx-border-radius: 22; " : ""));
        });
        // Animated spark on score hover
        scoreLabel.setOnMouseEntered(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(400), scoreLabel);
            ft.setFromValue(1.0);
            ft.setToValue(0.5);
            ft.setAutoReverse(true);
            ft.setCycleCount(2);
            ft.play();
        });
        // Layout
        card.getChildren().addAll(rankLabel, avatar, nameLabel, scoreLabel, xpBar, statsBox, challengeBtn);
        getChildren().add(card);
    }
} 