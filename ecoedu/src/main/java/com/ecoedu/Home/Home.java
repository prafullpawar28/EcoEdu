package com.ecoedu.Home;

import com.ecoedu.dashboard.StudentDashboard;
import com.ecoedu.dashboard.StudentLoginPage;
import com.ecoedu.dashboard.StudentSignupPage;
import com.ecoedu.dashboard.AdminLoginPage;
import com.ecoedu.dashboard.AdminSignupPage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

public class Home extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        // Use a solid color or gradient background instead of an image
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa, #fffde7);");

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

        // --- Student Section ---
        VBox studentBox = new VBox(10);
        studentBox.setAlignment(Pos.CENTER);
        Label studentLabel = new Label("👦 Student");
        studentLabel.setFont(Font.font("Comic Sans MS", 20));
        HBox studentBtnBox = new HBox(16);
        studentBtnBox.setAlignment(Pos.CENTER);
        Button studentLoginBtn = new Button("Login");
        studentLoginBtn.setStyle("-fx-background-color: #00796b; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 8 24;");
        studentLoginBtn.setOnAction(e -> StudentLoginPage.show(primaryStage));
        Button studentSignupBtn = new Button("Sign Up");
        studentSignupBtn.setStyle("-fx-background-color: #fbc02d; -fx-text-fill: #fffde7; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 8 24;");
        studentSignupBtn.setOnAction(e -> StudentSignupPage.show(primaryStage));
        studentBtnBox.getChildren().addAll(studentLoginBtn, studentSignupBtn);
        studentBox.getChildren().addAll(studentLabel, studentBtnBox);

        // --- Admin Section ---
        VBox adminBox = new VBox(10);
        adminBox.setAlignment(Pos.CENTER);
        Label adminLabel = new Label("🧑‍💼 Admin");
        adminLabel.setFont(Font.font("Comic Sans MS", 20));
        HBox adminBtnBox = new HBox(16);
        adminBtnBox.setAlignment(Pos.CENTER);
        Button adminLoginBtn = new Button("Login");
        adminLoginBtn.setStyle("-fx-background-color: #6a1b9a; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 8 24;");
        adminLoginBtn.setOnAction(e -> AdminLoginPage.show(primaryStage));
        Button adminSignupBtn = new Button("Sign Up");
        adminSignupBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #6a1b9a; -fx-font-size: 16px; -fx-background-radius: 20; -fx-padding: 8 24;");
        adminSignupBtn.setOnAction(e -> AdminSignupPage.show(primaryStage));
        adminBtnBox.getChildren().addAll(adminLoginBtn, adminSignupBtn);
        adminBox.getChildren().addAll(adminLabel, adminBtnBox);

        VBox userBox = new VBox(24, studentBox, adminBox);
        userBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(imageView, title, subtitle, userBox);

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Home");
        primaryStage.show();
    }

    // After login/signup, show the student dashboard
    public static void showStudentDashboard(Stage primaryStage) {
        StudentDashboard.show(primaryStage);
    }
    
}
