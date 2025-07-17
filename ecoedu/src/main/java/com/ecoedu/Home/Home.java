package com.ecoedu.Home;

import com.ecoedu.auth.LoginPage;
import com.ecoedu.auth.SignupPage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Home extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa, #fffde7); -fx-padding: 50;");

        // Mascot or logo placeholder
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setStyle("-fx-background-color: #b2dfdb; -fx-background-radius: 60; -fx-border-radius: 60; -fx-effect: dropshadow(gaussian, #b2dfdb, 10, 0, 0, 2);");
        // You can set mascot.setImage(new Image("/images/mascot.png")); when you add your asset

        Label title = new Label("Welcome to EcoEdu!");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #00796b;");

        Label subtitle = new Label("Learn. Play. Save the Planet!");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #388e3c;");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #00796b; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 20; -fx-padding: 10 30;");
        loginBtn.setOnAction(e -> {
            com.ecoedu.auth.LoginPage.show(primaryStage);
        });
        Button signupBtn = new Button("Sign Up");
        signupBtn.setStyle("-fx-background-color: #fbc02d; -fx-text-fill: #fffde7; -fx-font-size: 18px; -fx-background-radius: 20; -fx-padding: 10 30;");
        signupBtn.setOnAction(e -> {
            com.ecoedu.auth.SignupPage.show(primaryStage);
        });
        buttonBox.getChildren().addAll(loginBtn, signupBtn);

        Label footer = new Label("© 2024 EcoEdu | For young eco heroes (Ages 8–14)");
        footer.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");

        root.getChildren().addAll(imageView, title, subtitle, buttonBox, footer);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Home");
        primaryStage.show();
    }
    
}
