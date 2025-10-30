package com.project.healthai.ui.home;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.project.healthai.R;
import com.project.healthai.utils.NavigationDropDown;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Spinner menuInHome = findViewById(R.id.navigation_spinner);
        NavigationDropDown.setupNavigationSpinner(this, menuInHome, "Home");
    }
}
