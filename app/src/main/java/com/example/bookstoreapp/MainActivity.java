package com.example.bookstoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.bookstoreapp.activities.AdminDashboardActivity;
import com.example.bookstoreapp.activities.CustomerDashboardActivity;
import com.example.bookstoreapp.database.DatabaseHelper;
import com.example.bookstoreapp.models.User;
import com.example.bookstoreapp.utils.SessionManager;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvTitle, tvSubtitle;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize components
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToDashboard();
            return;
        }

        // Initialize views
        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // Authenticate user
        User user = dbHelper.authenticateUser(username, password);

        progressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);

        if (user != null) {
            sessionManager.createLoginSession(user);
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            navigateToDashboard();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToDashboard() {
        if (sessionManager.isAdmin()) {
            startActivity(new Intent(this, AdminDashboardActivity.class));
        } else {
            startActivity(new Intent(this, CustomerDashboardActivity.class));
        }
        finish();
    }
}