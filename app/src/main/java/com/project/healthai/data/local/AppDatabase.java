package com.project.healthai.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.healthai.data.helpers.Converters;
import com.project.healthai.data.local.entities.Exercise;
import com.project.healthai.data.local.entities.FoodLog;
import com.project.healthai.data.local.entities.User;

@Database(entities = {User.class, FoodLog.class, Exercise.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    //DAOs go here
}
