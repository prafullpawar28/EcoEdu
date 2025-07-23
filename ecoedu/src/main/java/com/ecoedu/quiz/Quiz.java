package com.ecoedu.quiz;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class Quiz extends Application {
    List<Question> questions = QuizData.getQuestions();
    int currentQuestion = 0;
    int score = 0;
    private Label questionLabel;
    private Button[] optionButtons;
    private Label feedbackLabel;
    private Button nextButton;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #e8f5e9; -fx-padding: 40;");

        questionLabel = new Label();
        questionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        optionButtons = new Button[4];
        VBox optionsBox = new VBox(10);
        optionsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            Button btn = new Button();
            btn.setStyle("-fx-background-color: #aed581; -fx-font-size: 16px; -fx-background-radius: 10; -fx-padding: 8 20;");
            final int idx = i;
            btn.setOnAction(e -> handleAnswer(idx));
            optionButtons[i] = btn;
            optionsBox.getChildren().add(btn);
        }
        feedbackLabel = new Label("");
        feedbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #388e3c;");
        nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10; -fx-padding: 8 20;");
        nextButton.setOnAction(e -> nextQuestion());
        nextButton.setDisable(true);

        root.getChildren().addAll(questionLabel, optionsBox, feedbackLabel, nextButton);
        loadQuestion();

        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu Quiz");
        primaryStage.show();
    }

    private void loadQuestion() {
        if (currentQuestion < questions.size()) {
            Question q = questions.get(currentQuestion);
            questionLabel.setText((currentQuestion + 1) + ". " + q.getQuestionText());
            String[] opts = q.getOptions();
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(opts[i]);
                optionButtons[i].setDisable(false);
            }
            feedbackLabel.setText("");
            nextButton.setDisable(true);
        } else {
            showResult();
        }
    }

    private void handleAnswer(int idx) {
        Question q = questions.get(currentQuestion);
        for (Button btn : optionButtons) btn.setDisable(true);
        if (idx == q.getCorrectAnswerIndex()) {
            score++;
            feedbackLabel.setText("Correct!");
        } else {
            feedbackLabel.setText("Wrong! Correct answer: " + q.getOptions()[q.getCorrectAnswerIndex()]);
        }
        nextButton.setDisable(false);
        if (currentQuestion == questions.size() - 1) {
            nextButton.setText("Show Result");
        } else {
            nextButton.setText("Next");
        }
    }

    private void nextQuestion() {
        currentQuestion++;
        loadQuestion();
    }

    private void showResult() {
        questionLabel.setText("Quiz Completed! Your score: " + score + "/" + questions.size());
        for (Button btn : optionButtons) btn.setVisible(false);
        feedbackLabel.setText("");
        nextButton.setVisible(false);
    }
}
 