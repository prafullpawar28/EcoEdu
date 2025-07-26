package com.ecoedu.adminpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class AdminModulesPage extends VBox {
    private final ObservableList<AdminDataService.Module> modules;
    private final ListView<AdminDataService.Module> moduleListView;
    private List<AdminDataService.Module> allModules;
    private final AdminDataService dataService;
    private TextField searchField;
    private ComboBox<String> difficultyFilter;
    private ComboBox<String> statusFilter;

    public AdminModulesPage() {
        this.dataService = AdminDataService.getInstance();
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("📚 Module Management");
        title.getStyleClass().add("label-section");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        // Search and filter controls
        HBox searchBox = createSearchAndFilterControls();

        // Statistics panel
        HBox statsPanel = createStatisticsPanel();

        allModules = new ArrayList<>(dataService.getModules());
        modules = FXCollections.observableArrayList(allModules);
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
                    VBox container = new VBox(8);
                    container.setPadding(new Insets(12));
                    
                    // Title and status
                    HBox headerRow = new HBox(12);
                    headerRow.setAlignment(Pos.CENTER_LEFT);
                    
                    Label titleLabel = new Label(module.title);
                    titleLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
                    titleLabel.setStyle("-fx-text-fill: #1976d2;");
                    
                    Label statusBadge = new Label(module.status);
                    String statusColor = "Active".equals(module.status) ? "#e8f5e8" : "#fff3e0";
                    String statusTextColor = "Active".equals(module.status) ? "#2e7d32" : "#f57c00";
                    statusBadge.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 8; -fx-padding: 4 12; -fx-font-size: 12; -fx-text-fill: " + statusTextColor + "; -fx-font-weight: bold;");
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    
                    headerRow.getChildren().addAll(titleLabel, spacer, statusBadge);
                    
                    // Description
                    Label descLabel = new Label(module.description);
                    descLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14;");
                    descLabel.setWrapText(true);
                    
                    // Difficulty and duration
                    HBox detailsRow = new HBox(16);
                    detailsRow.setAlignment(Pos.CENTER_LEFT);
                    
                    Label difficultyLabel = new Label("Difficulty: " + module.difficulty);
                    difficultyLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
                    
                    Label durationLabel = new Label("Duration: " + module.durationMinutes + " minutes");
                    durationLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
                    
                    detailsRow.getChildren().addAll(difficultyLabel, durationLabel);
                    
                    container.getChildren().addAll(headerRow, descLabel, detailsRow);
                    setGraphic(container);
                }
            }
        });
        moduleListView.setPrefHeight(400);

        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        Button addBtn = new Button("➕ Add Module");
        Button editBtn = new Button("✏️ Edit Module");
        Button removeBtn = new Button("🗑️ Remove Module");
        Button refreshBtn = new Button("🔄 Refresh");
        
        addBtn.getStyleClass().add("button");
        editBtn.getStyleClass().add("button");
        removeBtn.getStyleClass().add("button");
        refreshBtn.getStyleClass().add("button");
        
        btnBox.getChildren().addAll(addBtn, editBtn, removeBtn, refreshBtn);
        
        addBtn.setOnAction(e -> showModuleDialog(null));
        editBtn.setOnAction(e -> {
            AdminDataService.Module selected = moduleListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showModuleDialog(selected);
            } else {
                showAlert("No Module Selected", "Please select a module to edit.", Alert.AlertType.WARNING);
            }
        });
        removeBtn.setOnAction(e -> {
            AdminDataService.Module selected = moduleListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showDeleteConfirmation(selected);
            } else {
                showAlert("No Module Selected", "Please select a module to delete.", Alert.AlertType.WARNING);
            }
        });
        refreshBtn.setOnAction(e -> refreshModuleList());

        getChildren().addAll(title, searchBox, statsPanel, moduleListView, btnBox);
    }

    private HBox createSearchAndFilterControls() {
        HBox searchBox = new HBox(16);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(16));
        searchBox.getStyleClass().add("card");
        
        // Search field
        searchField = new TextField();
        searchField.getStyleClass().add("text-field");
        searchField.setPromptText("Search modules by title, description, or difficulty...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterModules());
        
        // Difficulty filter
        difficultyFilter = new ComboBox<>(FXCollections.observableArrayList("All Difficulties", "Beginner", "Intermediate", "Advanced"));
        difficultyFilter.getStyleClass().add("combo-box");
        difficultyFilter.setValue("All Difficulties");
        difficultyFilter.setOnAction(e -> filterModules());
        
        // Status filter
        statusFilter = new ComboBox<>(FXCollections.observableArrayList("All Status", "Active", "Inactive"));
        statusFilter.getStyleClass().add("combo-box");
        statusFilter.setValue("All Status");
        statusFilter.setOnAction(e -> filterModules());
        
        // Clear filters button
        Button clearBtn = new Button("Clear Filters");
        clearBtn.getStyleClass().add("button");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            difficultyFilter.setValue("All Difficulties");
            statusFilter.setValue("All Status");
            filterModules();
        });
        
        searchBox.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Difficulty:"), difficultyFilter,
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
        
        // Total modules
        VBox totalBox = new VBox(4);
        totalBox.setAlignment(Pos.CENTER);
        Label totalLabel = new Label(String.valueOf(dataService.getModules().size()));
        totalLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        totalLabel.setStyle("-fx-text-fill: #1976d2;");
        Label totalText = new Label("Total Modules");
        totalText.setStyle("-fx-text-fill: #666;");
        totalBox.getChildren().addAll(totalLabel, totalText);
        
        // Active modules
        VBox activeBox = new VBox(4);
        activeBox.setAlignment(Pos.CENTER);
        Label activeLabel = new Label(String.valueOf(dataService.getActiveModules().size()));
        activeLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        activeLabel.setStyle("-fx-text-fill: #2e7d32;");
        Label activeText = new Label("Active Modules");
        activeText.setStyle("-fx-text-fill: #666;");
        activeBox.getChildren().addAll(activeLabel, activeText);
        
        // Beginner modules
        VBox beginnerBox = new VBox(4);
        beginnerBox.setAlignment(Pos.CENTER);
        long beginnerCount = dataService.getModules().stream().filter(m -> "Beginner".equals(m.difficulty)).count();
        Label beginnerLabel = new Label(String.valueOf(beginnerCount));
        beginnerLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        beginnerLabel.setStyle("-fx-text-fill: #388e3c;");
        Label beginnerText = new Label("Beginner");
        beginnerText.setStyle("-fx-text-fill: #666;");
        beginnerBox.getChildren().addAll(beginnerLabel, beginnerText);
        
        // Advanced modules
        VBox advancedBox = new VBox(4);
        advancedBox.setAlignment(Pos.CENTER);
        long advancedCount = dataService.getModules().stream().filter(m -> "Advanced".equals(m.difficulty)).count();
        Label advancedLabel = new Label(String.valueOf(advancedCount));
        advancedLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        advancedLabel.setStyle("-fx-text-fill: #f57c00;");
        Label advancedText = new Label("Advanced");
        advancedText.setStyle("-fx-text-fill: #666;");
        advancedBox.getChildren().addAll(advancedLabel, advancedText);
        
        statsPanel.getChildren().addAll(totalBox, activeBox, beginnerBox, advancedBox);
        return statsPanel;
    }

    public void filterModules() {
        String searchQuery = searchField.getText().toLowerCase();
        String difficultyFilterValue = difficultyFilter.getValue();
        String statusFilterValue = statusFilter.getValue();
        
        List<AdminDataService.Module> filteredModules = allModules.stream()
            .filter(module -> {
                // Search filter
                boolean matchesSearch = searchQuery.isEmpty() ||
                    module.title.toLowerCase().contains(searchQuery) ||
                    module.description.toLowerCase().contains(searchQuery) ||
                    module.difficulty.toLowerCase().contains(searchQuery);
                
                // Difficulty filter
                boolean matchesDifficulty = "All Difficulties".equals(difficultyFilterValue) || module.difficulty.equals(difficultyFilterValue);
                
                // Status filter
                boolean matchesStatus = "All Status".equals(statusFilterValue) || module.status.equals(statusFilterValue);
                
                return matchesSearch && matchesDifficulty && matchesStatus;
            })
            .toList();
        
        modules.setAll(filteredModules);
    }

    public void refreshModuleList() {
        allModules = new ArrayList<>(dataService.getModules());
        filterModules();
    }

    private void showDeleteConfirmation(AdminDataService.Module module) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Module");
        confirm.setContentText("Are you sure you want to delete module '" + module.title + "'? This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                dataService.removeModule(module);
                refreshModuleList();
                showAlert("Module Deleted", "Module '" + module.title + "' has been successfully deleted.", Alert.AlertType.INFORMATION);
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

    private void showModuleDialog(AdminDataService.Module module) {
        Dialog<AdminDataService.Module> dialog = new Dialog<>();
        dialog.setTitle(module == null ? "Add Module" : "Edit Module");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(Pos.CENTER_LEFT);
        
        TextField titleField = new TextField(module == null ? "" : module.title);
        titleField.getStyleClass().add("text-field");
        titleField.setPromptText("Module Title");
        
        TextArea descField = new TextArea(module == null ? "" : module.description);
        descField.getStyleClass().add("text-field");
        descField.setPromptText("Module Description");
        descField.setPrefRowCount(3);
        
        ComboBox<String> difficultyBox = new ComboBox<>(FXCollections.observableArrayList("Beginner", "Intermediate", "Advanced"));
        difficultyBox.getStyleClass().add("combo-box");
        difficultyBox.setValue(module == null ? "Beginner" : module.difficulty);
        
        Spinner<Integer> durationSpinner = new Spinner<>(15, 180, module == null ? 45 : module.durationMinutes, 15);
        durationSpinner.getStyleClass().add("spinner");
        durationSpinner.setEditable(true);
        
        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList("Active", "Inactive"));
        statusBox.getStyleClass().add("combo-box");
        statusBox.setValue(module == null ? "Active" : module.status);
        
        box.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Description:"), descField,
            new Label("Difficulty:"), difficultyBox,
            new Label("Duration (minutes):"), durationSpinner,
            new Label("Status:"), statusBox
        );
        
        dialog.getDialogPane().setContent(box);
        ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        
        // Disable save button if required fields are empty
        Node saveButton = dialog.getDialogPane().lookupButton(okType);
        saveButton.setDisable(titleField.getText().trim().isEmpty());
        
        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty());
        });
        
        dialog.setResultConverter(btn -> {
            if (btn == okType) {
                if (titleField.getText().trim().isEmpty()) {
                    showAlert("Validation Error", "Module title cannot be empty.", Alert.AlertType.ERROR);
                    return null;
                }
                
                if (module == null) {
                    return new AdminDataService.Module(
                        titleField.getText().trim(),
                        descField.getText().trim(),
                        difficultyBox.getValue(),
                        durationSpinner.getValue(),
                        statusBox.getValue()
                    );
                } else {
                    // Update existing module
                    module.title = titleField.getText().trim();
                    module.description = descField.getText().trim();
                    module.difficulty = difficultyBox.getValue();
                    module.durationMinutes = durationSpinner.getValue();
                    module.status = statusBox.getValue();
                    return module;
                }
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(result -> {
            if (module == null) {
                dataService.addModule(result);
                showAlert("Module Added", "Module '" + result.title + "' has been successfully added.", Alert.AlertType.INFORMATION);
            } else {
                dataService.updateModule(result);
                showAlert("Module Updated", "Module '" + result.title + "' has been successfully updated.", Alert.AlertType.INFORMATION);
            }
            refreshModuleList();
        });
    }
} 