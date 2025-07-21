package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContentManagerView extends VBox {
    public ContentManagerView() {
        setSpacing(18);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");
        Label title = new Label("Content Manager");
        title.getStyleClass().add("eco-title");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createTab("Modules"));
        tabPane.getTabs().add(createTab("Games"));
        getChildren().addAll(title, tabPane);
    }
    private Tab createTab(String type) {
        Tab tab = new Tab(type);
        tab.setClosable(false);
        HBox mainBox = new HBox(24);
        mainBox.setAlignment(Pos.CENTER);
        // ListView
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(); // loadData() stub
        listView.setItems(items);
        listView.setPrefWidth(200);
        // Buttons
        VBox btnBox = new VBox(10);
        btnBox.setAlignment(Pos.TOP_CENTER);
        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");
        for (Button b : new Button[]{addBtn, editBtn, deleteBtn}) {
            b.getStyleClass().add("eco-btn");
        }
        btnBox.getChildren().addAll(addBtn, editBtn, deleteBtn);
        // Preview
        VBox previewBox = new VBox(10);
        previewBox.setAlignment(Pos.TOP_CENTER);
        previewBox.setPadding(new Insets(0, 0, 0, 24));
        Label previewTitle = new Label("Select an item to preview");
        ImageView previewImage = new ImageView();
        previewImage.setFitWidth(120);
        previewImage.setFitHeight(80);
        Label tagsLabel = new Label("Tags: ");
        previewBox.getChildren().addAll(previewTitle, previewImage, tagsLabel);
        // FileChooser for image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        addBtn.setOnAction(e -> {/* TODO: Add content dialog, use fileChooser for image, save to Firebase */});
        editBtn.setOnAction(e -> {/* TODO: Edit content dialog, use fileChooser for image, save to Firebase */});
        deleteBtn.setOnAction(e -> {/* TODO: Delete content, update Firebase */});
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            // TODO: Load preview data from Firebase
            previewTitle.setText(newVal != null ? newVal : "Select an item to preview");
            // previewImage.setImage(...);
            // tagsLabel.setText(...);
        });
        mainBox.getChildren().addAll(listView, btnBox, previewBox);
        tab.setContent(mainBox);
        return tab;
    }
} 