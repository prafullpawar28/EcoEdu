package com.ecoedu.quiz;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;
import com.ecoedu.leaderboard.LeaderboardService;

public class QuizPage extends VBox {
    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private Label questionLabel, feedbackLabel, scoreLabel;
    private List<ToggleButton> optionButtons;
    private Button nextBtn, skipBtn, backBtn;
    private ToggleGroup group;
    private Stage primaryStage;
    private String quizCategory;

    public QuizPage(Stage primaryStage, String quizCategory, List<Question> questions) {
        this.primaryStage = primaryStage;
        this.quizCategory = quizCategory;
        this.questions = new ArrayList<>(questions);
        Collections.shuffle(this.questions, new Random());
        setSpacing(32);
        setPadding(new Insets(40, 60, 40, 60));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // Back button
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        topBar.getChildren().add(backBtn);
        getChildren().add(topBar);

        Label title = new Label("Quizzes & Puzzles");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        scoreLabel.setTextFill(Color.web("#43a047"));
        getChildren().add(scoreLabel);

        // Questions
        questionLabel = new Label();
        questionLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 24));
        questionLabel.setTextFill(Color.web("#388e3c"));
        questionLabel.setWrapText(true);
        getChildren().add(questionLabel);

        group = new ToggleGroup();
        optionButtons = new ArrayList<>();
        VBox optionsBox = new VBox(16);
        optionsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            ToggleButton btn = new ToggleButton();
            btn.setFont(Font.font("Quicksand", 18));
            btn.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 16; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #b2ff59, 4, 0.1, 0, 1);");
            btn.setToggleGroup(group);
            int idx = i;
            btn.setOnAction(e -> handleOptionSelected(idx));
            optionButtons.add(btn);
            optionsBox.getChildren().add(btn);
        }
        getChildren().add(optionsBox);

        feedbackLabel = new Label("");
        feedbackLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        feedbackLabel.setTextFill(Color.web("#d32f2f"));
        feedbackLabel.setWrapText(true);
        getChildren().add(feedbackLabel);

        HBox navBox = new HBox(24);
        navBox.setAlignment(Pos.CENTER);
        nextBtn = new Button("Next");
        nextBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        nextBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        nextBtn.setOnAction(e -> nextQuestion());
        skipBtn = new Button("Skip");
        skipBtn.setFont(Font.font("Quicksand", 16));
        skipBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #263238; -fx-background-radius: 16; -fx-padding: 8 32; -fx-cursor: hand;");
        skipBtn.setOnAction(e -> skipQuestion());
        navBox.getChildren().addAll(nextBtn, skipBtn);
        getChildren().add(navBox);

        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            showResults();
            return;
        }
        Question q = questions.get(currentIndex);
        questionLabel.setText("Q" + (currentIndex + 1) + ": " + q.text);
        for (int i = 0; i < optionButtons.size(); i++) {
            optionButtons.get(i).setText(q.options[i]);
            optionButtons.get(i).setDisable(false);
            optionButtons.get(i).setSelected(false);
            optionButtons.get(i).setStyle("-fx-background-color: #fffde7; -fx-background-radius: 16; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #b2ff59, 4, 0.1, 0, 1);");
        }
        feedbackLabel.setText("");
        nextBtn.setDisable(true);
        skipBtn.setDisable(false);
        group.selectToggle(null);
    }

    private void handleOptionSelected(int selectedIdx) {
        Question q = questions.get(currentIndex);
        for (int i = 0; i < optionButtons.size(); i++) {
            optionButtons.get(i).setDisable(true);
        }
        skipBtn.setDisable(true);
        if (selectedIdx == q.correctIdx) {
            score += 10;
            scoreLabel.setText("Score: " + score);
            feedbackLabel.setText("Correct! 🎉");
            feedbackLabel.setTextFill(Color.web("#43a047"));
            animateFeedback(true);
        } else {
            feedbackLabel.setText("Incorrect! The correct answer is: " + q.options[q.correctIdx]);
            feedbackLabel.setTextFill(Color.web("#d32f2f"));
            animateFeedback(false);
        }
        nextBtn.setDisable(false);
    }

    private void animateFeedback(boolean correct) {
        ScaleTransition st = new ScaleTransition(Duration.millis(350), feedbackLabel);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.15);
        st.setToY(1.15);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
        if (correct) {
            feedbackLabel.setStyle("-fx-background-color: #b2ff59; -fx-background-radius: 12; -fx-padding: 8 24; -fx-text-fill: #388e3c;");
        } else {
            feedbackLabel.setStyle("-fx-background-color: #ffd54f; -fx-background-radius: 12; -fx-padding: 8 24; -fx-text-fill: #d32f2f;");
        }
    }

    private void nextQuestion() {
        currentIndex++;
        showQuestion();
    }

    private void skipQuestion() {
        currentIndex++;
        showQuestion();
    }

    private void showResults() {
        // Award badge and update score in LeaderboardService
        LeaderboardService.getInstance().addScore(LeaderboardService.getInstance().getCurrentUser(), score, "completed quiz: " + quizCategory);
        LeaderboardService.getInstance().addBadge(LeaderboardService.getInstance().getCurrentUser(), quizCategory + " Quiz Master");
        getChildren().clear();
        Label result = new Label("Quiz Complete!\nYour Score: " + score + " / " + (questions.size() * 10));
        result.setFont(Font.font("Quicksand", FontWeight.BOLD, 28));
        result.setTextFill(Color.web("#0288d1"));
        result.setAlignment(Pos.CENTER);
        Button backBtn = new Button("← Back to Quiz Home");
        backBtn.setFont(Font.font("Quicksand", 16));
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 32; -fx-cursor: hand;");
        backBtn.setOnAction(e -> QuizHomePage.show(primaryStage));
        VBox box = new VBox(32, result, backBtn);
        box.setAlignment(Pos.CENTER);
        getChildren().add(box);
    }

    public static void show(Stage primaryStage, String quizCategory) {
        List<Question> questions = QuizData.getQuestionsForCategory(quizCategory);
        QuizPage page = new QuizPage(primaryStage, quizCategory, questions);
        Scene scene = new Scene(page, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Quiz: " + quizCategory);
        primaryStage.show();
    }

    public static class Question {
        public String text;
        public String[] options;
        public int correctIdx;
        public Question(String text, String[] options, int correctIdx) {
            this.text = text;
            this.options = options;
            this.correctIdx = correctIdx;
        }
    }
} 