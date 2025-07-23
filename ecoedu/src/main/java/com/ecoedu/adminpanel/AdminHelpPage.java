package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminHelpPage extends VBox {
    public AdminHelpPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("❓ Help & About");
        title.getStyleClass().add("label-section");
        Label info = new Label("Welcome to the EcoEdu Admin Panel!\n\nHere you can manage users, modules, quizzes, and view analytics.\n\nFor support, contact: support@ecoedu.com\n\nTip: Use the sidebar to navigate between sections.");
        info.setStyle("-fx-font-size: 16; -fx-text-fill: #333;");
        info.setWrapText(true);
        card.getChildren().addAll(title, info);
        getChildren().add(card);
    }
} 