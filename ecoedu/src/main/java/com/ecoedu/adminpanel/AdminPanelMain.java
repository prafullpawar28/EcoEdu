package com.ecoedu.adminpanel;

import javafx.application.Application;
import javafx.stage.Stage;

public class AdminPanelMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        AdminLoginPage.show(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
} 