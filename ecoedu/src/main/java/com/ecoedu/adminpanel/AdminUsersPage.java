package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("👤 User Management");
        title.getStyleClass().add("label-section");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        allUsers = new ArrayList<>(AdminDataService.getInstance().getUsers());
        users = FXCollections.observableArrayList(allUsers);
        userListView = new ListView<>(users);
        userListView.getStyleClass().add("top-list");
        userListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox(12);
                    row.setAlignment(Pos.CENTER_LEFT);
                    // Avatar (placeholder)
                    ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/avatar.png")));
                    avatar.setFitWidth(32);
                    avatar.setFitHeight(32);
                    avatar.setPreserveRatio(true);
                    VBox info = new VBox(
                        new Label(user.name),
                        new Label(user.email)
                    );
                    info.setSpacing(2);
                    // Role badge
                    Label badge = new Label(user.role);
                    badge.setStyle("-fx-background-color: #e0f7fa; -fx-background-radius: 8; -fx-padding: 2 10; -fx-font-size: 13; -fx-text-fill: #388e3c; -fx-font-weight: bold;");
                    row.getChildren().addAll(avatar, info, badge);
                    setGraphic(row);
                }
            }
        });
        userListView.setPrefHeight(340);

        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("Add User");
        Button editBtn = new Button("Edit User");
        Button removeBtn = new Button("Remove User");
        addBtn.getStyleClass().add("button");
        editBtn.getStyleClass().add("button");
        removeBtn.getStyleClass().add("button");
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
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(Pos.CENTER_LEFT);
        TextField nameField = new TextField(user == null ? "" : user.name);
        nameField.getStyleClass().add("text-field");
        nameField.setPromptText("Name");
        TextField emailField = new TextField(user == null ? "" : user.email);
        emailField.getStyleClass().add("text-field");
        emailField.setPromptText("Email");
        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList("Admin", "Teacher", "Student"));
        roleBox.getStyleClass().add("combo-box");
        roleBox.setValue(user == null ? "Student" : user.role);
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
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