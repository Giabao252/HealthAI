package com.project.healthai.ui.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthai.R;
import com.project.healthai.utils.ExerciseAdapter;
import com.project.healthai.data.local.entities.Exercise;
import com.project.healthai.data.repository.ExerciseRepository;
import com.project.healthai.utils.NavigationBar;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private ExerciseRepository repository;
    private EditText searchBar;
    private Spinner bodyPartSpinner;
    private Button searchButton;

    private String currentFilter = "All";
    private LiveData<List<Exercise>> currentLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        repository = new ExerciseRepository(this);

        // Setup navigation bar (it will find the views inside the included layout)
        Spinner navigationSpinner = findViewById(R.id.navigation_spinner);
        Button logoutButton = findViewById(R.id.logout_button);
        NavigationBar.setupNavigationSpinner(this, navigationSpinner, "Exercise");
        NavigationBar.setupLogoutButton(this, logoutButton);

        // Initialize other views
        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);
        bodyPartSpinner = findViewById(R.id.bodyPartSpinner);
        recyclerView = findViewById(R.id.exerciseRecyclerView);

        // Setup RecyclerView with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ExerciseAdapter(this, exercise -> {
            // Open detail activity
            Intent intent = new Intent(this, ExerciseDetailActivity.class);
            intent.putExtra("exercise_id", exercise.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Setup filter spinner
        setupBodyPartFilter();

        // Setup search
        searchButton.setOnClickListener(v -> performSearch());

        // Load all exercises initially
        loadAllExercises();
    }

    private void setupBodyPartFilter() {
        repository.getBodyParts().observe(this, bodyParts -> {
            List<String> filterOptions = new ArrayList<>();
            filterOptions.add("All");
            if (bodyParts != null) {
                filterOptions.addAll(bodyParts);
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    filterOptions
            );
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bodyPartSpinner.setAdapter(spinnerAdapter);
        });

        bodyPartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilter = parent.getItemAtPosition(position).toString();
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadAllExercises() {
        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }

        currentLiveData = repository.getAllExercises();
        currentLiveData.observe(this, exercises -> {
            if (exercises != null) {
                adapter.setExercises(exercises);
            }
        });
    }

    private void performSearch() {
        String query = searchBar.getText().toString().trim();

        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }

        if (query.isEmpty()) {
            applyFilter();
        } else {
            currentLiveData = repository.searchExercises(query);
            currentLiveData.observe(this, exercises -> {
                if (exercises != null) {
                    adapter.setExercises(exercises);
                }
            });
        }
    }

    private void applyFilter() {
        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }

        if (currentFilter.equals("All")) {
            loadAllExercises();
        } else {
            currentLiveData = repository.getExercisesByBodyPart(currentFilter);
            currentLiveData.observe(this, exercises -> {
                if (exercises != null) {
                    adapter.setExercises(exercises);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }
    }
}