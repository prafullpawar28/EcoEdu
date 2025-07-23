package com.ecoedu.avatar;

public class Avatar {
    private String hair;
    private String face;
    private String accessory;
    private String color;

    public Avatar(String hair, String face, String accessory, String color) {
        this.hair = hair;
        this.face = face;
        this.accessory = accessory;
        this.color = color;
    }

    public String getHair() { return hair; }
    public void setHair(String hair) { this.hair = hair; }
    public String getFace() { return face; }
    public void setFace(String face) { this.face = face; }
    public String getAccessory() { return accessory; }
    public void setAccessory(String accessory) { this.accessory = accessory; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
} 