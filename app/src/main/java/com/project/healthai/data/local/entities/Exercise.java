package com.project.healthai.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.project.healthai.data.helpers.Converters;

import java.util.ArrayList;

@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "exercise_id")
    public String id;

    @ColumnInfo(name = "exercise_name")
    public String name;

    @ColumnInfo(name = "body_part")
    public String bodyPart;

    @ColumnInfo(name = "target")
    public String target;

    @ColumnInfo(name = "equipment")
    public String equipment;

    @ColumnInfo(name = "gifUrl")
    public String gifUrl;

    // Changed from String to ArrayList<String> to match API
    @TypeConverters(Converters.class)
    @ColumnInfo(name = "instructions")
    public ArrayList<String> instructions;

    // Added field from API response
    @TypeConverters(Converters.class)
    @ColumnInfo(name = "secondary_muscles")
    public ArrayList<String> secondaryMuscles;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "difficulty")
    public String difficulty;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "cached_at")
    public long cachedAt;

    // Constructor
    public Exercise(@NonNull String id) {
        this.id = id;
        this.cachedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBodyPart() { return bodyPart; }
    public void setBodyPart(String bodyPart) { this.bodyPart = bodyPart; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getGifUrl() { return gifUrl; }
    public void setGifUrl(String gifUrl) { this.gifUrl = gifUrl; }

    public ArrayList<String> getInstructions() { return instructions; }
    public void setInstructions(ArrayList<String> instructions) { this.instructions = instructions; }

    public ArrayList<String> getSecondaryMuscles() { return secondaryMuscles; }
    public void setSecondaryMuscles(ArrayList<String> secondaryMuscles) { this.secondaryMuscles = secondaryMuscles; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public long getCachedAt() { return cachedAt; }
    public void setCachedAt(long cachedAt) { this.cachedAt = cachedAt; }
}