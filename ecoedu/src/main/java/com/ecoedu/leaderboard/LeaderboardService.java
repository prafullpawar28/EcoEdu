package com.ecoedu.leaderboard;

import com.google.firebase.database.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Comparator;

public class LeaderboardService {
    private static LeaderboardService instance;
    private final ObservableList<LeaderboardUserStats> users = FXCollections.observableArrayList();
    private String currentUser = "";

    private LeaderboardService() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("leaderboard");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Platform.runLater(() -> {
                    users.clear();
                    int rank = 1;
                    for (DataSnapshot child : snapshot.getChildren()) {
                        LeaderboardUserStats user = child.getValue(LeaderboardUserStats.class);
                        if (user != null) {
                            user.rank = rank++;
                            user.isCurrentUser = user.username != null && user.username.equals(currentUser);
                            users.add(user);
                        }
                    }
                    users.sort(Comparator.comparingInt(u -> -u.score));
                });
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    public static LeaderboardService getInstance() {
        if (instance == null) instance = new LeaderboardService();
        return instance;
    }

    public ObservableList<LeaderboardUserStats> getLeaderboardUsers() {
        return users;
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
        public java.util.List<String> badges;
        public boolean isCurrentUser;
        public double progress;
        public LeaderboardUserStats() {}
    }
} 