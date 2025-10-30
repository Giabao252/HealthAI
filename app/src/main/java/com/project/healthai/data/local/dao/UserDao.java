package com.project.healthai.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.healthai.data.local.entities.User;
import com.project.healthai.data.local.models.UserBodyMetrics;

@Dao
public interface UserDao {
    // Insert user (for registration)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    User getUserById(String userId);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users LIMIT 1")
    User getCurrentUser();

    @Query("SELECT COUNT(*) FROM users")
    int getUserCount();

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE user_id = :userId)")
    boolean userExists(String userId);

    @Query("UPDATE users SET current_weight = :weight, updated_at = :timestamp WHERE user_id = :userId")
    void updateWeight(String userId, double weight, long timestamp);

    @Query("UPDATE users SET fitness_goal = :goal, updated_at = :timestamp WHERE user_id = :userId")
    void updateFitnessGoal(String userId, String goal, long timestamp);

    @Query("UPDATE users SET age = :age, height = :height, gender = :gender, updated_at = :timestamp WHERE user_id = :userId")
    void updateBasicInfo(String userId, int age, double height, String gender, long timestamp);

    @Query("SELECT * FROM users WHERE fitness_goal = :goal")
    User[] getUsersByGoal(String goal);

    // Update notes
    @Query("UPDATE users SET notes = :notes, updated_at = :timestamp WHERE user_id = :userId")
    void updateNotes(String userId, String notes, long timestamp);
}
