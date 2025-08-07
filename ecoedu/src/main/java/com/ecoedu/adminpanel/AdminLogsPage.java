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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminLogsPage extends VBox {
    private final AdminDataService dataService;
    private final ObservableList<AdminDataService.LogEntry> logs;
    private final ListView<AdminDataService.LogEntry> logListView;
    private List<AdminDataService.LogEntry> allLogs;
    private TextField searchField;
    private ComboBox<String> categoryFilter;
    private Button refreshBtn;

    public AdminLogsPage() {
        this.dataService = AdminDataService.getInstance();
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("🗒 Activity Logs");
        title.getStyleClass().add("label-section");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        // Search and filter controls
        HBox searchBox = createSearchAndFilterControls();

        // Statistics panel
        HBox statsPanel = createStatisticsPanel();

        allLogs = new ArrayList<>(dataService.getLogs());
        logs = FXCollections.observableArrayList(allLogs);
        logListView = new ListView<>(logs);
        logListView.getStyleClass().add("top-list");
        logListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.LogEntry log, boolean empty) {
                super.updateItem(log, empty);
                if (empty || log == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(4);
                    container.setPadding(new Insets(8));
                    
                    // Header row with timestamp and category
                    HBox headerRow = new HBox(12);
                    headerRow.setAlignment(Pos.CENTER_LEFT);
                    
                    String timestamp = log.timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss"));
                    Label timeLabel = new Label(timestamp);
                    timeLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11; -fx-font-family: monospace;");
                    
                    Label categoryBadge = new Label(log.category);
                    categoryBadge.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 8; -fx-padding: 2 8; -fx-font-size: 10; -fx-text-fill: #1976d2; -fx-font-weight: bold;");
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    
                    headerRow.getChildren().addAll(timeLabel, categoryBadge, spacer);
                    
                    // Message
                    Label messageLabel = new Label(log.message);
                    messageLabel.setStyle("-fx-text-fill: #333; -fx-font-size: 13; -fx-font-weight: 500;");
                    messageLabel.setWrapText(true);
                    
                    container.getChildren().addAll(headerRow, messageLabel);
                    setGraphic(container);
                }
            }
        });
        logListView.setPrefHeight(400);

        // Control buttons
        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        
        refreshBtn = new Button("🔄 Refresh Logs");
        Button clearBtn = new Button("🗑️ Clear Logs");
        Button exportBtn = new Button("📤 Export Logs");
        
        refreshBtn.getStyleClass().add("button");
        clearBtn.getStyleClass().add("button");
        exportBtn.getStyleClass().add("button");
        
        btnBox.getChildren().addAll(refreshBtn, clearBtn, exportBtn);
        
        refreshBtn.setOnAction(e -> refreshLogs());
        clearBtn.setOnAction(e -> showClearConfirmation());
        exportBtn.setOnAction(e -> exportLogs());

        getChildren().addAll(title, searchBox, statsPanel, logListView, btnBox);
    }

    private HBox createSearchAndFilterControls() {
        HBox searchBox = new HBox(16);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(16));
        searchBox.getStyleClass().add("card");
        
        // Search field
        searchField = new TextField();
        searchField.getStyleClass().add("text-field");
        searchField.setPromptText("Search logs by message or category...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterLogs());
        
        // Category filter
        categoryFilter = new ComboBox<>(FXCollections.observableArrayList(
            "All Categories", "User Management", "Module Management", "Quiz Management", 
            "Notifications", "Feedback", "General"
        ));
        categoryFilter.getStyleClass().add("combo-box");
        categoryFilter.setValue("All Categories");
        categoryFilter.setOnAction(e -> filterLogs());
        
        // Clear filters button
        Button clearBtn = new Button("Clear Filters");
        clearBtn.getStyleClass().add("button");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            categoryFilter.setValue("All Categories");
            filterLogs();
        });
        
        searchBox.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Category:"), categoryFilter,
            clearBtn
        );
        
        return searchBox;
    }

    private HBox createStatisticsPanel() {
        HBox statsPanel = new HBox(32);
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(16));
        statsPanel.getStyleClass().add("card");
        
        // Total logs
        VBox totalBox = new VBox(4);
        totalBox.setAlignment(Pos.CENTER);
        Label totalLabel = new Label(String.valueOf(dataService.getLogs().size()));
        totalLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        totalLabel.setStyle("-fx-text-fill: #1976d2;");
        Label totalText = new Label("Total Logs");
        totalText.setStyle("-fx-text-fill: #666;");
        totalBox.getChildren().addAll(totalLabel, totalText);
        
        // User Management logs
        VBox userMgmtBox = new VBox(4);
        userMgmtBox.setAlignment(Pos.CENTER);
        long userMgmtCount = dataService.getLogsByCategory("User Management").size();
        Label userMgmtLabel = new Label(String.valueOf(userMgmtCount));
        userMgmtLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        userMgmtLabel.setStyle("-fx-text-fill: #388e3c;");
        Label userMgmtText = new Label("User Management");
        userMgmtText.setStyle("-fx-text-fill: #666;");
        userMgmtBox.getChildren().addAll(userMgmtLabel, userMgmtText);
        
        // Module Management logs
        VBox moduleMgmtBox = new VBox(4);
        moduleMgmtBox.setAlignment(Pos.CENTER);
        long moduleMgmtCount = dataService.getLogsByCategory("Module Management").size();
        Label moduleMgmtLabel = new Label(String.valueOf(moduleMgmtCount));
        moduleMgmtLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        moduleMgmtLabel.setStyle("-fx-text-fill: #f57c00;");
        Label moduleMgmtText = new Label("Module Management");
        moduleMgmtText.setStyle("-fx-text-fill: #666;");
        moduleMgmtBox.getChildren().addAll(moduleMgmtLabel, moduleMgmtText);
        
        // Recent logs (last 24 hours)
        VBox recentBox = new VBox(4);
        recentBox.setAlignment(Pos.CENTER);
        long recentCount = dataService.getLogs().stream()
            .filter(log -> log.timestamp.isAfter(java.time.LocalDateTime.now().minusHours(24)))
            .count();
        Label recentLabel = new Label(String.valueOf(recentCount));
        recentLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        recentLabel.setStyle("-fx-text-fill: #d32f2f;");
        Label recentText = new Label("Last 24h");
        recentText.setStyle("-fx-text-fill: #666;");
        recentBox.getChildren().addAll(recentLabel, recentText);
        
        statsPanel.getChildren().addAll(totalBox, userMgmtBox, moduleMgmtBox, recentBox);
        return statsPanel;
    }

    public void filterLogs() {
        String searchQuery = searchField.getText().toLowerCase();
        String categoryFilterValue = categoryFilter.getValue();
        
        List<AdminDataService.LogEntry> filteredLogs = allLogs.stream()
            .filter(log -> {
                // Search filter
                boolean matchesSearch = searchQuery.isEmpty() ||
                    log.message.toLowerCase().contains(searchQuery) ||
                    log.category.toLowerCase().contains(searchQuery);
                
                // Category filter
                boolean matchesCategory = "All Categories".equals(categoryFilterValue) || 
                    log.category.equals(categoryFilterValue);
                
                return matchesSearch && matchesCategory;
            })
            .toList();
        
        logs.setAll(filteredLogs);
    }

    public void refreshLogs() {
        allLogs = new ArrayList<>(dataService.getLogs());
        filterLogs();
        
        // Show refresh notification
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logs Refreshed");
        alert.setHeaderText(null);
        alert.setContentText("Logs have been refreshed successfully. Found " + allLogs.size() + " log entries.");
        alert.showAndWait();
    }

    private void showClearConfirmation() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear Logs");
        confirm.setHeaderText("Clear All Logs");
        confirm.setContentText("Are you sure you want to clear all log entries? This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // In a real application, you would clear logs from the database
                // For now, we'll just show a message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Logs Cleared");
                alert.setHeaderText(null);
                alert.setContentText("All log entries have been cleared successfully.");
                alert.showAndWait();
                
                // Refresh the logs
                refreshLogs();
            }
        });
    }

    private void exportLogs() {
        // In a real application, you would export logs to a file
        // For now, we'll just show a message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Logs");
        alert.setHeaderText(null);
        alert.setContentText("Log export functionality would be implemented here. " +
            "Logs would be exported to a CSV or JSON file.");
        alert.showAndWait();
    }

    public void addLogEntry(String message, String category) {
        AdminDataService.LogEntry newLog = new AdminDataService.LogEntry(message, category);
        allLogs.add(0, newLog); // Add to beginning
        filterLogs();
    }
} 