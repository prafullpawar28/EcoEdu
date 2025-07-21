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
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import com.ecoedu.dashboard.AdminDashboard;

public class Home extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox(40);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #A8E6CF, #DCEDC1);");

        // Mascot or logo placeholder
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setStyle("-fx-background-color: #b2dfdb; -fx-background-radius: 60; -fx-border-radius: 60; -fx-effect: dropshadow(gaussian, #b2dfdb, 10, 0, 0, 2);");

        Label title = new Label("EcoEdu");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #00796b; -fx-font-family: 'Quicksand', 'Nunito', sans-serif;");
        Label subtitle = new Label("Learn. Play. Grow. Save the Planet!");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #388e3c; -fx-font-family: 'Quicksand', 'Nunito', sans-serif;");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 20; -fx-padding: 10 36; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> StudentLoginPage.show(primaryStage));
        Button signupBtn = new Button("Sign Up");
        signupBtn.setStyle("-fx-background-color: #FFD3B6; -fx-text-fill: #388E3C; -fx-font-size: 18px; -fx-background-radius: 20; -fx-padding: 10 36; -fx-cursor: hand;");
        signupBtn.setOnAction(e -> StudentSignupPage.show(primaryStage));

        VBox btnBox = new VBox(20, loginBtn, signupBtn);
        btnBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(imageView, title, subtitle, btnBox);

        Scene scene = new Scene(root, 900, 700);
        com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Home");
        primaryStage.show();
    }

    // After login/signup, show the student dashboard
    public static void showStudentDashboard(Stage primaryStage) {
        System.out.println("Navigating to StudentDashboard on stage: " + primaryStage);
        StudentDashboard.show(primaryStage);
    }
    // Add a method to return to Home
    public static void showHome(Stage primaryStage) {
        try {
            System.out.println("Navigating to Home on stage: " + primaryStage);
            new Home().start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showAdminDashboard(Stage primaryStage) {
        System.out.println("Navigating to AdminDashboard on stage: " + primaryStage);
        AdminDashboard.show(primaryStage);
    }
    
}
