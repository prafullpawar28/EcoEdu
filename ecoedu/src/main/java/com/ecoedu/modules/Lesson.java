package com.ecoedu.modules;

public class Lesson {
    private final int id;
    private final String title;
    private final String content;
    private final String image;

    public Lesson(int id, String title, String content, String image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImage() { return image; }
} 