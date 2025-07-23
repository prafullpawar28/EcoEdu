package com.ecoedu.avatar;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AvatarCustomizer extends VBox {
    private final Map<String, String> hairOptions = Map.of(
            "Short", "/Assets/Images/hair1.png",
            "Long", "/Assets/Images/hair2.png",
            "Curly", "/Assets/Images/hair3.png"
    );
    private final Map<String, String> faceOptions = Map.of(
            "Smile", "/Assets/Images/face1.png",
            "Cool", "/Assets/Images/face2.png",
            "Happy", "/Assets/Images/face3.png"
    );
    private final Map<String, String> accessoryOptions = Map.of(
            "Glasses", "/Assets/Images/glasses1.png",
            "Hat", "/Assets/Images/glasses2.png",
            "Flower", "/Assets/Images/glasses3.png"
    );
    private final List<String> funNames = List.of("Eco Hero", "Green Kid", "Ocean Star", "Leafy", "Sunny", "Sprout", "Captain Clean", "Wave Rider", "Sunbeam", "Bloom");
    private final List<String> ecoTips = List.of(
            "🌱 Tip: Turn off lights when you leave a room!",
            "🌊 Fact: Oceans cover 71% of the Earth's surface.",
            "🌳 Tip: Plant a tree to help the planet!",
            "♻️ Fact: Recycling one can saves enough energy to run a TV for 3 hours!",
            "🚲 Tip: Bike or walk for a cleaner Earth!"
    );

    private ComboBox<String> hairBox, faceBox, accessoryBox;
    private ColorPicker colorPicker;
    private StackPane avatarPreview;
    private ImageView hairImg, faceImg, accessoryImg;
    private Circle bgCircle, borderCircle;
    private TextField nameField;
    private Label tipLabel;
    private Stage primaryStage;

    public AvatarCustomizer(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(18);
        setPadding(new Insets(24, 40, 24, 40));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");

        // Navigation bar
        HBox navBar = new HBox();
        navBar.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        backBtn.setStyle("-fx-background-radius: 16; -fx-background-color: #43e97b; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 32;");
        backBtn.setOnAction(e -> com.ecoedu.dashboard.StudentDashboard.show(primaryStage));
        navBar.getChildren().add(backBtn);
        getChildren().add(navBar);

        // Eco header
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        ImageView ecoIcon = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/hair1.png")));
        ecoIcon.setFitHeight(38);
        ecoIcon.setFitWidth(38);
        Label title = new Label("Eco Avatar Customization");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 30));
        title.setTextFill(Color.web("#0288d1"));
        header.getChildren().addAll(ecoIcon, title);
        getChildren().add(header);

        // Avatar name field
        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER);
        Label namePrompt = new Label("Avatar Name:");
        namePrompt.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        nameField = new TextField();
        nameField.setPromptText("Enter a fun name...");
        nameField.setFont(Font.font("Quicksand", 16));
        nameField.setPrefWidth(180);
        nameBox.getChildren().addAll(namePrompt, nameField);
        getChildren().add(nameBox);

        // Avatar preview with animated border
        avatarPreview = new StackPane();
        avatarPreview.setMinSize(120, 120);
        avatarPreview.setPrefSize(200, 200);
        avatarPreview.setMaxSize(320, 320);
        borderCircle = new Circle(100, Color.web("#43e97b", 0.18));
        borderCircle.setStroke(Color.web("#43e97b"));
        borderCircle.setStrokeWidth(6);
        borderCircle.setEffect(new javafx.scene.effect.DropShadow(18, Color.web("#43e97b", 0.4)));
        bgCircle = new Circle(90, Color.web("#b2dfdb"));
        faceImg = new ImageView();
        faceImg.setPreserveRatio(true);
        faceImg.setFitWidth(120);
        faceImg.setFitHeight(120);
        hairImg = new ImageView();
        hairImg.setPreserveRatio(true);
        hairImg.setFitWidth(120);
        hairImg.setFitHeight(120);
        accessoryImg = new ImageView();
        accessoryImg.setPreserveRatio(true);
        accessoryImg.setFitWidth(120);
        accessoryImg.setFitHeight(120);
        avatarPreview.getChildren().addAll(borderCircle, bgCircle, faceImg, hairImg, accessoryImg);
        getChildren().add(avatarPreview);
        widthProperty().addListener((obs, oldW, newW) -> {
            double w = newW.doubleValue();
            double size = Math.max(120, Math.min(220, w / 5));
            avatarPreview.setPrefSize(size, size);
            borderCircle.setRadius(size / 2 + 10);
            bgCircle.setRadius(size / 2);
            faceImg.setFitWidth(size * 0.7);
            faceImg.setFitHeight(size * 0.7);
            hairImg.setFitWidth(size * 0.7);
            hairImg.setFitHeight(size * 0.7);
            accessoryImg.setFitWidth(size * 0.7);
            accessoryImg.setFitHeight(size * 0.7);
        });

        // Controls with icon previews
        FlowPane controls = new FlowPane(24, 16);
        controls.setAlignment(Pos.CENTER);
        controls.setHgap(24);
        controls.setVgap(16);
        controls.setPrefWrapLength(700);
        hairBox = new ComboBox<>();
        for (String key : hairOptions.keySet()) {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(hairOptions.get(key))));
            icon.setFitWidth(32); icon.setFitHeight(32);
            hairBox.getItems().add(key);
        }
        hairBox.setTooltip(new Tooltip("Choose a hair style"));
        faceBox = new ComboBox<>();
        for (String key : faceOptions.keySet()) {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(faceOptions.get(key))));
            icon.setFitWidth(32); icon.setFitHeight(32);
            faceBox.getItems().add(key);
        }
        faceBox.setTooltip(new Tooltip("Choose a face expression"));
        accessoryBox = new ComboBox<>();
        for (String key : accessoryOptions.keySet()) {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(accessoryOptions.get(key))));
            icon.setFitWidth(32); icon.setFitHeight(32);
            accessoryBox.getItems().add(key);
        }
        accessoryBox.setTooltip(new Tooltip("Choose an accessory"));
        colorPicker = new ColorPicker(Color.web("#43e97b"));
        colorPicker.setTooltip(new Tooltip("Pick any background color!"));
        controls.getChildren().addAll(new Label("Hair:"), hairBox, new Label("Face:"), faceBox, new Label("Accessory:"), accessoryBox, new Label("BG Color:"), colorPicker);
        getChildren().add(controls);
        controls.prefWrapLengthProperty().bind(widthProperty().subtract(80));

        // Load saved avatar
        Avatar saved = AvatarManager.getInstance().getCurrentAvatar();
        hairBox.setValue(saved.getHair());
        faceBox.setValue(saved.getFace());
        accessoryBox.setValue(saved.getAccessory());
        colorPicker.setValue(Color.web(saved.getColor()));
        nameField.setText(saved.getHair() + " " + saved.getFace());
        hairBox.setOnAction(e -> animatePreview());
        faceBox.setOnAction(e -> animatePreview());
        accessoryBox.setOnAction(e -> animatePreview());
        colorPicker.setOnAction(e -> animatePreview());

        // Buttons: Save, Randomize, Reset
        HBox buttonBar = new HBox(18);
        buttonBar.setAlignment(Pos.CENTER);
        Button saveBtn = new Button("Save Avatar");
        saveBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 18));
        saveBtn.setStyle("-fx-background-radius: 16; -fx-background-color: linear-gradient(to right, #b2ff59, #81d4fa); -fx-text-fill: #0288d1; -fx-cursor: hand;");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(e -> saveAvatar());
        Button randomBtn = new Button("Randomize");
        randomBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        randomBtn.setStyle("-fx-background-radius: 16; -fx-background-color: #ffd54f; -fx-text-fill: #0288d1; -fx-cursor: hand;");
        randomBtn.setOnAction(e -> randomizeAvatar());
        Button resetBtn = new Button("Reset");
        resetBtn.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
        resetBtn.setStyle("-fx-background-radius: 16; -fx-background-color: #e1f5fe; -fx-text-fill: #388e3c; -fx-cursor: hand;");
        resetBtn.setOnAction(e -> resetAvatar());
        buttonBar.getChildren().addAll(saveBtn, randomBtn, resetBtn);
        getChildren().add(buttonBar);

        // Fun eco tip
        tipLabel = new Label(ecoTips.get(new Random().nextInt(ecoTips.size())));
        tipLabel.setFont(Font.font("Quicksand", FontWeight.BOLD, 15));
        tipLabel.setTextFill(Color.web("#43e97b"));
        tipLabel.setStyle("-fx-background-radius: 12; -fx-background-color: #e0f7fa; -fx-padding: 8 24; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.12, 0, 2);");
        tipLabel.setAlignment(Pos.CENTER);
        getChildren().add(tipLabel);

        animatePreview();
    }

    private void animatePreview() {
        updatePreview();
        FadeTransition ft = new FadeTransition(Duration.millis(350), avatarPreview);
        ft.setFromValue(0.7);
        ft.setToValue(1.0);
        ft.play();
        borderCircle.setStroke(colorPicker.getValue().darker());
    }

    private void saveAvatar() {
        String name = nameField.getText().trim();
        if (name.isEmpty() || name.length() < 3) {
            showErrorDialog("Please enter a fun name (at least 3 characters) for your avatar!");
            return;
        }
        Avatar avatar = new Avatar(hairBox.getValue(), faceBox.getValue(), accessoryBox.getValue(), toHex(colorPicker.getValue()));
        AvatarManager.getInstance().saveAvatar(avatar);
        showSavedDialog();
    }

    private void randomizeAvatar() {
        Random rand = new Random();
        hairBox.setValue(hairOptions.keySet().stream().skip(rand.nextInt(hairOptions.size())).findFirst().orElse("Short"));
        faceBox.setValue(faceOptions.keySet().stream().skip(rand.nextInt(faceOptions.size())).findFirst().orElse("Smile"));
        accessoryBox.setValue(accessoryOptions.keySet().stream().skip(rand.nextInt(accessoryOptions.size())).findFirst().orElse("Glasses"));
        colorPicker.setValue(Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()));
        nameField.setText(funNames.get(rand.nextInt(funNames.size())));
        tipLabel.setText(ecoTips.get(rand.nextInt(ecoTips.size())));
        animatePreview();
    }

    private void resetAvatar() {
        hairBox.setValue("Short");
        faceBox.setValue("Smile");
        accessoryBox.setValue("Glasses");
        colorPicker.setValue(Color.web("#43e97b"));
        nameField.setText("");
        tipLabel.setText(ecoTips.get(new Random().nextInt(ecoTips.size())));
        animatePreview();
    }

    private void updatePreview() {
        String hair = hairBox.getValue();
        String face = faceBox.getValue();
        String accessory = accessoryBox.getValue();
        Color color = colorPicker.getValue();
        bgCircle.setFill(color);
        faceImg.setImage(new Image(getClass().getResourceAsStream(faceOptions.get(face))));
        hairImg.setImage(new Image(getClass().getResourceAsStream(hairOptions.get(hair))));
        accessoryImg.setImage(new Image(getClass().getResourceAsStream(accessoryOptions.get(accessory))));
    }

    private void showSavedDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Avatar Saved");
        alert.setHeaderText(null);
        alert.setContentText("Your eco avatar has been saved! 🌱");
        alert.showAndWait();
    }

    private void showErrorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Name");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void show(Stage primaryStage) {
        AvatarCustomizer customizer = new AvatarCustomizer(primaryStage);
        Scene scene = new Scene(customizer, 1366, 768);
        // Keyboard shortcut for back
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                case B:
                    com.ecoedu.dashboard.StudentDashboard.show(primaryStage);
                    break;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Avatar Customization");
        // Fade-in animation
        FadeTransition ft = new FadeTransition(Duration.millis(500), customizer);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        primaryStage.show();
    }
} 