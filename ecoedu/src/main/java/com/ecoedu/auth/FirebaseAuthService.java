package com.ecoedu.auth;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    public static boolean registerUser(String name, String email, String password, String role) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerUser'");
    }
}
