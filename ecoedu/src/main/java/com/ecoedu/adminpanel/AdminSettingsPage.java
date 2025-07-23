package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminSettingsPage extends VBox {
    public AdminSettingsPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("⚙️ Settings");
        title.getStyleClass().add("label-section");
        HBox themeBox = new HBox(16);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        Label themeLabel = new Label("Theme:");
        ToggleButton lightBtn = new ToggleButton("Light");
        ToggleButton darkBtn = new ToggleButton("Dark");
        ToggleButton ecoBtn = new ToggleButton("Eco");
        themeBox.getChildren().addAll(themeLabel, lightBtn, darkBtn, ecoBtn);
        HBox soundBox = new HBox(16);
        soundBox.setAlignment(Pos.CENTER_LEFT);
        Label soundLabel = new Label("Sound:");
        ToggleButton soundToggle = new ToggleButton("On");
        soundToggle.setSelected(true);
        soundToggle.setOnAction(e -> soundToggle.setText(soundToggle.isSelected() ? "On" : "Off"));
        soundBox.getChildren().addAll(soundLabel, soundToggle);
        card.getChildren().addAll(title, themeBox, soundBox);
        getChildren().add(card);
    }
} 