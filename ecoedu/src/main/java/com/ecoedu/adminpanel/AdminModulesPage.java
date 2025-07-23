package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AdminModulesPage extends VBox {
    private final ObservableList<AdminDataService.Module> modules;
    private final ListView<AdminDataService.Module> moduleListView;

    public AdminModulesPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("📚 Module Management");
        title.getStyleClass().add("label-section");
        modules = FXCollections.observableArrayList(AdminDataService.getInstance().getModules());
        moduleListView = new ListView<>(modules);
        moduleListView.getStyleClass().add("top-list");
        moduleListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.Module module, boolean empty) {
                super.updateItem(module, empty);
                if (empty || module == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox info = new VBox(
                        new Label(module.title),
                        new Label(module.description)
                    );
                    info.setSpacing(2);
                    setGraphic(info);
                }
            }
        });
        moduleListView.setPrefHeight(340);
        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("Add Module");
        Button editBtn = new Button("Edit Module");
        Button removeBtn = new Button("Remove Module");
        addBtn.getStyleClass().add("button");
        editBtn.getStyleClass().add("button");
        removeBtn.getStyleClass().add("button");
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
        card.getChildren().addAll(title, moduleListView, btnBox);
        getChildren().add(card);
    }

    private void showModuleDialog(AdminDataService.Module module) {
        Dialog<AdminDataService.Module> dialog = new Dialog<>();
        dialog.setTitle(module == null ? "Add Module" : "Edit Module");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(Pos.CENTER_LEFT);
        TextField titleField = new TextField(module == null ? "" : module.title);
        titleField.getStyleClass().add("text-field");
        titleField.setPromptText("Title");
        TextArea descField = new TextArea(module == null ? "" : module.description);
        descField.getStyleClass().add("text-field");
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