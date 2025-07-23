package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpDialog {
    public static void show(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Help & About");
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        Label title = new Label("🌱 EcoEdu Help & About");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #388e3c;");
        Label info = new Label("Welcome to EcoEdu!\n\nLearn, play, and save the planet.\n\nFun Eco Fact: Did you know trees can communicate with each other through their roots? 🌳");
        info.setStyle("-fx-font-size: 15px; -fx-text-fill: #0288d1;");
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #81c784; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> dialog.close());
        root.getChildren().addAll(title, info, closeBtn);
        Scene scene = new Scene(root, 1366, 768);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 