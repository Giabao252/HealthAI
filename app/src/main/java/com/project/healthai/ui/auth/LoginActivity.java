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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.healthai.data.local.entities.User;
import com.project.healthai.ui.MainActivity;
import com.project.healthai.R;
import com.project.healthai.data.local.AppDatabase;
import com.project.healthai.ui.home.HomeActivity;
import com.project.healthai.utils.AuthContext;
import com.project.healthai.utils.RedirectionHelpers;

public class LoginActivity extends AppCompatActivity {
    private AuthContext authContext;

    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private AppDatabase db;
    private static final String TAG = "LoginActivity";
    private RedirectionHelpers redirectionHelper = new RedirectionHelpers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        authContext = AuthContext.getInstance(this);
        db = AppDatabase.getInstance(this);
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

        if (authContext.isUserLoggedIn()){
            redirectionHelper.redirect(this, HomeActivity.class);
        }
    }

    public void loginAccount() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        String userId = mAuth.getCurrentUser().getUid();

                        new Thread(() -> {
                            User user = db.userDao().getUserById(userId);

                            runOnUiThread(() -> {
                                if (user != null) {
                                    // User exists in database - proceed to home
                                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    authContext.refreshUserData();

                                    redirectionHelper.redirect(LoginActivity.this, HomeActivity.class);
                                    finish();
                                }
                            });
                        }).start();

                    } else {
                        // If sign in fails, display a message to the user
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        String errorMessage = task.getException() != null ?
                                task.getException().getMessage() : "Authentication failed";
                        Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
