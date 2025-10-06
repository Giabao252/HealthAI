package com.project.healthai.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_logs")
public class FoodLog {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userId;
    public String foodName;
    public int calories;
    public double protein; // pounds
    public double carbs; // pounds
    public double fats; // pounds
    public String mealType; // "breakfast", "lunch", "dinner", "snack"
    public long timestamp;

    // Constructor
    public FoodLog(String userId, String foodName, int calories,
                   double protein, double carbs, double fats,
                   String mealType, long timestamp) {
        this.userId = userId;
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.mealType = mealType;
        this.timestamp = timestamp;
    }
}
