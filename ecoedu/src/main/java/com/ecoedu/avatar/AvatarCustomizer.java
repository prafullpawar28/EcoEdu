package com.ecoedu.avatar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class AvatarCustomizer extends Application {
    private ComboBox<String> hairStyleBox;
    private ComboBox<String> hairColorBox;
    private ComboBox<String> skinToneBox;
    private ComboBox<String> eyeColorBox;
    private List<CheckBox> accessoryChecks;
    private Label summaryLabel;
    private StackPane avatarPreview;
    private final int AVATAR_SIZE = 180;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(18);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #fceabb, #f8b500, #fceabb);");

        Label title = new Label("🎨 Create Your Eco Hero!");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#ff6f00"));
        title.setEffect(new DropShadow(4, Color.web("#ffd54f")));

        // Avatar Preview
        avatarPreview = new StackPane();
        avatarPreview.setPrefSize(AVATAR_SIZE, AVATAR_SIZE + 30);
        avatarPreview.setStyle("-fx-background-color: #fffde7; -fx-background-radius: 100; -fx-border-radius: 100; -fx-border-color: #ffd54f; -fx-border-width: 4;");
        updateAvatarPreview();

        // Hair Style
        hairStyleBox = new ComboBox<>();
        hairStyleBox.getItems().addAll(AvatarOptions.getHairStyles());
        hairStyleBox.setPromptText("Hair Style");
        hairStyleBox.setStyle("-fx-font-size: 16px; -fx-background-color: #ffe082;");
        hairStyleBox.setTooltip(new Tooltip("Choose a fun hair style!"));
        hairStyleBox.setOnAction(e -> updateAvatarPreview());
        addHoverEffect(hairStyleBox);

        // Hair Color
        hairColorBox = new ComboBox<>();
        hairColorBox.getItems().addAll(AvatarOptions.getHairColors());
        hairColorBox.setPromptText("Hair Color");
        hairColorBox.setStyle("-fx-font-size: 16px; -fx-background-color: #ffd54f;");
        hairColorBox.setTooltip(new Tooltip("Pick a cool hair color!"));
        hairColorBox.setOnAction(e -> updateAvatarPreview());
        addHoverEffect(hairColorBox);

        // Skin Tone
        skinToneBox = new ComboBox<>();
        skinToneBox.getItems().addAll(AvatarOptions.getSkinTones());
        skinToneBox.setPromptText("Skin Tone");
        skinToneBox.setStyle("-fx-font-size: 16px; -fx-background-color: #fff9c4;");
        skinToneBox.setTooltip(new Tooltip("Select a skin tone!"));
        skinToneBox.setOnAction(e -> updateAvatarPreview());
        addHoverEffect(skinToneBox);

        // Eye Color
        eyeColorBox = new ComboBox<>();
        eyeColorBox.getItems().addAll(AvatarOptions.getEyeColors());
        eyeColorBox.setPromptText("Eye Color");
        eyeColorBox.setStyle("-fx-font-size: 16px; -fx-background-color: #b3e5fc;");
        eyeColorBox.setTooltip(new Tooltip("Choose eye color!"));
        eyeColorBox.setOnAction(e -> updateAvatarPreview());
        addHoverEffect(eyeColorBox);

        // Accessories
        Label accessoriesLabel = new Label("Accessories:");
        accessoriesLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        accessoriesLabel.setTextFill(Color.web("#6a1b9a"));
        accessoryChecks = new ArrayList<>();
        VBox accessoriesBox = new VBox(5);
        for (String acc : AvatarOptions.getAccessories()) {
            CheckBox cb = new CheckBox(acc);
            cb.setFont(Font.font("Comic Sans MS", 15));
            cb.setStyle("-fx-text-fill: #4a148c;");
            cb.setTooltip(new Tooltip("Add " + acc + " to your avatar!"));
            cb.setOnAction(e -> updateAvatarPreview());
            addHoverEffect(cb);
            accessoryChecks.add(cb);
            accessoriesBox.getChildren().add(cb);
        }

        // Buttons
        Button showBtn = new Button("Show My Avatar!");
        showBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        showBtn.setStyle("-fx-background-color: #ffb300; -fx-text-fill: #fffde7; -fx-background-radius: 15; -fx-padding: 10 30;");
        showBtn.setOnAction(e -> showSummary());
        addHoverEffect(showBtn);

        Button resetBtn = new Button("Reset");
        resetBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        resetBtn.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 8 24;");
        resetBtn.setOnAction(e -> resetSelections());
        addHoverEffect(resetBtn);

        Button randomBtn = new Button("Randomize!");
        randomBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        randomBtn.setStyle("-fx-background-color: #A8E6CF; -fx-text-fill: #388E3C; -fx-background-radius: 15; -fx-padding: 8 24;");
        randomBtn.setOnAction(e -> randomizeAvatar());
        addHoverEffect(randomBtn);

        Button saveBtn = new Button("Save Avatar");
        saveBtn.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
        saveBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 8 24;");
        saveBtn.setOnAction(e -> showSaveFeedback());
        addHoverEffect(saveBtn);

        summaryLabel = new Label("");
        summaryLabel.setFont(Font.font("Comic Sans MS", 16));
        summaryLabel.setTextFill(Color.web("#4a148c"));
        summaryLabel.setWrapText(true);
        summaryLabel.setPadding(new Insets(10, 0, 0, 0));

        // Layout
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(18);
        grid.setAlignment(Pos.CENTER);
        grid.add(new Label("Hair Style:"), 0, 0);
        grid.add(hairStyleBox, 1, 0);
        grid.add(new Label("Hair Color:"), 0, 1);
        grid.add(hairColorBox, 1, 1);
        grid.add(new Label("Skin Tone:"), 0, 2);
        grid.add(skinToneBox, 1, 2);
        grid.add(new Label("Eye Color:"), 0, 3);
        grid.add(eyeColorBox, 1, 3);

        HBox buttonBox = new HBox(20, showBtn, resetBtn, randomBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER);
        VBox mainBox = new VBox(14, title, avatarPreview, grid, accessoriesLabel, accessoriesBox, buttonBox, summaryLabel);
        mainBox.setAlignment(Pos.TOP_CENTER);
        // Add Back to Dashboard button at the top left
        HBox backBtnBox = new HBox();
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 8 16; -fx-cursor: hand;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        backBtnBox.setAlignment(Pos.TOP_LEFT);
        backBtnBox.getChildren().add(backBtn);
        backBtnBox.setPadding(new Insets(0, 0, 10, 0));
        root.getChildren().addAll(backBtnBox, mainBox);
        // Eco-themed animated background
        FadeTransition bgFade = new FadeTransition(Duration.seconds(2), root);
        bgFade.setFromValue(0.8);
        bgFade.setToValue(1.0);
        bgFade.setCycleCount(FadeTransition.INDEFINITE);
        bgFade.setAutoReverse(true);
        bgFade.play();
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu Avatar Customization");
        primaryStage.show();
    }

    private void updateAvatarPreview() {
        avatarPreview.getChildren().clear();
        // Skin tone color mapping
        String skinTone = skinToneBox != null && skinToneBox.getValue() != null ? skinToneBox.getValue() : "Fair";
        Color skinColor = switch (skinTone) {
            case "Light" -> Color.web("#ffe0b2");
            case "Fair" -> Color.web("#ffcc80");
            case "Medium" -> Color.web("#e0ac69");
            case "Olive" -> Color.web("#c68642");
            case "Brown" -> Color.web("#8d5524");
            case "Dark" -> Color.web("#5d4037");
            default -> Color.web("#ffcc80");
        };
        // Head
        Ellipse head = new Ellipse(AVATAR_SIZE/2.0, AVATAR_SIZE/2.0, 60, 75);
        head.setFill(skinColor);
        head.setStroke(Color.web("#a1887f"));
        head.setStrokeWidth(2);
        avatarPreview.getChildren().add(head);
        // Eyes
        String eyeColor = eyeColorBox != null && eyeColorBox.getValue() != null ? eyeColorBox.getValue() : "Brown";
        Color eyeFill = switch (eyeColor) {
            case "Blue" -> Color.web("#42a5f5");
            case "Green" -> Color.web("#66bb6a");
            case "Hazel" -> Color.web("#bcaaa4");
            case "Gray" -> Color.web("#b0bec5");
            case "Amber" -> Color.web("#ffb300");
            default -> Color.web("#6d4c41");
        };
        Circle leftEye = new Circle(AVATAR_SIZE/2.0 - 22, AVATAR_SIZE/2.0 - 10, 8, eyeFill);
        Circle rightEye = new Circle(AVATAR_SIZE/2.0 + 22, AVATAR_SIZE/2.0 - 10, 8, eyeFill);
        avatarPreview.getChildren().addAll(leftEye, rightEye);
        // Smile
        Rectangle smile = new Rectangle(AVATAR_SIZE/2.0 - 18, AVATAR_SIZE/2.0 + 30, 36, 8);
        smile.setArcWidth(12);
        smile.setArcHeight(12);
        smile.setFill(Color.web("#ff7043"));
        avatarPreview.getChildren().add(smile);
        // Hair
        String hairStyle = hairStyleBox != null && hairStyleBox.getValue() != null ? hairStyleBox.getValue() : "Short";
        String hairColor = hairColorBox != null && hairColorBox.getValue() != null ? hairColorBox.getValue() : "Brown";
        Color hairFill = switch (hairColor) {
            case "Black" -> Color.web("#212121");
            case "Blonde" -> Color.web("#fff176");
            case "Red" -> Color.web("#e57373");
            case "Blue" -> Color.web("#64b5f6");
            case "Green" -> Color.web("#81c784");
            case "Pink" -> Color.web("#f06292");
            default -> Color.web("#8d5524");
        };
        Shape hairShape = switch (hairStyle) {
            case "Long" -> new Ellipse(AVATAR_SIZE/2.0, AVATAR_SIZE/2.0 - 40, 55, 40);
            case "Curly" -> new Ellipse(AVATAR_SIZE/2.0, AVATAR_SIZE/2.0 - 55, 50, 30);
            case "Ponytail" -> new Ellipse(AVATAR_SIZE/2.0 + 30, AVATAR_SIZE/2.0 - 60, 18, 38);
            case "Bun" -> new Circle(AVATAR_SIZE/2.0, AVATAR_SIZE/2.0 - 70, 20);
            case "Mohawk" -> new Rectangle(AVATAR_SIZE/2.0 - 10, AVATAR_SIZE/2.0 - 80, 20, 40);
            case "Straight" -> new Ellipse(AVATAR_SIZE/2.0, AVATAR_SIZE/2.0 - 50, 45, 30);
            default -> new Ellipse(AVATAR_SIZE/2.0, AVATAR_SIZE/2.0 - 60, 40, 25);
        };
        hairShape.setFill(hairFill);
        avatarPreview.getChildren().add(hairShape);
        // Accessories
        if (accessoryChecks != null) {
            double y = AVATAR_SIZE/2.0 - 80;
            for (CheckBox cb : accessoryChecks) {
                if (cb.isSelected()) {
                    switch (cb.getText()) {
                        case "Glasses" -> {
                            Circle leftLens = new Circle(AVATAR_SIZE/2.0 - 22, AVATAR_SIZE/2.0 - 10, 13, Color.TRANSPARENT);
                            leftLens.setStroke(Color.web("#1976d2"));
                            leftLens.setStrokeWidth(2);
                            Circle rightLens = new Circle(AVATAR_SIZE/2.0 + 22, AVATAR_SIZE/2.0 - 10, 13, Color.TRANSPARENT);
                            rightLens.setStroke(Color.web("#1976d2"));
                            rightLens.setStrokeWidth(2);
                            avatarPreview.getChildren().addAll(leftLens, rightLens);
                        }
                        case "Hat" -> {
                            Rectangle hat = new Rectangle(AVATAR_SIZE/2.0 - 40, y - 20, 80, 25);
                            hat.setArcWidth(18);
                            hat.setArcHeight(18);
                            hat.setFill(Color.web("#ffb300"));
                            avatarPreview.getChildren().add(hat);
                        }
                        case "Earrings" -> {
                            Circle leftEarring = new Circle(AVATAR_SIZE/2.0 - 38, AVATAR_SIZE/2.0 + 30, 6, Color.web("#ffd600"));
                            Circle rightEarring = new Circle(AVATAR_SIZE/2.0 + 38, AVATAR_SIZE/2.0 + 30, 6, Color.web("#ffd600"));
                            avatarPreview.getChildren().addAll(leftEarring, rightEarring);
                        }
                        case "Necklace" -> {
                            Ellipse necklace = new Ellipse(AVATAR_SIZE/2.0, AVATAR_SIZE/2.0 + 70, 30, 8);
                            necklace.setFill(Color.web("#ff7043"));
                            avatarPreview.getChildren().add(necklace);
                        }
                        case "Scarf" -> {
                            Rectangle scarf = new Rectangle(AVATAR_SIZE/2.0 - 18, AVATAR_SIZE/2.0 + 55, 36, 12);
                            scarf.setArcWidth(8);
                            scarf.setArcHeight(8);
                            scarf.setFill(Color.web("#4fc3f7"));
                            avatarPreview.getChildren().add(scarf);
                        }
                        case "Headphones" -> {
                            Circle leftHeadphone = new Circle(AVATAR_SIZE/2.0 - 50, AVATAR_SIZE/2.0 - 10, 10, Color.web("#8e24aa"));
                            Circle rightHeadphone = new Circle(AVATAR_SIZE/2.0 + 50, AVATAR_SIZE/2.0 - 10, 10, Color.web("#8e24aa"));
                            avatarPreview.getChildren().addAll(leftHeadphone, rightHeadphone);
                        }
                        case "Bowtie" -> {
                            Rectangle bowtie = new Rectangle(AVATAR_SIZE/2.0 - 18, AVATAR_SIZE/2.0 + 60, 36, 10);
                            bowtie.setArcWidth(10);
                            bowtie.setArcHeight(10);
                            bowtie.setFill(Color.web("#e57373"));
                            avatarPreview.getChildren().add(bowtie);
                        }
                    }
                }
            }
        }
        // Animate avatar preview on update
        ScaleTransition st = new ScaleTransition(Duration.millis(300), avatarPreview);
        st.setFromX(1.1);
        st.setFromY(1.1);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    private void showSummary() {
        String hairStyle = hairStyleBox.getValue();
        String hairColor = hairColorBox.getValue();
        String skinTone = skinToneBox.getValue();
        String eyeColor = eyeColorBox.getValue();
        List<String> selectedAccessories = new ArrayList<>();
        for (CheckBox cb : accessoryChecks) {
            if (cb.isSelected()) selectedAccessories.add(cb.getText());
        }
        if (hairStyle == null || hairColor == null || skinTone == null || eyeColor == null) {
            summaryLabel.setText("Please select all main features for your avatar.");
            return;
        }
        Avatar avatar = new Avatar(hairStyle, hairColor, skinTone, eyeColor, selectedAccessories);
        StringBuilder sb = new StringBuilder();
        sb.append("🌟 Your Eco Hero Avatar:\n");
        sb.append("Hair Style: ").append(avatar.getHairStyle()).append("\n");
        sb.append("Hair Color: ").append(avatar.getHairColor()).append("\n");
        sb.append("Skin Tone: ").append(avatar.getSkinTone()).append("\n");
        sb.append("Eye Color: ").append(avatar.getEyeColor()).append("\n");
        if (!avatar.getAccessories().isEmpty()) {
            sb.append("Accessories: ").append(String.join(", ", avatar.getAccessories()));
        } else {
            sb.append("Accessories: None");
        }
        summaryLabel.setText(sb.toString());
    }

    private void resetSelections() {
        hairStyleBox.getSelectionModel().clearSelection();
        hairColorBox.getSelectionModel().clearSelection();
        skinToneBox.getSelectionModel().clearSelection();
        eyeColorBox.getSelectionModel().clearSelection();
        for (CheckBox cb : accessoryChecks) {
            cb.setSelected(false);
        }
        summaryLabel.setText("");
        updateAvatarPreview();
    }

    private void randomizeAvatar() {
        // Randomly select options for all fields
        hairStyleBox.getSelectionModel().select((int)(Math.random() * hairStyleBox.getItems().size()));
        hairColorBox.getSelectionModel().select((int)(Math.random() * hairColorBox.getItems().size()));
        skinToneBox.getSelectionModel().select((int)(Math.random() * skinToneBox.getItems().size()));
        eyeColorBox.getSelectionModel().select((int)(Math.random() * eyeColorBox.getItems().size()));
        for (CheckBox cb : accessoryChecks) {
            cb.setSelected(Math.random() < 0.4); // 40% chance for each accessory
        }
        updateAvatarPreview();
        summaryLabel.setText("Randomized! Try another or save your favorite.");
    }

    private void showSaveFeedback() {
        summaryLabel.setText("✅ Avatar saved! (Ready for future integration)");
        FadeTransition ft = new FadeTransition(Duration.millis(800), summaryLabel);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
    }

    private void addHoverEffect(Control control) {
        control.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> control.setStyle(control.getStyle() + "-fx-effect: dropshadow(gaussian, #ffd54f, 8, 0.5, 0, 0);"));
        control.addEventHandler(MouseEvent.MOUSE_EXITED, e -> control.setStyle(control.getStyle().replace("-fx-effect: dropshadow(gaussian, #ffd54f, 8, 0.5, 0, 0);", "")));
    }

    
} 