package com.project.healthai.utils;

import android.content.Context;
import android.content.Intent;
import android.os.UserManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.healthai.data.local.AppDatabase;
import com.project.healthai.data.local.entities.User;
import com.project.healthai.ui.auth.LoginActivity;

public class AuthContext {
    private static AuthContext instance;
    private FirebaseAuth mAuth;
    private AppDatabase database;
    private User currentUser;
    private Context appContext;

    private AuthContext(Context context) {
        this.appContext = context.getApplicationContext();
        this.mAuth = FirebaseAuth.getInstance();
        this.database = AppDatabase.getInstance(appContext);
        loadCurrentUser();
    }

    public static synchronized AuthContext getInstance(Context context) {
        if (instance == null) {
            instance = new AuthContext(context);
        }
        return instance;
    }

    // Load user from Room database in background thread if logged in user is detected
    private void loadCurrentUser() {
        new Thread(() -> {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (firebaseUser != null) {
                currentUser = database.userDao().getUserById(firebaseUser.getUid());
            } else {
                currentUser = null;
            }
        }).start();
    }

    // Get current user (synchronous - for immediate use)
    public User getCurrentUser() {
        return currentUser;
    }

    // Get current user from database (async with callback)
    public void getCurrentUserAsync(UserCallback callback) {
        new Thread(() -> {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (firebaseUser != null) {
                User user = database.userDao().getUserById(firebaseUser.getUid());
                // Run callback on main thread
                if (callback != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        callback.onUserLoaded(user);
                    });
                }
            } else {
                if (callback != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        callback.onUserLoaded(null);
                    });
                }
            }
        }).start();
    }

    // Check if user is logged in
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    // Get Firebase User ID
    public String getUserId() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : null;
    }

    // Update current user in memory and database
    public void updateCurrentUser(User user) {
        this.currentUser = user;
        new Thread(() -> {
            database.userDao().update(user);
        }).start();
    }

    // Sign out user
    public void signOut(Context context) {
        mAuth.signOut();
        currentUser = null;

        // Navigate to login
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    // Refresh user data from database
    public void refreshUserData() {
        loadCurrentUser();
    }

    // Callback interface for async operations
    public interface UserCallback {
        void onUserLoaded(User user);
    }
}
