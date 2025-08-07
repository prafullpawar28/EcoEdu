// package com.ecoedu.leaderboard;

// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.stage.Stage;

// import java.util.ArrayList;
// import java.util.Arrays;

// import com.ecoedu.auth.FirebaseAuthService;

// public class LeaderboardAndBadgesPage extends VBox {

//     private TableView<StudentScore> leaderboardTable;
//     private TableView<StudentScore> leaderboardTableQuiz;
//     private TextField searchField;
//     private ComboBox<String> sortBox;
//     private Label lastUpdatedLabel;

//     public LeaderboardAndBadgesPage() {
//         setSpacing(24);
//         setPadding(new Insets(32, 40, 24, 40));
//         setAlignment(Pos.TOP_CENTER);
//         setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa 60%, #fffde7 100%);");

//         // Header
//         HBox header = new HBox(18);
//         header.setAlignment(Pos.CENTER_LEFT);
//         Label title = new Label("Leaderboard & Badges");
//         title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
//         title.setTextFill(Color.web("#0288d1"));

//         Button refreshBtn = new Button("⟳ Refresh");
//         refreshBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
//         refreshBtn.setStyle("-fx-background-radius: 16; -fx-background-color: linear-gradient(to right, #b2ff59, #81d4fa); -fx-text-fill: #0288d1;");
//         refreshBtn.setOnAction(e -> refreshLeaderboard());

//         lastUpdatedLabel = new Label();
//         lastUpdatedLabel.setFont(Font.font("Quicksand", 13));
//         lastUpdatedLabel.setTextFill(Color.web("#388e3c"));

//         HBox.setMargin(refreshBtn, new Insets(0, 0, 0, 24));
//         HBox.setMargin(lastUpdatedLabel, new Insets(0, 0, 0, 18));
//         header.getChildren().addAll(title, refreshBtn, lastUpdatedLabel);
//         getChildren().add(header);

//         // Controls
//         HBox controls = new HBox(16);
//         controls.setAlignment(Pos.CENTER_LEFT);
//         searchField = new TextField();
//         searchField.setPromptText("Search user...");
//         searchField.setFont(Font.font("Quicksand", 15));
//         searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshLeaderboard());

//         sortBox = new ComboBox<>();
//         sortBox.getItems().addAll("Games","Quiz");
//         sortBox.setValue("Games");
//         sortBox.setOnAction(e -> refreshLeaderboard());

//         controls.getChildren().addAll(new Label("Sort by:"), sortBox, searchField);
//         getChildren().add(controls);

//         // Table setup
//         leaderboardTable = new TableView<>();
//         leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//         leaderboardTable.setPrefHeight(500);

//         TableColumn<StudentScore, String> nameCol = new TableColumn<>("Name");
//         nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

//         TableColumn<StudentScore, Number> game1Col = new TableColumn<>("Game 1");
//         game1Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGame1()));

//         TableColumn<StudentScore, Number> game2Col = new TableColumn<>("Game 2");
//         game2Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGame2()));

//         TableColumn<StudentScore, Number> game3Col = new TableColumn<>("Game 3");
//         game3Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getGame3()));

//         TableColumn<StudentScore, Number> totalCol = new TableColumn<>("Total");
//         totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTotal()));

//         TableColumn<StudentScore, Number> rankCol = new TableColumn<>("Rank");
//         rankCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRank()));

//         leaderboardTable.getColumns().addAll(nameCol, game1Col, game2Col, game3Col, totalCol, rankCol);

//         getChildren().add(leaderboardTable);
//         refreshLeaderboard();
//     }

//     public void refreshLeaderboard() {
//         String[][] list = new FirebaseAuthService().getAllStudents("Student");
//         if (list == null || list.length == 0) return;

//         // Sort by total score descending (assuming total is at index 5)
//         Arrays.sort(list, (a, b) -> Integer.compare(Integer.parseInt(b[5]), Integer.parseInt(a[5])));

//         String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();

//         ObservableList<StudentScore> tableData = FXCollections.observableArrayList();
//         for (int i = 0, rank = 1; i < list.length; i++) {
//             String[] s = list[i];
//             String name = s[0];
//             if (!name.toLowerCase().contains(searchText)) continue;
//             int game1 = Integer.parseInt(s[2]);
//             int game2 = Integer.parseInt(s[3]);
//             int game3 = Integer.parseInt(s[4]);
//             int total = Integer.parseInt(s[5]);

//             tableData.add(new StudentScore(name, game1, game2, game3, total, rank++));
//         }

//         // Sort based on selected field
//         String sortField = sortBox.getValue();
//         switch (sortField) {
//             case "Quiz":
//                 //tableData.sort((a, b) -> Integer.compare(b.getGame1(), a.getGame1()));
//                 if(leaderboardTableQuiz!=null) {
//                     getChildren().remove(leaderboardTable);
//                     getChildren().add(leaderboardTableQuiz);
//                     break;
//                 }
//                ArrayList<Object>[] al=new FirebaseAuthService().getAllStudentsQuiz("Student");
//                Arrays.sort(al, (a, b) ->(Integer)b.get(7)- (Integer)a.get(7));
//                 leaderboardTableQuiz = new TableView<>();
//                 getChildren().remove(leaderboardTable);
//                 getChildren().add(leaderboardTableQuiz);
//                 break;
//             case "Games":
//                 getChildren().add(leaderboardTable);
//                 break;
//             case "Game3":
//                 tableData.sort((a, b) -> Integer.compare(b.getGame3(), a.getGame3()));
//                 break;
//             default:
//                 tableData.sort((a, b) -> Integer.compare(b.getTotal(), a.getTotal()));
//         }

//         // Reassign ranks after sorting
//         for (int i = 0; i < tableData.size(); i++) {
//             tableData.get(i).setRank(i + 1);
//         }

//         leaderboardTable.setItems(tableData);
//         lastUpdatedLabel.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
//     }
//     private TableView createQuizeTable(ArrayList<Object>[] list){
//         TableView<StudentScore> leaderboardTable = new TableView<>();
//         leaderboardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//         leaderboardTable.setPrefHeight(500);

//         TableColumn<StudentScore, String> nameCol = new TableColumn<>("Name");
//         nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

//         TableColumn<StudentScore, Number> game1Col = new TableColumn<>("Quiz 1");
//         game1Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuiz1()));

//         TableColumn<StudentScore, Number> game2Col = new TableColumn<>("Quiz 2");
//         game2Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuiz2()));

//         TableColumn<StudentScore, Number> game3Col = new TableColumn<>("Quiz 3");
//         game3Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuiz3()));

//         TableColumn<StudentScore, Number> game4Col = new TableColumn<>("Quiz 4");
//         game3Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuiz4()));

//         TableColumn<StudentScore, Number> game5Col = new TableColumn<>("Quiz 5");
//         game3Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuiz5()));

//         TableColumn<StudentScore, Number> game6Col = new TableColumn<>("Quiz 6");
//         game3Col.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuiz6()));

//         TableColumn<StudentScore, Number> totalCol = new TableColumn<>("Total");
//         totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuizTotal()));

//         TableColumn<StudentScore, Number> rankCol = new TableColumn<>("Rank");
//         rankCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRank()));

//         leaderboardTable.getColumns().addAll(nameCol, game1Col, game2Col, game3Col,game4Col,game5Col,game6Col, totalCol, rankCol);
//           ObservableList<StudentScore> tableData = FXCollections.observableArrayList();
//         for (int i = 0, rank = 1; i < list.length; i++) {
//             ArrayList<Object> s = list[i];
//             String name = (String)s.get(0);
//           //  if (!name.toLowerCase().contains(searchText)) continue;
//             int quiz1 =(Integer)s.get(1);
//             int quiz2 =(Integer)s.get(2);
//             int quiz3 =(Integer)s.get(3);
//             int quiz4 =(Integer)s.get(4);
//             int quiz5 =(Integer)s.get(5);
//             int quiz6 =(Integer)s.get(6);
//             int total =quiz1+quiz2+quiz3+quiz4+quiz5+quiz6;
//             tableData.add(new StudentScore(name,quiz1,quiz2,quiz3,quiz4,quiz5,quiz6,total, rank++));
//         }

//         // Sort based on selected field
//         String sortField = sortBox.getValue();
//         switch (sortField) {
//             case "Quiz":
//                 //tableData.sort((a, b) -> Integer.compare(b.getGame1(), a.getGame1()));
//                 if(leaderboardTableQuiz!=null) {
//                     getChildren().remove(leaderboardTableQuiz);
//                     getChildren().add(leaderboardTableQuiz);
//                     break;
//                 }
//                  ArrayList<Object>[] al=new FirebaseAuthService().getAllStudentsQuiz("Student");
//                Arrays.sort(al, (a, b) ->(Integer)b.get(7)- (Integer)a.get(7));
//                 leaderboardTableQuiz = createQuizeTable(al);
//                 getChildren().remove(leaderboardTable);
//                 getChildren().add(leaderboardTableQuiz);
//                 break;
//             case "Games":
//                 getChildren().add(leaderboardTable);
//                 break;
//             case "Game3":
//                 tableData.sort((a, b) -> Integer.compare(b.getGame3(), a.getGame3()));
//                 break;
//             default:
//                 tableData.sort((a, b) -> Integer.compare(b.getTotal(), a.getTotal()));
//         }

//         // Reassign ranks after sorting
//         for (int i = 0; i < tableData.size(); i++) {
//             tableData.get(i).setRank(i + 1);
//         }

//         leaderboardTable.setItems(tableData);
//         lastUpdatedLabel.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
//         return leaderboardTable;

//     }

//     // Table model
//     public static class StudentScore {
//         private  String name;
//         private  int game1;
//         private  int game2;
//         private  int game3;
//         private  int quiz1;
//         private  int quiz2;
//         private  int quiz3;
//         private  int quiz4;
//         private  int quiz5;
//         private  int quiz6;
//         private  int total;
//         private  int quizTotal;
//         private int rank;

//         public StudentScore(String name, int game1, int game2, int game3, int total, int rank) {
//             this.name = name;
//             this.game1 = game1;
//             this.game2 = game2;
//             this.game3 = game3;

//             this.total = total;
//             this.rank = rank;
//         }
//         public StudentScore(String name, int quiz1, int quiz2, int quiz3, int quiz4, int quiz5, int quiz6,int quizTotal, int rank) {
//             this.quiz1=quiz1;
//             this.quiz2=quiz2;
//             this.quiz3=quiz3;
//             this.quiz4=quiz4;
//             this.quiz5=quiz5;
//             this.quiz6=quiz6;
//             this.name = name;
//             this.quizTotal = quizTotal;

//         }
//         public String getName() { return name; }
//         public int getGame1() { return game1; }
//         public int getGame2() { return game2; }
//         public int getGame3() { return game3; }
//         public int getTotal() { return total; }
//         public int getRank() { return rank; }
//         public int getQuiz1() { return quiz1; }
//         public int getQuiz2() { return quiz2; }
//         public int getQuiz3() { return quiz3; }
//         public int getQuiz4() { return quiz4; }
//         public int getQuiz5() { return quiz5; }
//         public int getQuiz6() { return quiz6; }
//         public int getQuizTotal() { return quizTotal; }
//         public void setRank(int rank) { this.rank = rank; }
//     }

//     // Launch page
//     public static void show(Stage primaryStage) {
//         LeaderboardAndBadgesPage page = new LeaderboardAndBadgesPage();
//         Scene scene = new Scene(page, 1366, 768);
//         primaryStage.setScene(scene);
//         primaryStage.setTitle("EcoEdu - Leaderboard Table");
//         primaryStage.show();
//     }
// }
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

import java.util.ArrayList;
import java.util.Arrays;

import com.ecoedu.auth.FirebaseAuthService;

public class LeaderboardAndBadgesPage extends VBox {

    private TableView<StudentScore> leaderboardTable;
    private TableView<StudentScore> leaderboardTableQuiz;
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
        sortBox.getItems().addAll("Games", "Quiz");
        sortBox.setValue("Games");
        sortBox.setOnAction(e -> refreshLeaderboard());

        controls.getChildren().addAll(new Label("Sort by:"), sortBox, searchField);
        getChildren().add(controls);

        // Game Table setup (initially visible)
        leaderboardTable = createGameTable();
        getChildren().add(leaderboardTable);

        refreshLeaderboard();
    }

    private TableView<StudentScore> createGameTable() {
        TableView<StudentScore> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(500);

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

        table.getColumns().addAll(nameCol, game1Col, game2Col, game3Col, totalCol, rankCol);
        return table;
    }

    private void switchTableView(TableView<?> newTable) {
        getChildren().remove(leaderboardTable);
        if (leaderboardTableQuiz != null) {
            getChildren().remove(leaderboardTableQuiz);
        }
        getChildren().add(newTable);
    }

    public void refreshLeaderboard() {
        String sortField = sortBox.getValue();
        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();

        if ("Quiz".equals(sortField)) {
            ArrayList<Object>[] al = new FirebaseAuthService().getAllStudentsQuiz("Student");
            Arrays.sort(al, (a, b) ->Long.compare((Long)b.get(7), (Long)a.get(7)));
            leaderboardTableQuiz = createQuizTable(al);
            switchTableView(leaderboardTableQuiz);
        } else {
            String[][] list = new FirebaseAuthService().getAllStudents("Student");
            if (list == null || list.length == 0) return;

            Arrays.sort(list, (a, b) -> Integer.compare(Integer.parseInt(b[5]), Integer.parseInt(a[5])));

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

            for (int i = 0; i < tableData.size(); i++) {
                tableData.get(i).setRank(i + 1);
            }

            leaderboardTable.setItems(tableData);
            switchTableView(leaderboardTable);
        }

        lastUpdatedLabel.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
    }

    private TableView<StudentScore> createQuizTable(ArrayList<Object>[] list) {
        TableView<StudentScore> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(500);

        TableColumn<StudentScore, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<StudentScore, Number> q1 = new TableColumn<>("Quiz 1");
        q1.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((int)data.getValue().getQuiz1()));
        TableColumn<StudentScore, Number> q2 = new TableColumn<>("Quiz 2");
        q2.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((int)data.getValue().getQuiz2()));
        TableColumn<StudentScore, Number> q3 = new TableColumn<>("Quiz 3");
        q3.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((int)data.getValue().getQuiz3()));
        TableColumn<StudentScore, Number> q4 = new TableColumn<>("Quiz 4");
        q4.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((int)data.getValue().getQuiz4()));
        TableColumn<StudentScore, Number> q5 = new TableColumn<>("Quiz 5");
        q5.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((int)data.getValue().getQuiz5()));
        TableColumn<StudentScore, Number> q6 = new TableColumn<>("Quiz 6");
        q6.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((int)data.getValue().getQuiz6()));

        TableColumn<StudentScore, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty((int)data.getValue().getQuizTotal()));

        TableColumn<StudentScore, Number> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRank()));

        table.getColumns().addAll(nameCol, q1, q2, q3, q4, q5, q6, totalCol, rankCol);

        ObservableList<StudentScore> tableData = FXCollections.observableArrayList();
        for (int i = 0, rank = 1; i < list.length; i++) {
            ArrayList<Object> s = list[i];
            String name = (String) s.get(0);
            long quiz1 = (Long) s.get(1);
            long quiz2 = (Long) s.get(2);
            long quiz3 = (Long) s.get(3);
            long quiz4 = (Long) s.get(4);
            long quiz5 = (Long) s.get(5);
            long quiz6 = (Long) s.get(6);
            long total = quiz1 + quiz2 + quiz3 + quiz4 + quiz5 + quiz6;
            tableData.add(new StudentScore(name, quiz1, quiz2, quiz3, quiz4, quiz5, quiz6, total, rank++));
        }

        for (int i = 0; i < tableData.size(); i++) {
            tableData.get(i).setRank(i + 1);
        }

        table.setItems(tableData);
        return table;
    }

    public static class StudentScore {
        private String name;
        private int game1, game2, game3, total, rank;
        private long quiz1, quiz2, quiz3, quiz4, quiz5, quiz6, quizTotal;

        public StudentScore(String name, int game1, int game2, int game3, int total, int rank) {
            this.name = name;
            this.game1 = game1;
            this.game2 = game2;
            this.game3 = game3;
            this.total = total;
            this.rank = rank;
        }
        public StudentScore(String name, long quiz1, long quiz2, long quiz3, long quiz4,long quiz5, long quiz6, long quizTotal, int rank) {
            this.name = name;
            this.quiz1 = quiz1;
            this.quiz2 = quiz2;
            this.quiz3 = quiz3;
            this.quiz4 = quiz4;
            this.quiz5 = quiz5;
            this.quiz6 = quiz6;
            this.quizTotal = quizTotal;
            this.rank = rank;
        }

        public String getName() { return name; }
        public int getGame1() { return game1; }
        public int getGame2() { return game2; }
        public int getGame3() { return game3; }
        public int getTotal() { return total; }
        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }

        public long getQuiz1() { return quiz1; }
        public long getQuiz2() { return quiz2; }
        public long getQuiz3() { return quiz3; }
        public long getQuiz4() { return quiz4; }
        public long getQuiz5() { return quiz5; }
        public long getQuiz6() { return quiz6; }
        public long getQuizTotal() { return quizTotal; }
    }

    public static void show(Stage primaryStage) {
        LeaderboardAndBadgesPage page = new LeaderboardAndBadgesPage();
        Scene scene = new Scene(page, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Leaderboard Table");
        primaryStage.show();
    }
}
