package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;

import com.ecoedu.adminpanel.AdminDashboard;

public class ManageStudentsPage extends VBox {
    private Stage primaryStage;

    public ManageStudentsPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe, #f3e5f5);");

        Label title = new Label("👥 Manage Students");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        TableView<Student> table = new TableView<>();
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("email"));
        TableColumn<Student, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            {
                editBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #263238; -fx-background-radius: 12; -fx-padding: 4 12;");
                deleteBtn.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 4 12;");
                editBtn.setOnAction(e -> {/* TODO: Edit logic */});
                deleteBtn.setOnAction(e -> {/* TODO: Delete logic */});
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8, editBtn, deleteBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
        table.getColumns().addAll(nameCol, emailCol, actionCol);
        table.setPrefWidth(600);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Example data
        List<Student> students = Arrays.asList(
            new Student("Alice Smith", "alice@example.com"),
            new Student("Bob Johnson", "bob@example.com"),
            new Student("Charlie Lee", "charlie@example.com")
        );
        table.getItems().addAll(students);
        getChildren().add(table);

        Button backBtn = new Button("Back to Admin Dashboard");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> AdminDashboard.show(primaryStage));
        getChildren().add(backBtn);
    }

    public static class Student {
        private final String name;
        private final String email;
        public Student(String name, String email) {
            this.name = name;
            this.email = email;
        }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    public static void show(Stage primaryStage) {
        ManageStudentsPage page = new ManageStudentsPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Students");
        primaryStage.show();
    }
} 