package com.ecoedu.modules;

import java.util.List;

public class Module {
    private final int id;
    private final String title;
    private final String description;
    private final String icon;
    private final List<Lesson> lessons;

    public Module(int id, String title, String description, String icon, List<Lesson> lessons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.lessons = lessons;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public List<Lesson> getLessons() { return lessons; }
} 