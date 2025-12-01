package com.project.healthai.ui.nutrition;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.project.healthai.R;
import com.project.healthai.data.local.entities.User;
import com.project.healthai.data.repository.NutritionRepository;
import com.project.healthai.ui.MainActivity;
import com.project.healthai.utils.AuthContext;
import com.project.healthai.utils.NavigationBar;
import com.project.healthai.utils.UIHelper;
import com.project.healthai.utils.RedirectionHelpers;

import org.json.JSONObject;

public class NutritionActivity extends AppCompatActivity {

    // Repository
    private NutritionRepository repository;
    private FirebaseAuth mAuth;
    private AuthContext authContext;
    private RedirectionHelpers redirectionHelper;
    private User currentUser;

    // Input views
    private EditText etFoodsConsumed;
    private Spinner spinnerMealType;
    private Button btnAnalyzeMeal;
    private ProgressBar progressBar;

    // Results container
    private LinearLayout resultsContainer;

    // Feedback views
    private TextView tvOverallAssessment;
    private TextView tvEstimatedCalories;
    private TextView tvProtein, tvCarbs, tvFats;
    private LinearLayout goodPointsContainer;
    private LinearLayout concernsContainer;

    // Improved meal views
    private TextView tvMealName;
    private TextView tvImprovedCalories;
    private LinearLayout foodsListContainer;
    private TextView tvPreparationTips;
    private LinearLayout improvementsContainer;
    private TextView tvHowThisHelps;
    private TextView tvEncouragement;

    // User data
    private String currentWeight = "";
    private String targetWeight = "";
    private String fitnessGoal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        mAuth = FirebaseAuth.getInstance();
        authContext = AuthContext.getInstance(this);

        authContext.getCurrentUserAsync(user -> {
            if (user != null) {
                currentUser = user;
                currentWeight = String.valueOf(currentUser.getCurrentWeight());
                targetWeight = String.valueOf(currentUser.getTargetWeight());
                fitnessGoal = currentUser.getFitnessGoal();
            }
        });

        Spinner menuInHome = findViewById(R.id.navigation_spinner);
        NavigationBar.setupNavigationSpinner(this, menuInHome, "Nutrition");

        if (!authContext.isUserLoggedIn()) {
            redirectionHelper.redirect(this, MainActivity.class);
            finish();
        }

        Button btnLogout = findViewById(R.id.logout_button);
        NavigationBar.setupLogoutButton(this, btnLogout);

        // Initialize repository
        repository = new NutritionRepository(this);

        // Initialize views
        initializeViews();
        setupSpinner();

        // Setup button click
        btnAnalyzeMeal.setOnClickListener(v -> analyzeMeal());
    }

    private void initializeViews() {
        // Input section
        etFoodsConsumed = findViewById(R.id.etFoodsConsumed);
        spinnerMealType = findViewById(R.id.spinnerMealType);
        btnAnalyzeMeal = findViewById(R.id.btnAnalyzeMeal);
        progressBar = findViewById(R.id.progressBar);

        // Results container
        resultsContainer = findViewById(R.id.resultsContainer);

        // Feedback section
        tvOverallAssessment = findViewById(R.id.tvOverallAssessment);
        tvEstimatedCalories = findViewById(R.id.tvEstimatedCalories);
        tvProtein = findViewById(R.id.tvProtein);
        tvCarbs = findViewById(R.id.tvCarbs);
        tvFats = findViewById(R.id.tvFats);
        goodPointsContainer = findViewById(R.id.goodPointsContainer);
        concernsContainer = findViewById(R.id.concernsContainer);

        // Improved meal section
        tvMealName = findViewById(R.id.tvMealName);
        tvImprovedCalories = findViewById(R.id.tvImprovedCalories);
        foodsListContainer = findViewById(R.id.foodsListContainer);
        tvPreparationTips = findViewById(R.id.tvPreparationTips);
        improvementsContainer = findViewById(R.id.improvementsContainer);
        tvHowThisHelps = findViewById(R.id.tvHowThisHelps);
        tvEncouragement = findViewById(R.id.tvEncouragement);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.meal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(adapter);
    }

    private void analyzeMeal() {
        // Get input
        String foodsConsumed = etFoodsConsumed.getText().toString().trim();
        String mealType = spinnerMealType.getSelectedItem().toString();

        // Validate
        if (foodsConsumed.isEmpty()) {
            etFoodsConsumed.setError("Please enter the foods you consumed");
            etFoodsConsumed.requestFocus();
            return;
        }

        // Show loading
        setLoadingState(true);

        // Call repository
        repository.analyzeMeal(targetWeight, currentWeight, foodsConsumed, fitnessGoal, mealType,
                new NutritionRepository.MealAnalysisCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        setLoadingState(false);
                        displayResults(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        setLoadingState(false);
                        Toast.makeText(NutritionActivity.this,
                                "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoadingState(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnAnalyzeMeal.setEnabled(!isLoading);
        btnAnalyzeMeal.setText(isLoading ? "Analyzing..." : "Analyze My Meal");
    }

    private void displayResults(JSONObject response) {
        try {
            // Parse meal feedback
            JSONObject feedback = response.getJSONObject("mealFeedback");
            tvOverallAssessment.setText(feedback.getString("overallAssessment"));
            tvEstimatedCalories.setText(feedback.getInt("estimatedCalories") + " cal");

            // Macro breakdown
            JSONObject macros = feedback.getJSONObject("macronutrientBreakdown");
            tvProtein.setText(UIHelper.capitalize(macros.getString("protein")));
            tvCarbs.setText(UIHelper.capitalize(macros.getString("carbs")));
            tvFats.setText(UIHelper.capitalize(macros.getString("fats")));

            // What is good
            UIHelper.populateBulletList(this, goodPointsContainer,
                    feedback.getJSONArray("whatIsGood"), "#4CAF50");

            // What is wrong
            UIHelper.populateBulletList(this, concernsContainer,
                    feedback.getJSONArray("whatIsWrong"), "#FF9800");

            // Parse improved meal
            JSONObject improved = response.getJSONObject("improvedMealRecommendation");
            tvMealName.setText(improved.getString("mealName"));
            tvImprovedCalories.setText(improved.getInt("totalEstimatedCalories") + " cal");
            tvPreparationTips.setText(improved.getString("preparationTips"));
            tvHowThisHelps.setText(improved.getString("howThisHelpsYourGoal"));

            // Food items
            UIHelper.populateFoodList(this, foodsListContainer,
                    improved.getJSONArray("foods"));

            // Key improvements
            UIHelper.populateBulletList(this, improvementsContainer,
                    improved.getJSONArray("keyImprovements"), "#2196F3");

            // Encouragement
            tvEncouragement.setText(response.getString("encouragement"));

            // Show results
            resultsContainer.setVisibility(View.VISIBLE);
            resultsContainer.requestFocus();

        } catch (Exception e) {
            Toast.makeText(this, "Error displaying results: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
