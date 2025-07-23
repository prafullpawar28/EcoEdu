package com.ecoedu.dashboard;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentProfileDialog {
    // Minimal StudentProfile data class
    public static class StudentProfile {
        private String name;
        private String avatarPath;
        private String ecoLevel;
        private int badges;
        public StudentProfile(String name, String avatarPath, String ecoLevel, int badges) {
            this.name = name;
            this.avatarPath = avatarPath;
            this.ecoLevel = ecoLevel;
            this.badges = badges;
        }
        public String getName() { return name; }
        public String getAvatarPath() { return avatarPath; }
        public String getEcoLevel() { return ecoLevel; }
        public int getBadges() { return badges; }
    }

    public static void show(Stage owner, StudentProfile profile) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Profile");
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%); -fx-background-radius: 24; -fx-effect: dropshadow(gaussian, #81c784, 16, 0.2, 0, 4);");
        ImageView avatarImg = new ImageView();
        try {
            avatarImg.setImage(new Image(StudentProfileDialog.class.getResourceAsStream(profile.getAvatarPath())));
        } catch (Exception e) {
            avatarImg.setImage(null);
        }
        avatarImg.setFitWidth(80);
        avatarImg.setFitHeight(80);
        avatarImg.setStyle("-fx-background-radius: 40; -fx-border-radius: 40; -fx-border-color: #0288d1; -fx-border-width: 3;");
        Label nameLabel = new Label(profile.getName());
        nameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        nameLabel.setTextFill(Color.web("#0288d1"));
        Label levelLabel = new Label("Eco Level: " + profile.getEcoLevel() + " 🌿");
        levelLabel.setFont(Font.font("Quicksand", 16));
        levelLabel.setTextFill(Color.web("#388e3c"));
        HBox badgesBox = new HBox(4);
        badgesBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < profile.getBadges(); i++) {
            Label badge = new Label("\ud83c\udfc5");
            badge.setFont(Font.font(22));
            badgesBox.getChildren().add(badge);
        }
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #81c784; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> dialog.close());
        root.getChildren().addAll(avatarImg, nameLabel, levelLabel, badgesBox, closeBtn);
        Scene scene = new Scene(root, 1366, 768);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 