package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersPage extends VBox {
    private final ObservableList<AdminDataService.User> users;
    private final ListView<AdminDataService.User> userListView;
    private List<AdminDataService.User> allUsers;

    public AdminUsersPage() {
        setSpacing(24);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("👤 User Management");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#6a1b9a"));

        allUsers = new ArrayList<>(AdminDataService.getInstance().getUsers());
        users = FXCollections.observableArrayList(allUsers);
        userListView = new ListView<>(users);
        userListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox v = new VBox(
                        new Label(user.name + " (" + user.role + ")"),
                        new Label(user.email)
                    );
                    v.setSpacing(2);
                    setGraphic(v);
                }
            }
        });
        userListView.setPrefHeight(320);

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("Add User");
        Button editBtn = new Button("Edit User");
        Button removeBtn = new Button("Remove User");
        addBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        editBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        removeBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        btnBox.getChildren().addAll(addBtn, editBtn, removeBtn);

        addBtn.setOnAction(e -> showUserDialog(null));
        editBtn.setOnAction(e -> {
            AdminDataService.User selected = userListView.getSelectionModel().getSelectedItem();
            if (selected != null) showUserDialog(selected);
        });
        removeBtn.setOnAction(e -> {
            AdminDataService.User selected = userListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AdminDataService.getInstance().removeUser(selected);
                allUsers = new ArrayList<>(AdminDataService.getInstance().getUsers());
                users.setAll(allUsers);
            }
        });

        getChildren().addAll(title, userListView, btnBox);
    }

    /**
     * Filters the user list by name or email (case-insensitive, substring match).
     */
    public void filterUsers(String query) {
        if (query == null || query.isEmpty()) {
            users.setAll(allUsers);
            return;
        }
        String q = query.toLowerCase();
        users.setAll(allUsers.stream()
            .filter(u -> u.name.toLowerCase().contains(q) || u.email.toLowerCase().contains(q))
            .toList());
    }

    private void showUserDialog(AdminDataService.User user) {
        Dialog<AdminDataService.User> dialog = new Dialog<>();
        dialog.setTitle(user == null ? "Add User" : "Edit User");
        dialog.getDialogPane().setStyle("-fx-background-color: #fffde7;");
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setAlignment(Pos.CENTER_LEFT);
        TextField nameField = new TextField(user == null ? "" : user.name);
        nameField.setPromptText("Name");
        TextField emailField = new TextField(user == null ? "" : user.email);
        emailField.setPromptText("Email");
        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList("Admin", "Teacher", "Student"));
        roleBox.setValue(user == null ? "Student" : user.role);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        if (user != null) passwordField.setText(user.password);
        box.getChildren().addAll(new Label("Name:"), nameField, new Label("Email:"), emailField, new Label("Role:"), roleBox,
            new Label("Password:"), passwordField);
        dialog.getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn == okType) {
                return new AdminDataService.User(nameField.getText(), emailField.getText(), roleBox.getValue(), passwordField.getText());
            }
            return null;
        });
        dialog.showAndWait().ifPresent(result -> {
            if (user == null) {
                AdminDataService.getInstance().addUser(result);
            } else {
                user.name = result.name;
                user.email = result.email;
                user.role = result.role;
                user.password = result.password;
                AdminDataService.getInstance().updateUser(user);
            }
            allUsers = new ArrayList<>(AdminDataService.getInstance().getUsers());
            users.setAll(allUsers);
        });
    }
} 