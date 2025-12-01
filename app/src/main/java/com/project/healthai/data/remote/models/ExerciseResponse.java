package com.project.healthai.data.remote.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ExerciseResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("bodyPart")
    public String bodyPart;

    @SerializedName("target")
    public String target;

    @SerializedName("equipment")
    public String equipment;

    @SerializedName("gifUrl")
    public String gifUrl;

    @SerializedName("instructions")
    public ArrayList<String> instructions;

    @SerializedName("secondaryMuscles")
    public ArrayList<String> secondaryMuscles;

    @SerializedName("description")
    public String description;

    @SerializedName("difficulty")
    public String difficulty;

    @SerializedName("category")
    public String category;
}
