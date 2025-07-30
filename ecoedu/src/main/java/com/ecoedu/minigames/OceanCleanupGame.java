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

import com.ecoedu.auth.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuth;

import javafx.scene.layout.HBox;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.layout.Region;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.SVGPath;
import javafx.application.Platform;
import javafx.animation.AnimationTimer;

public class OceanCleanupGame extends VBox {
    private Stage primaryStage;
    private static int finalScore=0;
    private static int score = 0;
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
        setSpacing(0);
        setPadding(new Insets(0));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: transparent;");
        StackPane mainStack = new StackPane();
        mainStack.setPrefSize(1366, 768);
        // Animated gradient background
        Region gradientBg = new Region();
        gradientBg.setPrefSize(1366, 768);
        gradientBg.setStyle("-fx-background-radius: 0; -fx-background-color: linear-gradient(to bottom, #b3e5fc 0%, #e0f7fa 60%, #b2dfdb 100%);");
        mainStack.getChildren().add(gradientBg);
        // Animated waves at the bottom
        Pane wavePane = new Pane();
        wavePane.setPrefSize(1366, 120);
        wavePane.setLayoutY(768 - 120);
        for (int i = 0; i < 4; i++) {
            SVGPath wave = new SVGPath();
            wave.setContent("M0,60 Q150,80 300,60 T600,60 T900,60 T1200,60 T1366,60 V120 H0 Z");
            wave.setFill(Color.web(i % 2 == 0 ? "#4fc3f7" : "#b2ebf2", 0.18 + 0.08 * i));
            wave.setLayoutY(i * 10);
            wavePane.getChildren().add(wave);
            Timeline waveAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(wave.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(6 + i * 2), new KeyValue(wave.translateXProperty(), -200 + i * 50))
            );
            waveAnim.setCycleCount(Timeline.INDEFINITE);
            waveAnim.setAutoReverse(true);
            waveAnim.play();
        }
        mainStack.getChildren().add(wavePane);
        // Bubble animation
        Pane bubblePane = new Pane();
        bubblePane.setPrefSize(1366, 768);
        mainStack.getChildren().add(bubblePane);
        AnimationTimer bubbleTimer = new AnimationTimer() {
            private Random rand = new Random();
            private ArrayList<Circle> bubbles = new ArrayList<>();
            @Override
            public void handle(long now) {
                if (rand.nextDouble() < 0.03) {
                    Circle bubble = new Circle(8 + rand.nextInt(10), Color.web("#b3e5fc", 0.4 + rand.nextDouble() * 0.3));
                    bubble.setCenterX(30 + rand.nextInt(1366 - 60));
                    bubble.setCenterY(768 + 20);
                    bubbles.add(bubble);
                    bubblePane.getChildren().add(0, bubble);
                }
                Iterator<Circle> iterator = bubbles.iterator();
                while (iterator.hasNext()) {
                    Circle bubble = iterator.next();
                    bubble.setCenterY(bubble.getCenterY() - (1.5 + rand.nextDouble() * 1.5));
                    if (bubble.getCenterY() < -20) {
                        bubblePane.getChildren().remove(bubble);
                        iterator.remove();
                    }
                }
            }
        };
        bubbleTimer.start();
        // Main game VBox
        VBox gameVBox = new VBox(20);
        gameVBox.setAlignment(Pos.TOP_CENTER);
        gameVBox.setPrefWidth(1366);
        gameVBox.setPadding(new Insets(40, 0, 0, 0));
        mainStack.getChildren().add(gameVBox);

        Label title = new Label("🌊 Ocean Cleanup Game");
        title.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.web("#00c6ff"));
        title.setStyle("-fx-effect: dropshadow(gaussian, #43e97b, 10, 0.3, 0, 2); -fx-padding: 0 0 18 0; -fx-alignment: center;");
        gameVBox.getChildren().add(title);

        // Score label
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        scoreLabel.setTextFill(Color.web("#43a047"));
        // Floating score panel
        StackPane scorePanel = new StackPane();
        scorePanel.setPrefSize(220, 54);
        scorePanel.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.2, 0, 2); -fx-border-color: #00c6ff; -fx-border-width: 2; -fx-border-radius: 18;");
        HBox scoreBox = new HBox(scoreLabel);
        scoreBox.setAlignment(Pos.CENTER);
        scorePanel.getChildren().add(scoreBox);
        gameVBox.getChildren().add(scorePanel);

        oceanPane = new Pane();
        oceanPane.setPrefSize(OCEAN_WIDTH, OCEAN_HEIGHT);
        oceanPane.setStyle("-fx-background-color: linear-gradient(to bottom, #4fc3f7 80%, #01579b 100%); -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #0288d1; -fx-border-width: 4;");
        oceanPane.setEffect(new DropShadow(16, Color.web("#0288d1")));
        gameVBox.getChildren().add(oceanPane);

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

        Button backBtn = new Button("Back to Minigames");
        backBtn.setFont(Font.font("Quicksand", 18));
        backBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 8 32; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: #fffde7; -fx-background-radius: 18; -fx-padding: 8 32; -fx-cursor: hand; -fx-scale-x:1.07;-fx-scale-y:1.07;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 8 32; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> {
        finalScore=Integer.max(score,finalScore);
            new Thread(() -> {
                FirebaseAuthService fb = new FirebaseAuthService();
                fb.updateGameScore("game2",finalScore);
               System.out.println("game2 score updated");
            }).start();
        com.ecoedu.minigames.MinigamesPage.show(primaryStage);
        });
        gameVBox.getChildren().add(backBtn);
        getChildren().add(mainStack);

        resetGame();
    }

    private void resetGame() {
        finalScore=Integer.max(score,finalScore);
        score = 0;
        gameOverShown = false;
        updateScore(0);
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

    private void updateScore(int delta) {
        score += delta;
        scoreLabel.setText("Score: " + score);
        // Animate score label
        ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(250), scoreLabel);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    // Only called when drop is on netBin
    private void handleDrop(TrashSprite sprite) {
        updateScore(15);
        playSuccessAnimation(sprite);
        oceanPane.getChildren().remove(sprite);
        trashSprites.remove(sprite);
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
        StackPane overlay = new StackPane();
        overlay.setPrefSize(1366, 768);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        VBox box = new VBox(24);
        box.setAlignment(Pos.CENTER);
        Label gameOver = new Label("Ocean Cleaned!\nFinal Score: " + score);
        gameOver.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 32));
        gameOver.setTextFill(Color.web("#fffde7"));
        Button playAgainBtn = new Button("🔄 Play Again");
        playAgainBtn.setFont(Font.font("Quicksand", 20));
        playAgainBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 12 40; -fx-cursor: hand;");
        playAgainBtn.setOnMouseEntered(e -> playAgainBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: #fffde7; -fx-background-radius: 18; -fx-padding: 12 40; -fx-cursor: hand; -fx-scale-x:1.07;-fx-scale-y:1.07;"));
        playAgainBtn.setOnMouseExited(e -> playAgainBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 12 40; -fx-cursor: hand;"));
        playAgainBtn.setOnAction(e -> {
            getChildren().remove(overlay);
            resetGame();
        });
        Button backBtn = new Button("← Back to Minigames");
        backBtn.setFont(Font.font("Quicksand", 18));
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #0288d1, 4, 0.2, 0, 1);");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #00c6ff; -fx-text-fill: #fffde7; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand; -fx-scale-x:1.07;-fx-scale-y:1.07;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #0288d1, 4, 0.2, 0, 1);"));
        backBtn.setOnAction(e -> {
             finalScore=Integer.max(score,finalScore);
            new Thread(() -> {
                FirebaseAuthService fb = new FirebaseAuthService();
                fb.updateGameScore("game2",finalScore);
               System.out.println("game2 score updated");
            }).start();
        
            javafx.stage.Stage stage = (javafx.stage.Stage) getScene().getWindow();
            com.ecoedu.minigames.MinigamesPage.show(stage);
        });
        box.getChildren().addAll(gameOver, playAgainBtn, backBtn);
        overlay.getChildren().add(box);
        overlay.setOpacity(0);
        getChildren().add(overlay);
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), overlay);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public static void show(Stage primaryStage) {
        OceanCleanupGame game = new OceanCleanupGame(primaryStage);
        VBox root = new VBox();
        root.setSpacing(0);
        // Top bar with back button
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button backBtn = new Button("← Back to Minigames");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> {
            finalScore=Integer.max(score,finalScore);
            new Thread(() -> {
                FirebaseAuthService fb = new FirebaseAuthService();
                fb.updateGameScore("game2",finalScore);
               System.out.println("game2 score updated");
            }).start();
            com.ecoedu.minigames.MinigamesPage.show(primaryStage);
        });
        topBar.getChildren().add(backBtn);
        root.getChildren().add(topBar);
        root.getChildren().add(game);
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ocean Cleanup Game");
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