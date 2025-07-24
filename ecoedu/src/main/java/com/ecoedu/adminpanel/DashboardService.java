package com.ecoedu.adminpanel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.*;

public class DashboardService {
    private final ObservableList<String> notifications = FXCollections.observableArrayList();
    private final ObservableList<String> recentActivity = FXCollections.observableArrayList();
    private final ObservableList<TopStudent> topStudents = FXCollections.observableArrayList();
    private int studentCount = 0;
    private int teacherCount = 0;
    private int privateTeacherCount = 0;
    private final Random random = new Random();

    public DashboardService() {
        // Simulate initial data
        notifications.addAll("Welcome to EcoEdu Admin!", "New user registered.", "Module updated: Recycling");
        recentActivity.addAll("Quiz submitted for review", "Badge awarded: Eco Star");
        topStudents.addAll(
            new TopStudent("Lucas Jones", "/Assets/Images/face1.png"),
            new TopStudent("Emma Smith", "/Assets/Images/face2.png"),
            new TopStudent("Olivia Brown", "/Assets/Images/face3.png"),
            new TopStudent("Noah Lee", "/Assets/Images/face1.png")
        );
        studentCount = 1256;
        teacherCount = 102;
        privateTeacherCount = 102;
        // Simulate real-time updates
        startSimulatedUpdates();
    }

    public ObservableList<String> getNotifications() { return notifications; }
    public ObservableList<String> getRecentActivity() { return recentActivity; }
    public ObservableList<TopStudent> getTopStudents() { return topStudents; }
    public int getStudentCount() { return studentCount; }
    public int getTeacherCount() { return teacherCount; }
    public int getPrivateTeacherCount() { return privateTeacherCount; }

    public void addNotification(String notif) {
        Platform.runLater(() -> notifications.add(0, notif));
    }
    public void addRecentActivity(String activity) {
        Platform.runLater(() -> recentActivity.add(0, activity));
    }

    // Simulate real-time updates
    private void startSimulatedUpdates() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Randomly update counts
                    studentCount += random.nextInt(3) - 1;
                    teacherCount += random.nextInt(2) - 1;
                    privateTeacherCount += random.nextInt(2) - 1;
                    // Add a notification sometimes
                    if (random.nextDouble() < 0.2) {
                        addNotification("New student joined: " + UUID.randomUUID().toString().substring(0, 5));
                    }
                });
            }
        }, 2000, 4000);
    }

    // Simulate async fetch with Service
    public Service<Void> fetchAllData(Runnable onSuccess) {
        return new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(1200); // Simulate network delay
                        return null;
                    }
                    @Override
                    protected void succeeded() {
                        if (onSuccess != null) onSuccess.run();
                    }
                };
            }
        };
    }

    public static class TopStudent {
        public final String name;
        public final String avatarPath;
        public TopStudent(String name, String avatarPath) {
            this.name = name;
            this.avatarPath = avatarPath;
        }
    }
} 