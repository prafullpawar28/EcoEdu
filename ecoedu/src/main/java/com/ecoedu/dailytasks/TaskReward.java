package com.ecoedu.dailytasks;

import java.time.LocalDate;
import java.util.*;

public class TaskReward {
    private int id;
    private String name;
    private String description;
    private RewardType type;
    private int pointsRequired;
    private boolean unlocked;
    private LocalDate unlockedDate;
    private String iconPath;

    public enum RewardType {
        BADGE("Badge", "🏆"),
        TITLE("Title", "👑"),
        AVATAR_ITEM("Avatar Item", "👤"),
        BACKGROUND("Background", "🖼️"),
        SPECIAL_TASK("Special Task", "⭐"),
        BONUS_POINTS("Bonus Points", "💎");

        private final String displayName;
        private final String emoji;

        RewardType(String displayName, String emoji) {
            this.displayName = displayName;
            this.emoji = emoji;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEmoji() {
            return emoji;
        }
    }

    public TaskReward(int id, String name, String description, RewardType type, int pointsRequired) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.pointsRequired = pointsRequired;
        this.unlocked = false;
        this.iconPath = "/assets/rewards/" + type.name().toLowerCase() + "_" + id + ".png";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        if (unlocked && this.unlockedDate == null) {
            this.unlockedDate = LocalDate.now();
        }
    }

    public LocalDate getUnlockedDate() {
        return unlockedDate;
    }

    public void setUnlockedDate(LocalDate unlockedDate) {
        this.unlockedDate = unlockedDate;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    public String toString() {
        return "TaskReward{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", unlocked=" + unlocked +
                '}';
    }
} 