package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RealTimeUserManagementPage extends VBox {
    private TableView<UserRow> userTable;
    private ObservableList<UserRow> users;
    private Label messageLabel;
    private Stage primaryStage;

    public RealTimeUserManagementPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(20);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("root");
        Label title = new Label("Real-Time User Management");
        title.getStyleClass().add("eco-title");
        userTable = new TableView<>();
        users = FXCollections.observableArrayList(); // Will be filled from Firebase in future
        userTable.setItems(users);
        TableColumn<UserRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        TableColumn<UserRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        TableColumn<UserRow, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> data.getValue().roleProperty());
        userTable.getColumns().addAll(nameCol, emailCol, roleCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Add/Edit/Delete buttons
        Button addBtn = new Button("Add User");
        addBtn.getStyleClass().add("eco-btn");
        addBtn.setOnAction(e -> {/* TODO: Add user dialog and Firebase logic */ messageLabel.setText("Add user (stub)");});
        Button editBtn = new Button("Edit User");
        editBtn.getStyleClass().add("eco-btn");
        editBtn.setOnAction(e -> {/* TODO: Edit user dialog and Firebase logic */ messageLabel.setText("Edit user (stub)");});
        Button deleteBtn = new Button("Delete User");
        deleteBtn.getStyleClass().add("eco-btn");
        deleteBtn.setOnAction(e -> {/* TODO: Delete user and Firebase logic */ messageLabel.setText("Delete user (stub)");});
        HBox btnBox = new HBox(16, addBtn, editBtn, deleteBtn);
        btnBox.setAlignment(Pos.CENTER);
        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");
        getChildren().addAll(title, userTable, btnBox, messageLabel);
    }
    // UserRow class for TableView
    public static class UserRow {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty email;
        private final javafx.beans.property.SimpleStringProperty role;
        public UserRow(String name, String email, String role) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.email = new javafx.beans.property.SimpleStringProperty(email);
            this.role = new javafx.beans.property.SimpleStringProperty(role);
        }
        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty emailProperty() { return email; }
        public javafx.beans.property.StringProperty roleProperty() { return role; }
    }
} 