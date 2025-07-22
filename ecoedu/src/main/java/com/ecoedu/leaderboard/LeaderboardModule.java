package com.ecoedu.leaderboard;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardModule extends VBox {
    private final TableView<LeaderboardEntry> table = new TableView<>();
    private final ObservableList<LeaderboardEntry> leaderboardData = FXCollections.observableArrayList();
    private final Label userRankLabel = new Label();
    private String currentUser = "Eco Kid";
    private String leaderboardType = "All";
    private String sortType = "Score";

    public LeaderboardModule(Stage primaryStage) {
        setSpacing(24);
        setPadding(new Insets(36, 60, 36, 60));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // Animated eco background
        Region animatedBg = new Region();
        animatedBg.setPrefSize(1366, 768);
        animatedBg.setStyle("-fx-background-radius: 0; -fx-background-color: linear-gradient(to bottom, #b2ebf2 0%, #e0f7fa 60%, #b2dfdb 100%);");
        getChildren().add(animatedBg);

        // Header
        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("🌱 EcoEdu Leaderboard");
        title.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.web("#43a047"));
        ComboBox<String> typeSwitch = new ComboBox<>(FXCollections.observableArrayList("All", "Quizzes", "Games"));
        typeSwitch.setValue("All");
        typeSwitch.setStyle("-fx-background-radius: 16; -fx-background-color: #b2ff59; -fx-font-size: 16px;");
        typeSwitch.setOnAction(e -> {
            leaderboardType = typeSwitch.getValue();
            reloadData();
        });
        ComboBox<String> sortSwitch = new ComboBox<>(FXCollections.observableArrayList("Score", "Fastest", "Recent"));
        sortSwitch.setValue("Score");
        sortSwitch.setStyle("-fx-background-radius: 16; -fx-background-color: #fffde7; -fx-font-size: 16px;");
        sortSwitch.setOnAction(e -> {
            sortType = sortSwitch.getValue();
            reloadData();
        });
        Button refreshBtn = new Button("⟳ Refresh");
        refreshBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24; -fx-cursor: hand;");
        refreshBtn.setOnAction(e -> reloadData());
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(title, typeSwitch, sortSwitch, spacer, refreshBtn, backBtn);
        header.setStyle("-fx-background-radius: 0 0 32 32; -fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); -fx-effect: dropshadow(gaussian, #43e97b, 12, 0.2, 0, 4);");
        getChildren().add(header);

        // TableView setup
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(480);
        table.setPlaceholder(new Label("No data available."));
        table.setStyle("-fx-background-radius: 18; -fx-background-color: #fffde7; -fx-effect: dropshadow(gaussian, #b2ff59, 12, 0.15, 0, 4);");

        TableColumn<LeaderboardEntry, String> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(data -> data.getValue().rankProperty());
        rankCol.setCellFactory(col -> new MedalCell());

        TableColumn<LeaderboardEntry, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(data -> data.getValue().usernameProperty());
        userCol.setCellFactory(col -> new HighlightUserCell());

        TableColumn<LeaderboardEntry, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty());

        TableColumn<LeaderboardEntry, String> timeCol = new TableColumn<>("Time Taken");
        timeCol.setCellValueFactory(data -> data.getValue().timeTakenProperty());

        TableColumn<LeaderboardEntry, String> typeCol = new TableColumn<>("Activity");
        typeCol.setCellValueFactory(data -> data.getValue().activityTypeProperty());
        typeCol.setCellFactory(col -> new ActivityTypeCell());

        TableColumn<LeaderboardEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());

        table.getColumns().addAll(rankCol, userCol, scoreCol, timeCol, typeCol, dateCol);
        table.setItems(leaderboardData);

        // User rank at bottom
        userRankLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        userRankLabel.setTextFill(Color.web("#388e3c"));
        userRankLabel.setPadding(new Insets(18, 0, 0, 0));
        userRankLabel.setAlignment(Pos.CENTER);

        getChildren().addAll(table, userRankLabel);

        reloadData();
    }

    private void reloadData() {
        List<LeaderboardEntry> allEntries = fetchLeaderboardData();
        List<LeaderboardEntry> filtered = allEntries.stream()
                .filter(e -> leaderboardType.equals("All") || e.getActivityType().equalsIgnoreCase(leaderboardType))
                .collect(Collectors.toList());
        Comparator<LeaderboardEntry> comparator = Comparator.comparingInt(LeaderboardEntry::getScore).reversed();
        if (sortType.equals("Fastest")) {
            comparator = Comparator.comparingInt(LeaderboardEntry::getTimeTaken);
        } else if (sortType.equals("Recent")) {
            comparator = Comparator.comparing(LeaderboardEntry::getDate).reversed();
        }
        filtered.sort(comparator);
        List<LeaderboardEntry> top10 = filtered.stream().limit(10).collect(Collectors.toList());
        Platform.runLater(() -> {
            leaderboardData.clear();
            leaderboardData.addAll(top10);
            animateTable();
            OptionalInt userRank = filtered.stream()
                    .map(LeaderboardEntry::getUsername)
                    .collect(Collectors.toList())
                    .indexOf(currentUser) >= 0 ?
                    OptionalInt.of(filtered.stream().map(LeaderboardEntry::getUsername).collect(Collectors.toList()).indexOf(currentUser) + 1)
                    : OptionalInt.empty();
            LeaderboardEntry userEntry = filtered.stream().filter(e -> e.getUsername().equals(currentUser)).findFirst().orElse(null);
            if (userEntry != null && userRank.isPresent() && userRank.getAsInt() > 10) {
                userRankLabel.setText("Your Rank: #" + userRank.getAsInt() + " | " + userEntry.getUsername() + " | Score: " + userEntry.getScore() + " | " + userEntry.getActivityType() + " | " + userEntry.getDateString());
            } else if (userEntry != null) {
                userRankLabel.setText("You are in the Top 10!");
            } else {
                userRankLabel.setText("");
            }
        });
    }

    // Stub: Replace with real Firebase/MySQL fetch
    private List<LeaderboardEntry> fetchLeaderboardData() {
        // Demo data
        List<LeaderboardEntry> list = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        list.add(new LeaderboardEntry(1, "Eco Kid", 120, 90, "Quizzes", new Date()));
        list.add(new LeaderboardEntry(2, "Green Guru", 150, 80, "Games", new Date(System.currentTimeMillis() - 1000 * 60 * 60)));
        list.add(new LeaderboardEntry(3, "Planet Pro", 90, 110, "Quizzes", new Date(System.currentTimeMillis() - 1000 * 60 * 120)));
        list.add(new LeaderboardEntry(4, "Recycle Ranger", 70, 130, "Games", new Date(System.currentTimeMillis() - 1000 * 60 * 180)));
        list.add(new LeaderboardEntry(5, "Nature Ninja", 60, 140, "Quizzes", new Date(System.currentTimeMillis() - 1000 * 60 * 240)));
        // ... add more demo entries as needed
        return list;
    }

    // Animate table rows (fade in, highlight current user)
    private void animateTable() {
        for (int i = 0; i < table.getItems().size(); i++) {
            TableRow<LeaderboardEntry> row = (TableRow<LeaderboardEntry>) table.queryAccessibleAttribute(javafx.scene.AccessibleAttribute.ROW_AT_INDEX, i);
            if (row != null) {
                FadeTransition ft = new FadeTransition(Duration.millis(600), row);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
                LeaderboardEntry entry = table.getItems().get(i);
                if (entry.getUsername().equals(currentUser)) {
                    row.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #ffd54f, 10, 0.15, 0, 3);");
                } else {
                    row.setStyle("");
                }
                row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #b2ff59; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #43e97b, 10, 0.18, 0, 4);"));
                row.setOnMouseExited(e -> row.setStyle(entry.getUsername().equals(currentUser) ? "-fx-background-color: #fffde7; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #ffd54f, 10, 0.15, 0, 3);" : ""));
            }
        }
    }

    // Custom cell for activity type with eco icons
    private static class ActivityTypeCell extends TableCell<LeaderboardEntry, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Label label = new Label(item);
                if (item.equalsIgnoreCase("Quizzes")) label.setText("📝 Quizzes");
                else if (item.equalsIgnoreCase("Games")) label.setText("🎮 Games");
                else label.setText("🌱 " + item);
                label.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
                setGraphic(label);
                setText(null);
            }
        }
    }

    // Custom cell to highlight current user
    private class HighlightUserCell extends TableCell<LeaderboardEntry, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Label label = new Label(item);
                label.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
                if (item.equals(currentUser)) {
                    label.setTextFill(Color.web("#ffb300"));
                    label.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 12; -fx-padding: 2 8; -fx-effect: dropshadow(gaussian, #ffd54f, 6, 0.15, 0, 2);");
                } else {
                    label.setTextFill(Color.web("#263238"));
                }
                setGraphic(label);
                setText(null);
            }
        }
    }

    // Medal cell for top 3
    private static class MedalCell extends TableCell<LeaderboardEntry, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Label label = new Label(item);
                int rank = 0;
                try { rank = Integer.parseInt(item.replace("#", "")); } catch (Exception ignored) {}
                if (rank == 1) label.setText("🥇");
                else if (rank == 2) label.setText("🥈");
                else if (rank == 3) label.setText("🥉");
                label.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
                setGraphic(label);
                setText(null);
            }
        }
    }

    // Data model
    public static class LeaderboardEntry {
        private final int rank;
        private final String username;
        private final int score;
        private final int timeTaken; // seconds
        private final String activityType;
        private final Date date;

        public LeaderboardEntry(int rank, String username, int score, int timeTaken, String activityType, Date date) {
            this.rank = rank;
            this.username = username;
            this.score = score;
            this.timeTaken = timeTaken;
            this.activityType = activityType;
            this.date = date;
        }

        public javafx.beans.property.SimpleStringProperty rankProperty() { return new javafx.beans.property.SimpleStringProperty("#" + rank); }
        public javafx.beans.property.SimpleStringProperty usernameProperty() { return new javafx.beans.property.SimpleStringProperty(username); }
        public javafx.beans.property.SimpleStringProperty scoreProperty() { return new javafx.beans.property.SimpleStringProperty(String.valueOf(score)); }
        public javafx.beans.property.SimpleStringProperty timeTakenProperty() { return new javafx.beans.property.SimpleStringProperty(timeTaken + "s"); }
        public javafx.beans.property.SimpleStringProperty activityTypeProperty() { return new javafx.beans.property.SimpleStringProperty(activityType); }
        public javafx.beans.property.SimpleStringProperty dateProperty() { return new javafx.beans.property.SimpleStringProperty(getDateString()); }
        public String getUsername() { return username; }
        public int getScore() { return score; }
        public int getTimeTaken() { return timeTaken; }
        public String getActivityType() { return activityType; }
        public Date getDate() { return date; }
        public String getDateString() { return java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()); }
    }

    // Show method for integration
    public static void show(Stage primaryStage) {
        LeaderboardModule page = new LeaderboardModule(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard");
        primaryStage.show();
    }
} 