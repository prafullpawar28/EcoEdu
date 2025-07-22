package com.ecoedu.quiz;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.ecoedu.dashboard.StudentDashboard;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class QuizPage {
    public static void show(Stage primaryStage, Object arg) {
        if (arg instanceof String) {
            // Start the quiz for the given category
            List<Question> questions = QuizData.getQuestionsForCategory((String) arg);
            showQuiz(primaryStage, (String) arg, questions);
        } else {
            // Show category selection
            VBox root = new VBox(32);
            root.setStyle("-fx-background-color: #fffde7; -fx-padding: 60;");
            root.setAlignment(Pos.TOP_CENTER);
            Label label = new Label("Select a Quiz Category:");
            label.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #0288d1;");
            root.getChildren().add(label);
            List<String> categories = QuizData.getQuizCategories();
            for (String cat : categories) {
                Button btn = new Button(cat);
                btn.setStyle("-fx-background-color: linear-gradient(to right, #ffd54f, #81d4fa); -fx-font-size: 22px; -fx-font-weight: bold; -fx-background-radius: 22; -fx-padding: 18 60; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #ffd54f, 8, 0.2, 0, 2);");
                btn.setOnAction(e -> show(primaryStage, cat));
                root.getChildren().add(btn);
            }
            Button backBtn = new Button("Back to Dashboard");
            backBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 16; -fx-padding: 10 36; -fx-cursor: hand;");
            backBtn.setOnAction(e -> StudentDashboard.show(primaryStage));
            root.getChildren().add(backBtn);
            Scene scene = new Scene(root, 1366, 768);
            primaryStage.setScene(scene);
            primaryStage.setTitle("EcoEdu - Quiz Categories");
            primaryStage.show();
        }
    }

    private static void showQuiz(Stage primaryStage, String category, List<Question> questions) {
        VBox root = new VBox(28);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(60, 0, 0, 0));
        Label title = new Label("Quiz: " + category);
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #0288d1;");
        Label progressLabel = new Label();
        progressLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #388e3c;");
        Label scoreLabel = new Label();
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #43a047;");
        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #d32f2f;");
        Label questionLabel = new Label();
        questionLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #263238;");
        VBox optionsBox = new VBox(16);
        optionsBox.setAlignment(Pos.CENTER);
        Button[] optionButtons = new Button[4];
        for (int i = 0; i < 4; i++) {
            Button btn = new Button();
            btn.setStyle("-fx-background-color: #aed581; -fx-font-size: 18px; -fx-background-radius: 14; -fx-padding: 10 32; -fx-cursor: hand;");
            optionButtons[i] = btn;
            optionsBox.getChildren().add(btn);
        }
        Label feedbackLabel = new Label("");
        feedbackLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #388e3c;");
        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 14; -fx-padding: 10 32; -fx-cursor: hand;");
        nextButton.setDisable(true);
        Button backBtn = new Button("Back to Categories");
        backBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #0288d1; -fx-font-size: 16px; -fx-background-radius: 12; -fx-padding: 8 24; -fx-cursor: hand;");
        root.getChildren().addAll(title, progressLabel, scoreLabel, timerLabel, questionLabel, optionsBox, feedbackLabel, nextButton, backBtn);

        // Quiz state
        final int[] currentQuestion = {0};
        final int[] score = {0};
        final Timeline[] timer = {null};
        final int timePerQuestion = 20; // seconds
        final int[] timeLeft = {timePerQuestion};

        Runnable loadQuestion = () -> {
            if (timer[0] != null) timer[0].stop();
            if (currentQuestion[0] < questions.size()) {
                Question q = questions.get(currentQuestion[0]);
                questionLabel.setText((currentQuestion[0] + 1) + ". " + q.getQuestionText());
                String[] opts = q.getOptions();
                for (int i = 0; i < 4; i++) {
                    optionButtons[i].setText(opts[i]);
                    optionButtons[i].setDisable(false);
                }
                feedbackLabel.setText("");
                nextButton.setDisable(true);
                progressLabel.setText("Question " + (currentQuestion[0] + 1) + " of " + questions.size());
                scoreLabel.setText("Score: " + score[0]);
                timeLeft[0] = timePerQuestion;
                timerLabel.setText("Time left: " + timeLeft[0] + "s");
                timer[0] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                    timeLeft[0]--;
                    timerLabel.setText("Time left: " + timeLeft[0] + "s");
                    if (timeLeft[0] <= 0) {
                        timer[0].stop();
                        for (Button btn : optionButtons) btn.setDisable(true);
                        feedbackLabel.setText("Time's up! Correct answer: " + q.getOptions()[q.getCorrectAnswerIndex()]);
                        feedbackLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 18px;");
                        nextButton.setDisable(false);
                        if (currentQuestion[0] == questions.size() - 1) {
                            nextButton.setText("Show Result");
                        } else {
                            nextButton.setText("Next");
                        }
                    }
                }));
                timer[0].setCycleCount(timePerQuestion);
                timer[0].play();
            } else {
                showResult(primaryStage, category, score[0], questions.size());
            }
        };

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            optionButtons[i].setOnAction(e -> {
                if (timer[0] != null) timer[0].stop();
                Question q = questions.get(currentQuestion[0]);
                for (Button btn : optionButtons) btn.setDisable(true);
                if (idx == q.getCorrectAnswerIndex()) {
                    score[0]++;
                    feedbackLabel.setText("Correct!");
                    feedbackLabel.setStyle("-fx-text-fill: #43a047; -fx-font-size: 18px;");
                } else {
                    feedbackLabel.setText("Wrong! Correct answer: " + q.getOptions()[q.getCorrectAnswerIndex()]);
                    feedbackLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 18px;");
                }
                nextButton.setDisable(false);
                if (currentQuestion[0] == questions.size() - 1) {
                    nextButton.setText("Show Result");
                } else {
                    nextButton.setText("Next");
                }
                scoreLabel.setText("Score: " + score[0]);
            });
        }

        nextButton.setOnAction(e -> {
            currentQuestion[0]++;
            loadQuestion.run();
        });
        backBtn.setOnAction(e -> {
            if (timer[0] != null) timer[0].stop();
            show(primaryStage, null);
        });
        loadQuestion.run();
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Quiz: " + category);
        primaryStage.show();
    }

    private static void showResult(Stage primaryStage, String category, int score, int total) {
        VBox root = new VBox(36);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #e1f5fe; -fx-padding: 80;");
        Label congrats = new Label("Quiz Complete!");
        congrats.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #43e97b;");
        Label result = new Label("Your Score: " + score + " / " + total);
        result.setStyle("-fx-font-size: 26px; -fx-text-fill: #0288d1;");
        Button tryAnother = new Button("Try Another Quiz");
        tryAnother.setStyle("-fx-background-color: #ffd54f; -fx-font-size: 20px; -fx-background-radius: 18; -fx-padding: 12 40; -fx-cursor: hand;");
        tryAnother.setOnAction(e -> show(primaryStage, null));
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 16; -fx-padding: 10 36; -fx-cursor: hand;");
        backBtn.setOnAction(e -> StudentDashboard.show(primaryStage));
        root.getChildren().addAll(congrats, result, tryAnother, backBtn);
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Quiz Result");
        primaryStage.show();
    }
} 