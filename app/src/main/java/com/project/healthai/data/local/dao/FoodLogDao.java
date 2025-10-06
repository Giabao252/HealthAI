package com.project.healthai.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.project.healthai.data.local.entities.FoodLog;

import java.util.List;

@Dao
public interface FoodLogDao {
    @Insert
    void insert(FoodLog foodLog);

    @Delete
    void delete(FoodLog foodLog);

    @Query("SELECT * FROM food_logs WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<FoodLog>> getAllFoodLogs(String userId);

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND timestamp >= :startOfDay AND timestamp < :endOfDay ORDER BY timestamp DESC")
    LiveData<List<FoodLog>> getFoodLogsForDay(String userId, long startOfDay, long endOfDay);

    @Query("SELECT * FROM food_logs WHERE userId = :userId AND timestamp >= :startDate ORDER BY timestamp DESC")
    List<FoodLog> getFoodLogsSince(String userId, long startDate);

    @Query("SELECT SUM(calories) FROM food_logs WHERE userId = :userId AND timestamp >= :startOfDay AND timestamp < :endOfDay")
    LiveData<Integer> getTotalCaloriesForDay(String userId, long startOfDay, long endOfDay);

}
