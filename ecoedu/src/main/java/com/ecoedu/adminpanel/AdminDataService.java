package com.ecoedu.adminpanel;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AdminDataService {
    private static AdminDataService instance;
    private final List<User> users = new ArrayList<>();
    private final List<Module> modules = new ArrayList<>();
    private final List<Quiz> quizzes = new ArrayList<>();
    private final List<LogEntry> logs = new ArrayList<>();
    private final List<Notification> notifications = new ArrayList<>();
    private final List<Feedback> feedbacks = new ArrayList<>();
    private final List<StudentProgress> studentProgress = new ArrayList<>();
    private final Map<String, Integer> analytics = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // Real-time data tracking
    private int activeUsers = 0;
    private int totalSessions = 0;
    private double averageSessionTime = 0.0;
    private final List<SessionData> sessionHistory = new ArrayList<>();

    public static AdminDataService getInstance() {
        if (instance == null) instance = new AdminDataService();
        return instance;
    }

    private AdminDataService() {
        initializeRealisticData();
        startRealTimeUpdates();
    }

    private void initializeRealisticData() {
        // Initialize with realistic user data
        users.addAll(Arrays.asList(
            new User("Admin", "prafull@gmail.com", "Admin", "123456", "Active", LocalDateTime.now().minusDays(30)),
            new User(" Aditya Mogal", "aditya@gmail.com", "Teacher", "123456", "Active", LocalDateTime.now().minusDays(25)),
            new User("Krushna Sangle ", "krushna@gmail.com", "Teacher", "123456", "Active", LocalDateTime.now().minusDays(20)),
            new User("Neev Bagal", "neev@gmail.com", "Student", "123456", "Active", LocalDateTime.now().minusDays(15)),
            new User("Rushi Pawar ", "rushi@gmail.com", "Student", "123456", "Active", LocalDateTime.now().minusDays(10)),
            new User("Shivling Biradar", "shivling@gmail.com", "Student", "123456", "Active", LocalDateTime.now().minusDays(8)),
            new User("Shraddha Choughule", "shraddha@gmail.com", "Student", "123456", "Active", LocalDateTime.now().minusDays(5)),
            new User("Smita Shinde", "smita@gmail.com", "Student", "123456", "Active", LocalDateTime.now().minusDays(3)),
            new User("Tanmay Bodhale", "tanmay@gmail.com", "Student", "123456", "Active", LocalDateTime.now().minusDays(2)),
            new User("Sarvesh Ingle", "sarvesh@gmail.com", "Student", "123456", "Active", LocalDateTime.now().minusDays(1))
        ));

        // Initialize with realistic modules
        modules.addAll(Arrays.asList(
            new Module("Introduction to Recycling", "Learn the basics of recycling and its importance", "Beginner", 45, "Active"),
            new Module("Water Conservation", "Understanding water scarcity and conservation methods", "Intermediate", 60, "Active"),
            new Module("Renewable Energy", "Exploring solar, wind, and other renewable energy sources", "Advanced", 90, "Active"),
            new Module("Sustainable Living", "Practical tips for reducing your environmental footprint", "Beginner", 30, "Active"),
            new Module("Climate Change Awareness", "Understanding the science behind climate change", "Intermediate", 75, "Active"),
            new Module("Biodiversity Conservation", "Protecting ecosystems and wildlife", "Advanced", 80, "Active")
        ));

        // Initialize with realistic quizzes
        quizzes.addAll(Arrays.asList(
            new Quiz("Recycling Basics Quiz", "Recycling", 10, "Active"),
            new Quiz("Water Conservation Test", "Water", 15, "Active"),
            new Quiz("Energy Knowledge Check", "Energy", 12, "Active"),
            new Quiz("Sustainability Assessment", "General", 20, "Active")
        ));

        // Initialize notifications
        notifications.addAll(Arrays.asList(
            new Notification("New student registration", "Neev Bagal has joined the platform", "Info", LocalDateTime.now().minusHours(2)),
            new Notification("Quiz completion", "Tanmay Bodhale completed Recycling Basics Quiz with 85%", "Success", LocalDateTime.now().minusHours(1)),
            new Notification("Module update", "Water Conservation module has been updated", "Info", LocalDateTime.now().minusMinutes(30)),
            new Notification("System maintenance", "Scheduled maintenance completed successfully", "Info", LocalDateTime.now().minusMinutes(15))
        ));

        // Initialize feedback
        feedbacks.addAll(Arrays.asList(
            new Feedback("Great learning experience!", "The recycling module was very informative", "Rushi Pawar", 5, LocalDateTime.now().minusDays(1)),
            new Feedback("Could be more interactive", "Would love to see more hands-on activities", "Shraddha Choughule", 4, LocalDateTime.now().minusDays(2)),
            new Feedback("Excellent content", "The water conservation lessons are well-structured", "Sarvesh Ingle", 5, LocalDateTime.now().minusDays(3))
        ));

        // Initialize student progress
        studentProgress.addAll(Arrays.asList(
            new StudentProgress("Rushi Pawar", "Introduction to Recycling", 85, LocalDateTime.now().minusDays(1)),
            new StudentProgress("Shivling Biradar", "Water Conservation", 92, LocalDateTime.now().minusDays(2)),
            new StudentProgress("Tanmay Bodhale", "Renewable Energy", 78, LocalDateTime.now().minusDays(3)),
            new StudentProgress("Smita Shinde", "Sustainable Living", 88, LocalDateTime.now().minusDays(4))
        ));

        // Initialize analytics
        updateAnalytics();
    }

    private void startRealTimeUpdates() {
        // Simulate real-time user activity
        scheduler.scheduleAtFixedRate(() -> {
            // Simulate user logins/logouts
            int newActiveUsers = new Random().nextInt(5) + 1;
            activeUsers = Math.min(newActiveUsers, users.size());
            
            // Simulate session data
            if (new Random().nextBoolean()) {
                totalSessions++;
                sessionHistory.add(new SessionData(LocalDateTime.now(), new Random().nextInt(30) + 10));
            }
            
            // Update average session time
            if (!sessionHistory.isEmpty()) {
                averageSessionTime = sessionHistory.stream()
                    .mapToInt(s -> s.durationMinutes)
                    .average()
                    .orElse(0.0);
            }
            
            // Add random notifications
            if (new Random().nextInt(10) == 0) {
                addRandomNotification();
            }
            
            updateAnalytics();
        }, 0, 30, TimeUnit.SECONDS);
    }

    private void addRandomNotification() {
        String[] messages = {
            "New quiz submission received",
            "Module completion milestone reached",
            "Student achievement unlocked",
            "System performance optimized"
        };
        String[] types = {"Info", "Success", "Warning"};
        
        String message = messages[new Random().nextInt(messages.length)];
        String type = types[new Random().nextInt(types.length)];
        
        notifications.add(0, new Notification(
            message, 
            "System generated notification", 
            type, 
            LocalDateTime.now()
        ));
        
        // Keep only recent notifications
        if (notifications.size() > 50) {
            notifications.remove(notifications.size() - 1);
        }
    }

    private void updateAnalytics() {
        analytics.clear();
        analytics.put("Total Students", (int) users.stream().filter(u -> "Student".equals(u.role)).count());
        analytics.put("Total Teachers", (int) users.stream().filter(u -> "Teacher".equals(u.role)).count());
        analytics.put("Active Modules", (int) modules.stream().filter(m -> "Active".equals(m.status)).count());
        analytics.put("Completed Quizzes", totalSessions);
        analytics.put("Average Score", (int) studentProgress.stream().mapToInt(p -> p.score).average().orElse(0));
        analytics.put("Active Users", activeUsers);
    }

    // Enhanced User management
    public List<User> getUsers() { return new ArrayList<>(users); }
    public List<User> getUsersByRole(String role) { 
        return users.stream().filter(u -> role.equals(u.role)).collect(Collectors.toList()); 
    }
    public List<User> getActiveUsers() { 
        return users.stream().filter(u -> "Active".equals(u.status)).collect(Collectors.toList()); 
    }
    
    public void addUser(User user) { 
        users.add(user); 
        logs.add(new LogEntry("Added user: " + user.name, "User Management"));
        updateAnalytics();
    }
    
    public void removeUser(User user) { 
        users.remove(user); 
        logs.add(new LogEntry("Removed user: " + user.name, "User Management"));
        updateAnalytics();
    }
    
    public void updateUser(User user) { 
        logs.add(new LogEntry("Updated user: " + user.name, "User Management"));
        updateAnalytics();
    }

    // Enhanced Module management
    public List<Module> getModules() { return new ArrayList<>(modules); }
    public List<Module> getActiveModules() { 
        return modules.stream().filter(m -> "Active".equals(m.status)).collect(Collectors.toList()); 
    }
    
    public void addModule(Module m) { 
        modules.add(m); 
        logs.add(new LogEntry("Added module: " + m.title, "Module Management"));
        updateAnalytics();
    }
    
    public void removeModule(Module m) { 
        modules.remove(m); 
        logs.add(new LogEntry("Removed module: " + m.title, "Module Management"));
        updateAnalytics();
    }
    
    public void updateModule(Module m) { 
        logs.add(new LogEntry("Updated module: " + m.title, "Module Management"));
        updateAnalytics();
    }

    // Enhanced Quiz management
    public List<Quiz> getQuizzes() { return new ArrayList<>(quizzes); }
    public List<Quiz> getActiveQuizzes() { 
        return quizzes.stream().filter(q -> "Active".equals(q.status)).collect(Collectors.toList()); 
    }
    
    public void addQuiz(Quiz q) { 
        quizzes.add(q); 
        logs.add(new LogEntry("Added quiz: " + q.title, "Quiz Management"));
        updateAnalytics();
    }
    
    public void removeQuiz(Quiz q) { 
        quizzes.remove(q); 
        logs.add(new LogEntry("Removed quiz: " + q.title, "Quiz Management"));
        updateAnalytics();
    }
    
    public void updateQuiz(Quiz q) { 
        logs.add(new LogEntry("Updated quiz: " + q.title, "Quiz Management"));
        updateAnalytics();
    }

    // Enhanced Analytics
    public Map<String, Integer> getAnalytics() { return new HashMap<>(analytics); }
    public int getActiveUsersCount() { return activeUsers; }
    public int getTotalSessions() { return totalSessions; }
    public double getAverageSessionTime() { return averageSessionTime; }
    public List<SessionData> getSessionHistory() { return new ArrayList<>(sessionHistory); }

    // Enhanced Logs
    public List<LogEntry> getLogs() { return new ArrayList<>(logs); }
    public List<LogEntry> getLogsByCategory(String category) {
        return logs.stream().filter(l -> category.equals(l.category)).collect(Collectors.toList());
    }

    // Enhanced Notifications
    public List<Notification> getNotifications() { return new ArrayList<>(notifications); }
    public List<Notification> getNotificationsByType(String type) {
        return notifications.stream().filter(n -> type.equals(n.type)).collect(Collectors.toList());
    }
    public void addNotification(Notification notification) {
        notifications.add(0, notification);
        logs.add(new LogEntry("Notification added: " + notification.title, "Notifications"));
    }

    // Enhanced Feedbacks
    public List<Feedback> getFeedbacks() { return new ArrayList<>(feedbacks); }
    public double getAverageRating() {
        return feedbacks.stream().mapToInt(f -> f.rating).average().orElse(0.0);
    }
    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        logs.add(new LogEntry("Feedback received from: " + feedback.author, "Feedback"));
    }

    // Student Progress
    public List<StudentProgress> getStudentProgress() { return new ArrayList<>(studentProgress); }
    public List<StudentProgress> getProgressByStudent(String studentName) {
        return studentProgress.stream().filter(p -> studentName.equals(p.studentName)).collect(Collectors.toList());
    }

    // Search functionality
    public List<User> searchUsers(String query) {
        String q = query.toLowerCase();
        return users.stream()
            .filter(u -> u.name.toLowerCase().contains(q) || 
                        u.email.toLowerCase().contains(q) || 
                        u.role.toLowerCase().contains(q))
            .collect(Collectors.toList());
    }

    public List<Module> searchModules(String query) {
        String q = query.toLowerCase();
        return modules.stream()
            .filter(m -> m.title.toLowerCase().contains(q) || 
                        m.description.toLowerCase().contains(q) ||
                        m.difficulty.toLowerCase().contains(q))
            .collect(Collectors.toList());
    }

    // Data classes with enhanced fields
    public static class User {
        public String name, email, role, password, status;
        public LocalDateTime registrationDate;
        
        public User(String name, String email, String role, String password) {
            this(name, email, role, password, "Active", LocalDateTime.now());
        }
        
        public User(String name, String email, String role, String password, String status, LocalDateTime registrationDate) {
            this.name = name;
            this.email = email;
            this.role = role;
            this.password = password;
            this.status = status;
            this.registrationDate = registrationDate;
        }
    }
    
    public static class Module {
        public String title, description, difficulty, status;
        public int durationMinutes;
        
        public Module(String title, String description) {
            this(title, description, "Beginner", 45, "Active");
        }
        
        public Module(String title, String description, String difficulty, int durationMinutes, String status) {
            this.title = title;
            this.description = description;
            this.difficulty = difficulty;
            this.durationMinutes = durationMinutes;
            this.status = status;
        }
    }
    
    public static class Quiz {
        public String title, category, status;
        public int questionCount;
        
        public Quiz(String title, String category) {
            this(title, category, 10, "Active");
        }
        
        public Quiz(String title, String category, int questionCount, String status) {
            this.title = title;
            this.category = category;
            this.questionCount = questionCount;
            this.status = status;
        }
    }
    
    public static class LogEntry {
        public String message, category;
        public LocalDateTime timestamp;
        
        public LogEntry(String message) {
            this(message, "General");
        }
        
        public LogEntry(String message, String category) {
            this.message = message;
            this.category = category;
            this.timestamp = LocalDateTime.now();
        }
    }
    
    public static class Notification {
        public String title, message, type;
        public LocalDateTime timestamp;
        
        public Notification(String title, String message, String type, LocalDateTime timestamp) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.timestamp = timestamp;
        }
    }
    
    public static class Feedback {
        public String title, message, author;
        public int rating;
        public LocalDateTime timestamp;
        
        public Feedback(String title, String message, String author, int rating, LocalDateTime timestamp) {
            this.title = title;
            this.message = message;
            this.author = author;
            this.rating = rating;
            this.timestamp = timestamp;
        }
    }
    
    public static class StudentProgress {
        public String studentName, moduleName;
        public int score;
        public LocalDateTime completionDate;
        
        public StudentProgress(String studentName, String moduleName, int score, LocalDateTime completionDate) {
            this.studentName = studentName;
            this.moduleName = moduleName;
            this.score = score;
            this.completionDate = completionDate;
        }
    }
    
    public static class SessionData {
        public LocalDateTime startTime;
        public int durationMinutes;
        
        public SessionData(LocalDateTime startTime, int durationMinutes) {
            this.startTime = startTime;
            this.durationMinutes = durationMinutes;
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
} 