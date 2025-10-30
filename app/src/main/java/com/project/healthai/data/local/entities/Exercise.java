package com.project.healthai.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "instructions")
    public String instructions;       // Stored as string

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

    // Getters
    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public String getTarget() {
        return target;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public long getCachedAt() {
        return cachedAt;
    }

    // Setters
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCachedAt(long cachedAt) {
        this.cachedAt = cachedAt;
    }
}
