package com.project.healthai.ui.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.project.healthai.R;
import com.project.healthai.data.local.AppDatabase;
import com.project.healthai.data.local.entities.Exercise;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;

public class ExerciseDetailActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseDetailActivity";

    private ImageView exerciseGif;
    private TextView exerciseName;
    private TextView bodyPart;
    private TextView targetMuscle;
    private TextView equipment;
    private TextView difficulty;
    private TextView category;
    private TextView description;
    private TextView instructions;
    private Button backButton;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        executorService = Executors.newSingleThreadExecutor();

        // Initialize views
        exerciseGif = findViewById(R.id.exerciseGif);
        exerciseName = findViewById(R.id.exerciseName);
        bodyPart = findViewById(R.id.bodyPart);
        targetMuscle = findViewById(R.id.targetMuscle);
        equipment = findViewById(R.id.equipment);
        difficulty = findViewById(R.id.difficulty);
        category = findViewById(R.id.category);
        description = findViewById(R.id.description);
        instructions = findViewById(R.id.instructions);
        backButton = findViewById(R.id.backButton);

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Get exercise ID from intent
        String exerciseId = getIntent().getStringExtra("exercise_id");

        if (exerciseId != null) {
            loadExerciseDetails(exerciseId);
        } else {
            Toast.makeText(this, "Error: No exercise ID provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadExerciseDetails(String exerciseId) {
        executorService.execute(() -> {
            try {
                // Get exercise from database
                AppDatabase db = AppDatabase.getInstance(this);
                Exercise exercise = db.exerciseDao().getExerciseById(exerciseId);

                if (exercise == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Exercise not found", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                    return;
                }

                // Update UI on main thread
                runOnUiThread(() -> displayExerciseDetails(exercise));

            } catch (Exception e) {
                Log.e(TAG, "Error loading exercise details", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error loading exercise", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void displayExerciseDetails(Exercise exercise) {
        // Set exercise name
        exerciseName.setText(exercise.getName());

        // Set basic info
        bodyPart.setText(capitalize(exercise.getBodyPart()));
        targetMuscle.setText(capitalize(exercise.getTarget()));
        equipment.setText(capitalize(exercise.getEquipment()));
        difficulty.setText(capitalize(exercise.getDifficulty()));
        category.setText(capitalize(exercise.getCategory()));

        // Set description
        if (exercise.getDescription() != null && !exercise.getDescription().isEmpty()) {
            description.setText(exercise.getDescription());
        } else {
            description.setText("No description available.");
        }

        // Format and set instructions
        ArrayList<String> instructionsList = exercise.getInstructions();
        if (instructionsList != null && !instructionsList.isEmpty()) {
            StringBuilder instructionsText = new StringBuilder();
            for (int i = 0; i < instructionsList.size(); i++) {
                instructionsText.append((i + 1))
                        .append(". ")
                        .append(instructionsList.get(i))
                        .append("\n\n");
            }
            instructions.setText(instructionsText.toString().trim());
        } else {
            instructions.setText("No instructions available.");
        }

        // Load GIF
        if (exercise.getGifUrl() != null && !exercise.getGifUrl().isEmpty()) {
            Log.d(TAG, "Loading GIF: " + exercise.getGifUrl());

            Glide.with(this)
                    .asGif()
                    .load(exercise.getGifUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<GifDrawable> target, boolean isFirstResource) {
                            Log.e(TAG, "Failed to load GIF", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model,
                                                       Target<GifDrawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            Log.d(TAG, "GIF loaded successfully");
                            return false;
                        }
                    })
                    .into(exerciseGif);
        } else {
            exerciseGif.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
