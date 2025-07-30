package com.ecoedu.auth;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {
    public static Firestore db;
    public static void initialize() {
        try {
            FileInputStream serviceAccount = 
                new FileInputStream("D:/EcoEdu/ecoedu/servicekey.json");
            
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ecoedu-7025d.firebaseio.com")
                .build();
            
            FirebaseApp.initializeApp(options);
           // db = FirestoreClient.getFirestore();  
             //db = FirestoreOptions.getDefaultInstance().getService();
        } catch (IOException e) {
            System.out.println("Exception");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
    public static Firestore getFirestore() {
        if (db == null) {
            db =  FirestoreClient.getFirestore();
        }
        return db;
    }
}