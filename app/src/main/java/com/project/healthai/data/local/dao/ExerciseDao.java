package com.project.healthai.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.healthai.data.local.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exercise> exercises);

    @Query("SELECT * FROM exercises WHERE exercise_id = :exerciseId LIMIT 1")
    Exercise getExerciseById(String exerciseId);

    @Query("SELECT * FROM exercises ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercises WHERE target = :target ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> getExercisesByTargetMuscle(String target);

    @Query("SELECT * FROM exercises WHERE body_part = :bodyPart ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> getExercisesByBodyPart(String bodyPart);

    // Search by keyword (local, case-insensitive)
    @Query("SELECT * FROM exercises WHERE LOWER(exercise_name) LIKE '%' || LOWER(:keyword) || '%' ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> searchExercises(String keyword);

    // Get distinct body parts for filter
    @Query("SELECT DISTINCT body_part FROM exercises ORDER BY body_part ASC")
    LiveData<List<String>> getDistinctBodyParts();

    // Count rows (to check if we've cached data)
    @Query("SELECT COUNT(*) FROM exercises")
    int getExerciseCount();

    // Check if cache exists and is fresh
    @Query("SELECT COUNT(*) > 0 FROM exercises WHERE cached_at > :timestamp")
    boolean isCacheFresh(long timestamp);

    // Clear cache
    @Query("DELETE FROM exercises")
    void clearAll();
}