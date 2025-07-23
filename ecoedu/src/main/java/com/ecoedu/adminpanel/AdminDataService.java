package com.ecoedu.adminpanel;

import java.util.*;

public class AdminDataService {
    private static AdminDataService instance;
    private final List<User> users = new ArrayList<>();
    private final List<Module> modules = new ArrayList<>();
    private final List<Quiz> quizzes = new ArrayList<>();
    private final List<LogEntry> logs = new ArrayList<>();
    private final Map<String, Integer> analytics = new HashMap<>();

    public static AdminDataService getInstance() {
        if (instance == null) instance = new AdminDataService();
        return instance;
    }

    private AdminDataService() {
        // Add a default admin user if none exist
        if (users.isEmpty()) {
            users.add(new User("Admin", "admin@ecoedu.com", "Admin", "admin123"));
        }
    }

    // User management
    public List<User> getUsers() { return new ArrayList<>(users); }
    public void addUser(User user) { users.add(user); logs.add(new LogEntry("Added user: " + user.name)); }
    public void removeUser(User user) { users.remove(user); logs.add(new LogEntry("Removed user: " + user.name)); }
    public void updateUser(User user) { logs.add(new LogEntry("Updated user: " + user.name)); }

    // Module management
    public List<Module> getModules() { return new ArrayList<>(modules); }
    public void addModule(Module m) { modules.add(m); logs.add(new LogEntry("Added module: " + m.title)); }
    public void removeModule(Module m) { modules.remove(m); logs.add(new LogEntry("Removed module: " + m.title)); }
    public void updateModule(Module m) { logs.add(new LogEntry("Updated module: " + m.title)); }

    // Quiz management
    public List<Quiz> getQuizzes() { return new ArrayList<>(quizzes); }
    public void addQuiz(Quiz q) { quizzes.add(q); logs.add(new LogEntry("Added quiz: " + q.title)); }
    public void removeQuiz(Quiz q) { quizzes.remove(q); logs.add(new LogEntry("Removed quiz: " + q.title)); }
    public void updateQuiz(Quiz q) { logs.add(new LogEntry("Updated quiz: " + q.title)); }

    // Analytics
    public Map<String, Integer> getAnalytics() { return new HashMap<>(analytics); }
    public void updateAnalytics(String key, int value) { analytics.put(key, value); }

    // Logs
    public List<LogEntry> getLogs() { return new ArrayList<>(logs); }

    // Data classes
    public static class User {
        public String name, email, role, password;
        public User(String name, String email, String role, String password) {
            this.name = name;
            this.email = email;
            this.role = role;
            this.password = password;
        }
    }
    public static class Module {
        public String title, description;
        public Module(String title, String description) { this.title = title; this.description = description; }
    }
    public static class Quiz {
        public String title, category;
        public Quiz(String title, String category) { this.title = title; this.category = category; }
    }
    public static class LogEntry {
        public String message;
        public Date date = new Date();
        public LogEntry(String message) { this.message = message; }
    }
} 