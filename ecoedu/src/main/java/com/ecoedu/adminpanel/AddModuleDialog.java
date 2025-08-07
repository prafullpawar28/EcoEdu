package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class AddModuleDialog extends Dialog<AdminDataService.Module> {
    public AddModuleDialog() {
        setTitle("Add Module");
        getDialogPane().getStyleClass().add("dialog-pane");
        
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        TextField titleField = new TextField();
        titleField.getStyleClass().add("text-field");
        titleField.setPromptText("Module Title");
        
        TextArea descField = new TextArea();
        descField.getStyleClass().add("text-field");
        descField.setPromptText("Module Description");
        descField.setPrefRowCount(3);
        
        ComboBox<String> difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll("Beginner", "Intermediate", "Advanced");
        difficultyBox.getStyleClass().add("combo-box");
        difficultyBox.setValue("Beginner");
        difficultyBox.setPromptText("Select Difficulty");
        
        Spinner<Integer> durationSpinner = new Spinner<>(15, 180, 45, 15);
        durationSpinner.getStyleClass().add("spinner");
        durationSpinner.setEditable(true);
        durationSpinner.setPromptText("Duration in minutes");
        
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Active", "Inactive");
        statusBox.getStyleClass().add("combo-box");
        statusBox.setValue("Active");
        statusBox.setPromptText("Select Status");
        
        box.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Description:"), descField,
            new Label("Difficulty:"), difficultyBox,
            new Label("Duration (minutes):"), durationSpinner,
            new Label("Status:"), statusBox
        );
        
        getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        
        // Disable save button if required fields are empty
        Node saveButton = getDialogPane().lookupButton(okType);
        saveButton.setDisable(titleField.getText().trim().isEmpty());
        
        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty());
        });
        
        setResultConverter(btn -> {
            if (btn == okType) {
                if (titleField.getText().trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Validation Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Module title cannot be empty.");
                    alert.showAndWait();
                    return null;
                }
                
                return new AdminDataService.Module(
                    titleField.getText().trim(),
                    descField.getText().trim(),
                    difficultyBox.getValue(),
                    durationSpinner.getValue(),
                    statusBox.getValue()
                );
            }
            return null;
        });
    }
} 