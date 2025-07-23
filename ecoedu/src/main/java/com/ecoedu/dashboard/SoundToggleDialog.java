package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SoundToggleDialog {
    private static boolean soundOn = true;
    public static void show(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Sound & Music");
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        ToggleButton toggle = new ToggleButton(soundOn ? "🔊 Sound On" : "🔇 Sound Off");
        toggle.setSelected(soundOn);
        toggle.setOnAction(e -> {
            soundOn = toggle.isSelected();
            toggle.setText(soundOn ? "🔊 Sound On" : "🔇 Sound Off");
        });
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #81c784; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> dialog.close());
        root.getChildren().addAll(toggle, closeBtn);
        Scene scene = new Scene(root, 1366, 768);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 