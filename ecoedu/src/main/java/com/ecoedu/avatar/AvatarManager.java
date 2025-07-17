package com.ecoedu.avatar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvatarManager {
    private static AvatarManager instance;
    private Avatar currentAvatar;
    private Map<String, Avatar> savedAvatars;
    private List<String> unlockedAccessories;
    private List<String> unlockedHairStyles;
    private List<String> unlockedHairColors;

    private AvatarManager() {
        this.currentAvatar = new Avatar(); // Default avatar
        this.savedAvatars = new HashMap<>();
        this.unlockedAccessories = new ArrayList<>();
        this.unlockedHairStyles = new ArrayList<>();
        this.unlockedHairColors = new ArrayList<>();
        
        // Initialize with default unlocked items
        initializeDefaultUnlocks();
    }

    public static AvatarManager getInstance() {
        if (instance == null) {
            instance = new AvatarManager();
        }
        return instance;
    }

    private void initializeDefaultUnlocks() {
        // Default unlocked items
        unlockedHairStyles.addAll(List.of("Short", "Long", "Straight"));
        unlockedHairColors.addAll(List.of("Black", "Brown", "Blonde"));
        unlockedAccessories.addAll(List.of("Glasses", "Hat"));
    }

    public Avatar getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(Avatar avatar) {
        this.currentAvatar = avatar;
    }

    public void saveAvatar(String name, Avatar avatar) {
        savedAvatars.put(name, avatar);
    }

    public Avatar loadAvatar(String name) {
        return savedAvatars.get(name);
    }

    public List<String> getSavedAvatarNames() {
        return new ArrayList<>(savedAvatars.keySet());
    }

    public void deleteAvatar(String name) {
        savedAvatars.remove(name);
    }

    public List<String> getUnlockedAccessories() {
        return new ArrayList<>(unlockedAccessories);
    }

    public List<String> getUnlockedHairStyles() {
        return new ArrayList<>(unlockedHairStyles);
    }

    public List<String> getUnlockedHairColors() {
        return new ArrayList<>(unlockedHairColors);
    }

    public void unlockAccessory(String accessory) {
        if (!unlockedAccessories.contains(accessory)) {
            unlockedAccessories.add(accessory);
        }
    }

    public void unlockHairStyle(String hairStyle) {
        if (!unlockedHairStyles.contains(hairStyle)) {
            unlockedHairStyles.add(hairStyle);
        }
    }

    public void unlockHairColor(String hairColor) {
        if (!unlockedHairColors.contains(hairColor)) {
            unlockedHairColors.add(hairColor);
        }
    }

    public boolean isAccessoryUnlocked(String accessory) {
        return unlockedAccessories.contains(accessory);
    }

    public boolean isHairStyleUnlocked(String hairStyle) {
        return unlockedHairStyles.contains(hairStyle);
    }

    public boolean isHairColorUnlocked(String hairColor) {
        return unlockedHairColors.contains(hairColor);
    }

    public void addExperienceToAvatar(int experience) {
        if (currentAvatar != null) {
            currentAvatar.addExperience(experience);
        }
    }

    public void rewardAvatarForTaskCompletion(String taskCategory) {
        // Give experience based on task category
        int experience = switch (taskCategory) {
            case "RECYCLING" -> 25;
            case "ENERGY" -> 20;
            case "WATER" -> 30;
            case "TRANSPORTATION" -> 40;
            case "WASTE" -> 35;
            case "EDUCATION" -> 15;
            default -> 10;
        };
        
        addExperienceToAvatar(experience);
        
        // Check for level-based unlocks
        checkLevelUnlocks();
    }

    private void checkLevelUnlocks() {
        int level = currentAvatar.getLevel();
        
        // Unlock items based on level
        if (level >= 2 && !unlockedHairStyles.contains("Curly")) {
            unlockHairStyle("Curly");
        }
        if (level >= 3 && !unlockedHairColors.contains("Red")) {
            unlockHairColor("Red");
        }
        if (level >= 4 && !unlockedAccessories.contains("Earrings")) {
            unlockAccessory("Earrings");
        }
        if (level >= 5 && !unlockedHairStyles.contains("Ponytail")) {
            unlockHairStyle("Ponytail");
        }
        if (level >= 6 && !unlockedHairColors.contains("Blue")) {
            unlockHairColor("Blue");
        }
        if (level >= 7 && !unlockedAccessories.contains("Necklace")) {
            unlockAccessory("Necklace");
        }
        if (level >= 8 && !unlockedHairStyles.contains("Bun")) {
            unlockHairStyle("Bun");
        }
        if (level >= 9 && !unlockedHairColors.contains("Green")) {
            unlockHairColor("Green");
        }
        if (level >= 10 && !unlockedAccessories.contains("Scarf")) {
            unlockAccessory("Scarf");
        }
        if (level >= 12 && !unlockedHairStyles.contains("Mohawk")) {
            unlockHairStyle("Mohawk");
        }
        if (level >= 15 && !unlockedHairColors.contains("Pink")) {
            unlockHairColor("Pink");
        }
        if (level >= 18 && !unlockedAccessories.contains("Headphones")) {
            unlockAccessory("Headphones");
        }
        if (level >= 20 && !unlockedAccessories.contains("Bowtie")) {
            unlockAccessory("Bowtie");
        }
    }

    public String getAvatarStatus() {
        if (currentAvatar == null) {
            return "No avatar created yet!";
        }
        
        StringBuilder status = new StringBuilder();
        status.append("🌟 ").append(currentAvatar.getName()).append(" - Level ").append(currentAvatar.getLevel()).append("\n");
        status.append("Experience: ").append(currentAvatar.getExperience()).append("/").append(currentAvatar.getLevel() * 100).append("\n");
        status.append("Unlocked Items: ").append(unlockedAccessories.size() + unlockedHairStyles.size() + unlockedHairColors.size()).append(" total");
        
        return status.toString();
    }

    public List<String> getRecentlyUnlockedItems() {
        List<String> recent = new ArrayList<>();
        // This would track recently unlocked items
        // For now, return empty list
        return recent;
    }

    public void resetAvatar() {
        currentAvatar = new Avatar();
        unlockedAccessories.clear();
        unlockedHairStyles.clear();
        unlockedHairColors.clear();
        initializeDefaultUnlocks();
    }

    public Map<String, Object> getAvatarStats() {
        Map<String, Object> stats = new HashMap<>();
        if (currentAvatar != null) {
            stats.put("level", currentAvatar.getLevel());
            stats.put("experience", currentAvatar.getExperience());
            stats.put("totalUnlockedItems", unlockedAccessories.size() + unlockedHairStyles.size() + unlockedHairColors.size());
            stats.put("unlockedAccessories", unlockedAccessories.size());
            stats.put("unlockedHairStyles", unlockedHairStyles.size());
            stats.put("unlockedHairColors", unlockedHairColors.size());
        }
        return stats;
    }
} 