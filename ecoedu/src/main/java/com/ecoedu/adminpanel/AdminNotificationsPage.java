package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class AdminNotificationsPage extends VBox {
    public AdminNotificationsPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("🔔 Notifications");
        title.getStyleClass().add("label-section");
        ObservableList<String> notifications = FXCollections.observableArrayList(AdminDataService.getInstance().getNotifications());
        ListView<String> notifListView = new ListView<>(notifications);
        notifListView.getStyleClass().add("top-list");
        notifListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String notif, boolean empty) {
                super.updateItem(notif, empty);
                setText(empty || notif == null ? null : notif);
            }
        });
        notifListView.setPrefHeight(340);
        card.getChildren().addAll(title, notifListView);
        getChildren().add(card);
    }
} 