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