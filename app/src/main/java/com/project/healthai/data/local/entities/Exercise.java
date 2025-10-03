package com.project.healthai.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.project.healthai.data.helpers.Converters;

@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey @NonNull
    private String id;

    private String name;
    private String bodyPart;
    private String target;
    private String equipment;              // ADD THIS
    private String gifUrl;                 // ADD THIS (critical!)
    private String instructions;       // Stored as string because Room does not recognize arrays
    private String secondaryMusclesJson;   // Store as JSON string
    private String description;            // ADD THIS
    private String difficulty;
    private String category;
    private long cachedAt;                 // For cache validation

    public Exercise(@NonNull String id) {
        this.id = id;
        this.cachedAt = System.currentTimeMillis();
    }


}
