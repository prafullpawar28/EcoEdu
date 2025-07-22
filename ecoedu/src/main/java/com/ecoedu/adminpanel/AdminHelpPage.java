package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminHelpPage extends VBox {
    public AdminHelpPage() {
        setSpacing(24);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("❓ Help & About");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#4fc3f7"));

        Label info = new Label("Welcome to the EcoEdu Admin Panel!\n\nHere you can manage users, modules, quizzes, and view analytics.\n\nFor support, contact: support@ecoedu.com\n\nTip: Use the sidebar to navigate between sections.");
        info.setFont(Font.font("Quicksand", 16));
        info.setTextFill(Color.web("#333"));
        info.setWrapText(true);

        getChildren().addAll(title, info);
    }
} 