package com.project.healthai.data.remote;

import android.content.Context;

import com.project.healthai.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiApiService {

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";
    private final String apiKey;
    private final Context context;

    public GeminiApiService(Context context, String apiKey) {
        this.context = context;
        this.apiKey = apiKey;
    }

    /**
     * Generate feedback of the input meal with further meal plan suggestions using selected Gemini model
     * @param systemPrompt: The system instruction for the AI
     * @param targetWeight: User's target weight
     * @param currentWeight: User's current weight
     * @param foodsConsumed: User's meal broken down into each kind of food
     * @param mealType: User's meal type
     * @return AI-generated plan as JSON string
     */
    public String generateOutput(String systemPrompt, String targetWeight, String currentWeight, String foodsConsumed, String fitnessGoal, String mealType) throws Exception {
        // Build request body with proper structure
        JSONObject requestBody = buildRequestBody(systemPrompt, targetWeight, currentWeight, foodsConsumed, fitnessGoal, mealType);

        // Construct model-specific URL
        String apiUrl = BASE_URL + "gemini-2.5-pro" + ":generateContent?key=" + apiKey;

        // Make API call
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        // Send request
        OutputStream os = conn.getOutputStream();
        os.write(requestBody.toString().getBytes());
        os.flush();
        os.close();

        // Read response
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            conn.disconnect();

            // Parse Gemini response
            JSONObject jsonResponse = new JSONObject(response.toString());
            String aiResponse = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            return aiResponse;

        } else {
            // Read error
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }
            errorReader.close();
            conn.disconnect();
            throw new Exception("API Error " + responseCode + ": " + errorResponse.toString());
        }
    }

    /**
     * Build request body with proper Gemini API structure
     * Uses system_instruction field (recommended by Gemini)
     * instead of mixing system and user messages in parts
     */
    private JSONObject buildRequestBody(String systemPrompt, String targetWeight, String currentWeight, String foodsConsumed, String fitnessGoal, String mealType) throws Exception {
        JSONObject requestBody = new JSONObject();

        // 1. Create system_instruction object (NEW STRUCTURE)
        JSONObject systemInstruction = new JSONObject();
        JSONArray systemParts = new JSONArray();
        JSONObject systemTextPart = new JSONObject();
        systemTextPart.put("text", systemPrompt);
        systemParts.put(systemTextPart);
        systemInstruction.put("parts", systemParts);
        requestBody.put("system_instruction", systemInstruction);

        // 2. Create contents array with user role (NEW STRUCTURE)
        JSONArray contents = new JSONArray();
        JSONObject userContent = new JSONObject();
        userContent.put("role", "user");

        JSONArray userParts = new JSONArray();
        JSONObject userTextPart = new JSONObject();

        // Format user message with context
        String userMessage = String.format(
                "Current user's fitness information:\n\n " +
                        "Target Weight: %s\n" +
                        "Current Weight: %s\n" +
                        "Foods Consumed this meal: %s\n" +
                        "Current user's fitness goal: %s\n" +
                        "Type of the meal: %s\n\n" +
                        "Please generate a feedback regarding the meal user just consumed as well as recommend a better meal plan for the user.",
                targetWeight,
                currentWeight,
                foodsConsumed,
                fitnessGoal,
                mealType
        );
        userTextPart.put("text", userMessage);
        userParts.put(userTextPart);

        userContent.put("parts", userParts);
        contents.put(userContent);
        requestBody.put("contents", contents);

        return requestBody;
    }

    /**
     * Load system prompt from res/raw/planner_prompt.txt
     */
    public String loadSystemPrompt() throws Exception {

        InputStream is = context.getResources().openRawResource(R.raw.prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder prompt = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            prompt.append(line).append("\n");
        }
        reader.close();
        return prompt.toString();

    }
}
