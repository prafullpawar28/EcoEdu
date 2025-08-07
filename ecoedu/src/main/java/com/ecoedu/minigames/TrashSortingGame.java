package com.ecoedu.minigames;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.control.Label;
import javafx.scene.text.FontWeight;
import javafx.scene.shape.SVGPath;
import javafx.application.Platform;
import javafx.animation.AnimationTimer;

public class TrashSortingGame extends VBox {
    private Stage primaryStage;
    private int finalScore = 0;
    private int score = 0;
    private Label scoreLabel;
    private GridPane trashGrid;
    private List<TrashItem> trashItems;
    private Map<TrashType, VBox> bins;
    private final int GRID_SIZE = 3;
    private final int TRASH_COUNT = 9;

    public TrashSortingGame(Stage primaryStage) {
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
        gradientBg.setStyle("-fx-background-radius: 0; -fx-background-color: linear-gradient(to bottom, #e1f5fe 0%, #b2ebf2 60%, #b2dfdb 100%);");
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

        Label title = new Label("Trash Sorting Minigame");
        title.setFont(Font.font("Quicksand", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.web("#00c6ff"));
        title.setStyle("-fx-effect: dropshadow(gaussian, #43e97b, 10, 0.3, 0, 2); -fx-padding: 0 0 18 0; -fx-alignment: center;");
        gameVBox.getChildren().add(title);

        HBox binsBox = new HBox(40);
        binsBox.setAlignment(Pos.CENTER);
        bins = new HashMap<>();
        for (TrashType type : TrashType.values()) {
            VBox bin = createBin(type);
            bins.put(type, bin);
            binsBox.getChildren().add(bin);
        }
        gameVBox.getChildren().add(binsBox);

        trashGrid = new GridPane();
        trashGrid.setHgap(30);
        trashGrid.setVgap(30);
        trashGrid.setAlignment(Pos.CENTER);
        gameVBox.getChildren().add(trashGrid);

        Button backBtn = new Button("Back to Minigames");
        backBtn.setFont(Font.font("Quicksand", 18));
        backBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 8 32; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: #fffde7; -fx-background-radius: 18; -fx-padding: 8 32; -fx-cursor: hand; -fx-scale-x:1.07;-fx-scale-y:1.07;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 8 32; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> com.ecoedu.minigames.MinigamesPage.show(primaryStage));
        gameVBox.getChildren().add(backBtn);
        getChildren().add(mainStack);

        resetGame();
    }

    private VBox createBin(TrashType type) {
        VBox bin = new VBox(8);
        bin.setAlignment(Pos.CENTER);
        Rectangle binShape = new Rectangle(80, 100);
        binShape.setArcWidth(24);
        binShape.setArcHeight(24);
        binShape.setFill(Color.web(type.getColor()));
        binShape.setStroke(Color.web("#263238"));
        binShape.setStrokeWidth(3);
        Label label = new Label(type.getDisplayName() + " Bin");
        label.setFont(Font.font("Comic Sans MS", 14));
        label.setTextFill(Color.web("#fffde7"));
        bin.getChildren().addAll(binShape, label);
        bin.setOnDragOver(e -> {
            if (e.getGestureSource() != bin && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });
        bin.setOnDragDropped(e -> handleDrop(e, type));
        return bin;
    }

    private void resetGame() {
        finalScore =Integer.max(score, finalScore);
        score = 0;
        updateScore(0);
        trashGrid.getChildren().clear();
        trashItems = new ArrayList<>();
        List<TrashType> types = Arrays.asList(TrashType.values());
        Random rand = new Random();
        for (int i = 0; i < TRASH_COUNT; i++) {
            TrashType type = types.get(rand.nextInt(types.size()));
            TrashItem item = new TrashItem(type, i);
            trashItems.add(item);
        }
        int idx = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (idx < trashItems.size()) {
                    trashGrid.add(trashItems.get(idx), col, row);
                    idx++;
                }
            }
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

    private void handleDrop(DragEvent e, TrashType binType) {
        String trashId = e.getDragboard().getString();
        TrashItem item = trashItems.stream().filter(t -> t.getTrashId().equals(trashId)).findFirst().orElse(null);
        if (item != null) {
            if (item.getType() == binType) {
                updateScore(10);
                playSuccessAnimation(item);
            } else {
                updateScore(-5);
                playFailAnimation(item);
            }
            trashGrid.getChildren().remove(item);
            trashItems.remove(item);
            if (trashItems.isEmpty()) {
                showGameOver();
            }
        }
        e.setDropCompleted(true);
        e.consume();
    }

    private void playSuccessAnimation(TrashItem item) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), item);
        st.setToX(1.3);
        st.setToY(1.3);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.setOnFinished(ev -> item.setVisible(false));
        st.play();
        // Bubble pop effect
        Circle pop = new Circle(item.getLayoutX() + 35, item.getLayoutY() + 35, 5, Color.web("#b3e5fc", 0.7));
        trashGrid.getChildren().add(pop);
        ScaleTransition popAnim = new ScaleTransition(Duration.millis(400), pop);
        popAnim.setToX(6);
        popAnim.setToY(6);
        popAnim.setOnFinished(e2 -> trashGrid.getChildren().remove(pop));
        popAnim.play();
    }

    private void playFailAnimation(TrashItem item) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), item);
        ft.setFromValue(1.0);
        ft.setToValue(0.2);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        ft.setOnFinished(ev -> item.setVisible(false));
        ft.play();
    }

    private void showGameOver() {
        // Optionally, update LeaderboardService here if you want to simulate a real-time update
        // For now, just show the game over UI
        StackPane overlay = new StackPane();
        overlay.setPrefSize(1366, 768);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        VBox box = new VBox(24);
        box.setAlignment(Pos.CENTER);
        Label gameOver = new Label("Game Over!\nFinal Score: " + score);
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
                fb.updateGameScore("game1",finalScore);
               System.out.println("game1 score updated");
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
        TrashSortingGame game = new TrashSortingGame(primaryStage);
        VBox root = new VBox();
        root.setSpacing(0);
        // Top bar with back button
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_LEFT);
        Button backBtn = new Button("← Back to Minigames");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.minigames.MinigamesPage.show(primaryStage));
        topBar.getChildren().add(backBtn);
        root.getChildren().add(topBar);
        root.getChildren().add(game);
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trash Sorting Game");
        primaryStage.show();
    }

    // --- Trash Types ---
    public enum TrashType {
        SOLID("Solid Waste", "#8d6e63"),
        WET("Wet Waste", "#43a047"),
        ELECTRICAL("Electrical Waste", "#0288d1");
        private final String displayName;
        private final String color;
        TrashType(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }

    // --- Trash Item Node ---
    public static class TrashItem extends StackPane {
        private final TrashType type;
        private final String trashId;
        public TrashItem(TrashType type, int uniqueId) {
            this.type = type;
            this.trashId = type.name() + "_" + uniqueId;
            setPrefSize(70, 70);
            setMaxSize(70, 70);
            setStyle("-fx-background-radius: 35; -fx-cursor: hand; -fx-border-color: #bdbdbd; -fx-border-width: 2; -fx-background-color: white;");
            Circle icon = new Circle(30, Color.web(type.getColor()));
            icon.setEffect(new DropShadow(8, Color.web("#bdbdbd")));
            Label label = new Label(type.getDisplayName().split(" ")[0]);
            label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 14));
            label.setTextFill(Color.WHITE);
            getChildren().addAll(icon, label);
            setOnDragDetected(this::handleDragDetected);
            // Gentle floating animation
            TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(2 + Math.random()), this);
            floatAnim.setByY(10 + Math.random() * 10);
            floatAnim.setAutoReverse(true);
            floatAnim.setCycleCount(javafx.animation.Animation.INDEFINITE);
            floatAnim.play();
            TranslateTransition driftAnim = new TranslateTransition(Duration.seconds(3 + Math.random()), this);
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
            e.consume();
        }
    }
}
