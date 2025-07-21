package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentSettingsView extends VBox {
    public StudentSettingsView(Stage primaryStage) {
        setSpacing(18);
        setPadding(new Insets(32));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");
        Label title = new Label("Settings");
        title.getStyleClass().add("eco-title");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createTab("Profile"));
        tabPane.getTabs().add(createTab("Preferences"));
        tabPane.getTabs().add(createTab("Notifications"));
        tabPane.getTabs().add(createTab("Help"));
        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("eco-btn");
        closeBtn.setOnAction(e -> primaryStage.setScene(new Scene(new StudentDashboard(primaryStage), 900, 700)));
        getChildren().addAll(title, tabPane, closeBtn);
    }
    private Tab createTab(String name) {
        Tab tab = new Tab(name);
        tab.setClosable(false);
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(18));
        content.getChildren().add(new Label(name + " settings coming soon!"));
        tab.setContent(content);
        return tab;
    }
    public static void show(Stage primaryStage) {
        StudentSettingsView view = new StudentSettingsView(primaryStage);
        Scene scene = new Scene(view, 500, 500);
        com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Settings");
        primaryStage.show();
    }
} 