package com.ecoedu.dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.units.qual.g;

import com.ecoedu.auth.FirebaseAuthService;
import com.ecoedu.auth.FirebaseInitializer;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

// Add a static ProfileData class for demo purposes
class ProfileData {
    public static String name = "";
    public static String email = "";
    public static String avatarPath = null;
    // Simulate a user database
    public static List<User> users = new ArrayList<>();

    public static class User {
        public String name, email, password, role, gender, contact, age;

        public User(String name, String email, String password, String role, String age, String Contact,
                String gender) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.role = role;
            this.age = age;
            this.gender = gender;
            this.contact = contact;
        }
    }
}

public class StudentSignupPage extends VBox {
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;
    private TextField nameField;
    private TextField ageField;
    private TextField contactField;
    private ComboBox<String> genderDropdown;
    
    private ComboBox<String> roleDropdown;

    public StudentSignupPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setSpacing(0);
        setAlignment(Pos.CENTER);
        getStyleClass().add("root");

        // Background image
        StackPane root = new StackPane();
        ImageView bgImage = new ImageView();
        try {
            bgImage.setImage(new Image(getClass().getResourceAsStream("/Assets/Images/login_signup.jpg")));
        } catch (Exception e) {
            bgImage.setImage(null);
        }
        bgImage.setFitWidth(1366);
        bgImage.setFitHeight(768);
        bgImage.setPreserveRatio(false);
        bgImage.setOpacity(0.92);

        VBox card = new VBox(22);
        card.getStyleClass().add("eco-card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setMaxHeight(650);
        card.setMinHeight(520);
        card.setPadding(new Insets(22, 32, 22, 32));
        card.setStyle("-fx-background-color: transparent; " +
                "-fx-background-radius: 28; " +
                "-fx-border-color: rgba(255,255,255,0.7); " +
                "-fx-border-width: 2.5; " +
                "-fx-border-radius: 28; " +
                "-fx-effect: dropshadow(gaussian, #00000055, 32, 0.18, 0, 8);");

        Label title = new Label("Sign Up");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#6a1b9a"));
        title.setStyle(
                "-fx-effect: dropshadow(gaussian, #fff, 8, 0.2, 0, 2); -fx-padding: 22 0 22 0; -fx-alignment: center;");
        card.setAlignment(Pos.CENTER);

        HBox nameBox = new HBox(6);
        Label nameIcon = new Label("\uD83C\uDF33"); // Tree emoji as icon
        nameIcon.getStyleClass().add("eco-icon");
        nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.getStyleClass().add("eco-field");
        nameField.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        nameField.setPrefWidth(280);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.getChildren().addAll(nameIcon, nameField);

        HBox emailBox = new HBox(6);
        Label emailIcon = new Label("\uD83D\uDCE7"); // Envelope emoji as icon
        emailIcon.getStyleClass().add("eco-icon");
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("eco-field");
        emailField.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        emailField.setPrefWidth(280);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.getChildren().addAll(emailIcon, emailField);

        HBox passBox = new HBox(6);
        Label passIcon = new Label("\uD83D\uDD12"); // Lock emoji as icon
        passIcon.getStyleClass().add("eco-icon");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("eco-field");
        passwordField.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        passwordField.setPrefWidth(280);
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.getChildren().addAll(passIcon, passwordField);

        HBox confirmBox = new HBox(6);
        Label confirmIcon = new Label("\uD83D\uDD12"); // Lock emoji as icon
        confirmIcon.getStyleClass().add("eco-icon");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("eco-field");
        confirmPasswordField.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        confirmPasswordField.setPrefWidth(280);
        confirmBox.setAlignment(Pos.CENTER_LEFT);
        confirmBox.getChildren().addAll(confirmIcon, confirmPasswordField);

        // Age Field
        HBox ageBox = new HBox(6);
        Label ageIcon = new Label("\uD83D\uDC76"); // Baby emoji
        ageIcon.getStyleClass().add("eco-icon");
        ageField = new TextField();
        ageField.setPromptText("Age");
        ageField.getStyleClass().add("eco-field");
        ageField.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        ageField.setPrefWidth(280);
        ageBox.setAlignment(Pos.CENTER_LEFT);
        ageBox.getChildren().addAll(ageIcon, ageField);

        // Contact Field
        HBox contactBox = new HBox(6);
        Label contactIcon = new Label("\u260E"); // Telephone icon
        contactIcon.getStyleClass().add("eco-icon");
        contactField = new TextField();
        contactField.setPromptText("Contact Number");
        contactField.getStyleClass().add("eco-field");
        contactField.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        contactField.setPrefWidth(280);
        contactBox.setAlignment(Pos.CENTER_LEFT);
        contactBox.getChildren().addAll(contactIcon, contactField);

        // Gender Dropdown
        HBox genderBox = new HBox(6);
        Label genderIcon = new Label("\u2642\u2640"); // Gender symbol
        genderIcon.getStyleClass().add("eco-icon");
        genderDropdown = new ComboBox<>();
        genderDropdown.getItems().addAll("Male", "Female", "Other");
        genderDropdown.setPromptText("Gender");
        genderDropdown.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        genderDropdown.setPrefWidth(280);
        genderBox.setAlignment(Pos.CENTER_LEFT);
        genderBox.getChildren().addAll(genderIcon, genderDropdown);

        

        // ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown = new ComboBox<>();
        roleDropdown.getStyleClass().add("eco-icon");
        roleDropdown.getItems().addAll("Child", "Parent", "Teacher");
        roleDropdown.setPromptText("Select Role");
        roleDropdown.getStyleClass().add("eco-dropdown");
        roleDropdown.setMaxWidth(280);
        roleDropdown.setStyle(
                "-fx-background-radius: 16; -fx-background-color: rgba(255,255,255,0.7); -fx-font-size: 16px; -fx-padding: 8 16; -fx-border-radius: 16; -fx-border-color: #b2dfdb; -fx-border-width: 1.2;");
        roleDropdown.setPrefWidth(280);

        Button signupBtn = new Button("Sign Up");
        signupBtn.getStyleClass().add("eco-btn");
        signupBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 24; -fx-padding: 12 40; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.2, 0, 2); -fx-border-color: rgba(255,255,255,0.4); -fx-border-width: 2; -fx-border-radius: 24; -fx-background-insets: 0;");
        signupBtn.setOnAction(e -> {
            handleSignup();
        });
        signupBtn.setOnMouseEntered(e -> signupBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #00c6ff, #43e97b); -fx-text-fill: #fff; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 24; -fx-padding: 12 40; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #00c6ff, 12, 0.3, 0, 4); -fx-border-color: #fff; -fx-border-width: 2; -fx-border-radius: 24; -fx-background-insets: 0; -fx-scale-x:1.07;-fx-scale-y:1.07;"));
        signupBtn.setOnMouseExited(e -> {
            signupBtn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 24; -fx-padding: 12 40; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.2, 0, 2); -fx-border-color: rgba(255,255,255,0.4); -fx-border-width: 2; -fx-border-radius: 24; -fx-background-insets: 0;");
        });

        HBox links = new HBox(14);
        links.setAlignment(Pos.CENTER);
        Hyperlink loginLink = new Hyperlink("Already have an account? Login");
        loginLink.getStyleClass().add("eco-link");
        loginLink.setStyle(
                "-fx-border-color: #43e97b; -fx-border-width: 2; -fx-border-radius: 12; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 6 18; -fx-background-radius: 12; -fx-text-fill: #43e97b; -fx-background-color: rgba(255,255,255,0.5);");
        loginLink.setOnAction(e -> StudentLoginPage.show(primaryStage));
        loginLink.setOnMouseEntered(e2 -> loginLink.setStyle(
                "-fx-border-color: #00c6ff; -fx-border-width: 2; -fx-border-radius: 12; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 6 18; -fx-background-radius: 12; -fx-text-fill: #00c6ff; -fx-background-color: #e0f7fa;"));
        loginLink.setOnMouseExited(e2 -> loginLink.setStyle(
                "-fx-border-color: #43e97b; -fx-border-width: 2; -fx-border-radius: 12; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 6 18; -fx-background-radius: 12; -fx-text-fill: #43e97b; -fx-background-color: rgba(255,255,255,0.5);"));
        links.getChildren().add(loginLink);

        messageLabel = new Label("");
        messageLabel.getStyleClass().add("eco-error");

        card.getChildren().addAll(title, nameBox, emailBox, passBox, confirmBox, ageBox,  contactBox,genderBox,
                roleDropdown, signupBtn, links, messageLabel);
        root.getChildren().addAll(bgImage, card);
        getChildren().add(root);
    }

    private void handleSignup() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String name = nameField.getText();
        String age = ageField.getText();
        String contact = contactField.getText();
        String gender = genderDropdown.getValue();
        String role = roleDropdown.getValue();
        messageLabel.setText("");
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
            messageLabel.setText("Please fill all fields.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            messageLabel.setText("Please enter a valid email address.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }
        if (age.isEmpty() || contact.isEmpty() || gender == null || gender.isEmpty()) {
            messageLabel.setText("Please fill all fields including age, gender, and contact.");
            return;
        }
        if (role == null || role.isEmpty()) {
            messageLabel.setText("Please select a role.");
            return;
        }
        // Check for duplicate email
        System.out.println("For loop");
        for (ProfileData.User u : ProfileData.users) {

            if (u.email.equalsIgnoreCase(email)) {
                messageLabel.setText("Email already registered. Please use a different email.");
                System.out.println("email exists");
                // return;
            }
        }
        System.out.println("--------------------------------------------");
        ArrayList<Integer> al = new ArrayList();
        for (int i = 0; i < 6; i++)
            al.add(0);
         String roleLower = role.equals("Child") ? "Student" : "Admin";
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("age", age);
        userData.put("contact", contact);
        userData.put("gender", gender);
        userData.put("role", roleLower);
        userData.put("game1", 0);
        userData.put("game2", 0);
        userData.put("game3", 0);
        userData.put("quiz1", al);
        boolean success1 = addUser(roleLower, email, userData);

        if (success1) {
            messageLabel.setText("Signup successful! Redirecting to login...");
            StudentLoginPage.show(primaryStage);
        } else {
            messageLabel.setText("Signup failed! Email may already exist.");
        }

        // Save to ProfileData
        ProfileData.name = name;
        ProfileData.email = email;
        ProfileData.users.add(new ProfileData.User(name, email, password, role, age, contact,gender));
        // After signup, navigate to login
        StudentLoginPage.show(primaryStage);
    }

    public static void show(Stage primaryStage) {
        StudentSignupPage page = new StudentSignupPage(primaryStage);
        Scene scene = new Scene(page, 1366, 768);
        // com.ecoedu.Main.applyEcoEduTheme(scene);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Sign Up");
        primaryStage.show();
    }

    public static boolean addUser(String collectionName, String documentId, Map<String, Object> data) {
        try {
            Firestore db = FirebaseInitializer.getFirestore();
            db.collection(collectionName).document(documentId).set(data);
            System.out.println("Document created with ID: " + documentId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}