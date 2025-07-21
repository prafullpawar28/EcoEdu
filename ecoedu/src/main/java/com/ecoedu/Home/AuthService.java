package com.ecoedu.Home;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    // TODO: Replace with your Firebase Web API Key
    private static final String API_KEY = "YOUR_FIREBASE_API_KEY";
    private static final String SIGNUP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;
    private static final String LOGIN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

    public static Map<String, String> signup(String email, String password) throws Exception {
        return sendAuthRequest(SIGNUP_URL, email, password);
    }

    public static Map<String, String> login(String email, String password) throws Exception {
        return sendAuthRequest(LOGIN_URL, email, password);
    }

    private static Map<String, String> sendAuthRequest(String urlStr, String email, String password) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes("UTF-8"));
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        String resp = response.toString();
        Map<String, String> result = new HashMap<>();
        if (responseCode == 200) {
            // Parse idToken and email from response
            result.put("idToken", extractJsonValue(resp, "idToken"));
            result.put("email", extractJsonValue(resp, "email"));
        } else {
            // Parse error message
            String errorMsg = extractJsonValue(resp, "message");
            throw new Exception(errorMsg != null ? errorMsg : "Authentication failed");
        }
        return result;
    }

    // Minimal JSON value extractor for flat fields
    private static String extractJsonValue(String json, String key) {
        String pattern = String.format("\"%s\":\"", key);
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;
        int start = idx + pattern.length();
        int end = json.indexOf('"', start);
        if (end == -1) return null;
        return json.substring(start, end);
    }
} 