package com.project.healthai.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.healthai.data.helpers.Converters;
import com.project.healthai.data.local.dao.ExerciseDao;
import com.project.healthai.data.local.dao.UserDao;
import com.project.healthai.data.local.entities.Exercise;
import com.project.healthai.data.local.entities.User;

@Database(
        entities = {User.class, Exercise.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    // DAOs
    public abstract UserDao userDao();
    public abstract ExerciseDao exerciseDao();

    // The INSTANCE is created only once
    private static volatile AppDatabase DB_INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (DB_INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (DB_INSTANCE == null) {
                    DB_INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "health_ai"
                    ).build();
                }
            }
        }
        return DB_INSTANCE;
    }

}
