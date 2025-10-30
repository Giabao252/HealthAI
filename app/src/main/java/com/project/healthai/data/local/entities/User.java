package com.project.healthai.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "age")
    public int age;

    @ColumnInfo(name = "current_weight")
    public double currentWeight; // pounds

    @ColumnInfo(name = "target_weight")
    public double targetWeight; // pounds

    @ColumnInfo(name = "height")
    public double height; // feet

    @ColumnInfo(name = "gender")
    public String gender; // "male", "female", "other"

    @ColumnInfo(name = "fitness_goal")
    public String fitnessGoal; // "lose_weight", "gain_muscle", "maintain"

    @ColumnInfo(name = "notes")
    public String notes;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    // Constructor
    public User(@NonNull String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters
    @NonNull
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public int getAge() { return age; }
    public double getCurrentWeight() { return currentWeight; }
    public double getTargetWeight() { return targetWeight; }
    public double getHeight() { return height; }
    public String getGender() { return gender; }
    public String getFitnessGoal() { return fitnessGoal; }
    public String getNotes() { return notes; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }


    // Setters
    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
        this.updatedAt = System.currentTimeMillis(); // Update timestamp
    }
    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
        this.updatedAt = System.currentTimeMillis();
    }
    public void setHeight(double height) {
        this.height = height;
        this.updatedAt = System.currentTimeMillis();
    }
    public void setGender(String gender) {
        this.gender = gender;
        this.updatedAt = System.currentTimeMillis();
    }
    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
        this.updatedAt = System.currentTimeMillis();
    }
    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = System.currentTimeMillis();
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}