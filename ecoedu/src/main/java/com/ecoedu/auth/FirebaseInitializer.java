package com.ecoedu.auth;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {
    public static void initialize() {
        try {
            FileInputStream serviceAccount = 
                new FileInputStream("D:\\EcoEdu\\ecoedu\\servicekey.json");
            
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ecoedu-7025d.firebaseio.com")
                .build();
            
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
}