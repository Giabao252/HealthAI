package com.project.healthai.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    public String userId;

    public String email;
    public String name;
    public int age;
    public double currentWeight; // pounds
    public double targetWeight; // pounds
    public double height; // ft
    public String gender; // "male", "female", "other"
    public String fitnessGoal; // "lose_weight", "gain_muscle", "maintain"
    public String notes;
    public long createdAt;
    public long updatedAt;

    // Constructor
    public User(@NonNull String userId) {
        this.userId = userId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
}
