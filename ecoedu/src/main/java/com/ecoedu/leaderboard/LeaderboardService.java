package com.ecoedu.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class LeaderboardService {
    private static LeaderboardService instance;
    private String currentUser = "Daisy";
    private final List<LeaderboardUserStats> users;

    private LeaderboardService() {
        users = new ArrayList<>();
        // Simulate real-time data (could be replaced with DB/service calls)
        users.add(new LeaderboardUserStats("Alice", 1500, 12, 8, List.of("Quiz Master"), false, 0.95));
        users.add(new LeaderboardUserStats("Bob", 1200, 10, 7, List.of("Minigame Master"), false, 0.82));
        users.add(new LeaderboardUserStats("Charlie", 1100, 8, 6, List.of(), false, 0.77));
        users.add(new LeaderboardUserStats("Daisy", 950, 7, 5, List.of("Quiz Master", "Minigame Master"), true, 0.65));
        users.add(new LeaderboardUserStats("Evan", 900, 6, 4, List.of(), false, 0.58));
    }

    public static LeaderboardService getInstance() {
        if (instance == null) instance = new LeaderboardService();
        return instance;
    }

    public List<LeaderboardUserStats> getLeaderboardUsers() {
        // Simulate real-time update (shuffle, update scores, etc.)
        users.sort((a, b) -> Integer.compare(b.score, a.score));
        int rank = 1;
        for (LeaderboardUserStats u : users) {
            u.rank = rank++;
            u.isCurrentUser = u.username.equals(currentUser);
        }
        return new ArrayList<>(users);
    }

    public void refreshData() {
        // Simulate a data refresh (could pull from DB or API)
        Random rand = new Random();
        for (LeaderboardUserStats u : users) {
            u.score += rand.nextInt(10); // Simulate score change
            u.progress = Math.min(1.0, u.progress + rand.nextDouble() * 0.01);
        }
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    public static class LeaderboardUserStats {
        public int rank;
        public String username;
        public int score;
        public int quizzesCompleted;
        public int minigamesPlayed;
        public List<String> badges;
        public boolean isCurrentUser;
        public double progress;
        public LeaderboardUserStats(String username, int score, int quizzesCompleted, int minigamesPlayed, List<String> badges, boolean isCurrentUser, double progress) {
            this.username = username;
            this.score = score;
            this.quizzesCompleted = quizzesCompleted;
            this.minigamesPlayed = minigamesPlayed;
            this.badges = badges;
            this.isCurrentUser = isCurrentUser;
            this.progress = progress;
        }
    }
} 