package com.example.bookstoreapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstoreapp.MainActivity;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.adapters.BookAdapter;
import com.example.bookstoreapp.database.DatabaseHelper;
import com.example.bookstoreapp.models.Book;
import com.example.bookstoreapp.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class CustomerDashboardActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigation;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        // Initialize
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Book Store");

        // Initialize views
        initViews();
        loadBooks();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Set welcome message
        tvWelcome.setText("Welcome, " + sessionManager.getUserDetails().getUsername());

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup Bottom Navigation
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_books) {
                // Already on books page
                return true;
            } else if (itemId == R.id.nav_purchases) {
                startActivity(new Intent(this, PurchaseHistoryActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadBooks() {
        bookList = dbHelper.getAllBooks();
        bookAdapter = new BookAdapter(this, bookList, false); // false for customer view
        recyclerView.setAdapter(bookAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {
            sessionManager.logout();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.action_theme) {
            // Toggle between light and dark mode
            int currentNightMode = getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // Recreate activity to apply theme
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadBooks(); // Reload books to update stock
    }
}