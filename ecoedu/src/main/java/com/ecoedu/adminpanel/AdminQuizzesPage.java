package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AdminQuizzesPage extends VBox {
    private final ObservableList<AdminDataService.Quiz> quizzes;
    private final ListView<AdminDataService.Quiz> quizListView;

    public AdminQuizzesPage() {
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(24));
        Label title = new Label("📝 Quiz Management");
        title.getStyleClass().add("label-section");
        quizzes = FXCollections.observableArrayList(AdminDataService.getInstance().getQuizzes());
        quizListView = new ListView<>(quizzes);
        quizListView.getStyleClass().add("top-list");
        quizListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.Quiz quiz, boolean empty) {
                super.updateItem(quiz, empty);
                if (empty || quiz == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox(12);
                    row.setAlignment(Pos.CENTER_LEFT);
                    VBox info = new VBox(
                        new Label(quiz.title),
                        new Label("Category: " + quiz.category)
                    );
                    info.setSpacing(2);
                    Label badge = new Label(quiz.category);
                    badge.setStyle("-fx-background-color: #e0f7fa; -fx-background-radius: 8; -fx-padding: 2 10; -fx-font-size: 13; -fx-text-fill: #4fc3f7; -fx-font-weight: bold;");
                    row.getChildren().addAll(info, badge);
                    setGraphic(row);
                }
            }
        });
        quizListView.setPrefHeight(340);
        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("Add Quiz");
        Button editBtn = new Button("Edit Quiz");
        Button removeBtn = new Button("Remove Quiz");
        addBtn.getStyleClass().add("button");
        editBtn.getStyleClass().add("button");
        removeBtn.getStyleClass().add("button");
        btnBox.getChildren().addAll(addBtn, editBtn, removeBtn);
        addBtn.setOnAction(e -> showQuizDialog(null));
        editBtn.setOnAction(e -> {
            AdminDataService.Quiz selected = quizListView.getSelectionModel().getSelectedItem();
            if (selected != null) showQuizDialog(selected);
        });
        removeBtn.setOnAction(e -> {
            AdminDataService.Quiz selected = quizListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                AdminDataService.getInstance().removeQuiz(selected);
                quizzes.setAll(AdminDataService.getInstance().getQuizzes());
            }
        });
        card.getChildren().addAll(title, quizListView, btnBox);
        getChildren().add(card);
    }

    private void showQuizDialog(AdminDataService.Quiz quiz) {
        Dialog<AdminDataService.Quiz> dialog = new Dialog<>();
        dialog.setTitle(quiz == null ? "Add Quiz" : "Edit Quiz");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(Pos.CENTER_LEFT);
        TextField titleField = new TextField(quiz == null ? "" : quiz.title);
        titleField.getStyleClass().add("text-field");
        titleField.setPromptText("Title");
        TextField categoryField = new TextField(quiz == null ? "" : quiz.category);
        categoryField.getStyleClass().add("text-field");
        categoryField.setPromptText("Category");
        box.getChildren().addAll(new Label("Title:"), titleField, new Label("Category:"), categoryField);
        dialog.getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn == okType) {
                return new AdminDataService.Quiz(titleField.getText(), categoryField.getText());
            }
            return null;
        });
        dialog.showAndWait().ifPresent(result -> {
            if (quiz == null) {
                AdminDataService.getInstance().addQuiz(result);
            } else {
                quiz.title = result.title;
                quiz.category = result.category;
                AdminDataService.getInstance().updateQuiz(quiz);
            }
            quizzes.setAll(AdminDataService.getInstance().getQuizzes());
        });
    }
} 