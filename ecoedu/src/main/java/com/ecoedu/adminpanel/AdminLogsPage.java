package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.text.SimpleDateFormat;

public class AdminLogsPage extends VBox {
    public AdminLogsPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("🗒 Activity Logs");
        title.getStyleClass().add("label-section");
        ObservableList<AdminDataService.LogEntry> logs = FXCollections.observableArrayList(AdminDataService.getInstance().getLogs());
        ListView<AdminDataService.LogEntry> logListView = new ListView<>(logs);
        logListView.getStyleClass().add("top-list");
        logListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.LogEntry log, boolean empty) {
                super.updateItem(log, empty);
                if (empty || log == null) {
                    setText(null);
                } else {
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(log.date);
                    setText("[" + date + "] " + log.message);
                }
            }
        });
        logListView.setPrefHeight(340);
        card.getChildren().addAll(title, logListView);
        getChildren().add(card);
    }
} 