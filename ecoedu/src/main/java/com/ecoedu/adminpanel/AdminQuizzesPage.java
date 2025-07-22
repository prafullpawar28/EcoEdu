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

public class AdminQuizzesPage extends VBox {
    private final ObservableList<AdminDataService.Quiz> quizzes;
    private final ListView<AdminDataService.Quiz> quizListView;

    public AdminQuizzesPage() {
        setSpacing(24);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");

        Label title = new Label("📝 Quiz Management");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#4fc3f7"));

        quizzes = FXCollections.observableArrayList(AdminDataService.getInstance().getQuizzes());
        quizListView = new ListView<>(quizzes);
        quizListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.Quiz quiz, boolean empty) {
                super.updateItem(quiz, empty);
                if (empty || quiz == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox v = new VBox(
                        new Label(quiz.title),
                        new Label("Category: " + quiz.category)
                    );
                    v.setSpacing(2);
                    setGraphic(v);
                }
            }
        });
        quizListView.setPrefHeight(320);

        HBox btnBox = new HBox(12);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("Add Quiz");
        Button editBtn = new Button("Edit Quiz");
        Button removeBtn = new Button("Remove Quiz");
        addBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        editBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        removeBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
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

        getChildren().addAll(title, quizListView, btnBox);
    }

    private void showQuizDialog(AdminDataService.Quiz quiz) {
        Dialog<AdminDataService.Quiz> dialog = new Dialog<>();
        dialog.setTitle(quiz == null ? "Add Quiz" : "Edit Quiz");
        dialog.getDialogPane().setStyle("-fx-background-color: #fffde7;");
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setAlignment(Pos.CENTER_LEFT);
        TextField titleField = new TextField(quiz == null ? "" : quiz.title);
        titleField.setPromptText("Title");
        TextField categoryField = new TextField(quiz == null ? "" : quiz.category);
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