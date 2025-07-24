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
    // --- NEW FIELDS FOR ENHANCED GAMEPLAY ---
    private int combo = 0;
    private int maxCombo = 0;
    private int multiplier = 1;
    private boolean doublePointsActive = false;
    private boolean slowMotionActive = false;
    private long powerUpEndTime = 0;
    private ArrayList<Image> powerUpImages = new ArrayList<>();
    private ArrayList<Image> allTrashImages = new ArrayList<>();
    private ArrayList<ScorePopup> scorePopups = new ArrayList<>();
    private VBox leaderboardBox = new VBox(8);
    private ArrayList<Integer> leaderboard = new ArrayList<>();
    private Button pauseBtn = new Button("Pause");
    private boolean paused = false;
    private StackPane pauseOverlay = new StackPane();

    public PollutionPatrolGame(Stage primaryStage) {
        setAlignment(Pos.CENTER);
        setPrefSize(1366, 768);
        gamePane.setPrefSize(1366, 768);
        // Load images from resources
        try {
            trashImage = new Image(getClass().getResourceAsStream("/Assets/Images/trash1.png"));
            trashVariety.add(trashImage);
            allTrashImages.add(trashImage);
           // allTrashImages.add(new Image(getClass().getResourceAsStream("/Assets/Images/trashsorter.jpeg")));
            allTrashImages.add(new Image(getClass().getResourceAsStream("/Assets/Images/trash1.png")));
            // Add more trash images as desired
            
            // Use glasses as a power-up icon for demo
        } catch (Exception e) {}
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
        // Add pause button to overlay
        pauseBtn.setFont(Font.font("Quicksand", 18));
        pauseBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 8 32; -fx-cursor: hand;");
        pauseBtn.setOnAction(e -> togglePause());
        pauseBtn.setLayoutX(WIDTH - 120);
        pauseBtn.setLayoutY(20);
        gamePane.getChildren().add(pauseBtn);
        // Pause overlay
        pauseOverlay.setPrefSize(1366, 768);
        pauseOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        Text pauseText = new Text("Paused");
        pauseText.setFont(Font.font("Quicksand", 48));
        pauseText.setFill(Color.web("#fffde7"));
        pauseOverlay.getChildren().add(pauseText);
        pauseOverlay.setVisible(false);
        getChildren().add(pauseOverlay);
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
            if (!gameOver && gameStarted && !paused)
                boat.setX(Math.max(0, Math.min(e.getX() - boat.getFitWidth() / 2, WIDTH - boat.getFitWidth())));
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameStarted) return;
                if (gameOver) return;
                if (paused) return;
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
        if (allTrashImages.isEmpty()) {
            System.err.println("Error: No trash images loaded. Cannot spawn trash.");
            return;
        }
        if (random.nextDouble() < 0.08) {
            spawnPowerUp();
            return;
        }
        Image img = allTrashImages.get(random.nextInt(allTrashImages.size()));
        ImageView trash = new ImageView(img);
        // Animate trash (rotation)
        trash.setRotate(random.nextInt(360));
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
            trash.setY(trash.getY() + trashSpeed * deltaSeconds * (slowMotionActive ? 0.5 : 1.0));
            trash.setRotate(trash.getRotate() + 2);
        }
    }

    private void checkCollisions(Stage primaryStage) {
        Iterator<ImageView> iterator = trashItems.iterator();
        while (iterator.hasNext()) {
            ImageView trash = iterator.next();
            if ("powerup".equals(trash.getUserData())) {
                activatePowerUp();
                gamePane.getChildren().remove(trash);
                iterator.remove();
                continue;
            }
            if (trash.getBoundsInParent().intersects(boat.getBoundsInParent())) {
                gamePane.getChildren().remove(trash);
                iterator.remove();
                int points = 10 * multiplier * (doublePointsActive ? 2 : 1);
                score += points;
                combo++;
                maxCombo = Math.max(maxCombo, combo);
                showScorePopup(trash.getX(), trash.getY(), "+" + points);
                scoreText.setText("Score: " + score);
                if (combo % 5 == 0) {
                    multiplier++;
                    showScorePopup(trash.getX(), trash.getY() - 20, "Combo! x" + multiplier);
                }
            } else if (trash.getY() > HEIGHT) {
                gamePane.getChildren().remove(trash);
                iterator.remove();
                missedTrash++;
                combo = 0;
                multiplier = 1;
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
        // Update leaderboard and show
        leaderboard.add(score);
        leaderboard.sort((a, b) -> b - a);
        while (leaderboard.size() > 5) leaderboard.remove(leaderboard.size() - 1);
        leaderboardBox.getChildren().clear();
        leaderboardBox.getChildren().add(new Text("Leaderboard (Top 5):"));
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboardBox.getChildren().add(new Text((i + 1) + ". " + leaderboard.get(i)));
        }
        gamePane.getChildren().add(leaderboardBox);
        leaderboardBox.setLayoutX(WIDTH / 2.0 - 100);
        leaderboardBox.setLayoutY(HEIGHT / 2.0 + 100);
    }

    public static void show(Stage primaryStage) {
        PollutionPatrolGame game = new PollutionPatrolGame(primaryStage);
        Scene scene = new Scene(game, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pollution Patrol");
        primaryStage.show();
    }

    // Add spawnPowerUp():
    private void spawnPowerUp() {
        Image img = powerUpImages.get(random.nextInt(powerUpImages.size()));
        ImageView powerUp = new ImageView(img);
        powerUp.setFitWidth(36);
        powerUp.setFitHeight(36);
        powerUp.setX(random.nextInt(WIDTH - 36));
        powerUp.setY(0);
        powerUp.setUserData("powerup");
        trashItems.add(powerUp);
        gamePane.getChildren().add(powerUp);
    }

    // Add showScorePopup():
    private void showScorePopup(double x, double y, String text) {
        ScorePopup popup = new ScorePopup(x, y, text);
        scorePopups.add(popup);
        gamePane.getChildren().add(popup);
        popup.play(() -> {
            gamePane.getChildren().remove(popup);
            scorePopups.remove(popup);
        });
    }

    // Add ScorePopup class:
    private static class ScorePopup extends Text {
        public ScorePopup(double x, double y, String text) {
            super(text);
            setFont(Font.font("Quicksand", 22));
            setFill(Color.web("#43e97b"));
            setX(x);
            setY(y);
        }
        public void play(Runnable onFinish) {
            FadeTransition ft = new FadeTransition(Duration.seconds(1.2), this);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> onFinish.run());
            ft.play();
        }
    }

    // Add activatePowerUp():
    private void activatePowerUp() {
        if (random.nextBoolean()) {
            doublePointsActive = true;
            powerUpEndTime = System.currentTimeMillis() + 8000;
            showScorePopup(WIDTH / 2.0, 80, "Double Points!");
        } else {
            slowMotionActive = true;
            powerUpEndTime = System.currentTimeMillis() + 8000;
            showScorePopup(WIDTH / 2.0, 80, "Slow Motion!");
        }
    }

    // In gameLoop handle(), check power-up expiration:
    private void togglePause() {
        paused = !paused;
        pauseOverlay.setVisible(paused);
    }

    // In gameLoop handle(), check power-up expiration:
    private void resizeGame() {
        double scaleX = getWidth() / 1366.0;
        double scaleY = getHeight() / 768.0;
        gamePane.setScaleX(scaleX);
        gamePane.setScaleY(scaleY);
        overlayPane.setPrefSize(getWidth(), getHeight());
        pauseOverlay.setPrefSize(getWidth(), getHeight());
    }
} 