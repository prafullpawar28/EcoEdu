package com.ecoedu;
import com.ecoedu.Home.Home;

import javafx.application.Application;
import javafx.scene.Scene;

public class Main  {
    public static void main(String[] args) {
       Application.launch(Home.class,args);
    }

    public static void applyEcoEduTheme(Scene scene) {
        scene.getStylesheets().add(Main.class.getResource("/css/ecoedu-theme.css").toExternalForm());
    }
}
