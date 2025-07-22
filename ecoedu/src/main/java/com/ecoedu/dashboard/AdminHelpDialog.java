package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminHelpDialog {
    public static void show(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Admin Help & About");
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        Label title = new Label("🛠️ EcoEdu Admin Help & About");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #6a1b9a;");
        Label info = new Label("Welcome, Admin!\n\nHere you can manage users, modules, and view analytics.\n\nTip: Use strong passwords and log out when done.");
        info.setStyle("-fx-font-size: 15px; -fx-text-fill: #0288d1;");
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #81c784; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> dialog.close());
        root.getChildren().addAll(title, info, closeBtn);
        Scene scene = new Scene(root, 360, 220);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 