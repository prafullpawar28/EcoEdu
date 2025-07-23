package com.ecoedu.dailytasks;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TaskNotification {
    private static TaskNotification instance;
    private DailyTaskManager taskManager;
    private List<NotificationMessage> notificationQueue;
    private boolean notificationsEnabled;

    private TaskNotification() {
        this.taskManager = DailyTaskManager.getInstance();
        this.notificationQueue = new ArrayList<>();
        this.notificationsEnabled = true;
    }

    public static TaskNotification getInstance() {
        if (instance == null) {
            instance = new TaskNotification();
        }
        return instance;
    }

    public enum NotificationType {
        TASK_REMINDER("Task Reminder", "🔔", "#FF9800"),
        TASK_COMPLETED("Task Completed", "✅", "#4CAF50"),
        STREAK_MILESTONE("Streak Milestone", "🔥", "#FF5722"),
        ACHIEVEMENT("Achievement Unlocked", "🏆", "#9C27B0"),
        DAILY_RESET("New Day", "🌅", "#2196F3"),
        MOTIVATIONAL("Motivation", "💪", "#607D8B");

        private final String title;
        private final String emoji;
        private final String color;

        NotificationType(String title, String emoji, String color) {
            this.title = title;
            this.emoji = emoji;
            this.color = color;
        }

        public String getTitle() {
            return title;
        }

        public String getEmoji() {
            return emoji;
        }

        public String getColor() {
            return color;
        }
    }

    public static class NotificationMessage {
        private NotificationType type;
        private String title;
        private String message;
        private LocalDate date;
        private LocalTime time;
        private boolean shown;

        public NotificationMessage(NotificationType type, String title, String message) {
            this.type = type;
            this.title = title;
            this.message = message;
            this.date = LocalDate.now();
            this.time = LocalTime.now();
            this.shown = false;
        }

        // Getters
        public NotificationType getType() { return type; }
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public LocalDate getDate() { return date; }
        public LocalTime getTime() { return time; }
        public boolean isShown() { return shown; }
        public void setShown(boolean shown) { this.shown = shown; }
    }

    public void addNotification(NotificationType type, String title, String message) {
        if (notificationsEnabled) {
            NotificationMessage notification = new NotificationMessage(type, title, message);
            notificationQueue.add(notification);
            showNotification(notification);
        }
    }

    public void showTaskReminder() {
        List<DailyTask> tasks = taskManager.getCurrentDailyTasks();
        int incompleteTasks = (int) tasks.stream().filter(task -> !task.isCompleted()).count();
        
        if (incompleteTasks > 0) {
            String message = "You have " + incompleteTasks + " eco-challenge" + 
                           (incompleteTasks > 1 ? "s" : "") + " waiting for you!";
            addNotification(NotificationType.TASK_REMINDER, "Daily Tasks Reminder", message);
        }
    }

    public void showTaskCompleted(DailyTask task) {
        String message = "Great job! You completed '" + task.getTitle() + "' and earned " + 
                        task.getPoints() + " points!";
        addNotification(NotificationType.TASK_COMPLETED, "Task Completed!", message);
        
        // Check for streak milestone
        checkStreakMilestone();
    }

    public void showStreakMilestone(int streakDays) {
        String message = "🔥 Amazing! You've maintained a " + streakDays + "-day streak! " +
                        "Keep up the fantastic eco-work!";
        addNotification(NotificationType.STREAK_MILESTONE, "Streak Milestone!", message);
    }

    public void showAchievement(String achievementName, String description) {
        String message = "🏆 Achievement Unlocked: " + achievementName + " - " + description;
        addNotification(NotificationType.ACHIEVEMENT, "Achievement Unlocked!", message);
    }

    public void showDailyReset() {
        String message = "🌅 New day, new eco-challenges! Check out today's tasks and make a difference!";
        addNotification(NotificationType.DAILY_RESET, "New Day!", message);
    }

    public void showMotivationalMessage() {
        String[] messages = {
            "💪 Every small eco-action adds up to big environmental impact!",
            "🌱 You're helping create a greener future with every task completed!",
            "🌍 Your daily choices matter for our planet's health!",
            "🌟 You're becoming an eco-warrior one task at a time!",
            "🌿 Small changes lead to big environmental improvements!",
            "🔥 Keep the momentum going - you're making a difference!",
            "🌎 Every recycled item, every saved drop of water counts!",
            "💚 You're part of the solution for a sustainable future!"
        };
        
        Random random = new Random();
        String message = messages[random.nextInt(messages.length)];
        addNotification(NotificationType.MOTIVATIONAL, "Eco Motivation", message);
    }

    private void checkStreakMilestone() {
        int currentStreak = taskManager.getStreakDays();
        if (currentStreak > 0 && (currentStreak % 7 == 0 || currentStreak % 30 == 0)) {
            showStreakMilestone(currentStreak);
        }
    }

    private void showNotification(NotificationMessage notification) {
        // Create notification window
        Stage notificationStage = new Stage();
        notificationStage.initStyle(StageStyle.UNDECORATED);
        notificationStage.setAlwaysOnTop(true);

        VBox notificationBox = new VBox(10);
        notificationBox.setPadding(new Insets(20));
        notificationBox.setPrefWidth(350);
        notificationBox.setAlignment(Pos.CENTER);
        notificationBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                               "-fx-border-color: " + notification.getType().getColor() + "; " +
                               "-fx-border-radius: 15; -fx-border-width: 2; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        // Header
        Label headerLabel = new Label(notification.getType().getEmoji() + " " + notification.getTitle());
        headerLabel.setFont(Font.font("Comic Sans MS", 16));
        headerLabel.setTextFill(Color.web(notification.getType().getColor()));

        // Message
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.setFont(Font.font("Comic Sans MS", 12));
        messageLabel.setTextFill(Color.web("#333333"));
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);

        notificationBox.getChildren().addAll(headerLabel, messageLabel);

        Scene scene = new Scene(notificationBox, 1366, 768);
        notificationStage.setScene(scene);

        // Position notification (top-right corner)
        notificationStage.setX(1200); // Adjust based on screen size
        notificationStage.setY(50);

        // Show notification with animation
        notificationStage.show();

        // Auto-hide after 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), notificationBox);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> notificationStage.close());

        SequentialTransition sequence = new SequentialTransition(pause, fadeOut);
        sequence.play();

        notification.setShown(true);
    }

    public void enableNotifications(boolean enabled) {
        this.notificationsEnabled = enabled;
    }

    public boolean areNotificationsEnabled() {
        return notificationsEnabled;
    }

    public List<NotificationMessage> getNotificationHistory() {
        return new ArrayList<>(notificationQueue);
    }

    public void clearNotificationHistory() {
        notificationQueue.clear();
    }

    public int getUnreadNotificationCount() {
        return (int) notificationQueue.stream().filter(n -> !n.isShown()).count();
    }

    public void markAllAsRead() {
        notificationQueue.forEach(n -> n.setShown(true));
    }

    // Schedule daily reminder
    public void scheduleDailyReminder() {
        // This would typically use a timer or scheduler
        // For now, we'll just add it to the queue
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Check if it's morning (9 AM) and show reminder
                LocalTime now = LocalTime.now();
                if (now.getHour() == 9 && now.getMinute() == 0) {
                    showTaskReminder();
                }
            }
        }, 0, 60000); // Check every minute
    }

    // Show completion celebration
    public void showCompletionCelebration() {
        List<DailyTask> tasks = taskManager.getCurrentDailyTasks();
        boolean allCompleted = tasks.stream().allMatch(DailyTask::isCompleted);
        
        if (allCompleted) {
            int totalPoints = tasks.stream().mapToInt(DailyTask::getPoints).sum();
            String message = "🎉 Congratulations! You've completed all today's challenges! " +
                           "You earned " + totalPoints + " points!";
            addNotification(NotificationType.ACHIEVEMENT, "Daily Challenge Complete!", message);
        }
    }
} 