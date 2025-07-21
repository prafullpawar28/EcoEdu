package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsView extends VBox {
    public SettingsView() {
        setSpacing(18);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");
        Label title = new Label("Settings");
        title.getStyleClass().add("eco-title");
        // Role management
        HBox roleBox = new HBox(10);
        roleBox.setAlignment(Pos.CENTER_LEFT);
        Label roleLabel = new Label("Role:");
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton adminBtn = new RadioButton("Admin");
        RadioButton editorBtn = new RadioButton("Editor");
        RadioButton viewerBtn = new RadioButton("Viewer");
        adminBtn.setToggleGroup(roleGroup);
        editorBtn.setToggleGroup(roleGroup);
        viewerBtn.setToggleGroup(roleGroup);
        adminBtn.setSelected(true);
        roleBox.getChildren().addAll(roleLabel, adminBtn, editorBtn, viewerBtn);
        // Theme selection
        HBox themeBox = new HBox(10);
        themeBox.setAlignment(Pos.CENTER_LEFT);
        Label themeLabel = new Label("Theme:");
        ChoiceBox<String> themeChoice = new ChoiceBox<>();
        themeChoice.getItems().addAll("Light", "Dark", "Eco");
        themeChoice.setValue("Eco");
        themeBox.getChildren().addAll(themeLabel, themeChoice);
        // Firebase sync
        Button syncBtn = new Button("Sync with Firebase");
        syncBtn.getStyleClass().add("eco-btn");
        syncBtn.setOnAction(e -> {/* TODO: Sync with Firebase */});
        // Language selector
        HBox langBox = new HBox(10);
        langBox.setAlignment(Pos.CENTER_LEFT);
        Label langLabel = new Label("Language:");
        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("English", "Spanish", "French", "German", "Hindi");
        langCombo.setValue("English");
        langBox.getChildren().addAll(langLabel, langCombo);
        getChildren().addAll(title, roleBox, themeBox, syncBtn, langBox);
    }
} 