package com.example.bookstoreapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.adapters.PurchaseAdapter;
import com.example.bookstoreapp.database.DatabaseHelper;
import com.example.bookstoreapp.models.Purchase;
import com.example.bookstoreapp.utils.SessionManager;
import java.util.List;

public class PurchaseHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private PurchaseAdapter purchaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        // Initialize
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Purchase History");

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load purchases
        loadPurchases();
    }

    private void loadPurchases() {
        List<Purchase> purchases = dbHelper.getUserPurchases(sessionManager.getUserId());
        if (purchases.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            purchaseAdapter = new PurchaseAdapter(this, purchases, dbHelper);
            recyclerView.setAdapter(purchaseAdapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}