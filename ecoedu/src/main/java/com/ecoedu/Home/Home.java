package com.ecoedu.Home;

import com.ecoedu.dashboard.StudentDashboard;
import com.ecoedu.dashboard.StudentLoginPage;
import com.ecoedu.dashboard.StudentSignupPage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.ecoedu.adminpanel.AdminPanelMain;
import javafx.animation.FadeTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

public class Home extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Use a StackPane to layer the background image and content
        StackPane root = new StackPane();
        // Background image
        ImageView bgImage = new ImageView();
        try {
            bgImage.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/homepagef.jpg")));
        } catch (Exception e) {
            bgImage.setImage(null);
        }
        bgImage.setFitWidth(1366);
        bgImage.setFitHeight(768);
        bgImage.setPreserveRatio(false);
        bgImage.setOpacity(0.92);

        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: rgba(255,255,255,0.15);");
        content.setPadding(new Insets(60, 0, 0, 0));

        // Mascot or logo placeholder
        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setStyle("-fx-background-color: #fff; -fx-background-radius: 60; -fx-border-radius: 60; -fx-effect: dropshadow(gaussian, #43e97b, 18, 0.3, 0, 4);");

        Label title = new Label("EcoEdu");
        title.setStyle("-fx-font-size: 80px; -fx-font-weight: 900; -fx-text-fill: linear-gradient(to right, #00c6ff, #0072ff, #43e97b, #38f9d7); -fx-font-family: 'Quicksand', 'Nunito', sans-serif; -fx-effect: dropshadow(gaussian, #222, 16, 0.3, 0, 4);");
        Label subtitle = new Label("Learn. Play. Grow. Save the Planet!");
        subtitle.setStyle("-fx-font-size: 35px; -fx-font-style: italic; -fx-text-fill: #00bcd4; -fx-font-family: 'Quicksand', 'Nunito', sans-serif; -fx-padding: 0 0 2 0; -fx-effect: dropshadow(gaussian, #fff, 8, 0.2, 0, 2);");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #00c6ff, #43e97b); -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 32; -fx-padding: 16 54; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #00c6ff, 10, 0.2, 0, 2); -fx-border-color: rgba(255,255,255,0.4); -fx-border-width: 2; -fx-border-radius: 32; -fx-background-insets: 0;");
        loginBtn.setOnAction(e -> StudentLoginPage.show(primaryStage));
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #38f9d7, #00c6ff); -fx-text-fill: #fff; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 32; -fx-padding: 16 54; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #38f9d7, 14, 0.3, 0, 4); -fx-border-color: #fff; -fx-border-width: 2; -fx-border-radius: 32; -fx-background-insets: 0; -fx-scale-x:1.07;-fx-scale-y:1.07;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: linear-gradient(to right, #00c6ff, #43e97b); -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 32; -fx-padding: 16 54; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #00c6ff, 10, 0.2, 0, 2); -fx-border-color: rgba(255,255,255,0.4); -fx-border-width: 2; -fx-border-radius: 32; -fx-background-insets: 0;"));

        Button signupBtn = new Button("Sign Up");
        signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 32; -fx-padding: 16 54; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #43e97b, 10, 0.2, 0, 2); -fx-border-color: rgba(255,255,255,0.4); -fx-border-width: 2; -fx-border-radius: 32; -fx-background-insets: 0;");
        signupBtn.setOnAction(e -> StudentSignupPage.show(primaryStage));
        signupBtn.setOnMouseEntered(e -> signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #00c6ff, #43e97b); -fx-text-fill: #fff; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 32; -fx-padding: 16 54; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #00c6ff, 14, 0.3, 0, 4); -fx-border-color: #fff; -fx-border-width: 2; -fx-border-radius: 32; -fx-background-insets: 0; -fx-scale-x:1.07;-fx-scale-y:1.07;"));
        signupBtn.setOnMouseExited(e -> signupBtn.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 32; -fx-padding: 16 54; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #43e97b, 10, 0.2, 0, 2); -fx-border-color: rgba(255,255,255,0.4); -fx-border-width: 2; -fx-border-radius: 32; -fx-background-insets: 0;"));

        HBox btnBox = new HBox(32, loginBtn, signupBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(32, 0, 0, 0));

        // Top section (title, subtitle, logo)
        VBox topSection = new VBox(4, title, subtitle, imageView);
        topSection.setAlignment(Pos.TOP_CENTER);
        topSection.setPadding(new Insets(80, 0, 0, 0));

        // Spacer to push buttons to vertical center
        Region spacerTop = new Region();
        Region spacerBottom = new Region();
        VBox.setVgrow(spacerTop, Priority.ALWAYS);
        VBox.setVgrow(spacerBottom, Priority.ALWAYS);

        content.getChildren().addAll(topSection, spacerTop, btnBox, spacerBottom);
        root.getChildren().addAll(bgImage, content);

        Scene scene = new Scene(root, 1366, 768);
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
    // Show the new admin panel
    public static void showAdminPanel() {
        try {
            AdminPanelMain.main(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
