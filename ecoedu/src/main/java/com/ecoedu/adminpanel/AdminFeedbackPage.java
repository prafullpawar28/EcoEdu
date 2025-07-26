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

public class AdminFeedbackPage extends VBox {
    private final AdminDataService dataService;
    private final ObservableList<AdminDataService.Feedback> feedbacks;
    private final ListView<AdminDataService.Feedback> feedbackListView;
    private List<AdminDataService.Feedback> allFeedbacks;
    private TextField searchField;
    private ComboBox<String> ratingFilter;
    private Button refreshBtn;

    public AdminFeedbackPage() {
        this.dataService = AdminDataService.getInstance();
        getStyleClass().add("main-content");
        setSpacing(24);
        setPadding(new Insets(32, 32, 32, 32));
        setAlignment(Pos.TOP_CENTER);

        Label title = new Label("📬 Feedback Management");
        title.getStyleClass().add("label-section");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#388e3c"));

        // Search and filter controls
        HBox searchBox = createSearchAndFilterControls();

        // Statistics panel
        HBox statsPanel = createStatisticsPanel();

        allFeedbacks = new ArrayList<>(dataService.getFeedbacks());
        feedbacks = FXCollections.observableArrayList(allFeedbacks);
        feedbackListView = new ListView<>(feedbacks);
        feedbackListView.getStyleClass().add("top-list");
        feedbackListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AdminDataService.Feedback feedback, boolean empty) {
                super.updateItem(feedback, empty);
                if (empty || feedback == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox container = new VBox(8);
                    container.setPadding(new Insets(12));
                    
                    // Header row with author and rating
                    HBox headerRow = new HBox(12);
                    headerRow.setAlignment(Pos.CENTER_LEFT);
                    
                    String timestamp = feedback.timestamp.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
                    Label timeLabel = new Label(timestamp);
                    timeLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11; -fx-font-family: monospace;");
                    
                    Label authorLabel = new Label("by " + feedback.author);
                    authorLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11;");
                    
                    // Rating stars
                    HBox ratingBox = new HBox(2);
                    ratingBox.setAlignment(Pos.CENTER_LEFT);
                    for (int i = 1; i <= 5; i++) {
                        Label star = new Label(i <= feedback.rating ? "★" : "☆");
                        star.setStyle("-fx-text-fill: " + (i <= feedback.rating ? "#ffd700" : "#ddd") + "; -fx-font-size: 14;");
                        ratingBox.getChildren().add(star);
                    }
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    
                    headerRow.getChildren().addAll(timeLabel, authorLabel, spacer, ratingBox);
                    
                    // Title
                    Label titleLabel = new Label(feedback.title);
                    titleLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 14));
                    titleLabel.setStyle("-fx-text-fill: #333;");
                    
                    // Message
                    Label messageLabel = new Label(feedback.message);
                    messageLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");
                    messageLabel.setWrapText(true);
                    
                    container.getChildren().addAll(headerRow, titleLabel, messageLabel);
                    setGraphic(container);
                }
            }
        });
        feedbackListView.setPrefHeight(400);

        // Control buttons
        HBox btnBox = new HBox(16);
        btnBox.setAlignment(Pos.CENTER);
        
        refreshBtn = new Button("🔄 Refresh");
        Button replyBtn = new Button("💬 Reply");
        Button exportBtn = new Button("📤 Export");
        
        refreshBtn.getStyleClass().add("button");
        replyBtn.getStyleClass().add("button");
        exportBtn.getStyleClass().add("button");
        
        btnBox.getChildren().addAll(refreshBtn, replyBtn, exportBtn);
        
        refreshBtn.setOnAction(e -> refreshFeedbacks());
        replyBtn.setOnAction(e -> replyToFeedback());
        exportBtn.setOnAction(e -> exportFeedbacks());

        getChildren().addAll(title, searchBox, statsPanel, feedbackListView, btnBox);
    }

    private HBox createSearchAndFilterControls() {
        HBox searchBox = new HBox(16);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(16));
        searchBox.getStyleClass().add("card");
        
        // Search field
        searchField = new TextField();
        searchField.getStyleClass().add("text-field");
        searchField.setPromptText("Search feedback by title, message, or author...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterFeedbacks());
        
        // Rating filter
        ratingFilter = new ComboBox<>(FXCollections.observableArrayList(
            "All Ratings", "5 Stars", "4 Stars", "3 Stars", "2 Stars", "1 Star"
        ));
        ratingFilter.getStyleClass().add("combo-box");
        ratingFilter.setValue("All Ratings");
        ratingFilter.setOnAction(e -> filterFeedbacks());
        
        // Clear filters button
        Button clearBtn = new Button("Clear Filters");
        clearBtn.getStyleClass().add("button");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            ratingFilter.setValue("All Ratings");
            filterFeedbacks();
        });
        
        searchBox.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Rating:"), ratingFilter,
            clearBtn
        );
        
        return searchBox;
    }

    private HBox createStatisticsPanel() {
        HBox statsPanel = new HBox(32);
        statsPanel.setAlignment(Pos.CENTER);
        statsPanel.setPadding(new Insets(16));
        statsPanel.getStyleClass().add("card");
        
        // Total feedback
        VBox totalBox = new VBox(4);
        totalBox.setAlignment(Pos.CENTER);
        Label totalLabel = new Label(String.valueOf(dataService.getFeedbacks().size()));
        totalLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        totalLabel.setStyle("-fx-text-fill: #1976d2;");
        Label totalText = new Label("Total Feedback");
        totalText.setStyle("-fx-text-fill: #666;");
        totalBox.getChildren().addAll(totalLabel, totalText);
        
        // Average rating
        VBox avgRatingBox = new VBox(4);
        avgRatingBox.setAlignment(Pos.CENTER);
        double avgRating = dataService.getAverageRating();
        Label avgRatingLabel = new Label(String.format("%.1f", avgRating));
        avgRatingLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        avgRatingLabel.setStyle("-fx-text-fill: #ff9800;");
        Label avgRatingText = new Label("Avg Rating");
        avgRatingText.setStyle("-fx-text-fill: #666;");
        avgRatingBox.getChildren().addAll(avgRatingLabel, avgRatingText);
        
        // 5-star feedback
        VBox fiveStarBox = new VBox(4);
        fiveStarBox.setAlignment(Pos.CENTER);
        long fiveStarCount = dataService.getFeedbacks().stream().filter(f -> f.rating == 5).count();
        Label fiveStarLabel = new Label(String.valueOf(fiveStarCount));
        fiveStarLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        fiveStarLabel.setStyle("-fx-text-fill: #4caf50;");
        Label fiveStarText = new Label("5-Star Reviews");
        fiveStarText.setStyle("-fx-text-fill: #666;");
        fiveStarBox.getChildren().addAll(fiveStarLabel, fiveStarText);
        
        // Recent feedback (last 7 days)
        VBox recentBox = new VBox(4);
        recentBox.setAlignment(Pos.CENTER);
        long recentCount = dataService.getFeedbacks().stream()
            .filter(f -> f.timestamp.isAfter(java.time.LocalDateTime.now().minusDays(7)))
            .count();
        Label recentLabel = new Label(String.valueOf(recentCount));
        recentLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        recentLabel.setStyle("-fx-text-fill: #d32f2f;");
        Label recentText = new Label("Last 7 Days");
        recentText.setStyle("-fx-text-fill: #666;");
        recentBox.getChildren().addAll(recentLabel, recentText);
        
        statsPanel.getChildren().addAll(totalBox, avgRatingBox, fiveStarBox, recentBox);
        return statsPanel;
    }

    public void filterFeedbacks() {
        String searchQuery = searchField.getText().toLowerCase();
        String ratingFilterValue = ratingFilter.getValue();
        
        List<AdminDataService.Feedback> filteredFeedbacks = allFeedbacks.stream()
            .filter(feedback -> {
                // Search filter
                boolean matchesSearch = searchQuery.isEmpty() ||
                    feedback.title.toLowerCase().contains(searchQuery) ||
                    feedback.message.toLowerCase().contains(searchQuery) ||
                    feedback.author.toLowerCase().contains(searchQuery);
                
                // Rating filter
                boolean matchesRating = "All Ratings".equals(ratingFilterValue);
                if (!matchesRating) {
                    int requiredRating = Integer.parseInt(ratingFilterValue.split(" ")[0]);
                    matchesRating = feedback.rating == requiredRating;
                }
                
                return matchesSearch && matchesRating;
            })
            .toList();
        
        feedbacks.setAll(filteredFeedbacks);
    }

    public void refreshFeedbacks() {
        allFeedbacks = new ArrayList<>(dataService.getFeedbacks());
        filterFeedbacks();
        
        // Show refresh notification
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feedback Refreshed");
        alert.setHeaderText(null);
        alert.setContentText("Feedback has been refreshed successfully. Found " + allFeedbacks.size() + " feedback entries.");
        alert.showAndWait();
    }

    private void replyToFeedback() {
        AdminDataService.Feedback selected = feedbackListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Feedback Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a feedback entry to reply to.");
            alert.showAndWait();
            return;
        }
        
        // Create reply dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Reply to Feedback");
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        
        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        
        Label feedbackLabel = new Label("Original Feedback: " + selected.title);
        feedbackLabel.setStyle("-fx-font-weight: bold;");
        
        TextArea replyArea = new TextArea();
        replyArea.getStyleClass().add("text-field");
        replyArea.setPromptText("Enter your reply...");
        replyArea.setPrefRowCount(4);
        
        box.getChildren().addAll(feedbackLabel, new Label("Your Reply:"), replyArea);
        dialog.getDialogPane().setContent(box);
        
        ButtonType sendType = new ButtonType("Send Reply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendType, ButtonType.CANCEL);
        
        dialog.setResultConverter(btn -> {
            if (btn == sendType) {
                return replyArea.getText().trim();
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(reply -> {
            if (!reply.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reply Sent");
                alert.setHeaderText(null);
                alert.setContentText("Your reply has been sent to " + selected.author + ".");
                alert.showAndWait();
            }
        });
    }

    private void exportFeedbacks() {
        // In a real application, you would export feedback to a file
        // For now, we'll just show a message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Feedback");
        alert.setHeaderText(null);
        alert.setContentText("Feedback export functionality would be implemented here. " +
            "Feedback would be exported to a CSV or Excel file with ratings and timestamps.");
        alert.showAndWait();
    }

    public void addFeedback(String title, String message, String author, int rating) {
        AdminDataService.Feedback newFeedback = new AdminDataService.Feedback(
            title, message, author, rating, java.time.LocalDateTime.now()
        );
        allFeedbacks.add(0, newFeedback); // Add to beginning
        filterFeedbacks();
    }
} 