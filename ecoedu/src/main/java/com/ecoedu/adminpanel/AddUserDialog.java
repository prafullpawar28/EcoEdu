// package com.ecoedu.adminpanel;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Map;

// import com.ecoedu.auth.FirebaseAuthService;
// import com.google.firebase.auth.FirebaseAuthException;

// import javafx.geometry.Insets;
// import javafx.scene.control.*;
// import javafx.scene.layout.VBox;
// import javafx.scene.Node;

// public class AddUserDialog extends Dialog<AdminDataService.User> {
//     public AddUserDialog() {
//         setTitle("Add User");
//         getDialogPane().getStyleClass().add("dialog-pane");
        
//         VBox box = new VBox(16);
//         box.setPadding(new Insets(18));
//         box.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
//         TextField nameField = new TextField();
//         nameField.getStyleClass().add("text-field");
//         nameField.setPromptText("Full Name");
        
//         TextField emailField = new TextField();
//         emailField.getStyleClass().add("text-field");
//         emailField.setPromptText("Email Address");
        
//         TextField ageField = new TextField();
//         ageField.getStyleClass().add("text-field");
//         ageField.setPromptText("Age");
        
//         TextField contactField = new TextField();
//         contactField.getStyleClass().add("text-field");
//         contactField.setPromptText("Contact");
        
        
//         ComboBox<String> roleBox = new ComboBox<>();
//         roleBox.getStyleClass().add("combo-box");
//         roleBox.getItems().addAll("Admin", "Teacher", "Student");
//         roleBox.setValue("Student");
//         roleBox.setPromptText("Select Role");
        
//         ComboBox<String> genderBox = new ComboBox<>();
//         genderBox.getStyleClass().add("combo-box");
//         genderBox.getItems().addAll("Male", "Female", "Other");
//         genderBox.setValue("Male");
//         genderBox.setPromptText("Select Gender");
        
//         ComboBox<String> statusBox = new ComboBox<>();
//         statusBox.getStyleClass().add("combo-box");
//         statusBox.getItems().addAll("Active", "Inactive");
//         statusBox.setValue("Active");
//         statusBox.setPromptText("Select Status");
        
//         PasswordField passwordField = new PasswordField();
//         passwordField.getStyleClass().add("password-field");
//         passwordField.setPromptText("Password");
        
//         Label name=new Label("Name:");
//            Label email= new Label("Email:");
//           Label role = new Label("Role:");
//             Label status=new Label("Status:");
//             Label password=new Label("Password:");
//             Label age=new Label("Age:");
//             Label contact=new Label("Contact:");
//             Label gender=new Label("Gender:");
//         box.getChildren().addAll(
//            name, nameField,
//             email, emailField,
//             role, roleBox,
//             status, statusBox,
//             password, passwordField,
//             age,ageField,
//             gender,genderBox,
//             contact,contactField
//         );
        
//         getDialogPane().setContent(box);
//         ButtonType okType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
//         getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
//         // Disable save button if required fields are empty
//         Node saveButton = getDialogPane().lookupButton(okType);
//        // saveButton.setDisable(nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty());
        
//        (Button)saveButton.setOnAction(e->{
//         System.out.println("Save Button Clicked");
//         ArrayList<Integer> al = new ArrayList();
//         for (int i = 0; i < 6; i++)
//             al.add(0);
//          String roleLower = roleBox.getValue().equals("Child") ? "Student" : "Admin";
//         Map<String, Object> userData = new HashMap<>();
//         userData.put("name", nameField.getText());
//         userData.put("email", emailField.getText());
//         userData.put("password", passwordField.getText());
//         userData.put("age", ageField.getText());
//         userData.put("contact", contactField.getText());
//         userData.put("gender", genderBox.getValue());
//         userData.put("role", roleLower);
//         userData.put("game1", 0);
//         userData.put("game2", 0);
//         userData.put("game3", 0);
//         userData.put("quiz1", al);
//         com.ecoedu.dashboard.StudentSignupPage.addUser(roleLower,email.getText(),userData);
//         });
//         nameField.textProperty().addListener((obs, oldVal, newVal) -> {
//             saveButton.setDisable(newVal.trim().isEmpty() || emailField.getText().trim().isEmpty());
//         });
        
//         emailField.textProperty().addListener((obs, oldVal, newVal) -> {
//             saveButton.setDisable(newVal.trim().isEmpty() || nameField.getText().trim().isEmpty());
//         });
        
//         setResultConverter(btn -> {
//             if (btn == okType) {
//                 if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
//                     Alert alert = new Alert(Alert.AlertType.ERROR);
//                     alert.setTitle("Validation Error");
//                     alert.setHeaderText(null);
//                     alert.setContentText("Name and email are required fields.");
//                     alert.showAndWait();
//                     return null;
//                 }
                
//                 return new AdminDataService.User(
//                     nameField.getText().trim(),
//                     emailField.getText().trim(),
//                     roleBox.getValue(),
//                     passwordField.getText(),
//                     statusBox.getValue(),
//                     java.time.LocalDateTime.now()
//                 );
//             }
//             return null;
//         });
//     }
// } 
package com.ecoedu.adminpanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ecoedu.auth.FirebaseAuthService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import java.time.LocalDateTime;

public class AddUserDialog extends Dialog<AdminDataService.User> {
    public AddUserDialog() {
        setTitle("Add User");
        getDialogPane().getStyleClass().add("dialog-pane");

        VBox box = new VBox(16);
        box.setPadding(new Insets(18));
        box.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Input Fields
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact Number");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Admin", "Teacher", "Student");
        roleBox.setValue("Student");

        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female", "Other");
        genderBox.setValue("Male");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Active", "Inactive");
        statusBox.setValue("Active");

        // Labels and Fields
        box.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Email:"), emailField,
            new Label("Role:"), roleBox,
            new Label("Status:"), statusBox,
            new Label("Password:"), passwordField,
            new Label("Age:"), ageField,
            new Label("Gender:"), genderBox,
            new Label("Contact:"), contactField
        );

        getDialogPane().setContent(box);

        // Button Setup
        ButtonType okType = new ButtonType("Save", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);

        Node saveButton = getDialogPane().lookupButton(okType);
        ((Button) saveButton).setOnAction(e -> {
            // Input Validation
            if (nameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                passwordField.getText().trim().isEmpty()) {

                showAlert("Validation Error", "Name, Email, and Password are required.");
                return;
            }

            ArrayList<Integer> quizList = new ArrayList<>();
            for (int i = 0; i < 6; i++) quizList.add(0);

            String role = roleBox.getValue();
            String email = emailField.getText().trim();

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", nameField.getText().trim());
            userData.put("email", email);
            userData.put("password", passwordField.getText().trim());
            userData.put("age", ageField.getText().trim());
            userData.put("contact", contactField.getText().trim());
            userData.put("gender", genderBox.getValue());
            userData.put("role", role);
            userData.put("status", statusBox.getValue());
            userData.put("game1", 0);
            userData.put("game2", 0);
            userData.put("game3", 0);
            userData.put("quiz1", quizList);

            // Firebase call
            com.ecoedu.dashboard.StudentSignupPage.addUser(role, email, userData);

            // Return result and close
            setResult(new AdminDataService.User(
                nameField.getText().trim(),
                email,
                role,
                passwordField.getText().trim(),
                statusBox.getValue(),
                LocalDateTime.now()
            ));
            close();
        });

        // Enable/disable save button based on required fields
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty() || emailField.getText().trim().isEmpty());
        });

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            saveButton.setDisable(newVal.trim().isEmpty() || nameField.getText().trim().isEmpty());
        });

        // Initial validation
        saveButton.setDisable(nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty());

        setResultConverter(btn -> null); // Result handled manually in setOnAction
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
