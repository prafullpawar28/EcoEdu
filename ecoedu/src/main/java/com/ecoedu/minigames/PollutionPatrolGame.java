package com.ecoedu.minigames;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

public class PollutionPatrolGame extends StackPane {
    private Pane gamePane = new Pane();
    private ImageView boat;
    private ArrayList<ImageView> trashItems = new ArrayList<>();
    private Random random = new Random();
    private int score = 0;
    private int missedTrash = 0;
    private final int maxMissedTrash = 5;
    private int timeRemaining = 60;
    private Text scoreText = new Text("Score: 0");
    private Text timerText = new Text("Time: 60");
    private Text gameOverText = new Text();
    private AnimationTimer gameLoop;
    private boolean gameOver = false;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private long lastSpawnTime = 0;
    private long lastTimerUpdate = 0;
    private long lastDifficultyIncreaseTime = 0;
    private long lastUpdate = 0;
    private double trashSpeed = 200;
    private long spawnInterval = 1_000_000_000L;
    private long difficultyIncreaseInterval = 5_000_000_000L;
    private final long minSpawnInterval = 300_000_000L;
    private Image trashImage;
    private Image boatImage;
    private ArrayList<Circle> bubbles = new ArrayList<>();
    private ArrayList<Image> trashVariety = new ArrayList<>();
    private Rectangle missedBar;
    private StackPane overlayPane = new StackPane();
    private boolean gameStarted = false;

    public PollutionPatrolGame(Stage primaryStage) {
        setAlignment(Pos.CENTER);
        setPrefSize(1366, 768);
        gamePane.setPrefSize(1366, 768);
        // Load images from resources
        try {
            trashImage = new Image(getClass().getResourceAsStream("/Assets/Images/trash1.png"));
            trashVariety.add(trashImage);
           
        } catch (Exception e) {
            trashImage = null;
        }
        try {
            boatImage = new Image(getClass().getResourceAsStream("/Assets/Images/boat.png"));
        } catch (Exception e) {
            boatImage = null;
        }
        setupGame(primaryStage);
        // Animated gradient background
        Rectangle gradientBg = new Rectangle(1366, 768);
        gradientBg.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#b2ebf2")),
            new Stop(0.6, Color.web("#e0f7fa")),
            new Stop(1, Color.web("#b2dfdb"))));
        getChildren().add(gradientBg);
        // Animated waves at the bottom
        Pane wavePane = new Pane();
        wavePane.setPrefSize(1366, 120);
        wavePane.setLayoutY(768 - 120);
        for (int i = 0; i < 4; i++) {
            javafx.scene.shape.SVGPath wave = new javafx.scene.shape.SVGPath();
            wave.setContent("M0,60 Q150,80 300,60 T600,60 T900,60 T1200,60 T1366,60 V120 H0 Z");
            wave.setFill(Color.web(i % 2 == 0 ? "#4fc3f7" : "#b2ebf2", 0.18 + 0.08 * i));
            wave.setLayoutY(i * 10);
            wavePane.getChildren().add(wave);
            javafx.animation.Timeline waveAnim = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.ZERO, new javafx.animation.KeyValue(wave.translateXProperty(), 0)),
                new javafx.animation.KeyFrame(Duration.seconds(6 + i * 2), new javafx.animation.KeyValue(wave.translateXProperty(), -200 + i * 50))
            );
            waveAnim.setCycleCount(javafx.animation.Timeline.INDEFINITE);
            waveAnim.setAutoReverse(true);
            waveAnim.play();
        }
        getChildren().add(wavePane);
        getChildren().add(gamePane);
        // Overlay for instructions/start
        overlayPane.setPrefSize(1366, 768);
        overlayPane.setStyle("-fx-background-color: rgba(0,0,0,0.35);");
        VBox startBox = new VBox(18);
        startBox.setAlignment(Pos.CENTER);
        Text startTitle = new Text("Pollution Patrol");
        startTitle.setFont(Font.font("Quicksand", 48));
        startTitle.setFill(Color.web("#00c6ff"));
        Text startDesc = new Text("Catch trash with your boat!\nMove your mouse to steer.\nDon't let trash fall into the water.");
        startDesc.setFont(Font.font("Quicksand", 22));
        startDesc.setFill(Color.web("#fffde7"));
        Button startBtn = new Button("Start Game");
        startBtn.setFont(Font.font("Quicksand", 22));
        startBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 12 40; -fx-cursor: hand;");
        startBtn.setOnAction(e -> {
            overlayPane.setVisible(false);
            gameStarted = true;
        });
        startBox.getChildren().addAll(startTitle, startDesc, startBtn);
        overlayPane.getChildren().add(startBox);
        getChildren().add(overlayPane);
    }

    private void setupGame(Stage primaryStage) {
        gamePane.getChildren().clear();
        trashItems.clear();
        score = 0;
        missedTrash = 0;
        timeRemaining = 60;
        gameOver = false;
        lastSpawnTime = 0;
        lastTimerUpdate = 0;
        lastUpdate = 0;
        spawnInterval = 1_000_000_000L;
        trashSpeed = 150;
        lastDifficultyIncreaseTime = 0;

        gamePane.setStyle("-fx-background-color: linear-gradient(to bottom, #b2ebf2, #e0f7fa 60%, #b2dfdb 100%);");

        boat = new ImageView(boatImage);
        boat.setFitWidth(200);
        boat.setFitHeight(80);
        boat.setX(WIDTH / 2 - 100);
        boat.setY(HEIGHT - 80);

        scoreText.setFont(Font.font("Quicksand", 24));
        scoreText.setFill(Color.web("#0288d1"));
        scoreText.setX(10);
        scoreText.setY(30);
        scoreText.setText("Score: 0");

        timerText.setFont(Font.font("Quicksand", 24));
        timerText.setFill(Color.web("#388e3c"));
        timerText.setX(WIDTH - 120);
        timerText.setY(30);
        timerText.setText("Time: 60");

        missedBar = new Rectangle(0, 0, 180, 18);
        missedBar.setFill(Color.web("#ff5252", 0.7));
        missedBar.setArcWidth(12);
        missedBar.setArcHeight(12);
        missedBar.setLayoutX(WIDTH / 2.0 - 90);
        missedBar.setLayoutY(60);
        gamePane.getChildren().add(missedBar);

        gamePane.getChildren().addAll(boat, scoreText, timerText);

        gamePane.setOnMouseMoved(e -> {
            if (!gameOver && gameStarted)
                boat.setX(e.getX() - boat.getFitWidth() / 2);
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameStarted) return;
                if (gameOver) return;
                if (lastUpdate > 0) {
                    double deltaSeconds = (now - lastUpdate) / 1_000_000_000.0;
                    if (now - lastDifficultyIncreaseTime > difficultyIncreaseInterval) {
                        if (spawnInterval > minSpawnInterval) {
                            spawnInterval -= 100_000_000L;
                        }
                        lastDifficultyIncreaseTime = now;
                    }
                    int elapsedTime = 60 - timeRemaining;
                    double baseSpeed = 150;
                    double speedFromScore = (score / 10) * 10;
                    double speedFromTime = (elapsedTime / 10) * 20;
                    trashSpeed = baseSpeed + speedFromScore + speedFromTime;
                    trashSpeed = Math.min(trashSpeed, 600);
                    if (now - lastSpawnTime > spawnInterval) {
                        spawnTrash();
                        lastSpawnTime = now;
                    }
                    if (now - lastTimerUpdate > 1_000_000_000) {
                        timeRemaining--;
                        timerText.setText("Time: " + timeRemaining);
                        lastTimerUpdate = now;
                        if (timeRemaining <= 0) {
                            endGame(primaryStage);
                            return;
                        }
                    }
                    moveTrash(deltaSeconds);
                    checkCollisions(primaryStage);
                    missedBar.setWidth(180 * ((double)missedTrash / maxMissedTrash));
                }
                lastUpdate = now;
            }
        };
        gameLoop.start();

        AnimationTimer bubbleTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver && random.nextDouble() < 0.04) {
                    spawnBubble();
                }
                moveBubbles();
            }
        };
        bubbleTimer.start();
    }

    private void spawnTrash() {
        if (trashVariety.isEmpty()) {
            System.err.println("Error: No trash images loaded. Cannot spawn trash.");
            return;
        }
        Image img = trashVariety.get(random.nextInt(trashVariety.size()));
        ImageView trash = new ImageView(img);
        trash.setFitWidth(40);
        trash.setFitHeight(40);
        trash.setX(random.nextInt(WIDTH - 40));
        trash.setY(0);
        trash.setCache(true);
        trashItems.add(trash);
        gamePane.getChildren().add(trash);
    }

    private void moveTrash(double deltaSeconds) {
        for (ImageView trash : trashItems) {
            trash.setY(trash.getY() + trashSpeed * deltaSeconds);
        }
    }

    private void checkCollisions(Stage primaryStage) {
        Iterator<ImageView> iterator = trashItems.iterator();
        while (iterator.hasNext()) {
            ImageView trash = iterator.next();
            if (trash.getBoundsInParent().intersects(boat.getBoundsInParent())) {
                gamePane.getChildren().remove(trash);
                iterator.remove();
                score += 10;
                scoreText.setText("Score: " + score);
            } else if (trash.getY() > HEIGHT) {
                gamePane.getChildren().remove(trash);
                iterator.remove();
                missedTrash++;
                if (missedTrash >= maxMissedTrash) {
                    endGame(primaryStage);
                }
            }
        }
    }

    private void spawnBubble() {
        Circle bubble = new Circle(8 + random.nextInt(10), Color.web("#b3e5fc", 0.5 + random.nextDouble() * 0.3));
        bubble.setCenterX(30 + random.nextInt(WIDTH - 60));
        bubble.setCenterY(HEIGHT + 20);
        bubbles.add(bubble);
        gamePane.getChildren().add(0, bubble);
    }
    private void moveBubbles() {
        Iterator<Circle> iterator = bubbles.iterator();
        while (iterator.hasNext()) {
            Circle bubble = iterator.next();
            bubble.setCenterY(bubble.getCenterY() - (1.5 + random.nextDouble() * 1.5));
            if (bubble.getCenterY() < -20) {
                gamePane.getChildren().remove(bubble);
                iterator.remove();
            }
        }
    }

    private void endGame(Stage primaryStage) {
        gameOver = true;
        gameLoop.stop();
        gameOverText.setText("Game Over!\nScore: " + score);
        gameOverText.setFont(Font.font("Quicksand", 36));
        gameOverText.setFill(Color.web("#fffde7"));
        gameOverText.setX(WIDTH / 2.0 - 150);
        gameOverText.setY(HEIGHT / 2.0 - 30);
        Button restartButton = new Button("Play Again");
        restartButton.setLayoutX(WIDTH / 2.0 - 50);
        restartButton.setLayoutY(HEIGHT / 2.0 + 20);
        restartButton.setOnAction(e -> {
            overlayPane.setVisible(true);
            gameStarted = false;
            setupGame(primaryStage);
            gameLoop.start();
        });
        Button backButton = new Button("Back to Minigames");
        backButton.setLayoutX(WIDTH / 2.0 - 50);
        backButton.setLayoutY(HEIGHT / 2.0 + 60);
        backButton.setOnAction(e -> MinigamesPage.show(primaryStage));
        gamePane.getChildren().addAll(gameOverText, restartButton, backButton);
    }

    public static void show(Stage primaryStage) {
        PollutionPatrolGame game = new PollutionPatrolGame(primaryStage);
        Scene scene = new Scene(game, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pollution Patrol");
        primaryStage.show();
    }
} 