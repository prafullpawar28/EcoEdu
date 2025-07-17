package com.ecoedu.quiz;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;

public class QuizPage extends VBox {
    private Stage primaryStage;
    private int currentQuestion = 0;
    private int score = 0;
    private List<Question> questions;
    private Label questionLabel;
    private ToggleGroup optionsGroup;
    private Button nextButton;
    private Label feedbackLabel;
    private ProgressBar progressBar;
    private VBox optionsBox;

    public QuizPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.questions = QuizData.getQuestions();

        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #fffde7);");

        Label title = new Label("🌱 EcoEdu Quiz Challenge");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.setStyle("-fx-accent: #43a047; -fx-background-radius: 10; -fx-background-color: #E0E0E0;");
        getChildren().add(progressBar);

        questionLabel = new Label();
        questionLabel.setFont(Font.font("Comic Sans MS", 20));
        questionLabel.setWrapText(true);
        questionLabel.setTextFill(Color.web("#263238"));
        getChildren().add(questionLabel);

        optionsGroup = new ToggleGroup();
        optionsBox = new VBox(14);
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        getChildren().add(optionsBox);

        feedbackLabel = new Label();
        feedbackLabel.setFont(Font.font("Comic Sans MS", 16));
        feedbackLabel.setWrapText(true);
        feedbackLabel.setPadding(new Insets(8, 0, 0, 0));
        getChildren().add(feedbackLabel);

        nextButton = new Button("Next");
        nextButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        nextButton.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 32;");
        nextButton.setOnAction(e -> handleNext());
        getChildren().add(nextButton);

        showQuestion();
    }

    private void showQuestion() {
        if (currentQuestion >= questions.size()) {
            showResult();
            return;
        }
        Question q = questions.get(currentQuestion);
        questionLabel.setText((currentQuestion + 1) + ". " + q.getQuestionText());

        optionsBox.getChildren().clear();
        optionsGroup.getToggles().clear();

        for (String option : q.getOptions()) {
            RadioButton rb = new RadioButton(option);
            rb.setFont(Font.font("Comic Sans MS", 16));
            rb.setTextFill(Color.web("#1565c0"));
            rb.setToggleGroup(optionsGroup);
            rb.setStyle("-fx-background-radius: 12; -fx-padding: 6 18;");
            optionsBox.getChildren().add(rb);
        }
        feedbackLabel.setText("");
        feedbackLabel.setTextFill(Color.web("#263238"));
        nextButton.setDisable(false);
        progressBar.setProgress((double) currentQuestion / questions.size());

        FadeTransition ft = new FadeTransition(Duration.millis(400), questionLabel);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
    }

    private void handleNext() {
        Toggle selected = optionsGroup.getSelectedToggle();
        if (selected == null) {
            feedbackLabel.setText("Please select an answer.");
            feedbackLabel.setTextFill(Color.web("#d84315"));
            return;
        }
        String answer = ((RadioButton) selected).getText();
        Question q = questions.get(currentQuestion);
        String correctAnswer = q.getOptions()[q.getCorrectAnswerIndex()];
        if (answer.equals(correctAnswer)) {
            score++;
            feedbackLabel.setText("Correct! 🎉");
            feedbackLabel.setTextFill(Color.web("#43a047"));
        } else {
            feedbackLabel.setText("Incorrect. The correct answer is: " + correctAnswer);
            feedbackLabel.setTextFill(Color.web("#d84315"));
        }
        nextButton.setDisable(true);

        FadeTransition ft = new FadeTransition(Duration.millis(400), feedbackLabel);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();

        // Move to next question after a short delay
        new Thread(() -> {
            try { Thread.sleep(1200); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(() -> {
                currentQuestion++;
                showQuestion();
            });
        }).start();
    }

    private void showResult() {
        getChildren().clear();
        VBox summaryCard = new VBox(18);
        summaryCard.setAlignment(Pos.CENTER);
        summaryCard.setPadding(new Insets(40));
        summaryCard.setStyle("-fx-background-color: white; -fx-background-radius: 24; -fx-effect: dropshadow(gaussian, #b3e5fc, 16, 0.2, 0, 2);");

        Label result = new Label("Quiz Complete!");
        result.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        result.setTextFill(Color.web("#0288d1"));
        summaryCard.getChildren().add(result);

        Label scoreLabel = new Label("Your score: " + score + " / " + questions.size());
        scoreLabel.setFont(Font.font("Comic Sans MS", 22));
        scoreLabel.setTextFill(Color.web("#43a047"));
        summaryCard.getChildren().add(scoreLabel);

        ProgressBar finalBar = new ProgressBar((double) score / questions.size());
        finalBar.setPrefWidth(300);
        finalBar.setStyle("-fx-accent: #43a047; -fx-background-radius: 10; -fx-background-color: #E0E0E0;");
        summaryCard.getChildren().add(finalBar);

        Label message = new Label();
        if (score == questions.size()) {
            message.setText("🌟 Perfect! You're an Eco Genius!");
            message.setTextFill(Color.web("#ffb300"));
        } else if (score >= questions.size() * 0.7) {
            message.setText("Great job! Keep learning and helping the planet!");
            message.setTextFill(Color.web("#43a047"));
        } else {
            message.setText("Keep practicing! Every step counts for Earth.");
            message.setTextFill(Color.web("#0288d1"));
        }
        message.setFont(Font.font("Comic Sans MS", 18));
        summaryCard.getChildren().add(message);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> com.ecoedu.Home.Dashboard.show(primaryStage));
        summaryCard.getChildren().add(backBtn);

        FadeTransition ft = new FadeTransition(Duration.millis(600), summaryCard);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();

        getChildren().add(summaryCard);
    }

    public static void show(Stage primaryStage) {
        QuizPage page = new QuizPage(primaryStage);
        Scene scene = new Scene(page, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Quiz");
        primaryStage.show();
    }
} 