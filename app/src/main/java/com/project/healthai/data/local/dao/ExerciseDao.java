package com.project.healthai.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.healthai.data.local.entities.Exercise;

import java.util.List;

public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exercise> exercises);

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercises WHERE target = :target ORDER BY name ASC")
    LiveData<List<Exercise>> getExercisesByTargetMuscle(String target);

    // Search by keyword (local, case-insensitive)
    @Query("SELECT * FROM exercises WHERE LOWER(name) LIKE '%' || LOWER(:keyword) || '%' ORDER BY name ASC")
    LiveData<List<Exercise>> searchExercises(String keyword);

    // Count rows (to check if we’ve cached data)
    @Query("SELECT COUNT(*) FROM exercises")
    int getExerciseCount();

    // clear cache
    @Query("DELETE FROM exercises")
    void clearAll();

}
