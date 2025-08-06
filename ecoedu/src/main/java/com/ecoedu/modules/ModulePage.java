// package com.ecoedu.modules;

// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.StackPane;
// import javafx.scene.layout.Region;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.stage.Stage;
// import com.ecoedu.dashboard.StudentDashboard;
// import java.util.List;
// import com.ecoedu.adminpanel.AdminDataService;
// import java.util.ArrayList;

// public class ModulePage {
//     public static void show(Stage primaryStage) {
//         showModuleList(primaryStage);
//     }

//     private static void showModuleList(Stage primaryStage) {
//         VBox root = new VBox(0);
//         root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");
//         root.setPadding(new Insets(0));
//         root.setAlignment(Pos.TOP_CENTER);

//         // Expressive Header
//         HBox header = new HBox();
//         header.setMinHeight(80);
//         header.setAlignment(Pos.CENTER_LEFT);
//         header.setSpacing(18);
//         header.setStyle("-fx-background-radius: 0 0 32 32; " +
//             "-fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); " +
//             "-fx-effect: dropshadow(gaussian, #43e97b, 12, 0.2, 0, 4);");
//         Label icon = new Label("📚");
//         icon.setFont(Font.font("Quicksand", FontWeight.BOLD, 48));
//         Label title = new Label("EcoEdu Modules");
//         title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
//         title.setTextFill(Color.web("#fffde7"));
//         Region headerSpacer = new Region();
//         HBox.setHgrow(headerSpacer, javafx.scene.layout.Priority.ALWAYS);
//         Button backBtn = new Button("Back to Dashboard");
//         backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
//         backBtn.setOnAction(e -> StudentDashboard.show(primaryStage));
//         header.getChildren().addAll(icon, title, headerSpacer, backBtn);
//         root.getChildren().add(header);

//         VBox moduleList = new VBox(32);
//         moduleList.setAlignment(Pos.TOP_CENTER);
//         moduleList.setPadding(new Insets(40, 0, 40, 0));
//         // Combine static and admin modules
//         ArrayList<Module> allModules = new ArrayList<>();
//         allModules.addAll(ModuleData.getModules());
//         // Add admin modules (convert to Module)
//         for (AdminDataService.Module adminMod : AdminDataService.getInstance().getModules()) {
//             // Use a default icon and no lessons for admin modules
//             Module mod = new Module(
//                 -1, // id for admin modules
//                 adminMod.title,
//                 adminMod.description,
//                 "🌱", // default icon for admin modules
//                 new ArrayList<>() // no lessons
//             );
//             allModules.add(mod);
//         }
//         int index = 0;
//         for (Module module : allModules) {
//             HBox card = new HBox(24);
//             card.setAlignment(Pos.CENTER_LEFT);
//             card.setPadding(new Insets(24));
//             card.setStyle("-fx-background-color: white; -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #b3e5fc; -fx-border-width: 2.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 12, 0.15, 0, 4);");
//             Label modIcon = new Label(module.getIcon());
//             modIcon.setFont(Font.font("Quicksand", FontWeight.BOLD, 38));
//             VBox info = new VBox(8);
//             Label moduleTitle = new Label(module.getTitle());
//             moduleTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
//             moduleTitle.setTextFill(Color.web("#0288d1"));
//             Label desc = new Label(module.getDescription());
//             desc.setFont(Font.font("Quicksand", 15));
//             desc.setTextFill(Color.web("#388e3c"));
//             desc.setWrapText(true);
//             info.getChildren().addAll(moduleTitle, desc);
//             Button openBtn = new Button("View Lessons");
//             openBtn.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold; -fx-background-radius: 18; -fx-padding: 10 36; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.2, 0, 2);");
//             openBtn.setOnAction(e -> showLessonList(primaryStage, module));
//             // If admin module, add badge and animation
//             if (module.getId() == -1) {
//                 Label badge = new Label("Admin");
//                 badge.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 4 16; -fx-effect: dropshadow(gaussian, #43e97b, 6, 0.18, 0, 1);");
//                 badge.setRotate(-8);
//                 badge.setTranslateY(-18);
//                 // Subtle fade-in animation
//                 javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(1.2), card);
//                 fade.setFromValue(0.0);
//                 fade.setToValue(1.0);
//                 fade.setDelay(javafx.util.Duration.millis(100 * index));
//                 fade.play();
//                 info.getChildren().add(badge);
//                 // Add a glowing border effect
//                 card.setStyle(card.getStyle() + "; -fx-border-color: #43e97b; -fx-border-width: 3; -fx-effect: dropshadow(gaussian, #43e97b, 18, 0.22, 0, 8);");
//             }
//             card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #e0f7fa; -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #00c6ff; -fx-border-width: 2.5; -fx-effect: dropshadow(gaussian, #00c6ff, 18, 0.22, 0, 8);" + (module.getId() == -1 ? "; -fx-border-color: #43e97b; -fx-border-width: 3;" : "")));
//             card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #b3e5fc; -fx-border-width: 2.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 12, 0.15, 0, 4);" + (module.getId() == -1 ? "; -fx-border-color: #43e97b; -fx-border-width: 3;" : "")));
//             card.getChildren().addAll(modIcon, info, openBtn);
//             moduleList.getChildren().add(card);
//             index++;
//         }
//         root.getChildren().add(moduleList);
//         Scene scene = new Scene(root, 1366, 768);
//         primaryStage.setScene(scene);
//         primaryStage.setTitle("EcoEdu - Modules");
//         primaryStage.show();
//     }

//     private static void showLessonList(Stage primaryStage, Module module) {
//         VBox root = new VBox(0);
//         root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");
//         root.setPadding(new Insets(0));
//         root.setAlignment(Pos.TOP_CENTER);

//         // Breadcrumb/top bar
//         HBox topBar = new HBox();
//         topBar.setMinHeight(60);
//         topBar.setAlignment(Pos.CENTER_LEFT);
//         topBar.setSpacing(12);
//         topBar.setStyle("-fx-background-radius: 0 0 24 24; -fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.15, 0, 2);");
//         Button backBtn = new Button("← Modules");
//         backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 14px; -fx-background-radius: 16; -fx-padding: 6 18; -fx-cursor: hand;");
//         backBtn.setOnAction(e -> showModuleList(primaryStage));
//         Label section = new Label(module.getIcon() + " " + module.getTitle());
//         section.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
//         section.setTextFill(Color.web("#fffde7"));
//         topBar.getChildren().addAll(backBtn, section);
//         root.getChildren().add(topBar);

//         VBox lessonList = new VBox(28);
//         lessonList.setAlignment(Pos.TOP_CENTER);
//         lessonList.setPadding(new Insets(36, 0, 36, 0));
//         for (Lesson lesson : module.getLessons()) {
//             HBox card = new HBox(14);
//             card.setAlignment(Pos.CENTER_LEFT);
//             card.setPadding(new Insets(18));
//             card.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-color: #b3e5fc; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 8, 0.1, 0, 2);");
//             Label icon = new Label("📘");
//             icon.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
//             VBox info = new VBox(2);
//             Label lessonTitle = new Label(lesson.getTitle());
//             lessonTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
//             lessonTitle.setTextFill(Color.web("#388e3c"));
//             info.getChildren().add(lessonTitle);
//             Button openBtn = new Button("Open");
//             openBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 18; -fx-cursor: hand;");
//             openBtn.setOnAction(e -> showLessonContent(primaryStage, module, lesson));
//             card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #b2ebf2; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-color: #00c6ff; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, #00c6ff, 12, 0.18, 0, 6);"));
//             card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-color: #b3e5fc; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 8, 0.1, 0, 2);"));
//             card.getChildren().addAll(icon, info, openBtn);
//             lessonList.getChildren().add(card);
//         }
//         root.getChildren().add(lessonList);
//         Scene scene = new Scene(root, 1366, 768);
//         primaryStage.setScene(scene);
//         primaryStage.setTitle("EcoEdu - Lessons");
//         primaryStage.show();
//     }

//     private static void showLessonContent(Stage primaryStage, Module module, Lesson lesson) {
//         List<Lesson> lessons = module.getLessons();
//         int currentIndex = lessons.indexOf(lesson);
//         VBox root = new VBox(0);
//         root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");
//         root.setPadding(new Insets(0));
//         root.setAlignment(Pos.TOP_CENTER);

//         // Breadcrumb/top bar
//         HBox topBar = new HBox();
//         topBar.setMinHeight(60);
//         topBar.setAlignment(Pos.CENTER_LEFT);
//         topBar.setSpacing(12);
//         topBar.setStyle("-fx-background-radius: 0 0 24 24; -fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.15, 0, 2);");
//         Button backBtn = new Button("← Lessons");
//         backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 14px; -fx-background-radius: 16; -fx-padding: 6 18; -fx-cursor: hand;");
//         backBtn.setOnAction(e -> showLessonList(primaryStage, module));
//         Label section = new Label(module.getIcon() + " " + module.getTitle() + " > " + lesson.getTitle());
//         section.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
//         section.setTextFill(Color.web("#fffde7"));
//         topBar.getChildren().addAll(backBtn, section);
//         root.getChildren().add(topBar);

//         VBox contentBox = new VBox(32);
//         contentBox.setAlignment(Pos.TOP_CENTER);
//         contentBox.setPadding(new Insets(60, 60, 60, 60));
//         Label title = new Label(lesson.getTitle());
//         title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
//         title.setTextFill(Color.web("#0288d1"));
//         Label content = new Label(lesson.getContent());
//         content.setFont(Font.font("Quicksand", 18));
//         content.setTextFill(Color.web("#263238"));
//         content.setWrapText(true);
//         contentBox.getChildren().addAll(title, content);

//         HBox navBox = new HBox(18);
//         navBox.setAlignment(Pos.CENTER);
//         if (currentIndex > 0) {
//             Button prevBtn = new Button("← Previous Lesson");
//             prevBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #0288d1; -fx-font-size: 16px; -fx-background-radius: 14; -fx-padding: 8 28; -fx-cursor: hand;");
//             prevBtn.setOnAction(e -> showLessonContent(primaryStage, module, lessons.get(currentIndex - 1)));
//             navBox.getChildren().add(prevBtn);
//         }
//         if (currentIndex < lessons.size() - 1) {
//             Button nextBtn = new Button("Next Lesson →");
//             nextBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 14; -fx-padding: 8 28; -fx-cursor: hand;");
//             nextBtn.setOnAction(e -> showLessonContent(primaryStage, module, lessons.get(currentIndex + 1)));
//             navBox.getChildren().add(nextBtn);
//         } else {
//             Button backToModulesBtn = new Button("Back to Modules");
//             backToModulesBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 14; -fx-padding: 8 28; -fx-cursor: hand;");
//             backToModulesBtn.setOnAction(e -> showModuleList(primaryStage));
//             navBox.getChildren().add(backToModulesBtn);
//         }
//         contentBox.getChildren().add(navBox);
//         root.getChildren().add(contentBox);
//         Scene scene = new Scene(root, 1366, 768);
//         primaryStage.setScene(scene);
//         primaryStage.setTitle("EcoEdu - Lesson");
//         primaryStage.show();
//     }
// } 

package com.ecoedu.modules;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.ecoedu.dashboard.StudentDashboard;
import java.util.List;
import com.ecoedu.adminpanel.AdminDataService;
import java.util.ArrayList;

public class ModulePage {
    public static void show(Stage primaryStage) {
        showModuleList(primaryStage);
    }

    private static void showModuleList(Stage primaryStage) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");
        root.setPadding(new Insets(20, 40, 20, 40));
        root.setAlignment(Pos.TOP_CENTER);

        HBox header = new HBox();
        header.setMinHeight(80);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(18);
        header.setStyle("-fx-background-radius: 0 0 32 32; " +
            "-fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); " +
            "-fx-effect: dropshadow(gaussian, #43e97b, 12, 0.2, 0, 4);");
        Label icon = new Label("\uD83D\uDCDA");
        icon.setFont(Font.font("Quicksand", FontWeight.BOLD, 48));
        Label title = new Label("EcoEdu Modules");
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#fffde7"));
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, javafx.scene.layout.Priority.ALWAYS);
        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 15px; -fx-background-radius: 20; -fx-padding: 8 24; -fx-cursor: hand;");
        backBtn.setOnAction(e -> StudentDashboard.show(primaryStage));
        header.getChildren().addAll(icon, title, headerSpacer, backBtn);
        root.getChildren().add(header);

        VBox moduleList = new VBox(32);
        moduleList.setAlignment(Pos.TOP_CENTER);
        moduleList.setPadding(new Insets(40, 20, 40, 20));

        ArrayList<Module> allModules = new ArrayList<>();
        allModules.addAll(ModuleData.getModules());
        for (AdminDataService.Module adminMod : AdminDataService.getInstance().getModules()) {
            Module mod = new Module(-1, adminMod.title, adminMod.description, "\uD83C\uDF31", new ArrayList<>());
            allModules.add(mod);
        }

        int index = 0;
        for (Module module : allModules) {
            HBox card = new HBox(24);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(24));
            card.setStyle("-fx-background-color: white; -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #b3e5fc; -fx-border-width: 2.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 12, 0.15, 0, 4);");

            Label modIcon = new Label(module.getIcon());
            modIcon.setFont(Font.font("Quicksand", FontWeight.BOLD, 38));
            VBox info = new VBox(8);
            Label moduleTitle = new Label(module.getTitle());
            moduleTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
            moduleTitle.setTextFill(Color.web("#0288d1"));
            Label desc = new Label(module.getDescription());
            desc.setFont(Font.font("Quicksand", 15));
            desc.setTextFill(Color.web("#388e3c"));
            desc.setWrapText(true);
            info.getChildren().addAll(moduleTitle, desc);

            Button openBtn = new Button("View Lessons");
            openBtn.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold; -fx-background-radius: 18; -fx-padding: 10 36; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.2, 0, 2);");
            openBtn.setOnAction(e -> showLessonList(primaryStage, module));

            if (module.getId() == -1) {
                Label badge = new Label("Admin");
                badge.setStyle("-fx-background-color: linear-gradient(to right, #43e97b, #00c6ff); -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 4 16; -fx-effect: dropshadow(gaussian, #43e97b, 6, 0.18, 0, 1);");
                badge.setRotate(-8);
                badge.setTranslateY(-18);
                javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(1.2), card);
                fade.setFromValue(0.0);
                fade.setToValue(1.0);
                fade.setDelay(javafx.util.Duration.millis(100 * index));
                fade.play();
                info.getChildren().add(badge);
                card.setStyle(card.getStyle() + "; -fx-border-color: #43e97b; -fx-border-width: 3; -fx-effect: dropshadow(gaussian, #43e97b, 18, 0.22, 0, 8);");
            }

            card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #e0f7fa; -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #00c6ff; -fx-border-width: 2.5; -fx-effect: dropshadow(gaussian, #00c6ff, 18, 0.22, 0, 8);" + (module.getId() == -1 ? "; -fx-border-color: #43e97b; -fx-border-width: 3;" : "")));
            card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: #b3e5fc; -fx-border-width: 2.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 12, 0.15, 0, 4);" + (module.getId() == -1 ? "; -fx-border-color: #43e97b; -fx-border-width: 3;" : "")));
            card.getChildren().addAll(modIcon, info, openBtn);
            moduleList.getChildren().add(card);
            index++;
        }

        ScrollPane scrollPane = new ScrollPane(moduleList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.getChildren().add(scrollPane);

        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Modules");
        primaryStage.show();
    }

    

    // Show the lessons for a specific module
    private static void showLessonList(Stage primaryStage, Module module) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");
        root.setPadding(new Insets(20, 40, 20, 40));
        root.setAlignment(Pos.TOP_CENTER);

        // Breadcrumb/top bar
        HBox topBar = new HBox();
        topBar.setMinHeight(60);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(12);
        topBar.setStyle("-fx-background-radius: 0 0 24 24; -fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.15, 0, 2);");
        Button backBtn = new Button("← Modules");
        backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 14px; -fx-background-radius: 16; -fx-padding: 6 18; -fx-cursor: hand;");
        backBtn.setOnAction(e -> showModuleList(primaryStage));
        Label section = new Label(module.getIcon() + " " + module.getTitle());
        section.setFont(Font.font("Quicksand", FontWeight.BOLD, 22));
        section.setTextFill(Color.web("#fffde7"));
        topBar.getChildren().addAll(backBtn, section);
        root.getChildren().add(topBar);

        VBox lessonList = new VBox(28);
        lessonList.setAlignment(Pos.TOP_CENTER);
        lessonList.setPadding(new Insets(36, 20, 36, 20));
        for (Lesson lesson : module.getLessons()) {
            HBox card = new HBox(14);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(18));
            card.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-color: #b3e5fc; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 8, 0.1, 0, 2);");
            Label icon = new Label("📘");
            icon.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
            VBox info = new VBox(2);
            Label lessonTitle = new Label(lesson.getTitle());
            lessonTitle.setFont(Font.font("Quicksand", FontWeight.BOLD, 16));
            lessonTitle.setTextFill(Color.web("#388e3c"));
            info.getChildren().add(lessonTitle);
            Button openBtn = new Button("Open");
            openBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 6 18; -fx-cursor: hand;");
            openBtn.setOnAction(e -> showLessonContent(primaryStage, module, lesson));
            card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #b2ebf2; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-color: #00c6ff; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, #00c6ff, 12, 0.18, 0, 6);"));
            card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 18; -fx-border-radius: 18; -fx-border-color: #b3e5fc; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, #b3e5fc, 8, 0.1, 0, 2);"));
            card.getChildren().addAll(icon, info, openBtn);
            lessonList.getChildren().add(card);
        }
        ScrollPane scrollPane = new ScrollPane(lessonList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.getChildren().add(scrollPane);

        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Lessons");
        primaryStage.show();
    }

    // Show the content for a specific lesson in a module
    private static void showLessonContent(Stage primaryStage, Module module, Lesson lesson) {
        List<Lesson> lessons = module.getLessons();
        int currentIndex = lessons.indexOf(lesson);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e1f5fe 60%, #fffde7 100%);");
        root.setPadding(new Insets(20, 40, 20, 40));
        root.setAlignment(Pos.TOP_CENTER);

        // Breadcrumb/top bar
        HBox topBar = new HBox();
        topBar.setMinHeight(60);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(12);
        topBar.setStyle("-fx-background-radius: 0 0 24 24; -fx-background-color: linear-gradient(to right, #43e97b 0%, #38f9d7 100%); -fx-effect: dropshadow(gaussian, #43e97b, 8, 0.15, 0, 2);");
        Button backBtn = new Button("← Lessons");
        backBtn.setStyle("-fx-background-color: #fffde7; -fx-text-fill: #43e97b; -fx-font-size: 14px; -fx-background-radius: 16; -fx-padding: 6 18; -fx-cursor: hand;");
        backBtn.setOnAction(e -> showLessonList(primaryStage, module));
        Label section = new Label(module.getIcon() + " " + module.getTitle() + " > " + lesson.getTitle());
        section.setFont(Font.font("Quicksand", FontWeight.BOLD, 20));
        section.setTextFill(Color.web("#fffde7"));
        topBar.getChildren().addAll(backBtn, section);
        root.getChildren().add(topBar);

        VBox contentBox = new VBox(32);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(60, 60, 60, 60));
        Label title = new Label(lesson.getTitle());
        title.setFont(Font.font("Quicksand", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#0288d1"));
        Label content = new Label(lesson.getContent());
        content.setFont(Font.font("Quicksand", 18));
        content.setTextFill(Color.web("#263238"));
        content.setWrapText(true);
        contentBox.getChildren().addAll(title, content);

        HBox navBox = new HBox(18);
        navBox.setAlignment(Pos.CENTER);
        if (currentIndex > 0) {
            Button prevBtn = new Button("← Previous Lesson");
            prevBtn.setStyle("-fx-background-color: #ffd54f; -fx-text-fill: #0288d1; -fx-font-size: 16px; -fx-background-radius: 14; -fx-padding: 8 28; -fx-cursor: hand;");
            prevBtn.setOnAction(e -> showLessonContent(primaryStage, module, lessons.get(currentIndex - 1)));
            navBox.getChildren().add(prevBtn);
        }
        if (currentIndex < lessons.size() - 1) {
            Button nextBtn = new Button("Next Lesson →");
            nextBtn.setStyle("-fx-background-color: #43e97b; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 14; -fx-padding: 8 28; -fx-cursor: hand;");
            nextBtn.setOnAction(e -> showLessonContent(primaryStage, module, lessons.get(currentIndex + 1)));
            navBox.getChildren().add(nextBtn);
        } else {
            Button backToModulesBtn = new Button("Back to Modules");
            backToModulesBtn.setStyle("-fx-background-color: #0288d1; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 14; -fx-padding: 8 28; -fx-cursor: hand;");
            backToModulesBtn.setOnAction(e -> showModuleList(primaryStage));
            navBox.getChildren().add(backToModulesBtn);
        }
        contentBox.getChildren().add(navBox);
        root.getChildren().add(contentBox);

        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("EcoEdu - Lesson");
        primaryStage.show();
    }
}
