package com.ecoedu.leaderboard;

import java.util.*;

public class LeaderboardService {
    private static LeaderboardService instance;
    private final Map<String, UserStats> userStats = new HashMap<>();
    private final List<Activity> activityFeed = new ArrayList<>();
    private String currentUser = "Eco Kid";

    public static LeaderboardService getInstance() {
        if (instance == null) instance = new LeaderboardService();
        return instance;
    }

    public LeaderboardService() {
        // Demo data for a more realistic leaderboard
        userStats.put("Eco Kid", new UserStats("Eco Kid"));
        userStats.put("Green Guru", new UserStats("Green Guru"));
        userStats.put("Planet Pro", new UserStats("Planet Pro"));
        userStats.put("Recycle Ranger", new UserStats("Recycle Ranger"));
        userStats.put("Nature Ninja", new UserStats("Nature Ninja"));
        userStats.get("Eco Kid").score = 120;
        userStats.get("Eco Kid").badges.addAll(Arrays.asList("Quiz Master", "Minigame Master"));
        userStats.get("Green Guru").score = 150;
        userStats.get("Green Guru").badges.addAll(Arrays.asList("Quiz Master", "Quiz Master", "Minigame Master"));
        userStats.get("Planet Pro").score = 90;
        userStats.get("Planet Pro").badges.addAll(Arrays.asList("Quiz Master"));
        userStats.get("Recycle Ranger").score = 70;
        userStats.get("Recycle Ranger").badges.addAll(Arrays.asList("Minigame Master"));
        userStats.get("Nature Ninja").score = 60;
        userStats.get("Nature Ninja").badges.addAll(Arrays.asList("Quiz Master"));
        activityFeed.add(new Activity("Eco Kid", "completed quiz: Eco Basics", new Date(System.currentTimeMillis() - 1000 * 60 * 2)));
        activityFeed.add(new Activity("Green Guru", "completed minigame: Ocean Cleanup", new Date(System.currentTimeMillis() - 1000 * 60 * 5)));
        activityFeed.add(new Activity("Planet Pro", "earned badge: Quiz Master", new Date(System.currentTimeMillis() - 1000 * 60 * 10)));
        activityFeed.add(new Activity("Recycle Ranger", "completed quiz: Recycling", new Date(System.currentTimeMillis() - 1000 * 60 * 15)));
        activityFeed.add(new Activity("Nature Ninja", "earned badge: Minigame Master", new Date(System.currentTimeMillis() - 1000 * 60 * 20)));
    }

    public void setCurrentUser(String name) { currentUser = name; }
    public String getCurrentUser() { return currentUser; }

    public void addScore(String user, int delta, String activity) {
        UserStats stats = userStats.computeIfAbsent(user, k -> new UserStats(user));
        stats.score += delta;
        activityFeed.add(0, new Activity(user, activity, new Date()));
    }

    public void addBadge(String user, String badge) {
        UserStats stats = userStats.computeIfAbsent(user, k -> new UserStats(user));
        stats.badges.add(badge);
        activityFeed.add(0, new Activity(user, "earned badge: " + badge, new Date()));
    }

    public List<UserStats> getLeaderboard() {
        List<UserStats> list = new ArrayList<>(userStats.values());
        list.sort((a, b) -> Integer.compare(b.score, a.score));
        return list;
    }

    public List<Activity> getActivityFeed() {
        return new ArrayList<>(activityFeed);
    }

    public UserStats getCurrentUserStats() {
        return userStats.getOrDefault(currentUser, new UserStats(currentUser));
    }

    public static class UserStats {
        public String name;
        public int score = 0;
        public List<String> badges = new ArrayList<>();
        public UserStats(String name) { this.name = name; }
    }
    public static class Activity {
        public String user;
        public String action;
        public Date date;
        public Activity(String user, String action, Date date) {
            this.user = user; this.action = action; this.date = date;
        }
    }
} 