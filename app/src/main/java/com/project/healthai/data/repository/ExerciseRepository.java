package com.project.healthai.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.project.healthai.BuildConfig;
import com.project.healthai.data.local.AppDatabase;
import com.project.healthai.data.local.dao.ExerciseDao;
import com.project.healthai.data.local.entities.Exercise;
import com.project.healthai.data.remote.models.ExerciseResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExerciseRepository {
    private static final String TAG = "ExerciseRepository";
    private static final String API_URL = "https://exercisedb.p.rapidapi.com/exercises";
    private static final long CACHE_VALIDITY_MS = 7 * 24 * 60 * 60 * 1000; // 7 days

    private final ExerciseDao exerciseDao;
    private final ExecutorService executorService;
    private final OkHttpClient client;

    public ExerciseRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.exerciseDao = db.exerciseDao();
        this.executorService = Executors.newSingleThreadExecutor();
        this.client = new OkHttpClient();
    }

    // Get all exercises from local database
    public LiveData<List<Exercise>> getAllExercises() {
        return exerciseDao.getAllExercises();
    }

    // Search exercises locally
    public LiveData<List<Exercise>> searchExercises(String keyword) {
        return exerciseDao.searchExercises(keyword);
    }

    // Filter by body part locally
    public LiveData<List<Exercise>> getExercisesByBodyPart(String bodyPart) {
        return exerciseDao.getExercisesByBodyPart(bodyPart);
    }

    // Get distinct body parts for filter UI
    public LiveData<List<String>> getBodyParts() {
        return exerciseDao.getDistinctBodyParts();
    }

    // UPDATED: Fetch exercises with option to force refresh
    public void fetchAndCacheExercises(boolean forceRefresh, FetchCallback callback) {
        executorService.execute(() -> {
            try {
                // Check if cache is fresh (unless forcing refresh)
                if (!forceRefresh) {
                    long cacheThreshold = System.currentTimeMillis() - CACHE_VALIDITY_MS;
                    if (exerciseDao.isCacheFresh(cacheThreshold)) {
                        Log.d(TAG, "Cache is fresh, skipping API call");
                        if (callback != null) callback.onSuccess("Cache is fresh");
                        return;
                    }
                }

                Log.d(TAG, "Fetching exercises from API..." + (forceRefresh ? " (forced refresh)" : ""));

                // Build request
                Request request = new Request.Builder()
                        .url(API_URL + "?limit=0")  // limit=0 for all exercises
                        .addHeader("X-RapidAPI-Key", BuildConfig.RAPIDAPI_KEY)
                        .addHeader("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    String error = "API Error: " + response.code();
                    Log.e(TAG, error);
                    if (callback != null) callback.onError(error);
                    return;
                }

                String jsonData = response.body().string();

                // ADD LOGGING - Check first exercise
                JSONArray tempArray = new JSONArray(jsonData);
                if (tempArray.length() > 0) {
                    Log.d(TAG, "Sample exercise JSON: " + tempArray.getJSONObject(0).toString());
                }

                List<Exercise> exercises = parseExercises(jsonData);

                // Clear old cache and insert new data
                exerciseDao.clearAll();
                exerciseDao.insertAll(exercises);

                Log.d(TAG, "Successfully cached " + exercises.size() + " exercises");
                if (callback != null) callback.onSuccess("Cached " + exercises.size() + " exercises");

            } catch (IOException e) {
                Log.e(TAG, "Network error: " + e.getMessage());
                if (callback != null) callback.onError(e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage(), e);
                if (callback != null) callback.onError(e.getMessage());
            }
        });
    }

    // Convenience method - defaults to NOT forcing refresh
    public void fetchAndCacheExercises(FetchCallback callback) {
        fetchAndCacheExercises(false, callback);
    }

    private List<Exercise> parseExercises(String jsonData) throws Exception {
        List<Exercise> exercises = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonData);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);

            String exerciseId = json.getString("id");
            Exercise exercise = new Exercise(exerciseId);
            exercise.setName(json.optString("name", ""));
            exercise.setBodyPart(json.optString("bodyPart", ""));
            exercise.setTarget(json.optString("target", ""));
            exercise.setEquipment(json.optString("equipment", ""));

            // CONSTRUCT the GIF URL using the image endpoint
            // For BASIC tier, use 180 resolution
            String gifUrl = "https://exercisedb.p.rapidapi.com/image?exerciseId="
                    + exerciseId
                    + "&resolution=180"
                    + "&rapidapi-key=" + BuildConfig.RAPIDAPI_KEY;

            if (i < 3) {
                Log.d(TAG, "Exercise: " + exercise.getName() + " | Constructed gifUrl: " + gifUrl);
            }
            exercise.setGifUrl(gifUrl);

            exercise.setDescription(json.optString("description", ""));
            exercise.setDifficulty(json.optString("difficulty", "beginner"));
            exercise.setCategory(json.optString("category", "strength"));

            // Parse instructions array
            ArrayList<String> instructions = new ArrayList<>();
            JSONArray instructionsArray = json.optJSONArray("instructions");
            if (instructionsArray != null) {
                for (int j = 0; j < instructionsArray.length(); j++) {
                    instructions.add(instructionsArray.getString(j));
                }
            }
            exercise.setInstructions(instructions);

            // Parse secondaryMuscles array
            ArrayList<String> secondaryMuscles = new ArrayList<>();
            JSONArray musclesArray = json.optJSONArray("secondaryMuscles");
            if (musclesArray != null) {
                for (int j = 0; j < musclesArray.length(); j++) {
                    secondaryMuscles.add(musclesArray.getString(j));
                }
            }
            exercise.setSecondaryMuscles(secondaryMuscles);

            exercises.add(exercise);
        }

        return exercises;
    }



    public interface FetchCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
