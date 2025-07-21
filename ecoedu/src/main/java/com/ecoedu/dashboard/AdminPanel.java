package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminPanel extends BorderPane {
    private Stage primaryStage;
    private VBox sidebar;
    private StackPane mainContent;
    private Label headerTitle;

    public AdminPanel(Stage primaryStage) {
        this.primaryStage = primaryStage;
        getStyleClass().add("root");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(18, 32, 18, 32));
        header.setSpacing(24);
        header.setStyle("-fx-background-color: linear-gradient(to right, #388E3C 60%, #A8E6CF 100%); -fx-background-radius: 0 0 32 32; -fx-effect: dropshadow(gaussian, #A8E6CF, 12, 0.2, 0, 4);");
        headerTitle = new Label("Admin Panel");
        headerTitle.getStyleClass().add("eco-title");
        header.getChildren().add(headerTitle);
        setTop(header);

        // Sidebar
        sidebar = new VBox(18);
        sidebar.setPadding(new Insets(36, 18, 36, 18));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #DCEDC1; -fx-background-radius: 0 32 32 0;");
        sidebar.setPrefWidth(220);
        sidebar.getChildren().add(makeSidebarButton("Manage Users", this::showManageUsers));
        sidebar.getChildren().add(makeSidebarButton("Manage Modules", this::showManageModules));
        sidebar.getChildren().add(makeSidebarButton("Manage Quizzes", this::showManageQuizzes));
        sidebar.getChildren().add(makeSidebarButton("Analytics", this::showAnalytics));
        sidebar.getChildren().add(makeSidebarButton("Settings", this::showSettings));
        setLeft(sidebar);

        // Main content area
        mainContent = new StackPane();
        mainContent.setPadding(new Insets(36));
        mainContent.setStyle("-fx-background-color: white; -fx-background-radius: 32; -fx-effect: dropshadow(gaussian, #A8E6CF, 16, 0.2, 0, 4);");
        setCenter(mainContent);

        // Show default section
        showAnalytics();
    }

    private Button makeSidebarButton(String text, Runnable onClick) {
        Button btn = new Button(text);
        btn.getStyleClass().add("eco-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> onClick.run());
        return btn;
    }

    // Section placeholders
    private void showManageUsers() {
        headerTitle.setText("Manage Users");
        TableView<User> userTable = new TableView<>();
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> data.getValue().roleProperty());
        userTable.getColumns().addAll(nameCol, emailCol, roleCol);
        userTable.setItems(getDummyUsers());
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mainContent.getChildren().setAll(userTable);
    }
    private void showManageModules() {
        headerTitle.setText("Manage Modules");
        TableView<Module> moduleTable = new TableView<>();
        TableColumn<Module, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());
        TableColumn<Module, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> data.getValue().descriptionProperty());
        moduleTable.getColumns().addAll(titleCol, descCol);
        moduleTable.setItems(getDummyModules());
        moduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mainContent.getChildren().setAll(moduleTable);
    }
    private void showManageQuizzes() {
        headerTitle.setText("Manage Quizzes");
        TableView<Quiz> quizTable = new TableView<>();
        TableColumn<Quiz, String> quizTitleCol = new TableColumn<>("Quiz Title");
        quizTitleCol.setCellValueFactory(data -> data.getValue().titleProperty());
        TableColumn<Quiz, String> moduleCol = new TableColumn<>("Module");
        moduleCol.setCellValueFactory(data -> data.getValue().moduleProperty());
        quizTable.getColumns().addAll(quizTitleCol, moduleCol);
        quizTable.setItems(getDummyQuizzes());
        quizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        mainContent.getChildren().setAll(quizTable);
    }
    private void showAnalytics() {
        headerTitle.setText("Analytics");
        mainContent.getChildren().setAll(new Label("Analytics dashboard coming soon!"));
    }
    private void showSettings() {
        headerTitle.setText("Settings");
        mainContent.getChildren().setAll(new Label("Settings page coming soon!"));
    }

    public static void show(Stage primaryStage) {
        AdminPanel panel = new AdminPanel(primaryStage);
        Scene scene = new Scene(panel, 1100, 750);
        // com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Admin Panel");
        primaryStage.show();
    }

    // Dummy data classes and methods
    private static class User {
        private final StringProperty name;
        private final StringProperty email;
        private final StringProperty role;
        public User(String name, String email, String role) {
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.role = new SimpleStringProperty(role);
        }
        public StringProperty nameProperty() { return name; }
        public StringProperty emailProperty() { return email; }
        public StringProperty roleProperty() { return role; }
    }
    private static class Module {
        private final StringProperty title;
        private final StringProperty description;
        public Module(String title, String description) {
            this.title = new SimpleStringProperty(title);
            this.description = new SimpleStringProperty(description);
        }
        public StringProperty titleProperty() { return title; }
        public StringProperty descriptionProperty() { return description; }
    }
    private static class Quiz {
        private final StringProperty title;
        private final StringProperty module;
        public Quiz(String title, String module) {
            this.title = new SimpleStringProperty(title);
            this.module = new SimpleStringProperty(module);
        }
        public StringProperty titleProperty() { return title; }
        public StringProperty moduleProperty() { return module; }
    }
    private ObservableList<User> getDummyUsers() {
        return FXCollections.observableArrayList(
            new User("Alice Green", "alice@ecoedu.com", "Student"),
            new User("Bob Blue", "bob@ecoedu.com", "Teacher"),
            new User("Carol Earth", "carol@ecoedu.com", "Parent")
        );
    }
    private ObservableList<Module> getDummyModules() {
        return FXCollections.observableArrayList(
            new Module("Recycling Basics", "Learn how to recycle at home!"),
            new Module("Water Conservation", "Save water, save Earth!"),
            new Module("Plant Power", "The importance of trees.")
        );
    }
    private ObservableList<Quiz> getDummyQuizzes() {
        return FXCollections.observableArrayList(
            new Quiz("Recycling Quiz", "Recycling Basics"),
            new Quiz("Water Quiz", "Water Conservation"),
            new Quiz("Plant Quiz", "Plant Power")
        );
    }
} 