package com.project.healthai.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.healthai.MainActivity;
import com.project.healthai.R;
import com.project.healthai.data.local.AppDatabase;
import com.project.healthai.ui.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private AppDatabase db;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.emailInputField);
        inputPassword = findViewById(R.id.passwordInputField);

        findViewById(R.id.loginButton).setOnClickListener(v -> loginAccount());

        TextView signupRedirection = findViewById(R.id.signupRedirect);

        signupRedirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMain();
        }
    }

    public void loginAccount() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            navigateToHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
