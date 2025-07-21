package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BadgeManagerView extends VBox {
    public BadgeManagerView() {
        setSpacing(18);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");
        Label title = new Label("Badge Manager");
        title.getStyleClass().add("eco-title");
        // Badge grid
        GridPane badgeGrid = new GridPane();
        badgeGrid.setHgap(18);
        badgeGrid.setVgap(18);
        badgeGrid.setAlignment(Pos.CENTER);
        ObservableList<BadgeRow> badges = FXCollections.observableArrayList(); // loadData() stub
        for (int i = 0; i < badges.size(); i++) {
            BadgeRow badge = badges.get(i);
            VBox badgeBox = new VBox(6);
            badgeBox.setAlignment(Pos.CENTER);
            ImageView img = new ImageView();
            img.setFitWidth(48);
            img.setFitHeight(48);
            // img.setImage(...); // load from badge.getImagePath()
            Label name = new Label(badge.getName());
            name.setStyle("-fx-font-weight: bold; -fx-text-fill: #388E3C;");
            Label xp = new Label(badge.getXp() + " XP");
            badgeBox.getChildren().addAll(img, name, xp);
            badgeGrid.add(badgeBox, i % 4, i / 4);
        }
        // Add badge form
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(18));
        TextField nameField = new TextField();
        nameField.setPromptText("Badge Name");
        TextField xpField = new TextField();
        xpField.setPromptText("XP");
        ComboBox<String> ruleBox = new ComboBox<>();
        ruleBox.getItems().addAll("After 3 games", "After 5 modules", "Perfect quiz score", "Custom rule");
        ruleBox.setPromptText("Rule");
        Button imageBtn = new Button("Pick Image");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Badge Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        imageBtn.setOnAction(e -> {/* TODO: Pick image, update preview */});
        Button addBtn = new Button("Add Badge");
        addBtn.getStyleClass().add("eco-btn");
        addBtn.setOnAction(e -> {/* TODO: Add badge to Firebase */});
        formBox.getChildren().addAll(new Label("Add New Badge:"), nameField, xpField, ruleBox, imageBtn, addBtn);
        getChildren().addAll(title, badgeGrid, formBox);
    }
    // BadgeRow class
    public static class BadgeRow {
        private final String name;
        private final int xp;
        private final String imagePath;
        public BadgeRow(String name, int xp, String imagePath) {
            this.name = name;
            this.xp = xp;
            this.imagePath = imagePath;
        }
        public String getName() { return name; }
        public int getXp() { return xp; }
        public String getImagePath() { return imagePath; }
    }
} 