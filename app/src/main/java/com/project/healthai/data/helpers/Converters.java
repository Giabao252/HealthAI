package com.project.healthai.data.helpers;

import androidx.room.TypeConverter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        if (value == null) {
            return null;
        };
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        if (list == null) {
            return null;
        };
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
