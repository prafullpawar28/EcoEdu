package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class AddUserDialog extends Dialog<AdminDataService.User> {
    public AddUserDialog() {
        setTitle("Add User");
        getDialogPane().getStyleClass().add("dialog-pane");
        
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        TextField nameField = new TextField();
        nameField.getStyleClass().add("text-field");
        nameField.setPromptText("Full Name");
        
        TextField emailField = new TextField();
        emailField.getStyleClass().add("text-field");
        emailField.setPromptText("Email Address");
        
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getStyleClass().add("combo-box");
        roleBox.getItems().addAll("Admin", "Teacher", "Student");
        roleBox.setValue("Student");
        roleBox.setPromptText("Select Role");
        
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getStyleClass().add("combo-box");
        statusBox.getItems().addAll("Active", "Inactive");
        statusBox.setValue("Active");
        statusBox.setPromptText("Select Status");
        
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPromptText("Password");
        
        box.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Email:"), emailField,
            new Label("Role:"), roleBox,
            new Label("Status:"), statusBox,
            new Label("Password:"), passwordField
        );
        
        getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        
        // Disable save button if required fields are empty
        Node saveButton = getDialogPane().lookupButton(okType);
        saveButton.setDisable(nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty());
        
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty() || emailField.getText().trim().isEmpty());
        });
        
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty() || nameField.getText().trim().isEmpty());
        });
        
        setResultConverter(btn -> {
            if (btn == okType) {
                if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Name and email are required fields.");
                    alert.showAndWait();
                    return null;
                }
                
                return new AdminDataService.User(
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    roleBox.getValue(),
                    passwordField.getText(),
                    statusBox.getValue(),
                    java.time.LocalDateTime.now()
                );
            }
            return null;
        });
    }
} 