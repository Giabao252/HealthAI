package com.project.healthai.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey @NonNull
    public String id;

    public String name;
    public String bodyPart;
    public String target;
    public String equipment;              // ADD THIS
    public String gifUrl;                 // ADD THIS (critical!)
    public String instructions;       // Stored as string because Room does not recognize arrays
    public String secondaryMusclesJson;   // Store as JSON string
    public String description;            // ADD THIS
    public String difficulty;
    public String category;
    public long cachedAt;                 // For cache validation

    public Exercise(@NonNull String id) {
        this.id = id;
        this.cachedAt = System.currentTimeMillis();
    }

}
