package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class AdminFeedbackPage extends VBox {
    public AdminFeedbackPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("📬 Feedback");
        title.getStyleClass().add("label-section");
        ObservableList<String> feedbacks = FXCollections.observableArrayList(AdminDataService.getInstance().getFeedbacks());
        ListView<String> feedbackListView = new ListView<>(feedbacks);
        feedbackListView.getStyleClass().add("top-list");
        feedbackListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String feedback, boolean empty) {
                super.updateItem(feedback, empty);
                setText(empty || feedback == null ? null : feedback);
            }
        });
        feedbackListView.setPrefHeight(340);
        card.getChildren().addAll(title, feedbackListView);
        getChildren().add(card);
    }
} 