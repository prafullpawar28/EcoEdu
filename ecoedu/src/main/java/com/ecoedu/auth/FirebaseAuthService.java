package com.ecoedu.auth;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ecoedu.modules.POJO;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GdchCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
public class FirebaseAuthService {
    private static final String API_KEY ="AIzaSyB1P0XyqI6PXykgV4e21LUT6dHC5TyeSqY";
     private final FirebaseAuth auth = FirebaseAuth.getInstance();
    /** Logs in a user; returns true if credentials are valid */
    public static boolean login(String email, String password) {
        try {
            URL url = new URL(
              "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY
            );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            JsonObject req = new JsonObject();
            req.addProperty("email", email);
            req.addProperty("password", password);
            req.addProperty("returnSecureToken", true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(req.toString().getBytes());
            }

            if (conn.getResponseCode() == 200) {
                JsonObject json = JsonParser.parseString(
                    new Scanner(conn.getInputStream()).useDelimiter("\\A").next()
                ).getAsJsonObject();
                return json.has("idToken");
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            return false;
        }
    }
        public static Map<String, Object> getDocumentData(String collectionName, String documentId) {
        try {
            Firestore db = FirebaseInitializer.getFirestore();
            DocumentReference docRef = db.collection(collectionName).document(documentId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.getData();  // returns a Map<String, Object>
            } else {
                System.out.println("No such document!");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
            
    }

    public boolean addUser(String email, String password) {
        try {
            UserRecord user = auth.createUser(
                new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
            );
            System.out.println("Created user: " + user.getUid());
            return true;
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    // public static boolean registerUser(String name, String email, String password, String role) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'registerUser'");
    // }
    public String[][] getAllStudents(String collectionName) {
        Firestore db=FirebaseInitializer.db;
        if(db==null){
                db = FirebaseInitializer.getFirestore();
                FirebaseInitializer.db=db;
        }
        ApiFuture<QuerySnapshot> future = db.collection(collectionName).get();
            String[][]answer;
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            answer=new String[documents.size()][];
            int itr=0;
            for (QueryDocumentSnapshot doc : documents) {
                System.out.println("Document ID: " + doc.getId());
                HashMap<String, Object> data = (HashMap<String, Object>) doc.getData();
                long score1=(Long)( data.get ("game1") != null ? data.get("game1") :0);
                long score2=(Long)( data.get ("game2") != null ? data.get("game2"): 0);
                long score3=(Long)( data.get ("game3") != null ? data.get("game3") :0);
                answer[itr] = new String[]{
                    doc.getId(),
                    data.get("name") != null ? data.get("name").toString() : "N/A",
                   score1+"",
                   score2+"",
                   score3+"",
                   score1+score2+score3+""     
            };
                itr++;
            }

        } catch (InterruptedException | ExecutionException e) {
            answer = new String[0][];
            e.printStackTrace();
        }
        return answer;
    }
    public void updateGameScore(String key,int value) {
        Firestore db=FirebaseInitializer.db;
        if(db==null){
                db = FirebaseInitializer.getFirestore();
                FirebaseInitializer.db=db;
        }
         try {
        String docId = POJO.instance.getEmail(); // Replace with actual user ID
        String collection = "Student"; // Replace with actual collection name
        DocumentReference docRef = db.collection(collection).document(docId);
         ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        long score=(Long)document.getData().get(key);
        score=Long.max(score,value);
        Map<String, Object> updates = new HashMap<>();
        updates.put(key, score);
        ApiFuture<WriteResult> writeResult = docRef.update(updates);
       
            writeResult.get();  // Wait for update to complete
            System.out.println("Updated " + key + " successfully!");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        
    }
}
