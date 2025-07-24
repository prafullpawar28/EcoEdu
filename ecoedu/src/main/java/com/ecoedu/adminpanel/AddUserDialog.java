package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddUserDialog extends Dialog<AdminDataService.User> {
    public AddUserDialog() {
        setTitle("Add User");
        getDialogPane().getStyleClass().add("dialog-pane");
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        TextField nameField = new TextField();
        nameField.getStyleClass().add("text-field");
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.getStyleClass().add("text-field");
        emailField.setPromptText("Email");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getStyleClass().add("combo-box");
        roleBox.getItems().addAll("Admin", "Teacher", "Student");
        roleBox.setValue("Student");
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPromptText("Password");
        box.getChildren().addAll(new Label("Name:"), nameField, new Label("Email:"), emailField, new Label("Role:"), roleBox, new Label("Password:"), passwordField);
        getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        setResultConverter(btn -> {
            if (btn == okType) {
                return new AdminDataService.User(nameField.getText(), emailField.getText(), roleBox.getValue(), passwordField.getText());
            }
            return null;
        });
    }
} 