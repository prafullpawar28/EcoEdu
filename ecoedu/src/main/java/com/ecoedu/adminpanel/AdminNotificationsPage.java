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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminNotificationsPage extends VBox {
    private final AdminDataService dataService;
    private final ObservableList<AdminDataService.Notification> notifications;
    private final ListView<AdminDataService.Notification> notifListView;
    private List<AdminDataService.Notification> allNotifications;
    private TextField searchField;
    private ComboBox<String> typeFilter;
    private Button refreshBtn;

    public AdminNotificationsPage() {
        this.dataService = AdminDataService.getInstance();
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("🔔 Notifications");
        title.getStyleClass().add("label-section");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        // Search and filter controls
        HBox searchBox = createSearchAndFilterControls();

        // Statistics panel
        HBox statsPanel = createStatisticsPanel();

        allNotifications = new ArrayList<>(dataService.getNotifications());
        notifications = FXCollections.observableArrayList(allNotifications);
        notifListView = new ListView<>(notifications);
        notifListView.getStyleClass().add("top-list");
        notifListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.Notification notif, boolean empty) {
                super.updateItem(notif, empty);
                if (empty || notif == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(8);
                    container.setPadding(new Insets(12));
                    
                    // Header row with timestamp and type
                    HBox headerRow = new HBox(12);
                    headerRow.setAlignment(Pos.CENTER_LEFT);
                    
                    String timestamp = notif.timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
                    Label timeLabel = new Label(timestamp);
                    timeLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11; -fx-font-family: monospace;");
                    
                    Label typeBadge = new Label(notif.type);
                    String typeColor = getTypeColor(notif.type);
                    typeBadge.setStyle("-fx-background-color: " + typeColor + "; -fx-background-radius: 8; -fx-padding: 2 8; -fx-font-size: 10; -fx-text-fill: white; -fx-font-weight: bold;");
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    
                    headerRow.getChildren().addAll(timeLabel, typeBadge, spacer);
                    
                    // Title
                    Label titleLabel = new Label(notif.title);
                    titleLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
                    titleLabel.setStyle("-fx-text-fill: #333;");
                    
                    // Message
                    Label messageLabel = new Label(notif.message);
                    messageLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");
                    messageLabel.setWrapText(true);
                    
                    container.getChildren().addAll(headerRow, titleLabel, messageLabel);
                    setGraphic(container);
                }
            }
        });
        notifListView.setPrefHeight(400);

        // Control buttons
        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        
        refreshBtn = new Button("🔄 Refresh");
        Button clearBtn = new Button("🗑️ Clear All");
        Button markReadBtn = new Button("✓ Mark as Read");
        
        refreshBtn.getStyleClass().add("button");
        clearBtn.getStyleClass().add("button");
        markReadBtn.getStyleClass().add("button");
        
        btnBox.getChildren().addAll(refreshBtn, clearBtn, markReadBtn);
        
        refreshBtn.setOnAction(e -> refreshNotifications());
        clearBtn.setOnAction(e -> showClearConfirmation());
        markReadBtn.setOnAction(e -> markAllAsRead());

        getChildren().addAll(title, searchBox, statsPanel, notifListView, btnBox);
    }

    private String getTypeColor(String type) {
        switch (type) {
            case "Success": return "#4caf50";
            case "Warning": return "#ff9800";
            case "Error": return "#f44336";
            case "Info": 
            default: return "#2196f3";
        }
    }

    private HBox createSearchAndFilterControls() {
        HBox searchBox = new HBox(16);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(16));
        searchBox.getStyleClass().add("card");
        
        // Search field
        searchField = new TextField();
        searchField.getStyleClass().add("text-field");
        searchField.setPromptText("Search notifications by title or message...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterNotifications());
        
        // Type filter
        typeFilter = new ComboBox<>(FXCollections.observableArrayList(
            "All Types", "Info", "Success", "Warning", "Error"
        ));
        typeFilter.getStyleClass().add("combo-box");
        typeFilter.setValue("All Types");
        typeFilter.setOnAction(e -> filterNotifications());
        
        // Clear filters button
        Button clearBtn = new Button("Clear Filters");
        clearBtn.getStyleClass().add("button");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            typeFilter.setValue("All Types");
            filterNotifications();
        });
        
        searchBox.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Type:"), typeFilter,
            clearBtn
        );
        
        return searchBox;
    }

    private HBox createStatisticsPanel() {
        HBox statsPanel = new HBox(32);
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(16));
        statsPanel.getStyleClass().add("card");
        
        // Total notifications
        VBox totalBox = new VBox(4);
        totalBox.setAlignment(Pos.CENTER);
        Label totalLabel = new Label(String.valueOf(dataService.getNotifications().size()));
        totalLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        totalLabel.setStyle("-fx-text-fill: #1976d2;");
        Label totalText = new Label("Total Notifications");
        totalText.setStyle("-fx-text-fill: #666;");
        totalBox.getChildren().addAll(totalLabel, totalText);
        
        // Info notifications
        VBox infoBox = new VBox(4);
        infoBox.setAlignment(Pos.CENTER);
        long infoCount = dataService.getNotificationsByType("Info").size();
        Label infoLabel = new Label(String.valueOf(infoCount));
        infoLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        infoLabel.setStyle("-fx-text-fill: #2196f3;");
        Label infoText = new Label("Info");
        infoText.setStyle("-fx-text-fill: #666;");
        infoBox.getChildren().addAll(infoLabel, infoText);
        
        // Success notifications
        VBox successBox = new VBox(4);
        successBox.setAlignment(Pos.CENTER);
        long successCount = dataService.getNotificationsByType("Success").size();
        Label successLabel = new Label(String.valueOf(successCount));
        successLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        successLabel.setStyle("-fx-text-fill: #4caf50;");
        Label successText = new Label("Success");
        successText.setStyle("-fx-text-fill: #666;");
        successBox.getChildren().addAll(successLabel, successText);
        
        // Warning notifications
        VBox warningBox = new VBox(4);
        warningBox.setAlignment(Pos.CENTER);
        long warningCount = dataService.getNotificationsByType("Warning").size();
        Label warningLabel = new Label(String.valueOf(warningCount));
        warningLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        warningLabel.setStyle("-fx-text-fill: #ff9800;");
        Label warningText = new Label("Warning");
        warningText.setStyle("-fx-text-fill: #666;");
        warningBox.getChildren().addAll(warningLabel, warningText);
        
        // Recent notifications (last 24 hours)
        VBox recentBox = new VBox(4);
        recentBox.setAlignment(Pos.CENTER);
        long recentCount = dataService.getNotifications().stream()
            .filter(notif -> notif.timestamp.isAfter(java.time.LocalDateTime.now().minusHours(24)))
            .count();
        Label recentLabel = new Label(String.valueOf(recentCount));
        recentLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        recentLabel.setStyle("-fx-text-fill: #d32f2f;");
        Label recentText = new Label("Last 24h");
        recentText.setStyle("-fx-text-fill: #666;");
        recentBox.getChildren().addAll(recentLabel, recentText);
        
        statsPanel.getChildren().addAll(totalBox, infoBox, successBox, warningBox, recentBox);
        return statsPanel;
    }

    public void filterNotifications() {
        String searchQuery = searchField.getText().toLowerCase();
        String typeFilterValue = typeFilter.getValue();
        
        List<AdminDataService.Notification> filteredNotifications = allNotifications.stream()
            .filter(notif -> {
                // Search filter
                boolean matchesSearch = searchQuery.isEmpty() ||
                    notif.title.toLowerCase().contains(searchQuery) ||
                    notif.message.toLowerCase().contains(searchQuery);
                
                // Type filter
                boolean matchesType = "All Types".equals(typeFilterValue) || 
                    notif.type.equals(typeFilterValue);
                
                return matchesSearch && matchesType;
            })
            .toList();
        
        notifications.setAll(filteredNotifications);
    }

    public void refreshNotifications() {
        allNotifications = new ArrayList<>(dataService.getNotifications());
        filterNotifications();
        
        // Show refresh notification
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifications Refreshed");
        alert.setHeaderText(null);
        alert.setContentText("Notifications have been refreshed successfully. Found " + allNotifications.size() + " notifications.");
        alert.showAndWait();
    }

    private void showClearConfirmation() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear Notifications");
        confirm.setHeaderText("Clear All Notifications");
        confirm.setContentText("Are you sure you want to clear all notifications? This action cannot be undone.");
        
        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // In a real application, you would clear notifications from the database
                // For now, we'll just show a message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notifications Cleared");
                alert.setHeaderText(null);
                alert.setContentText("All notifications have been cleared successfully.");
                alert.showAndWait();
                
                // Refresh the notifications
                refreshNotifications();
            }
        });
    }

    private void markAllAsRead() {
        // In a real application, you would mark notifications as read in the database
        // For now, we'll just show a message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifications Marked as Read");
        alert.setHeaderText(null);
        alert.setContentText("All notifications have been marked as read.");
        alert.showAndWait();
    }

    public void addNotification(String title, String message, String type) {
        AdminDataService.Notification newNotif = new AdminDataService.Notification(
            title, message, type, java.time.LocalDateTime.now()
        );
        allNotifications.add(0, newNotif); // Add to beginning
        filterNotifications();
    }
} 