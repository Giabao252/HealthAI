package com.project.healthai.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.healthai.ui.exercises.ExerciseActivity;
import com.project.healthai.ui.home.HomeActivity;
import com.project.healthai.ui.nutrition.NutritionActivity;

public class NavigationDropDown {

    public static void setupNavigationSpinner(Activity activity, Spinner spinner, String currentActivity) {
        String[] navItems = {"Home", "Nutrition", "Exercise"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                navItems
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set current selection
        int currentPosition = getPositionForActivity(currentActivity);
        spinner.setSelection(currentPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Don't navigate if it's the current activity
                if (position == currentPosition) return;

                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(activity, HomeActivity.class);
                        break;
                    case 1:
                        intent = new Intent(activity, NutritionActivity.class);
                        break;
                    case 2:
                        intent = new Intent(activity, ExerciseActivity.class);
                        break;
                }

                if (intent != null) {
                    activity.startActivity(intent);
                    activity.finish(); // Optional: close current activity
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private static int getPositionForActivity(String activityName) {
        switch (activityName) {
            case "Home": return 0;
            case "Nutrition": return 1;
            case "Exercise": return 2;
            default: return 0;
        }
    }
}