package com.ecoedu.dailytasks;

import java.time.LocalDate;

public class DailyTask {
    private int id;
    private String title;
    private String description;
    private int points;
    private boolean completed;
    private LocalDate assignedDate;
    private TaskCategory category;
    private String iconPath;

    public enum TaskCategory {
        RECYCLING("Recycling", "#4CAF50"),
        ENERGY("Energy Conservation", "#FF9800"),
        WATER("Water Conservation", "#2196F3"),
        TRANSPORTATION("Green Transportation", "#9C27B0"),
        WASTE("Waste Reduction", "#795548"),
        EDUCATION("Environmental Education", "#607D8B");

        private final String displayName;
        private final String color;

        TaskCategory(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getColor() {
            return color;
        }
    }

    public DailyTask(int id, String title, String description, int points, TaskCategory category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.points = points;
        this.category = category;
        this.completed = false;
        this.assignedDate = LocalDate.now();
        this.iconPath = "/assets/tasks/" + category.name().toLowerCase() + ".png";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public String toString() {
        return "DailyTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", category=" + category +
                '}';
    }
} 