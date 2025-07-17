package com.ecoedu.minigames;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class OceanCleanupGame extends VBox {
    private Stage primaryStage;
    private int score = 0;
    private Label scoreLabel;
    private Pane oceanPane;
    private List<TrashSprite> trashSprites;
    private Rectangle netBin;
    private final int OCEAN_WIDTH = 700;
    private final int OCEAN_HEIGHT = 350;
    private final int TRASH_COUNT = 8;
    private TrashSprite currentlyDragged = null;
    private boolean gameOverShown = false;

    public OceanCleanupGame(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom, #b3e5fc 60%, #e0f7fa 100%);");

        Label title = new Label("🌊 Ocean Cleanup Game");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#0277bd"));
        getChildren().add(title);

        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Comic Sans MS", 18));
        scoreLabel.setTextFill(Color.web("#0288d1"));
        getChildren().add(scoreLabel);

        oceanPane = new Pane();
        oceanPane.setPrefSize(OCEAN_WIDTH, OCEAN_HEIGHT);
        oceanPane.setStyle("-fx-background-color: linear-gradient(to bottom, #4fc3f7 80%, #01579b 100%); -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #0288d1; -fx-border-width: 4;");
        oceanPane.setEffect(new DropShadow(16, Color.web("#0288d1")));
        getChildren().add(oceanPane);

        // Animated wave background
        Rectangle wave = new Rectangle(0, OCEAN_HEIGHT - 60, OCEAN_WIDTH, 60);
        wave.setFill(Color.web("#81d4fa", 0.7));
        oceanPane.getChildren().add(wave);
        javafx.animation.TranslateTransition waveAnim = new javafx.animation.TranslateTransition(Duration.seconds(3), wave);
        waveAnim.setByX(60);
        waveAnim.setAutoReverse(true);
        waveAnim.setCycleCount(javafx.animation.Animation.INDEFINITE);
        waveAnim.play();

        // Net/bin at the bottom
        netBin = new Rectangle(OCEAN_WIDTH / 2.0 - 60, OCEAN_HEIGHT - 30, 120, 40);
        netBin.setArcWidth(30);
        netBin.setArcHeight(30);
        netBin.setFill(Color.web("#ffd54f"));
        netBin.setStroke(Color.web("#ffb300"));
        netBin.setStrokeWidth(3);
        oceanPane.getChildren().add(netBin);

        Label netLabel = new Label("Cleanup Net");
        netLabel.setFont(Font.font("Comic Sans MS", 14));
        netLabel.setTextFill(Color.web("#ff6f00"));
        netLabel.setLayoutX(OCEAN_WIDTH / 2.0 - 40);
        netLabel.setLayoutY(OCEAN_HEIGHT - 25);
        oceanPane.getChildren().add(netLabel);

        // Robust drag-and-drop handlers for netBin
        netBin.setOnDragOver(e -> {
            if (currentlyDragged != null && e.getGestureSource() != netBin && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
        netBin.setOnDragDropped(e -> {
            if (currentlyDragged != null) {
                handleDrop(currentlyDragged);
            }
            e.setDropCompleted(true);
            e.consume();
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> com.ecoedu.Home.Dashboard.show(primaryStage));
        getChildren().add(backBtn);

        resetGame();
    }

    private void resetGame() {
        score = 0;
        gameOverShown = false;
        updateScore();
        oceanPane.getChildren().removeIf(node -> node instanceof TrashSprite);
        trashSprites = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < TRASH_COUNT + 6; i++) { // More trash
            TrashType type = TrashType.values()[rand.nextInt(TrashType.values().length)];
            double x = 40 + rand.nextDouble() * (OCEAN_WIDTH - 80);
            double y = 40 + rand.nextDouble() * (OCEAN_HEIGHT - 120);
            TrashSprite sprite = new TrashSprite(type, x, y, i);
            trashSprites.add(sprite);
            oceanPane.getChildren().add(sprite);
        }
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    // Only called when drop is on netBin
    private void handleDrop(TrashSprite sprite) {
        score += 15;
        playSuccessAnimation(sprite);
        oceanPane.getChildren().remove(sprite);
        trashSprites.remove(sprite);
        updateScore();
        if (trashSprites.isEmpty() && !gameOverShown) {
            showGameOver();
            gameOverShown = true;
        }
        currentlyDragged = null;
    }

    private void playSuccessAnimation(TrashSprite sprite) {
        ScaleTransition st = new ScaleTransition(Duration.millis(350), sprite);
        st.setToX(1.4);
        st.setToY(1.4);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.setOnFinished(ev -> {
            sprite.setVisible(false);
            // Splash animation
            Circle splash = new Circle(sprite.getLayoutX() + 27, sprite.getLayoutY() + 27, 5, Color.web("#b3e5fc", 0.7));
            oceanPane.getChildren().add(splash);
            javafx.animation.ScaleTransition splashAnim = new javafx.animation.ScaleTransition(Duration.millis(500), splash);
            splashAnim.setToX(6);
            splashAnim.setToY(6);
            splashAnim.setOnFinished(e2 -> oceanPane.getChildren().remove(splash));
            splashAnim.play();
        });
        st.play();
    }

    private void playFailAnimation(TrashSprite sprite) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), sprite);
        ft.setFromValue(1.0);
        ft.setToValue(0.2);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        ft.play();
    }

    private void showGameOver() {
        Label gameOver = new Label("Ocean Cleaned! Final Score: " + score);
        gameOver.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        gameOver.setTextFill(Color.web("#0277bd"));
        getChildren().add(gameOver);
    }

    public static void show(Stage primaryStage) {
        OceanCleanupGame game = new OceanCleanupGame(primaryStage);
        Scene scene = new Scene(game, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Ocean Cleanup Game");
        primaryStage.show();
    }

    // --- Trash Types ---
    public enum TrashType {
        BOTTLE("Plastic Bottle", "#90caf9"),
        CAN("Can", "#bdbdbd"),
        BAG("Plastic Bag", "#fff176"),
        CUP("Cup", "#ffb74d"),
        STRAW("Straw", "#e57373"),
        NET("Fishing Net", "#388e3c"),
        FLIPFLOP("Flip Flop", "#ff8a65"),
        TOOTHBRUSH("Toothbrush", "#9575cd"),
        WRAPPER("Food Wrapper", "#f06292"),
        CAP("Bottle Cap", "#00bcd4"),
        ROPE("Rope", "#6d4c41"),
        LIGHTER("Lighter", "#ff5252"),
        BALLOON("Balloon", "#ffd600"),
        SPOON("Spoon", "#b0bec5"),
        FORK("Fork", "#cfd8dc");
        private final String displayName;
        private final String color;
        TrashType(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }

    // --- Trash Sprite Node ---
    public class TrashSprite extends StackPane {
        private final TrashType type;
        private final String trashId;
        public TrashSprite(TrashType type, double x, double y, int uniqueId) {
            this.type = type;
            this.trashId = type.name() + "_" + uniqueId;
            setLayoutX(x);
            setLayoutY(y);
            setPrefSize(54, 54);
            setMaxSize(54, 54);
            setStyle("-fx-background-radius: 27; -fx-cursor: hand; -fx-border-color: #0288d1; -fx-border-width: 2; -fx-background-color: white;");
            Circle icon = new Circle(24, Color.web(type.getColor()));
            icon.setEffect(new DropShadow(8, Color.web("#0288d1")));
            Label label = new Label(type.getDisplayName());
            label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 12));
            label.setTextFill(Color.web("#01579b"));
            getChildren().addAll(icon, label);
            setOnDragDetected(this::handleDragDetected);
            setOnDragDone(e -> setOpacity(1.0));
            // Floating and drifting animation
            javafx.animation.TranslateTransition floatAnim = new javafx.animation.TranslateTransition(Duration.seconds(2 + Math.random()), this);
            floatAnim.setByY(10 + Math.random() * 10);
            floatAnim.setAutoReverse(true);
            floatAnim.setCycleCount(javafx.animation.Animation.INDEFINITE);
            floatAnim.play();
            javafx.animation.TranslateTransition driftAnim = new javafx.animation.TranslateTransition(Duration.seconds(3 + Math.random()), this);
            driftAnim.setByX(20 - Math.random() * 40);
            driftAnim.setAutoReverse(true);
            driftAnim.setCycleCount(javafx.animation.Animation.INDEFINITE);
            driftAnim.play();
        }
        public TrashType getType() { return type; }
        public String getTrashId() { return trashId; }
        private void handleDragDetected(MouseEvent e) {
            javafx.scene.input.Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(trashId);
            db.setContent(content);
            setOpacity(0.6);
            currentlyDragged = this;
            e.consume();
        }
    }
} 