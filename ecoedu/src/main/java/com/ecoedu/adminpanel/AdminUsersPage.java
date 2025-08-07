package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminUsersPage extends VBox {
    private final ObservableList<AdminDataService.User> users;
    private final ListView<AdminDataService.User> userListView;
    private List<AdminDataService.User> allUsers;
    private final AdminDataService dataService;
    private TextField searchField;
    private ComboBox<String> roleFilter;
    private ComboBox<String> statusFilter;

    public AdminUsersPage() {
        this.dataService = AdminDataService.getInstance();
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("👤 User Management");
        title.getStyleClass().add("label-section");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        // Search and filter controls
        HBox searchBox = createSearchAndFilterControls();
        
        allUsers = new ArrayList<>(dataService.getUsers());
        users = FXCollections.observableArrayList(allUsers);
        userListView = new ListView<>(users);
        userListView.getStyleClass().add("top-list");
        userListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox(12);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setPadding(new Insets(8));
                    
                    // Avatar (placeholder)
                    java.io.InputStream avatarStream = getClass().getResourceAsStream("/Assets/Images/avatar.png");
                    ImageView avatar;
                    if (avatarStream != null) {
                        avatar = new ImageView(new Image(avatarStream));
                    } else {
                        avatar = new ImageView();
                        avatar.setFitWidth(40);
                        avatar.setFitHeight(40);
                        avatar.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 20;");
                    }
                    avatar.setFitWidth(40);
                    avatar.setFitHeight(40);
                    avatar.setPreserveRatio(true);
                    
                    // User info
                    VBox info = new VBox(4);
                    info.setSpacing(2);
                    
                    Label nameLabel = new Label(user.name);
                    nameLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
                    
                    Label emailLabel = new Label(user.email);
                    emailLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");
                    
                    Label regDateLabel = new Label("Registered: " + 
                        user.registrationDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
                    regDateLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11;");
                    
                    info.getChildren().addAll(nameLabel, emailLabel, regDateLabel);
                    
                    // Role badge
                    Label roleBadge = new Label(user.role);
                    roleBadge.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 8; -fx-padding: 4 12; -fx-font-size: 12; -fx-text-fill: #1976d2; -fx-font-weight: bold;");
                    
                    // Status badge
                    Label statusBadge = new Label(user.status);
                    String statusColor = "Active".equals(user.status) ? "#e8f5e8" : "#fff3e0";
                    String statusTextColor = "Active".equals(user.status) ? "#2e7d32" : "#f57c00";
                    statusBadge.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 8; -fx-padding: 4 12; -fx-font-size: 12; -fx-text-fill: " + statusTextColor + "; -fx-font-weight: bold;");
                    
                    // Spacer
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    
                    row.getChildren().addAll(avatar, info, spacer, roleBadge, statusBadge);
                    setGraphic(row);
                }
            }
        });
        userListView.setPrefHeight(400);

        // Statistics panel
        HBox statsPanel = createStatisticsPanel();

        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("➕ Add User");
        Button editBtn = new Button("✏️ Edit User");
        Button removeBtn = new Button("🗑️ Remove User");
        Button refreshBtn = new Button("🔄 Refresh");
        
        addBtn.getStyleClass().add("button");
        editBtn.getStyleClass().add("button");
        removeBtn.getStyleClass().add("button");
        refreshBtn.getStyleClass().add("button");
        
        btnBox.getChildren().addAll(addBtn, editBtn, removeBtn, refreshBtn);

        addBtn.setOnAction(e -> showUserDialog(null));
        editBtn.setOnAction(e -> {
            AdminDataService.User selected = userListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showUserDialog(selected);
            } else {
                showAlert("No User Selected", "Please select a user to edit.", Alert.AlertType.WARNING);
            }
        });
        removeBtn.setOnAction(e -> {
            AdminDataService.User selected = userListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showDeleteConfirmation(selected);
            } else {
                showAlert("No User Selected", "Please select a user to remove.", Alert.AlertType.WARNING);
            }
        });
        refreshBtn.setOnAction(e -> refreshUserList());

        getChildren().addAll(title, searchBox, statsPanel, userListView, btnBox);
    }

    private HBox createSearchAndFilterControls() {
        HBox searchBox = new HBox(16);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(16));
        searchBox.getStyleClass().add("card");
        
        // Search field
        searchField = new TextField();
        searchField.getStyleClass().add("text-field");
        searchField.setPromptText("Search users by name, email, or role...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterUsers());
        
        // Role filter
        roleFilter = new ComboBox<>(FXCollections.observableArrayList("All Roles", "Admin", "Teacher", "Student"));
        roleFilter.getStyleClass().add("combo-box");
        roleFilter.setValue("All Roles");
        roleFilter.setOnAction(e -> filterUsers());
        
        // Status filter
        statusFilter = new ComboBox<>(FXCollections.observableArrayList("All Status", "Active", "Inactive"));
        statusFilter.getStyleClass().add("combo-box");
        statusFilter.setValue("All Status");
        statusFilter.setOnAction(e -> filterUsers());
        
        // Clear filters button
        Button clearBtn = new Button("Clear Filters");
        clearBtn.getStyleClass().add("button");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            roleFilter.setValue("All Roles");
            statusFilter.setValue("All Status");
            filterUsers();
        });
        
        searchBox.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Role:"), roleFilter,
            new Label("Status:"), statusFilter,
            clearBtn
        );
        
        return searchBox;
    }

    private HBox createStatisticsPanel() {
        HBox statsPanel = new HBox(32);
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(16));
        statsPanel.getStyleClass().add("card");
        
        // Total users
        VBox totalBox = new VBox(4);
        totalBox.setAlignment(Pos.CENTER);
        Label totalLabel = new Label(String.valueOf(dataService.getUsers().size()));
        totalLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        totalLabel.setStyle("-fx-text-fill: #1976d2;");
        Label totalText = new Label("Total Users");
        totalText.setStyle("-fx-text-fill: #666;");
        totalBox.getChildren().addAll(totalLabel, totalText);
        
        // Students
        VBox studentsBox = new VBox(4);
        studentsBox.setAlignment(Pos.CENTER);
        Label studentsLabel = new Label(String.valueOf(dataService.getUsersByRole("Student").size()));
        studentsLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        studentsLabel.setStyle("-fx-text-fill: #388e3c;");
        Label studentsText = new Label("Students");
        studentsText.setStyle("-fx-text-fill: #666;");
        studentsBox.getChildren().addAll(studentsLabel, studentsText);
        
        // Teachers
        VBox teachersBox = new VBox(4);
        teachersBox.setAlignment(Pos.CENTER);
        Label teachersLabel = new Label(String.valueOf(dataService.getUsersByRole("Teacher").size()));
        teachersLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        teachersLabel.setStyle("-fx-text-fill: #f57c00;");
        Label teachersText = new Label("Teachers");
        teachersText.setStyle("-fx-text-fill: #666;");
        teachersBox.getChildren().addAll(teachersLabel, teachersText);
        
        // Active users
        VBox activeBox = new VBox(4);
        activeBox.setAlignment(Pos.CENTER);
        Label activeLabel = new Label(String.valueOf(dataService.getActiveUsers().size()));
        activeLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        activeLabel.setStyle("-fx-text-fill: #2e7d32;");
        Label activeText = new Label("Active Users");
        activeText.setStyle("-fx-text-fill: #666;");
        activeBox.getChildren().addAll(activeLabel, activeText);
        
        statsPanel.getChildren().addAll(totalBox, studentsBox, teachersBox, activeBox);
        return statsPanel;
    }

    public void filterUsers() {
        String searchQuery = searchField.getText().toLowerCase();
        String roleFilterValue = roleFilter.getValue();
        String statusFilterValue = statusFilter.getValue();
        
        List<AdminDataService.User> filteredUsers = allUsers.stream()
            .filter(user -> {
                // Search filter
                boolean matchesSearch = searchQuery.isEmpty() ||
                    user.name.toLowerCase().contains(searchQuery) ||
                    user.email.toLowerCase().contains(searchQuery) ||
                    user.role.toLowerCase().contains(searchQuery);
                
                // Role filter
                boolean matchesRole = "All Roles".equals(roleFilterValue) || user.role.equals(roleFilterValue);
                
                // Status filter
                boolean matchesStatus = "All Status".equals(statusFilterValue) || user.status.equals(statusFilterValue);
                
                return matchesSearch && matchesRole && matchesStatus;
            })
            .toList();
        
        users.setAll(filteredUsers);
    }

    public void refreshUserList() {
        allUsers = new ArrayList<>(dataService.getUsers());
        filterUsers();
    }

    private void showDeleteConfirmation(AdminDataService.User user) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete User");
        confirm.setContentText("Are you sure you want to delete user '" + user.name + "'? This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                dataService.removeUser(user);
                refreshUserList();
                showAlert("User Deleted", "User '" + user.name + "' has been successfully deleted.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showUserDialog(AdminDataService.User user) {
        Dialog<AdminDataService.User> dialog = new Dialog<>();
        dialog.setTitle(user == null ? "Add User" : "Edit User");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(Pos.CENTER_LEFT);
        
        TextField nameField = new TextField(user == null ? "" : user.name);
        nameField.getStyleClass().add("text-field");
        nameField.setPromptText("Full Name");
        
        TextField emailField = new TextField(user == null ? "" : user.email);
        emailField.getStyleClass().add("text-field");
        emailField.setPromptText("Email Address");
        
        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList("Admin", "Teacher", "Student"));
        roleBox.getStyleClass().add("combo-box");
        roleBox.setValue(user == null ? "Student" : user.role);
        
        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList("Active", "Inactive"));
        statusBox.getStyleClass().add("combo-box");
        statusBox.setValue(user == null ? "Active" : user.status);
        
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");
        passwordField.setPromptText("Password");
        if (user != null) passwordField.setText(user.password);
        
        box.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Email:"), emailField,
            new Label("Role:"), roleBox,
            new Label("Status:"), statusBox,
            new Label("Password:"), passwordField
        );
        
        dialog.getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        
        // Disable save button if required fields are empty
        Node saveButton = dialog.getDialogPane().lookupButton(okType);
        saveButton.setDisable(nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty());
        
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty() || emailField.getText().trim().isEmpty());
        });
        
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty() || nameField.getText().trim().isEmpty());
        });
        
        dialog.setResultConverter(btn -> {
            if (btn == okType) {
                if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                    showAlert("Validation Error", "Name and email are required fields.", Alert.AlertType.ERROR);
                    return null;
                }
                
                if (user == null) {
                    return new AdminDataService.User(
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        roleBox.getValue(),
                        passwordField.getText()
                    );
                } else {
                    // Update existing user
                    user.name = nameField.getText().trim();
                    user.email = emailField.getText().trim();
                    user.role = roleBox.getValue();
                    user.status = statusBox.getValue();
                    user.password = passwordField.getText();
                    return user;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(result -> {
            if (user == null) {
                dataService.addUser(result);
                showAlert("User Added", "User '" + result.name + "' has been successfully added.", Alert.AlertType.INFORMATION);
            } else {
                dataService.updateUser(result);
                showAlert("User Updated", "User '" + result.name + "' has been successfully updated.", Alert.AlertType.INFORMATION);
            }
            refreshUserList();
        });
    }
} 