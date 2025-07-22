package com.ecoedu.avatar;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class AvatarCustomizer extends VBox {
    private ImageView avatarFace, avatarHair, avatarAccessory, avatarClothes;
    private int hairIndex = 0, faceIndex = 0, accessoryIndex = 0, clothesIndex = 0;
    private static final String[] HAIR_OPTIONS = {"/Assets/Images/hair1.png", "/Assets/Images/hair2.png", "/Assets/Images/hair3.png"};
    private static final String[] FACE_OPTIONS = {"/Assets/Images/face1.png", "/Assets/Images/face2.png", "/Assets/Images/face3.png"};
    private static final String[] ACCESSORY_OPTIONS = {"/Assets/Images/glasses1.png", "/Assets/Images/hat1.png", null};
    private static final String[] CLOTHES_OPTIONS = {"/Assets/Images/shirt1.png", "/Assets/Images/shirt2.png", "/Assets/Images/shirt3.png"};

    public AvatarCustomizer() {
        setSpacing(24);
        setPadding(new Insets(40, 60, 40, 60));
        setAlignment(Pos.TOP_CENTER);
        // Animated background
        Rectangle bgRect = new Rectangle(900, 700);
        bgRect.setArcWidth(60);
        bgRect.setArcHeight(60);
        bgRect.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#e1f5fe")),
            new Stop(0.6, Color.web("#b2ff59")),
            new Stop(1, Color.web("#fffde7"))
        ));
        FadeTransition fade = new FadeTransition(Duration.seconds(3), bgRect);
        fade.setFromValue(0.8);
        fade.setToValue(1.0);
        fade.setAutoReverse(true);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.play();
        getChildren().add(0, bgRect);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        Label title = new Label("Avatar Customization");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0288d1"));
        getChildren().add(title);

        // Avatar Preview
        VBox previewBox = new VBox();
        previewBox.setAlignment(Pos.CENTER);
        previewBox.setPadding(new Insets(16));
        previewBox.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 40; -fx-effect: dropshadow(gaussian, #81c784, 16, 0.2, 0, 4);");
        previewBox.setPrefSize(220, 260);
        // Layered avatar parts
        avatarFace = new ImageView();
        avatarHair = new ImageView();
        avatarAccessory = new ImageView();
        avatarClothes = new ImageView();
        for (ImageView part : new ImageView[]{avatarClothes, avatarFace, avatarHair, avatarAccessory}) {
            part.setFitWidth(160);
            part.setFitHeight(160);
            part.setPreserveRatio(true);
            part.setSmooth(true);
        }
        Circle clip = new Circle(80, 80, 80);
        avatarFace.setClip(clip);
        avatarHair.setClip(clip);
        avatarAccessory.setClip(clip);
        avatarClothes.setClip(clip);
        VBox avatarStack = new VBox(avatarClothes, avatarFace, avatarHair, avatarAccessory);
        avatarStack.setAlignment(Pos.CENTER);
        previewBox.getChildren().add(avatarStack);
        getChildren().add(previewBox);

        // Feature pickers
        getChildren().add(makeFeaturePicker("Hair", HAIR_OPTIONS, i -> {
            hairIndex = i;
            updateAvatar();
        }));
        getChildren().add(makeFeaturePicker("Face", FACE_OPTIONS, i -> {
            faceIndex = i;
            updateAvatar();
        }));
        getChildren().add(makeFeaturePicker("Accessory", ACCESSORY_OPTIONS, i -> {
            accessoryIndex = i;
            updateAvatar();
        }));
        getChildren().add(makeFeaturePicker("Clothes", CLOTHES_OPTIONS, i -> {
            clothesIndex = i;
            updateAvatar();
        }));

        // Buttons
        HBox btnBox = new HBox(24);
        btnBox.setAlignment(Pos.CENTER);
        Button saveBtn = new Button("Save");
        saveBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        saveBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 10 36; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> animateAvatar());
        Button resetBtn = new Button("Reset");
        resetBtn.setFont(Font.font("Quicksand", 16));
        resetBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #263238; -fx-background-radius: 20; -fx-padding: 8 28; -fx-cursor: hand;");
        resetBtn.setOnAction(e -> {
            hairIndex = faceIndex = accessoryIndex = clothesIndex = 0;
            updateAvatar();
        });
        Button randomBtn = new Button("Randomize");
        randomBtn.setFont(Font.font("Quicksand", 16));
        randomBtn.setStyle("-fx-background-color: #4fc3f7; -fx-text-fill: #fff; -fx-background-radius: 20; -fx-padding: 8 28; -fx-cursor: hand;");
        randomBtn.setOnAction(e -> {
            Random r = new Random();
            hairIndex = r.nextInt(HAIR_OPTIONS.length);
            faceIndex = r.nextInt(FACE_OPTIONS.length);
            accessoryIndex = r.nextInt(ACCESSORY_OPTIONS.length);
            clothesIndex = r.nextInt(CLOTHES_OPTIONS.length);
            updateAvatar();
            animateAvatar();
        });
        btnBox.getChildren().addAll(saveBtn, resetBtn, randomBtn);
        getChildren().add(btnBox);

        updateAvatar();
    }

    private void updateAvatar() {
        setImageSafe(avatarHair, HAIR_OPTIONS[hairIndex]);
        setImageSafe(avatarFace, FACE_OPTIONS[faceIndex]);
        setImageSafe(avatarAccessory, ACCESSORY_OPTIONS[accessoryIndex]);
        setImageSafe(avatarClothes, CLOTHES_OPTIONS[clothesIndex]);
        animateAvatar();
    }

    private void setImageSafe(ImageView view, String path) {
        if (path == null) {
            view.setImage(null);
            return;
        }
        try {
            view.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            view.setImage(null);
        }
    }

    private void animateAvatar() {
        ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(350), avatarFace);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.15);
        st.setToY(1.15);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private VBox makeFeaturePicker(String label, String[] options, java.util.function.IntConsumer onSelect) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        Label l = new Label(label);
        l.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        l.setTextFill(Color.web("#0288d1"));
        HBox picker = new HBox(12);
        picker.setAlignment(Pos.CENTER);
        for (int i = 0; i < options.length; i++) {
            VBox optBox = new VBox();
            optBox.setAlignment(Pos.CENTER);
            optBox.setPadding(new Insets(6));
            optBox.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 16; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #b2ff59, 4, 0.1, 0, 1);");
            ImageView icon = new ImageView();
            setImageSafe(icon, options[i]);
            icon.setFitWidth(48);
            icon.setFitHeight(48);
            icon.setPreserveRatio(true);
            optBox.getChildren().add(icon);
            int idx = i;
            optBox.setOnMouseClicked(e -> onSelect.accept(idx));
            optBox.setOnMouseEntered(e -> optBox.setStyle("-fx-background-color: #b2ff59; -fx-background-radius: 16; -fx-cursor: hand; -fx-scale-x:1.08;-fx-scale-y:1.08;-fx-effect: dropshadow(gaussian, #0288d1, 16, 0.3, 0, 4);"));
            optBox.setOnMouseExited(e -> optBox.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 16; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #b2ff59, 4, 0.1, 0, 1);"));
            picker.getChildren().add(optBox);
        }
        ScrollPane scroll = new ScrollPane(picker);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPrefHeight(80);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        box.getChildren().addAll(l, scroll);
        return box;
    }

    public void start(Stage stage) {
        Scene scene = new Scene(this, 700, 700);
        stage.setScene(scene);
        stage.setTitle("EcoEdu - Avatar Customization");
        stage.show();
    }
} 