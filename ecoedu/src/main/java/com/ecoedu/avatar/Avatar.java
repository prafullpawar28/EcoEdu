package com.ecoedu.avatar;

import java.util.List;

public class Avatar {
    private String hairStyle;
    private String hairColor;
    private String skinTone;
    private String eyeColor;
    private List<String> accessories;
    private String name;
    private int level;
    private int experience;

    public Avatar(String hairStyle, String hairColor, String skinTone, String eyeColor, List<String> accessories) {
        this.hairStyle = hairStyle;
        this.hairColor = hairColor;
        this.skinTone = skinTone;
        this.eyeColor = eyeColor;
        this.accessories = accessories;
        this.name = "Eco Hero";
        this.level = 1;
        this.experience = 0;
    }

    public Avatar() {
        this.hairStyle = "Short";
        this.hairColor = "Brown";
        this.skinTone = "Fair";
        this.eyeColor = "Brown";
        this.accessories = List.of();
        this.name = "Eco Hero";
        this.level = 1;
        this.experience = 0;
    }

    // Getters and Setters
    public String getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(String hairStyle) {
        this.hairStyle = hairStyle;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getSkinTone() {
        return skinTone;
    }

    public void setSkinTone(String skinTone) {
        this.skinTone = skinTone;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public List<String> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<String> accessories) {
        this.accessories = accessories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addExperience(int exp) {
        this.experience += exp;
        // Simple leveling system
        if (this.experience >= this.level * 100) {
            this.level++;
            this.experience = 0;
        }
    }

    public void addAccessory(String accessory) {
        if (!this.accessories.contains(accessory)) {
            this.accessories.add(accessory);
        }
    }

    public void removeAccessory(String accessory) {
        this.accessories.remove(accessory);
    }

    public boolean hasAccessory(String accessory) {
        return this.accessories.contains(accessory);
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("A level ").append(level).append(" eco hero with ");
        sb.append(hairStyle.toLowerCase()).append(" ").append(hairColor.toLowerCase()).append(" hair, ");
        sb.append(skinTone.toLowerCase()).append(" skin, and ");
        sb.append(eyeColor.toLowerCase()).append(" eyes.");
        
        if (!accessories.isEmpty()) {
            sb.append(" Wearing: ").append(String.join(", ", accessories));
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", hairStyle='" + hairStyle + '\'' +
                ", hairColor='" + hairColor + '\'' +
                ", skinTone='" + skinTone + '\'' +
                ", eyeColor='" + eyeColor + '\'' +
                ", accessories=" + accessories +
                '}';
    }

    public boolean equals(Avatar other) {
        if (other == null) return false;
        return this.hairStyle.equals(other.hairStyle) &&
               this.hairColor.equals(other.hairColor) &&
               this.skinTone.equals(other.skinTone) &&
               this.eyeColor.equals(other.eyeColor) &&
               this.accessories.equals(other.accessories);
    }
}
