package com.ecoedu.modules;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;

public class ModulePage extends VBox {
    private Stage primaryStage;
    private StackPane contentPane;
    private Module currentModule;
    private int currentLessonIndex;

    public ModulePage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(36, 36, 36, 36));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #fffde7, #e3f2fd);");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 8 16; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        Label title = new Label("📚 EcoEdu Modules");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#388e3c"));
        header.getChildren().addAll(backBtn, title);
        getChildren().add(header);

        contentPane = new StackPane();
        contentPane.setAlignment(Pos.TOP_CENTER);
        getChildren().add(contentPane);

        showModuleSelection();
    }

    // --- Module Selection Screen ---
    private void showModuleSelection() {
        VBox moduleList = new VBox(24);
        moduleList.setAlignment(Pos.TOP_CENTER);
        List<Module> modules = ModuleData.getModules();
        for (Module module : modules) {
            HBox card = new HBox(18);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(18));
            card.setPrefWidth(600);
            card.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-color: #b3e5fc; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, #b3e5fc, 8, 0.1, 0, 2);");
            Circle icon = new Circle(28, Color.web("#81d4fa"));
            Label iconLabel = new Label(module.getIcon());
            iconLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 26));
            iconLabel.setTextFill(Color.web("#0288d1"));
            StackPane iconPane = new StackPane(icon, iconLabel);
            VBox info = new VBox(6);
            Label title = new Label(module.getTitle());
            title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
            title.setTextFill(Color.web("#0288d1"));
            Label desc = new Label(module.getDescription());
            desc.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 14));
            desc.setTextFill(Color.web("#388e3c"));
            desc.setWrapText(true);
            info.getChildren().addAll(title, desc);
            Button openBtn = new Button("View Lessons");
            openBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
            openBtn.setOnAction(e -> startModule(module));
            card.getChildren().addAll(iconPane, info, openBtn);
            moduleList.getChildren().add(card);
        }
        FadeTransition ft = new FadeTransition(Duration.millis(500), moduleList);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
        contentPane.getChildren().setAll(moduleList);
    }

    // --- Start a Module ---
    private void startModule(Module module) {
        this.currentModule = module;
        this.currentLessonIndex = 0;
        showLessonList(module);
    }

    private void showLessonList(Module module) {
        VBox lessonList = new VBox(20);
        lessonList.setAlignment(Pos.TOP_CENTER);
        Label moduleTitle = new Label(module.getTitle() + " Lessons");
        moduleTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        moduleTitle.setTextFill(Color.web("#0288d1"));
        lessonList.getChildren().add(moduleTitle);
        // Progress bar for lessons
        ProgressBar lessonBar = new ProgressBar(0);
        lessonBar.setPrefWidth(400);
        lessonBar.setStyle("-fx-accent: #43a047; -fx-background-radius: 10; -fx-background-color: #E0E0E0;");
        lessonList.getChildren().add(lessonBar);
        List<Lesson> lessons = module.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            HBox card = new HBox(14);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(14));
            card.setPrefWidth(500);
            card.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: #b3e5fc; -fx-border-width: 1.5;");
            Circle icon = new Circle(18, Color.web("#b3e5fc"));
            Label iconLabel = new Label("📖");
            iconLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
            iconLabel.setTextFill(Color.web("#0288d1"));
            StackPane iconPane = new StackPane(icon, iconLabel);
            VBox info = new VBox(2);
            Label title = new Label(lesson.getTitle());
            title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
            title.setTextFill(Color.web("#388e3c"));
            info.getChildren().add(title);
            Button openBtn = new Button("Open");
            openBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 18;");
            int lessonIndex = i;
            openBtn.setOnAction(e -> showLessonContent(module, lesson, lessonIndex, lessons.size(), lessonBar));
            card.getChildren().addAll(iconPane, info, openBtn);
            lessonList.getChildren().add(card);
        }
        Button backBtn = new Button("← Back to Modules");
        backBtn.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        backBtn.setOnAction(e -> showModuleSelection());
        lessonList.getChildren().add(backBtn);
        FadeTransition ft = new FadeTransition(Duration.millis(400), lessonList);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
        contentPane.getChildren().setAll(lessonList);
    }

    private void showLessonContent(Module module, Lesson lesson, int lessonIndex, int totalLessons, ProgressBar lessonBar) {
        VBox lessonCard = new VBox(18);
        lessonCard.setAlignment(Pos.TOP_CENTER);
        lessonCard.setPadding(new Insets(30));
        lessonCard.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #b3e5fc, 12, 0.15, 0, 2);");
        Label title = new Label(lesson.getTitle());
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#0288d1"));
        Label content = new Label(lesson.getContent());
        content.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 15));
        content.setTextFill(Color.web("#263238"));
        content.setWrapText(true);
        lessonCard.getChildren().addAll(title, content);
        // Progress bar update
        double progress = (lessonIndex + 1) / (double) totalLessons;
        lessonBar.setProgress(progress);
        Label progressLabel = new Label("Lesson " + (lessonIndex + 1) + " of " + totalLessons);
        progressLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 14));
        progressLabel.setTextFill(Color.web("#43a047"));
        lessonCard.getChildren().add(progressLabel);
        Button nextBtn = new Button(lessonIndex < totalLessons - 1 ? "Next Lesson" : "Finish Module");
        nextBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        nextBtn.setOnAction(e -> {
            if (lessonIndex < totalLessons - 1) {
                showLessonContent(module, module.getLessons().get(lessonIndex + 1), lessonIndex + 1, totalLessons, lessonBar);
            } else {
                showModuleCompletion(module);
            }
        });
        lessonCard.getChildren().add(nextBtn);
        Button backBtn = new Button("← Back to Lessons");
        backBtn.setStyle("-fx-background-color: #bdbdbd; -fx-text-fill: white; -fx-background-radius: 16; -fx-padding: 8 24;");
        backBtn.setOnAction(e -> showLessonList(module));
        lessonCard.getChildren().add(backBtn);
        FadeTransition ft = new FadeTransition(Duration.millis(400), lessonCard);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
        contentPane.getChildren().setAll(lessonCard);
    }

    private void showModuleCompletion(Module module) {
        VBox completeCard = new VBox(24);
        completeCard.setAlignment(Pos.CENTER);
        completeCard.setPadding(new Insets(40));
        completeCard.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 24; -fx-effect: dropshadow(gaussian, #b3e5fc, 16, 0.2, 0, 2);");
        Label congrats = new Label("🎉 Module Complete!");
        congrats.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        congrats.setTextFill(Color.web("#43a047"));
        Label msg = new Label("You finished all lessons in " + module.getTitle() + "!");
        msg.setFont(Font.font("Comic Sans MS", 18));
        msg.setTextFill(Color.web("#0288d1"));
        Button replayBtn = new Button("Replay Module");
        replayBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        replayBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #263238; -fx-background-radius: 20; -fx-padding: 10 36;");
        replayBtn.setOnAction(e -> startModule(module));
        Button switchBtn = new Button("Switch Module");
        switchBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        switchBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        switchBtn.setOnAction(e -> showModuleSelection());
        HBox btnBox = new HBox(18, replayBtn, switchBtn);
        btnBox.setAlignment(Pos.CENTER);
        completeCard.getChildren().addAll(congrats, msg, btnBox);
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        completeCard.getChildren().add(backBtn);
        FadeTransition ft = new FadeTransition(Duration.millis(600), completeCard);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
        contentPane.getChildren().setAll(completeCard);
    }

    public static void show(Stage primaryStage) {
        ModulePage page = new ModulePage(primaryStage);
        Scene scene = new Scene(page, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Modules");
        primaryStage.show();
    }
} 