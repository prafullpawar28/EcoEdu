package com.ecoedu.adminpanel;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class StatCard extends StackPane {
    private final Label numberLabel;
    private int currentValue = 0;
    private final Timeline counterTimeline = new Timeline();

    public StatCard(String iconPath, String label, int value, Color bgColor) {
        setStyle("-fx-background-radius: 18; -fx-background-color: white; -fx-effect: dropshadow(gaussian, #e0e0e0, 10, 0.12, 0, 2);");
        setPrefSize(200, 110);
        setMinSize(120, 80);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER_LEFT);
        HBox iconBox = new HBox();
        iconBox.setAlignment(Pos.CENTER_LEFT);
        Circle circle = new Circle(24, bgColor);
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(28);
        icon.setFitHeight(28);
        iconBox.getChildren().addAll(circle, icon);
        icon.setTranslateX(-24);
        numberLabel = new Label("0");
        numberLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        numberLabel.setTextFill(Color.web("#22223b"));
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Quicksand", 16));
        lbl.setTextFill(Color.web("#7b7b93"));
        box.getChildren().addAll(iconBox, numberLabel, lbl);
        getChildren().add(box);
        setOnMouseEntered(e -> setStyle(getStyle() + "-fx-effect: dropshadow(gaussian, #43e97b, 18, 0.18, 0, 8);"));
        setOnMouseExited(e -> setStyle("-fx-background-radius: 18; -fx-background-color: white; -fx-effect: dropshadow(gaussian, #e0e0e0, 10, 0.12, 0, 2);"));
        animateTo(value);
    }
    public void animateTo(int newValue) {
        counterTimeline.stop();
        KeyValue kv = new KeyValue(numberLabel.textProperty(), String.valueOf(newValue));
        KeyFrame kf = new KeyFrame(Duration.seconds(1), event -> currentValue = newValue, kv);
        counterTimeline.getKeyFrames().setAll(kf);
        counterTimeline.play();
    }
} 