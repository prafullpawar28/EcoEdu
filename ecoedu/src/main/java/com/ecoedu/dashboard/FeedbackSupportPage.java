package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FeedbackSupportPage extends VBox {
    private TableView<FeedbackRow> feedbackTable;
    private ObservableList<FeedbackRow> feedbacks;
    private Stage primaryStage;
    private Label messageLabel;

    public FeedbackSupportPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(20);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("root");
        Label title = new Label("Feedback & Support");
        title.getStyleClass().add("eco-title");
        // Feedback form
        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Your Email");
        TextArea messageField = new TextArea();
        messageField.setPromptText("Your Message");
        messageField.setPrefRowCount(3);
        Button submitBtn = new Button("Submit Feedback");
        submitBtn.getStyleClass().add("eco-btn");
        submitBtn.setOnAction(e -> {/* TODO: Submit feedback to Firebase */ messageLabel.setText("Feedback submitted! (stub)");});
        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");
        VBox formBox = new VBox(10, nameField, emailField, messageField, submitBtn, messageLabel);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(0, 0, 20, 0));
        // Feedback table
        feedbackTable = new TableView<>();
        feedbacks = FXCollections.observableArrayList(); // Will be filled from Firebase in future
        feedbackTable.setItems(feedbacks);
        TableColumn<FeedbackRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        TableColumn<FeedbackRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        TableColumn<FeedbackRow, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(data -> data.getValue().messageProperty());
        feedbackTable.getColumns().addAll(nameCol, emailCol, messageCol);
        feedbackTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getChildren().addAll(title, formBox, feedbackTable);
    }
    // FeedbackRow class for TableView
    public static class FeedbackRow {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty email;
        private final javafx.beans.property.SimpleStringProperty message;
        public FeedbackRow(String name, String email, String message) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.email = new javafx.beans.property.SimpleStringProperty(email);
            this.message = new javafx.beans.property.SimpleStringProperty(message);
        }
        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty emailProperty() { return email; }
        public javafx.beans.property.StringProperty messageProperty() { return message; }
    }
    public static void show(Stage primaryStage2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }
} 