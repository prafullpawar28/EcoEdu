package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.text.SimpleDateFormat;

public class AdminLogsPage extends VBox {
    public AdminLogsPage() {
        setSpacing(24);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("🗒 Activity Logs");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#d32f2f"));

        ObservableList<AdminDataService.LogEntry> logs = FXCollections.observableArrayList(AdminDataService.getInstance().getLogs());
        ListView<AdminDataService.LogEntry> logListView = new ListView<>(logs);
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
        logListView.setPrefHeight(320);
        getChildren().addAll(title, logListView);
    }
} 