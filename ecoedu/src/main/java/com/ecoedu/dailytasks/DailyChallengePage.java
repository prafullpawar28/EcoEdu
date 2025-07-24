package com.ecoedu.dailytasks;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.image.Image;

public class DailyChallengePage extends VBox {
    private Stage primaryStage;
    private DailyTaskManager taskManager;
    private VBox tasksContainer;
    private Label progressLabel;
    private Label pointsLabel;
    private Label streakLabel;
    private ProgressBar progressBar;
    private Label motivationLabel;
    // Add field for summary card
    // Remove summaryCard and related code
    // Remove tasksScrollPane and use tasksContainer directly
    // Restore reset button style in createFooter()
    // Restore createTasksSection() to add tasksContainer directly

    public DailyChallengePage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.taskManager = DailyTaskManager.getInstance();
        
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #fffde7);");

        createHeader();
        createProgressSection();
        createMotivationSection();
        createTasksSection();
        // createSummaryCard(); // Removed as per edit hint
        createFooter();
        
        refreshTasks();
    }

    private void createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        // Back button
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
                        "-fx-background-radius: 20; -fx-padding: 8 16; -fx-cursor: hand;");
        backBtn.setOnAction(e -> {
            // Return to student dashboard
            com.ecoedu.dashboard.StudentDashboard.show(primaryStage);
        });

        // Title
        Label title = new Label("🌞 Daily Eco Challenges");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#2E7D32"));

        // Date
        Label dateLabel = new Label(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 14));
        dateLabel.setTextFill(Color.web("#666666"));

        VBox titleSection = new VBox(5);
        titleSection.getChildren().addAll(title, dateLabel);

        header.getChildren().addAll(backBtn, titleSection);
        HBox.setHgrow(titleSection, Priority.ALWAYS);

        getChildren().add(header);
    }

    private void createProgressSection() {
        VBox progressSection = new VBox(15);
        progressSection.setAlignment(Pos.CENTER);
        progressSection.setPadding(new Insets(20));
        progressSection.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Progress bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.setPrefHeight(20);
        progressBar.setStyle("-fx-accent: #4CAF50; -fx-background-radius: 10; -fx-background-color: #E0E0E0;");

        // Progress labels
        progressLabel = new Label("0/3 tasks completed");
        progressLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        progressLabel.setTextFill(Color.web("#2E7D32"));

        // Points and streak info
        HBox statsBox = new HBox(40);
        statsBox.setAlignment(Pos.CENTER);

        pointsLabel = new Label("Today's Points: 0");
        pointsLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 14));
        pointsLabel.setTextFill(Color.web("#FF6B35"));

        streakLabel = new Label("Streak: 0 days");
        streakLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 14));
        streakLabel.setTextFill(Color.web("#9C27B0"));

        statsBox.getChildren().addAll(pointsLabel, streakLabel);

        progressSection.getChildren().addAll(progressLabel, progressBar, statsBox);
        getChildren().add(progressSection);
    }

    private void createMotivationSection() {
        motivationLabel = new Label();
        motivationLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        motivationLabel.setTextFill(Color.web("#0288d1"));
        motivationLabel.setPadding(new Insets(10, 0, 20, 0));
        motivationLabel.setAlignment(Pos.CENTER);
        getChildren().add(motivationLabel);
    }

    private void createTasksSection() {
        VBox section = new VBox(15);
        section.setAlignment(Pos.CENTER);

        Label sectionTitle = new Label("Today's Challenges");
        sectionTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web("#2E7D32"));

        tasksContainer = new VBox(15);
        tasksContainer.setAlignment(Pos.CENTER);

        section.getChildren().clear();
        section.getChildren().addAll(sectionTitle, tasksContainer);
        getChildren().add(section);
    }

    // Remove summaryCard and related code
    // Remove tasksScrollPane and use tasksContainer directly
    // Restore reset button style in createFooter()
    // Restore createTasksSection() to add tasksContainer directly

    private void createFooter() {
        HBox footer = new HBox(20);
        footer.setAlignment(Pos.CENTER);

        Button refreshBtn = new Button("🔄 Refresh Tasks");
        refreshBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; " +
                          "-fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
        refreshBtn.setOnAction(e -> refreshTasks());

        Button resetBtn = new Button("🔄 Reset All");
        resetBtn.setStyle("-fx-background-color: linear-gradient(to right, #FF5722, #FF9800); -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 24; -fx-padding: 12 32; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #FF9800, 8, 0.18, 0, 2);");
        resetBtn.setTooltip(new Tooltip("Reset all daily challenges for today"));
        resetBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Reset Tasks");
            alert.setHeaderText("Reset All Tasks?");
            alert.setContentText("This will mark all tasks as incomplete. Are you sure?");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    taskManager.resetDailyTasks();
                    refreshTasks();
                }
            });
        });

        Button reResetBtn = new Button("�� Re-Reset Tasks");
        reResetBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 10 20; -fx-cursor: hand;");
        reResetBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Re-Reset Tasks");
            alert.setHeaderText("Re-Reset All Tasks?");
            alert.setContentText("This will regenerate your daily tasks for today. Are you sure?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    taskManager.resetDailyTasks();
                    refreshTasks();
                }
            });
        });

        footer.getChildren().addAll(refreshBtn, resetBtn, reResetBtn);
        getChildren().add(footer);
    }

    private void refreshTasks() {
        tasksContainer.getChildren().clear();
        List<DailyTask> tasks = taskManager.getCurrentDailyTasks();

        if (tasks.isEmpty()) {
            Label noTasksLabel = new Label("No tasks available for today!");
            noTasksLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 16));
            noTasksLabel.setTextFill(Color.web("#666666"));
            tasksContainer.getChildren().add(noTasksLabel);
        } else {
            for (DailyTask task : tasks) {
                tasksContainer.getChildren().add(createTaskCard(task));
            }
        }

        // Add a fun fact or tip below the challenges
        Label funFact = new Label(getRandomFunFact());
        funFact.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
        funFact.setTextFill(Color.web("#388E3C"));
        funFact.setStyle("-fx-background-color: #A8E6CF; -fx-background-radius: 12; -fx-padding: 10 18; -fx-effect: dropshadow(gaussian, #A8E6CF, 6, 0.1, 0, 2);");
        funFact.setWrapText(true);
        funFact.setAlignment(Pos.CENTER);
        tasksContainer.getChildren().add(funFact);

        updateProgress();
        updateMotivation();
    }

    private VBox createTaskCard(DailyTask task) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefWidth(500);
        card.setAlignment(Pos.CENTER_LEFT);
        String cardColor = task.isCompleted() ? "#E8F5E8" : "#FFFFFF";
        String borderColor = task.isCompleted() ? "#4CAF50" : "#E0E0E0";
        card.setStyle("-fx-background-color: " + cardColor + "; " +
                     "-fx-background-radius: 15; " +
                     "-fx-border-color: " + borderColor + "; " +
                     "-fx-border-radius: 15; " +
                     "-fx-border-width: 2; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Header with category and points
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);

        // Category icon placeholder (now with image if available)
        ImageView iconView = new ImageView();
        try {
            iconView.setImage(new Image(getClass().getResourceAsStream(task.getIconPath())));
            iconView.setFitWidth(32);
            iconView.setFitHeight(32);
        } catch (Exception e) {
            // fallback to colored circle
            iconView = null;
        }
        Circle categoryIcon = new Circle(16, Color.web(task.getCategory().getColor()));
        categoryIcon.setEffect(new DropShadow(4, Color.web("#bdbdbd")));

        VBox categoryInfo = new VBox(2);
        Label categoryLabel = new Label(task.getCategory().getDisplayName());
        categoryLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 12));
        categoryLabel.setTextFill(Color.web(task.getCategory().getColor()));

        Label pointsLabel = new Label(task.getPoints() + " points");
        pointsLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 10));
        pointsLabel.setTextFill(Color.web("#666666"));

        categoryInfo.getChildren().addAll(categoryLabel, pointsLabel);

        // Completion status
        CheckBox completionCheck = new CheckBox();
        completionCheck.setSelected(task.isCompleted());
        completionCheck.setStyle("-fx-text-fill: #4CAF50;");
        completionCheck.setOnAction(e -> {
            if (completionCheck.isSelected()) {
                taskManager.markTaskComplete(task.getId());
            } else {
                taskManager.markTaskIncomplete(task.getId());
            }
            refreshTasks();
        });

        if (iconView != null) {
            header.getChildren().addAll(iconView, categoryInfo, completionCheck);
        } else {
            header.getChildren().addAll(categoryIcon, categoryInfo, completionCheck);
        }
        HBox.setHgrow(categoryInfo, Priority.ALWAYS);

        // Task content
        VBox content = new VBox(8);
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#2E7D32"));
        titleLabel.setWrapText(true);
        Label descLabel = new Label(task.getDescription());
        descLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 12));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        content.getChildren().addAll(titleLabel, descLabel);
        card.getChildren().addAll(header, content);
        // Add hover and completion animation
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        if (task.isCompleted()) {
            FadeTransition ft = new FadeTransition(Duration.millis(400), card);
            ft.setFromValue(0.7);
            ft.setToValue(1.0);
            ft.play();
        }
        return card;
    }

    // Fun facts for kids
    private String getRandomFunFact() {
        String[] facts = new String[] {
            "Did you know? Recycling one aluminum can saves enough energy to run a TV for 3 hours!",
            "Turning off the tap while brushing your teeth can save up to 8 gallons of water a day.",
            "Trees are the longest-living organisms on Earth!",
            "Biking or walking instead of driving helps keep the air clean.",
            "Composting helps reduce landfill waste and makes plants happy!",
            "Plastic can take up to 1,000 years to decompose. Reuse and recycle!",
            "Turning off lights when you leave a room saves energy and money.",
            "Every little eco-action helps protect animals and nature!"
        };
        return "🌱 Fun Fact: " + facts[(int)(Math.random() * facts.length)];
    }

    private void updateProgress() {
        int completed = taskManager.getCompletedTasksCount();
        int total = taskManager.getTotalTasksCount();
        double percentage = taskManager.getCompletionPercentage();
        progressLabel.setText(completed + "/" + total + " tasks completed");
        progressBar.setProgress(percentage / 100.0);
        pointsLabel.setText("Today's Points: " + taskManager.getTodayPoints());
        streakLabel.setText("Streak: " + taskManager.getStreakDays() + " days");
        // Animate progress bar
        FadeTransition ft = new FadeTransition(Duration.millis(500), progressBar);
        ft.setFromValue(0.7);
        ft.setToValue(1.0);
        ft.play();
        // Confetti animation when all tasks are completed
        if (completed == total && total > 0) {
            showConfetti();
        }
    }

    // Simple confetti animation (expressive effect)
    private void showConfetti() {
        // For demo: show a dialog with confetti emoji
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText("🎉 You completed all your daily eco challenges! 🎉");
        alert.show();
    }

    private void updateMotivation() {
        int completed = taskManager.getCompletedTasksCount();
        int total = taskManager.getTotalTasksCount();
        if (completed == total && total > 0) {
            motivationLabel.setText("🌟 Amazing! You completed all today's eco challenges!");
            motivationLabel.setTextFill(Color.web("#ffb300"));
        } else if (completed > 0) {
            motivationLabel.setText("Keep going! Every task helps the planet 🌍");
            motivationLabel.setTextFill(Color.web("#43a047"));
        } else {
            motivationLabel.setText("Start your eco journey! Complete a challenge to earn points.");
            motivationLabel.setTextFill(Color.web("#0288d1"));
        }
        FadeTransition ft = new FadeTransition(Duration.millis(400), motivationLabel);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
    }

    // --- Utility to launch daily challenge page ---
    public static void show(Stage primaryStage) {
        DailyChallengePage page = new DailyChallengePage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Daily Challenges");
        primaryStage.show();
    }
} 