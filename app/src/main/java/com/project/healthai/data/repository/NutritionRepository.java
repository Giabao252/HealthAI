package com.project.healthai.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.project.healthai.BuildConfig;
import com.project.healthai.data.remote.GeminiApiService;

import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NutritionRepository {

    private final GeminiApiService geminiApiService;
    private final FirebaseAuth mAuth;
    private final Executor executor;
    private final Handler mainHandler;

    public NutritionRepository(Context context) {
        String apiKey = BuildConfig.GEMINI_API_KEY;
        this.geminiApiService = new GeminiApiService(context, apiKey);
        this.mAuth = FirebaseAuth.getInstance();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public interface MealAnalysisCallback {
        void onSuccess(JSONObject response);
        void onError(Exception e);
    }

    public void analyzeMeal(String targetWeight, String currentWeight,
                            String foodsConsumed, String fitnessGoal, String mealType,
                            MealAnalysisCallback callback) {
        executor.execute(() -> {
            try {
                // Load system prompt
                String systemPrompt = geminiApiService.loadSystemPrompt();

                // Call Gemini API
                String jsonResponse = geminiApiService.generateOutput(
                        systemPrompt,
                        targetWeight,
                        currentWeight,
                        foodsConsumed,
                        fitnessGoal,
                        mealType
                );

                // Clean and parse response
                JSONObject parsedResponse = cleanAndParse(jsonResponse);

                // Return on main thread
                mainHandler.post(() -> callback.onSuccess(parsedResponse));

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    private JSONObject cleanAndParse(String jsonResponse) throws Exception {
        // Clean potential markdown formatting
        jsonResponse = jsonResponse.trim();
        if (jsonResponse.startsWith("```json")) {
            jsonResponse = jsonResponse.substring(7);
        }
        if (jsonResponse.endsWith("```")) {
            jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 3);
        }
        jsonResponse = jsonResponse.trim();

        return new JSONObject(jsonResponse);
    }
}