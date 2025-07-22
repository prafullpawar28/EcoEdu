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

public class AdminModulesPage extends VBox {
    private final ObservableList<AdminDataService.Module> modules;
    private final ListView<AdminDataService.Module> moduleListView;

    public AdminModulesPage() {
        setSpacing(24);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("📚 Module Management");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        modules = FXCollections.observableArrayList(AdminDataService.getInstance().getModules());
        moduleListView = new ListView<>(modules);
        moduleListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.Module module, boolean empty) {
                super.updateItem(module, empty);
                if (empty || module == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox v = new VBox(
                        new Label(module.title),
                        new Label(module.description)
                    );
                    v.setSpacing(2);
                    setGraphic(v);
                }
            }
        });
        moduleListView.setPrefHeight(320);

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("Add Module");
        Button editBtn = new Button("Edit Module");
        Button removeBtn = new Button("Remove Module");
        addBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        editBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        removeBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        btnBox.getChildren().addAll(addBtn, editBtn, removeBtn);

        addBtn.setOnAction(e -> showModuleDialog(null));
        editBtn.setOnAction(e -> {
            AdminDataService.Module selected = moduleListView.getSelectionModel().getSelectedItem();
            if (selected != null) showModuleDialog(selected);
        });
        removeBtn.setOnAction(e -> {
            AdminDataService.Module selected = moduleListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AdminDataService.getInstance().removeModule(selected);
                modules.setAll(AdminDataService.getInstance().getModules());
            }
        });

        getChildren().addAll(title, moduleListView, btnBox);
    }

    private void showModuleDialog(AdminDataService.Module module) {
        Dialog<AdminDataService.Module> dialog = new Dialog<>();
        dialog.setTitle(module == null ? "Add Module" : "Edit Module");
        dialog.getDialogPane().setStyle("-fx-background-color: #fffde7;");
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setAlignment(Pos.CENTER_LEFT);
        TextField titleField = new TextField(module == null ? "" : module.title);
        titleField.setPromptText("Title");
        TextArea descField = new TextArea(module == null ? "" : module.description);
        descField.setPromptText("Description");
        descField.setPrefRowCount(3);
        box.getChildren().addAll(new Label("Title:"), titleField, new Label("Description:"), descField);
        dialog.getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn == okType) {
                return new AdminDataService.Module(titleField.getText(), descField.getText());
            }
            return null;
        });
        dialog.showAndWait().ifPresent(result -> {
            if (module == null) {
                AdminDataService.getInstance().addModule(result);
            } else {
                module.title = result.title;
                module.description = result.description;
                AdminDataService.getInstance().updateModule(module);
            }
            modules.setAll(AdminDataService.getInstance().getModules());
        });
    }
} 