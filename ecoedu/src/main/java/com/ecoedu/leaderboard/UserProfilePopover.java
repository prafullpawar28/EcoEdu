package com.ecoedu.leaderboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.List;

public class UserProfilePopover extends Stage {
    public UserProfilePopover(String name, String avatarPath, int level, int xp, List<String> badges, List<String> achievements) {
        setTitle(name + "'s Profile");
        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32, 36, 32, 36));
        root.setStyle("-fx-background-radius: 24; -fx-background-color: linear-gradient(to bottom, #e0f7fa 60%, #fffde7 100%); -fx-effect: dropshadow(gaussian, #43e97b, 24, 0.18, 0, 8);");
        // Avatar
        ImageView avatar;
        try {
            avatar = new ImageView(new Image(getClass().getResourceAsStream(avatarPath)));
            avatar.setFitHeight(80);
            avatar.setFitWidth(80);
            avatar.setClip(new Circle(40, 40, 40));
        } catch (Exception e) {
            // Fallback: colored circle with initials
            avatar = new ImageView();
            javafx.scene.shape.Circle fallback = new javafx.scene.shape.Circle(40, Color.web("#43e97b"));
            Label initials = new Label(name.length() > 0 ? name.substring(0, 1).toUpperCase() : "?");
            initials.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
            initials.setTextFill(Color.WHITE);
            StackPane avatarStack = new StackPane(fallback, initials);
            avatarStack.setPrefSize(80, 80);
            root.getChildren().add(0, avatarStack);
        }
        if (!root.getChildren().contains(avatar)) root.getChildren().add(0, avatar);
        // Name and level
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 28));
        nameLabel.setTextFill(Color.web("#22223b"));
        Label levelLabel = new Label("Level: " + level + "   XP: " + xp);
        levelLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        levelLabel.setTextFill(Color.web("#43a047"));
        // Achievements
        VBox achBox = new VBox(6);
        achBox.setAlignment(Pos.CENTER);
        Label achTitle = new Label("Recent Achievements");
        achTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        achTitle.setTextFill(Color.web("#388e3c"));
        achBox.getChildren().add(achTitle);
        for (String ach : achievements) {
            Label achLabel = new Label("🌱 " + ach);
            achLabel.setFont(Font.font("Quicksand", 14));
            achLabel.setTextFill(Color.web("#263238"));
            achBox.getChildren().add(achLabel);
        }
        // Badge gallery
        FlowPane badgePane = new FlowPane(10, 10);
        badgePane.setAlignment(Pos.CENTER);
        for (String badge : badges) {
            try {
                ImageView badgeIcon = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/badge.png")));
                badgeIcon.setFitHeight(36);
                badgeIcon.setFitWidth(36);
                Tooltip.install(badgeIcon, new Tooltip(badge));
                badgePane.getChildren().add(badgeIcon);
            } catch (Exception e) {
                // Fallback: colored circle with badge initial
                javafx.scene.shape.Circle fallback = new javafx.scene.shape.Circle(18, Color.web("#ffd54f"));
                Label bInit = new Label(badge.length() > 0 ? badge.substring(0, 1).toUpperCase() : "?");
                bInit.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
                bInit.setTextFill(Color.WHITE);
                StackPane badgeStack = new StackPane(fallback, bInit);
                Tooltip.install(badgeStack, new Tooltip(badge));
                badgePane.getChildren().add(badgeStack);
            }
        }
        Label badgeTitle = new Label("Badges");
        badgeTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        badgeTitle.setTextFill(Color.web("#ffd54f"));
        // Send Cheer button
        Button cheerBtn = new Button("Send Cheer 🌟");
        cheerBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        cheerBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        cheerBtn.setOnAction(e -> {
            cheerBtn.setText("Cheered!");
            cheerBtn.setDisable(true);
        });
        // Layout
        root.getChildren().addAll(avatar, nameLabel, levelLabel, achBox, badgeTitle, badgePane, cheerBtn);
        // Robust error handling for popover
        try {
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Could not display user profile: " + ex.getMessage());
            alert.showAndWait();
        }
    }
} 