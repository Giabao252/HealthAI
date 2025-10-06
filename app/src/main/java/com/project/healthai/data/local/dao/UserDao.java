package com.project.healthai.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.healthai.data.local.entities.User;
import com.project.healthai.data.local.models.UserBodyMetrics;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    //runs asynchronously and will auto update the LiveData when data in the users table changes
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    LiveData<User> getUserById(String userId);

    //Runs synchronously - for immediate access to data inside background services
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    User getUserByIdSync(String userId);

    @Query("DELETE FROM users WHERE userId = :userId")
    void deleteUser(String userId);

    @Query("SELECT currentWeight, targetWeight, height, gender, fitnessGoal, notes, age FROM users WHERE userId = :userId LIMIT 1")
    UserBodyMetrics getUserBodyMetrics(String userId);
}
