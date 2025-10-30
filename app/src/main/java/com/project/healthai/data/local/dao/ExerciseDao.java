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

    @Query("SELECT * FROM exercises ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercises WHERE target = :target ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> getExercisesByTargetMuscle(String target);

    @Query("SELECT * FROM exercises WHERE body_part = :bodyPart")
    List<Exercise> getExercisesByBodyPart(String bodyPart);

    // Search by keyword (local, case-insensitive)
    @Query("SELECT * FROM exercises WHERE LOWER(exercise_name) LIKE '%' || LOWER(:keyword) || '%' ORDER BY exercise_name ASC")
    LiveData<List<Exercise>> searchExercises(String keyword);

    // Count rows (to check if we’ve cached data)
    @Query("SELECT COUNT(*) FROM exercises")
    int getExerciseCount();

    // Cache validation - check if cache is older than specified time
    @Query("SELECT * FROM exercises WHERE cached_at < :timestamp")
    List<Exercise> getExpiredExercises(long timestamp);

    // clear cache
    @Query("DELETE FROM exercises")
    void clearAll();

}
