 package com.ecoedu.auth;
import java.io.FileInputStream;
import java.io.IOException;

 public class FirebaseConfig {
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;
        try {
            // TODO: Replace with the path to your Firebase service account key JSON file
            FileInputStream serviceAccount = new FileInputStream("PATH_TO_YOUR_SERVICE_ACCOUNT_KEY.json");

            // FirebaseOptions options = FirebaseOptions.builder()
            //   .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            //   .build();

            //com.google.firebase.FirebaseApp.initializeApp(options);
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage());
        }
    }
} 