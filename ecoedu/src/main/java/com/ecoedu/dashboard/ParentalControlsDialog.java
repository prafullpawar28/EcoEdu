package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ParentalControlsDialog {
    public static void show(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Parental Controls");
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        Label info = new Label("Parental controls coming soon!\n\nHere, parents will be able to manage settings and view progress.");
        info.setStyle("-fx-font-size: 15px; -fx-text-fill: #0288d1;");
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #81c784; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> dialog.close());
        root.getChildren().addAll(info, closeBtn);
        Scene scene = new Scene(root, 340, 160);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 