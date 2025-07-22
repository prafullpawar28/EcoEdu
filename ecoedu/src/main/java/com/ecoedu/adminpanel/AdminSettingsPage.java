package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminSettingsPage extends VBox {
    public AdminSettingsPage() {
        setSpacing(24);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("⚙️ Settings");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#6a1b9a"));

        HBox themeBox = new HBox(12);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        Label themeLabel = new Label("Theme:");
        ToggleButton lightBtn = new ToggleButton("Light");
        ToggleButton darkBtn = new ToggleButton("Dark");
        ToggleButton ecoBtn = new ToggleButton("Eco");
        themeBox.getChildren().addAll(themeLabel, lightBtn, darkBtn, ecoBtn);

        HBox soundBox = new HBox(12);
        soundBox.setAlignment(Pos.CENTER_LEFT);
        Label soundLabel = new Label("Sound:");
        ToggleButton soundToggle = new ToggleButton("On");
        soundToggle.setSelected(true);
        soundToggle.setOnAction(e -> soundToggle.setText(soundToggle.isSelected() ? "On" : "Off"));
        soundBox.getChildren().addAll(soundLabel, soundToggle);

        getChildren().addAll(title, themeBox, soundBox);
    }
} 