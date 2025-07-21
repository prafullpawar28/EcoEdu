package com.ecoedu.dashboard;

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

public class ManageModulesPage extends VBox {
    private Stage primaryStage;

    public ManageModulesPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe, #f3e5f5);");

        Label title = new Label("📚 Manage Modules");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        TableView<Module> table = new TableView<>();
        TableColumn<Module, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        TableColumn<Module, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("description"));
        TableColumn<Module, Void> actionCol = new TableColumn<>("Actions");
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
        table.getColumns().addAll(nameCol, descCol, actionCol);
        table.setPrefWidth(600);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Example data
        List<Module> modules = Arrays.asList(
            new Module("Recycling Basics", "Learn how to sort and recycle waste."),
            new Module("Water Conservation", "Tips to save water at home and school."),
            new Module("Green Transportation", "Eco-friendly ways to get around.")
        );
        table.getItems().addAll(modules);
        getChildren().add(table);

        Button backBtn = new Button("Back to Admin Dashboard");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> AdminDashboard.show(primaryStage));
        getChildren().add(backBtn);
    }

    public static class Module {
        private final String name;
        private final String description;
        public Module(String name, String description) {
            this.name = name;
            this.description = description;
        }
        public String getName() { return name; }
        public String getDescription() { return description; }
    }

    public static void show(Stage primaryStage) {
        ManageModulesPage page = new ManageModulesPage(primaryStage);
        Scene scene = new Scene(page, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Modules");
        primaryStage.show();
    }
} 