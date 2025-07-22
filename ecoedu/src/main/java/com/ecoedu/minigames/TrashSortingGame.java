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

public class TrashSortingGame extends VBox {
    private Stage primaryStage;
    private int score = 0;
    private Label scoreLabel;
    private GridPane trashGrid;
    private List<TrashItem> trashItems;
    private Map<TrashType, VBox> bins;
    private final int GRID_SIZE = 3;
    private final int TRASH_COUNT = 9;

    public TrashSortingGame(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(24);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // Score label
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        scoreLabel.setTextFill(Color.web("#43a047"));
        getChildren().add(scoreLabel);

        Label title = new Label("Trash Sorting Minigame");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#388e3c"));
        getChildren().add(title);

        HBox binsBox = new HBox(40);
        binsBox.setAlignment(Pos.CENTER);
        bins = new HashMap<>();
        for (TrashType type : TrashType.values()) {
            VBox bin = createBin(type);
            bins.put(type, bin);
            binsBox.getChildren().add(bin);
        }
        getChildren().add(binsBox);

        trashGrid = new GridPane();
        trashGrid.setHgap(30);
        trashGrid.setVgap(30);
        trashGrid.setAlignment(Pos.CENTER);
        getChildren().add(trashGrid);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        getChildren().add(backBtn);

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
        Label gameOver = new Label("Game Over! Final Score: " + score);
        gameOver.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        gameOver.setTextFill(Color.web("#d84315"));
        getChildren().add(gameOver);
        Button playAgainBtn = new Button("🔄 Play Again");
        playAgainBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 20; -fx-padding: 10 32; -fx-effect: dropshadow(gaussian, #388e3c, 8, 0.2, 0, 2);");
        playAgainBtn.setOnAction(e -> {
            getChildren().remove(gameOver);
            getChildren().remove(playAgainBtn);
            // Remove back button if present
            getChildren().removeIf(node -> node instanceof Button && ((Button)node).getText().contains("Back to Minigames"));
            resetGame();
        });
        getChildren().removeIf(node -> node instanceof Button && ((Button)node).getText().contains("Play Again"));
        getChildren().add(playAgainBtn);
        // Add Back to Minigames button
        Button backBtn = new Button("← Back to Minigames");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #0288d1, 4, 0.2, 0, 1);");
        backBtn.setOnAction(e -> {
            javafx.stage.Stage stage = (javafx.stage.Stage) getScene().getWindow();
            com.ecoedu.minigames.MinigamesPage.show(stage);
        });
        // Remove any existing back button before adding
        getChildren().removeIf(node -> node instanceof Button && ((Button)node).getText().contains("Back to Minigames"));
        getChildren().add(backBtn);
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
