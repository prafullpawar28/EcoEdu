package com.ecoedu.leaderboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Arrays;

import com.ecoedu.auth.FirebaseAuthService;

public class LeaderboardAndBadgesPage extends VBox {

    private TableView<StudentScore> leaderboardTable;
    private TextField searchField;
    private ComboBox<String> sortBox;
    private Label lastUpdatedLabel;

    public LeaderboardAndBadgesPage() {
        setSpacing(24);
        setPadding(new Insets(32, 40, 24, 40));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa 60%, #fffde7 100%);");

        // Header
        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Leaderboard & Badges");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));

        Button refreshBtn = new Button("⟳ Refresh");
        refreshBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        refreshBtn.setStyle("-fx-background-radius: 16; -fx-background-color: linear-gradient(to right, #b2ff59, #81d4fa); -fx-text-fill: #0288d1;");
        refreshBtn.setOnAction(e -> refreshLeaderboard());

        lastUpdatedLabel = new Label();
        lastUpdatedLabel.setFont(Font.font("Quicksand", 13));
        lastUpdatedLabel.setTextFill(Color.web("#388e3c"));

        HBox.setMargin(refreshBtn, new Insets(0, 0, 0, 24));
        HBox.setMargin(lastUpdatedLabel, new Insets(0, 0, 0, 18));
        header.getChildren().addAll(title, refreshBtn, lastUpdatedLabel);
        getChildren().add(header);

        // Controls
        HBox controls = new HBox(16);
        controls.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("Search user...");
        searchField.setFont(Font.font("Quicksand", 15));
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshLeaderboard());

        sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Total", "Game1", "Game2", "Game3");
        sortBox.setValue("Total");
        sortBox.setOnAction(e -> refreshLeaderboard());

        controls.getChildren().addAll(new Label("Sort by:"), sortBox, searchField);
        getChildren().add(controls);

        // Table setup
        leaderboardTable = new TableView<>();
        leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        leaderboardTable.setPrefHeight(500);

        TableColumn<StudentScore, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<StudentScore, Number> game1Col = new TableColumn<>("Game 1");
        game1Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGame1()));

        TableColumn<StudentScore, Number> game2Col = new TableColumn<>("Game 2");
        game2Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGame2()));

        TableColumn<StudentScore, Number> game3Col = new TableColumn<>("Game 3");
        game3Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGame3()));

        TableColumn<StudentScore, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTotal()));

        TableColumn<StudentScore, Number> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRank()));

        leaderboardTable.getColumns().addAll(nameCol, game1Col, game2Col, game3Col, totalCol, rankCol);

        getChildren().add(leaderboardTable);

        refreshLeaderboard();
    }

    public void refreshLeaderboard() {
        String[][] list = new FirebaseAuthService().getAllStudents("Student");
        if (list == null || list.length == 0) return;

        // Sort by total score descending (assuming total is at index 5)
        Arrays.sort(list, (a, b) -> Integer.compare(Integer.parseInt(b[5]), Integer.parseInt(a[5])));

        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();

        ObservableList<StudentScore> tableData = FXCollections.observableArrayList();
        for (int i = 0, rank = 1; i < list.length; i++) {
            String[] s = list[i];
            String name = s[0];
            if (!name.toLowerCase().contains(searchText)) continue;
            int game1 = Integer.parseInt(s[2]);
            int game2 = Integer.parseInt(s[3]);
            int game3 = Integer.parseInt(s[4]);
            int total = Integer.parseInt(s[5]);

            tableData.add(new StudentScore(name, game1, game2, game3, total, rank++));
        }

        // Sort based on selected field
        String sortField = sortBox.getValue();
        switch (sortField) {
            case "Game1":
                tableData.sort((a, b) -> Integer.compare(b.getGame1(), a.getGame1()));
                break;
            case "Game2":
                tableData.sort((a, b) -> Integer.compare(b.getGame2(), a.getGame2()));
                break;
            case "Game3":
                tableData.sort((a, b) -> Integer.compare(b.getGame3(), a.getGame3()));
                break;
            default:
                tableData.sort((a, b) -> Integer.compare(b.getTotal(), a.getTotal()));
        }

        // Reassign ranks after sorting
        for (int i = 0; i < tableData.size(); i++) {
            tableData.get(i).setRank(i + 1);
        }

        leaderboardTable.setItems(tableData);
        lastUpdatedLabel.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
    }

    // Table model
    public static class StudentScore {
        private final String name;
        private final int game1;
        private final int game2;
        private final int game3;
        private final int total;
        private int rank;

        public StudentScore(String name, int game1, int game2, int game3, int total, int rank) {
            this.name = name;
            this.game1 = game1;
            this.game2 = game2;
            this.game3 = game3;
            this.total = total;
            this.rank = rank;
        }

        public String getName() { return name; }
        public int getGame1() { return game1; }
        public int getGame2() { return game2; }
        public int getGame3() { return game3; }
        public int getTotal() { return total; }
        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }
    }

    // Launch page
    public static void show(Stage primaryStage) {
        LeaderboardAndBadgesPage page = new LeaderboardAndBadgesPage();
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard Table");
        primaryStage.show();
    }
}
