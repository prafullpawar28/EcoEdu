package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManagerView extends VBox {
    private TableView<UserRow> userTable;
    private ObservableList<UserRow> users;
    public UserManagerView() {
        setSpacing(18);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");
        Label title = new Label("User Manager");
        title.getStyleClass().add("eco-title");
        // Search and filter
        HBox searchBox = new HBox(12);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search users...");
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "Students", "Teachers");
        filterBox.setValue("All");
        searchBox.getChildren().addAll(searchField, filterBox);
        // Table
        userTable = new TableView<>();
        users = FXCollections.observableArrayList(); // loadData() stub
        userTable.setItems(users);
        TableColumn<UserRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        TableColumn<UserRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        TableColumn<UserRow, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> data.getValue().roleProperty());
        TableColumn<UserRow, String> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(data -> data.getValue().activeProperty());
        userTable.getColumns().addAll(nameCol, emailCol, roleCol, activeCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Buttons
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER);
        Button viewBtn = new Button("View");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        Button deactivateBtn = new Button("Deactivate");
        for (Button b : new Button[]{viewBtn, editBtn, deleteBtn, deactivateBtn}) {
            b.getStyleClass().add("eco-btn");
        }
        btnBox.getChildren().addAll(viewBtn, editBtn, deleteBtn, deactivateBtn);
        getChildren().addAll(title, searchBox, userTable, btnBox);
    }
    // UserRow class for TableView
    public static class UserRow {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty email;
        private final javafx.beans.property.SimpleStringProperty role;
        private final javafx.beans.property.SimpleStringProperty active;
        public UserRow(String name, String email, String role, String active) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.email = new javafx.beans.property.SimpleStringProperty(email);
            this.role = new javafx.beans.property.SimpleStringProperty(role);
            this.active = new javafx.beans.property.SimpleStringProperty(active);
        }
        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty emailProperty() { return email; }
        public javafx.beans.property.StringProperty roleProperty() { return role; }
        public javafx.beans.property.StringProperty activeProperty() { return active; }
    }
} 