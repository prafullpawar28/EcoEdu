package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ThemeSelectorDialog {
    public static void show(Stage owner) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Change Theme");
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        ToggleGroup group = new ToggleGroup();
        ToggleButton lightBtn = new ToggleButton("Light");
        ToggleButton darkBtn = new ToggleButton("Dark");
        ToggleButton ecoBtn = new ToggleButton("Eco");
        lightBtn.setToggleGroup(group);
        darkBtn.setToggleGroup(group);
        ecoBtn.setToggleGroup(group);
        HBox btnBox = new HBox(16, lightBtn, darkBtn, ecoBtn);
        btnBox.setAlignment(Pos.CENTER);
        Button applyBtn = new Button("Apply");
        applyBtn.setOnAction(e -> {
            if (lightBtn.isSelected()) owner.getScene().getRoot().setStyle("-fx-background-color: #fffde7;");
            else if (darkBtn.isSelected()) owner.getScene().getRoot().setStyle("-fx-background-color: #263238;");
            else owner.getScene().getRoot().setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");
            dialog.close();
        });
        root.getChildren().addAll(btnBox, applyBtn);
        Scene scene = new Scene(root, 1366, 768);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
} 