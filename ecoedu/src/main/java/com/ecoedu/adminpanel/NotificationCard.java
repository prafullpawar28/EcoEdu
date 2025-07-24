package com.ecoedu.adminpanel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationCard extends StackPane {
    public NotificationCard(String iconPath, String text, LocalDateTime time, Color bgColor) {
        setStyle("-fx-background-radius: 14; -fx-background-color: white; -fx-effect: dropshadow(gaussian, #e0e0e0, 8, 0.10, 0, 2);");
        setPrefHeight(60);
        setMaxWidth(Double.MAX_VALUE);
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        Circle circle = new Circle(18, bgColor);
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        Label label = new Label(text);
        label.setFont(Font.font("Quicksand", FontWeight.NORMAL, 15));
        label.setTextFill(Color.web("#22223b"));
        Label timeLabel = new Label(time.format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(Font.font("Quicksand", 12));
        timeLabel.setTextFill(Color.web("#7b7b93"));
        box.getChildren().addAll(circle, icon, label, timeLabel);
        getChildren().add(box);
        setOnMouseEntered(e -> setStyle(getStyle() + "-fx-effect: dropshadow(gaussian, #43e97b, 14, 0.18, 0, 8);"));
        setOnMouseExited(e -> setStyle("-fx-background-radius: 14; -fx-background-color: white; -fx-effect: dropshadow(gaussian, #e0e0e0, 8, 0.10, 0, 2);"));
    }
} 