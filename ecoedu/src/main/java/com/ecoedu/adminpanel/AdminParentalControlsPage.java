package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminParentalControlsPage extends VBox {
    public AdminParentalControlsPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("👪 Parental Controls");
        title.getStyleClass().add("label-section");
        Label info = new Label("Parental controls coming soon!\n\nHere, parents will be able to manage settings and view progress.");
        info.setStyle("-fx-font-size: 15px; -fx-text-fill: #0288d1;");
        card.getChildren().addAll(title, info);
        getChildren().add(card);
    }
} 