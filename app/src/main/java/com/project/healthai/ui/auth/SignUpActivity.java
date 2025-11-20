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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.healthai.ui.MainActivity;
import com.project.healthai.R;
import com.project.healthai.data.local.AppDatabase;
import com.project.healthai.data.local.entities.User;

import com.project.healthai.utils.AuthContext;
import com.project.healthai.utils.RedirectionHelpers;

public class SignUpActivity extends AppCompatActivity {
    private AuthContext authContext;
    private FirebaseAuth mAuth;
    private AppDatabase db;
    private EditText inputUsername, inputEmail, inputPassword;
    private static final String TAG = "SignUpActivity";
    private RedirectionHelpers redirection = new RedirectionHelpers();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        db = AppDatabase.getInstance(this);
        authContext = AuthContext.getInstance(this);

        inputUsername = findViewById(R.id.usernameInputField);
        inputEmail = findViewById(R.id.emailInputField);
        inputPassword = findViewById(R.id.passwordInputField);

        findViewById(R.id.signupButton).setOnClickListener(v -> createAccount());

        TextView signupRedirection = findViewById(R.id.loginRedirect);

        signupRedirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    public void createAccount() {
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            // Thread to save the new user in the Room database
                            new Thread(() -> {
                                User newUser = new User(userId, username, email);
                                db.userDao().insert(newUser);

                                runOnUiThread(() -> {
                                    Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    redirection.redirect(SignUpActivity.this, LoginActivity.class);
                                    finish();
                                });
                            }).start();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
