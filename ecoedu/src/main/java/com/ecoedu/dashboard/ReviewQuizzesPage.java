package com.ecoedu.dashboard;

import com.ecoedu.adminpanel.AdminPanelMain;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;

public class ReviewQuizzesPage extends VBox {
    private Stage primaryStage;

    public ReviewQuizzesPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe, #f3e5f5);");

        Label title = new Label("\ud83d\udcdd Review Quizzes");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        TableView<Quiz> table = new TableView<>();
        TableColumn<Quiz, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));
        TableColumn<Quiz, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        TableColumn<Quiz, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button approveBtn = new Button("Approve");
            private final Button rejectBtn = new Button("Reject");
            {
                approveBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 4 12;");
                rejectBtn.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 4 12;");
                approveBtn.setOnAction(e -> {/* TODO: Approve logic */});
                rejectBtn.setOnAction(e -> {/* TODO: Reject logic */});
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8, approveBtn, rejectBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
        table.getColumns().addAll(titleCol, statusCol, actionCol);
        table.setPrefWidth(600);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Example data
        List<Quiz> quizzes = Arrays.asList(
            new Quiz("Eco Quiz 1", "Pending"),
            new Quiz("Recycling Challenge", "Approved"),
            new Quiz("Water Saver Quiz", "Rejected")
        );
        table.getItems().addAll(quizzes);
        getChildren().add(table);

        Button backBtn = new Button("Back to Admin Panel");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> {
            try {
                AdminPanelMain.main(new String[]{});
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        getChildren().add(backBtn);
    }

    public static class Quiz {
        private final String title;
        private final String status;
        public Quiz(String title, String status) {
            this.title = title;
            this.status = status;
        }
        public String getTitle() { return title; }
        public String getStatus() { return status; }
    }

    public static void show(Stage primaryStage) {
        ReviewQuizzesPage page = new ReviewQuizzesPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Review Quizzes");
        primaryStage.show();
    }
} 