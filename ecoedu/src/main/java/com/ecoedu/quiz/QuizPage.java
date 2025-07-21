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
    private String currentQuizTitle;

    public QuizPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showQuizSelection();
    }

    // --- Quiz Selection Screen ---
    private void showQuizSelection() {
        getChildren().clear();
        setSpacing(32);
        setPadding(new Insets(48, 48, 48, 48));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #fffde7);");

        // Playful animated header
        HBox playfulHeader = new HBox(16);
        playfulHeader.setAlignment(Pos.CENTER);
        Label mascot = new Label("🧠");
        mascot.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 44));
        Label quizTitle = new Label("Quiz Time!");
        quizTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        quizTitle.setTextFill(Color.web("#0288d1"));
        playfulHeader.getChildren().addAll(mascot, quizTitle);
        getChildren().add(playfulHeader);

        // Motivational info bar
        HBox infoBar = new HBox();
        infoBar.setAlignment(Pos.CENTER);
        infoBar.setPadding(new Insets(10, 0, 10, 0));
        infoBar.setStyle("-fx-background-color: linear-gradient(to right, #ffd54f, #81d4fa); -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #ffd54f, 8, 0.1, 0, 2);");
        Label infoLabel = new Label("🌱 Tip: Every quiz helps you become an Eco Genius!");
        infoLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        infoLabel.setTextFill(Color.web("#388e3c"));
        infoBar.getChildren().add(infoLabel);
        getChildren().add(infoBar);

        Label title = new Label("🧩 Choose a Quiz");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        VBox quizListBox = new VBox(18);
        quizListBox.setAlignment(Pos.CENTER);
        for (String quizTitleStr : QuizData.getQuizList()) {
            HBox quizCard = new HBox(12);
            quizCard.setAlignment(Pos.CENTER_LEFT);
            quizCard.setPadding(new Insets(10, 24, 10, 24));
            quizCard.setStyle("-fx-background-color: linear-gradient(to right, #ffd54f, #fffde7 80%); -fx-background-radius: 18; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #ffd54f, 8, 0.1, 0, 2);");
            Label icon = new Label("❓");
            icon.setFont(Font.font("Comic Sans MS", 26));
            Label quizName = new Label(quizTitleStr);
            quizName.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
            quizName.setTextFill(Color.web("#263238"));
            quizCard.getChildren().addAll(icon, quizName);
            quizCard.setOnMouseClicked(e -> startQuiz(quizTitleStr));
            quizCard.setOnMouseEntered(e -> quizCard.setStyle("-fx-background-color: linear-gradient(to right, #fffde7, #ffd54f 80%); -fx-background-radius: 18; -fx-cursor: hand; -fx-scale-x:1.04;-fx-scale-y:1.04;-fx-effect: dropshadow(gaussian, #0288d1, 16, 0.2, 0, 4);"));
            quizCard.setOnMouseExited(e -> quizCard.setStyle("-fx-background-color: linear-gradient(to right, #ffd54f, #fffde7 80%); -fx-background-radius: 18; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #ffd54f, 8, 0.1, 0, 2);"));
            quizListBox.getChildren().add(quizCard);
        }
        getChildren().add(quizListBox);

        // Footer with eco-fact
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(24, 0, 0, 0));
        Label fact = new Label("🌍 Fun Fact: Recycling one aluminum can saves enough energy to run a TV for 3 hours!");
        fact.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
        fact.setTextFill(Color.web("#388e3c"));
        footer.getChildren().add(fact);
        getChildren().add(footer);
    }

    // --- Start a Quiz ---
    private void startQuiz(String quizTitle) {
        this.currentQuizTitle = quizTitle;
        this.questions = QuizData.getQuestionsForQuiz(quizTitle);
        this.currentQuestion = 0;
        this.score = 0;
        showQuizUI();
    }

    // --- Main Quiz UI ---
    private void showQuizUI() {
        getChildren().clear();
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #fffde7);");

        // Playful header
        HBox playfulHeader = new HBox(16);
        playfulHeader.setAlignment(Pos.CENTER);
        Label mascot = new Label("🧠");
        mascot.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 38));
        Label quizTitle = new Label(currentQuizTitle);
        quizTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        quizTitle.setTextFill(Color.web("#0288d1"));
        playfulHeader.getChildren().addAll(mascot, quizTitle);
        getChildren().add(playfulHeader);

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

        // Footer with encouragement
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(18, 0, 0, 0));
        Label encouragement = new Label("You can do it! 🌟");
        encouragement.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
        encouragement.setTextFill(Color.web("#43a047"));
        footer.getChildren().add(encouragement);
        getChildren().add(footer);

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
            rb.setStyle("-fx-background-radius: 12; -fx-padding: 6 18; -fx-background-color: #fffde7;");
            rb.setOnMouseEntered(e -> rb.setStyle("-fx-background-radius: 12; -fx-padding: 6 18; -fx-background-color: #b3e5fc;"));
            rb.setOnMouseExited(e -> rb.setStyle("-fx-background-radius: 12; -fx-padding: 6 18; -fx-background-color: #fffde7;"));
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
            feedbackLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #e8f5e9; -fx-background-radius: 12; -fx-padding: 6 18;");
        } else {
            feedbackLabel.setText("Incorrect. The correct answer is: " + correctAnswer + " ❌");
            feedbackLabel.setTextFill(Color.web("#d84315"));
            feedbackLabel.setStyle("-fx-font-weight: bold; -fx-background-color: #ffebee; -fx-background-radius: 12; -fx-padding: 6 18;");
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

        // Playful result header
        HBox resultHeader = new HBox(12);
        resultHeader.setAlignment(Pos.CENTER);
        Label resultIcon = new Label(score == questions.size() ? "🏆" : (score >= questions.size() * 0.7 ? "🎉" : "👍"));
        resultIcon.setFont(Font.font("Comic Sans MS", 32));
        Label result = new Label("Quiz Complete!");
        result.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        result.setTextFill(Color.web("#0288d1"));
        resultHeader.getChildren().addAll(resultIcon, result);
        summaryCard.getChildren().add(resultHeader);

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

        Button replayBtn = new Button("Replay Quiz");
        replayBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        replayBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #263238; -fx-background-radius: 20; -fx-padding: 10 36;");
        replayBtn.setOnAction(e -> startQuiz(currentQuizTitle));
        Button switchBtn = new Button("Switch Quiz");
        switchBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        switchBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        switchBtn.setOnAction(e -> showQuizSelection());
        HBox btnBox = new HBox(18, replayBtn, switchBtn);
        btnBox.setAlignment(Pos.CENTER);
        summaryCard.getChildren().add(btnBox);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
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