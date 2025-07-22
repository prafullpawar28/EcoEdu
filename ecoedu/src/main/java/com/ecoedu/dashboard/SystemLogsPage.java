package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SystemLogsPage extends VBox {
    private TableView<LogRow> logTable;
    private ObservableList<LogRow> logs;
    private Stage primaryStage;

    public SystemLogsPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(20);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("root");
        Label title = new Label("System Logs");
        title.getStyleClass().add("eco-title");
        logTable = new TableView<>();
        logs = FXCollections.observableArrayList(); // Will be filled from Firebase in future
        logTable.setItems(logs);
        TableColumn<LogRow, String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setCellValueFactory(data -> data.getValue().timestampProperty());
        TableColumn<LogRow, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(data -> data.getValue().userProperty());
        TableColumn<LogRow, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellValueFactory(data -> data.getValue().actionProperty());
        TableColumn<LogRow, String> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellValueFactory(data -> data.getValue().detailsProperty());
        logTable.getColumns().addAll(timeCol, userCol, actionCol, detailsCol);
        logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getChildren().addAll(title, logTable);
    }
    // LogRow class for TableView
    public static class LogRow {
        private final javafx.beans.property.SimpleStringProperty timestamp;
        private final javafx.beans.property.SimpleStringProperty user;
        private final javafx.beans.property.SimpleStringProperty action;
        private final javafx.beans.property.SimpleStringProperty details;
        public LogRow(String timestamp, String user, String action, String details) {
            this.timestamp = new javafx.beans.property.SimpleStringProperty(timestamp);
            this.user = new javafx.beans.property.SimpleStringProperty(user);
            this.action = new javafx.beans.property.SimpleStringProperty(action);
            this.details = new javafx.beans.property.SimpleStringProperty(details);
        }
        public javafx.beans.property.StringProperty timestampProperty() { return timestamp; }
        public javafx.beans.property.StringProperty userProperty() { return user; }
        public javafx.beans.property.StringProperty actionProperty() { return action; }
        public javafx.beans.property.StringProperty detailsProperty() { return details; }
    }
    public static void show(Stage primaryStage2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }
} 