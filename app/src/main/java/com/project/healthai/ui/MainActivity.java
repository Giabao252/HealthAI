package com.project.healthai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.project.healthai.R;
import com.project.healthai.ui.auth.SignUpActivity;
import com.project.healthai.ui.home.HomeActivity;
import com.project.healthai.utils.AuthContext;
import com.project.healthai.utils.RedirectionHelpers;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AuthContext currentUser;
    private RedirectionHelpers redirectionHelper = new RedirectionHelpers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth.getInstance().signOut(); // Force sign out for testing

        FirebaseApp.initializeApp(this);
    }

    public void buttonClick(View view) {
        Intent registrationLaunch = new Intent(this, SignUpActivity.class);
        startActivity(registrationLaunch);
    }
}