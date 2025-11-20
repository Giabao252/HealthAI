package com.project.healthai.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.project.healthai.R;
import com.project.healthai.data.local.entities.User;
import com.project.healthai.ui.MainActivity;
import com.project.healthai.ui.auth.LoginActivity;
import com.project.healthai.utils.AuthContext;
import com.project.healthai.utils.NavigationBar;
import com.project.healthai.utils.RedirectionHelpers;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AuthContext authContext;
    private User currentUser;
    private RedirectionHelpers redirectionHelper = new RedirectionHelpers();
    private TextView age, gender, height, currentWeight, targetWeight, fitnessGoal, notes;

    private EditText ageInput, genderInput, heightInput, currentWeightInput, targetWeightInput, fitnessGoalInput, notesInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        authContext = AuthContext.getInstance(this);

        Spinner menuInHome = findViewById(R.id.navigation_spinner);
        NavigationBar.setupNavigationSpinner(this, menuInHome, "Home");

        if (!authContext.isUserLoggedIn()) {
            redirectionHelper.redirect(this, MainActivity.class);
            finish();
        }

        Button btnLogout = findViewById(R.id.logout_button);
        NavigationBar.setupLogoutButton(this, btnLogout);

        TextView userName = findViewById(R.id.currentUser);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        height = findViewById(R.id.height);
        currentWeight = findViewById(R.id.currentWeight);
        targetWeight = findViewById(R.id.targetWeight);
        fitnessGoal = findViewById(R.id.fitnessGoal);
        notes = findViewById(R.id.notes);

        ageInput = findViewById(R.id.etAge);
        genderInput = findViewById(R.id.etGender);
        heightInput = findViewById(R.id.etHeight);
        currentWeightInput = findViewById(R.id.etCurrentWeight);
        targetWeightInput = findViewById(R.id.etTargetWeight);
        fitnessGoalInput = findViewById(R.id.etFitnessGoal);
        notesInput = findViewById(R.id.etNotes);

        authContext.getCurrentUserAsync(user -> {
            if (user != null) {
                currentUser = user;
                // NOW it's safe to use currentUser
                userName.setText(currentUser.username);
                displayUserMetrics();
            }
        });

    }

    private void displayUserMetrics() {

        if (currentUser.getAge() > 0) {
            age.setText(currentUser.getAge() + " years");
        } else {
            age.setText("--");
        }

        if (currentUser.getGender() != null && !currentUser.getGender().isEmpty()) {
            gender.setText(currentUser.getGender());
        } else {
            gender.setText("--");
        }

        if (currentUser.getHeight() > 0) {
            height.setText(String.format("%.1f ft", currentUser.getHeight()));
        } else {
            height.setText("--");
        }

        if (currentUser.getCurrentWeight() > 0) {
            currentWeight.setText(String.format("%.1f lbs", currentUser.getCurrentWeight()));
        } else {
            currentWeight.setText("--");
        }

        if (currentUser.getTargetWeight() > 0) {
            targetWeight.setText(String.format("%.1f lbs", currentUser.getTargetWeight()));
        } else {
            targetWeight.setText("--");
        }

        if (currentUser.getFitnessGoal() != null && !currentUser.getFitnessGoal().isEmpty()) {
            fitnessGoal.setText(currentUser.getFitnessGoal());
        } else {
            fitnessGoal.setText("--");
        }

        if (currentUser.getNotes() != null && !currentUser.getNotes().isEmpty()) {
            notes.setText(currentUser.getNotes());
        } else {
            notes.setText("No notes added");
        }
    }

    public void profileUpdateButton(View view) {
        // Get strings with trim
        String ageStr = ageInput.getText().toString().trim();
        String genderStr = genderInput.getText().toString().trim();
        String heightStr = heightInput.getText().toString().trim();
        String currentWeightStr = currentWeightInput.getText().toString().trim();
        String targetWeightStr = targetWeightInput.getText().toString().trim();
        String fitnessGoalStr = fitnessGoalInput.getText().toString().trim();
        String notesStr = notesInput.getText().toString().trim();

        // Validate and parse age
        if (ageStr.isEmpty()) {
            Toast.makeText(this, "Please enter your age", Toast.LENGTH_SHORT).show();
            return;
        }

        int ageNum;
        try {
            ageNum = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Age must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate gender
        if (genderStr.isEmpty()) {
            Toast.makeText(this, "Please enter your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate and parse height
        if (heightStr.isEmpty()) {
            Toast.makeText(this, "Please enter your height", Toast.LENGTH_SHORT).show();
            return;
        }

        double heightNum;
        try {
            heightNum = Double.parseDouble(heightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Height must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate and parse current weight
        if (currentWeightStr.isEmpty()) {
            Toast.makeText(this, "Please enter your current weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double currentWeightNum;
        try {
            currentWeightNum = Double.parseDouble(currentWeightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Current weight must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate and parse target weight
        if (targetWeightStr.isEmpty()) {
            Toast.makeText(this, "Please enter your target weight", Toast.LENGTH_SHORT).show();
            return;
        }

        double targetWeightNum;
        try {
            targetWeightNum = Double.parseDouble(targetWeightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Target weight must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate fitness goal
        if (fitnessGoalStr.isEmpty()) {
            Toast.makeText(this, "Please enter your fitness goal", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update user object
        currentUser.setAge(ageNum);
        currentUser.setGender(genderStr);
        currentUser.setHeight(heightNum);
        currentUser.setCurrentWeight(currentWeightNum);
        currentUser.setTargetWeight(targetWeightNum);
        currentUser.setFitnessGoal(fitnessGoalStr);
        currentUser.setNotes(notesStr);

        // Save to database
        authContext.updateCurrentUser(currentUser);

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        // Refresh display
        displayUserMetrics();

        // Hide keyboard
        try {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // Ignore
        }
    }

}
