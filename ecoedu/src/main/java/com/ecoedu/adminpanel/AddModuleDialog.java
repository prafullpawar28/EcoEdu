package com.ecoedu.adminpanel;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddModuleDialog extends Dialog<AdminDataService.Module> {
    public AddModuleDialog() {
        setTitle("Add Module");
        getDialogPane().getStyleClass().add("dialog-pane");
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        TextField titleField = new TextField();
        titleField.getStyleClass().add("text-field");
        titleField.setPromptText("Title");
        TextArea descField = new TextArea();
        descField.getStyleClass().add("text-field");
        descField.setPromptText("Description");
        descField.setPrefRowCount(3);
        box.getChildren().addAll(new Label("Title:"), titleField, new Label("Description:"), descField);
        getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        setResultConverter(btn -> {
            if (btn == okType) {
                return new AdminDataService.Module(titleField.getText(), descField.getText());
            }
            return null;
        });
    }
} 