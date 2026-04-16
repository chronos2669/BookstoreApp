package com.example.bookstoreapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.database.DatabaseHelper;
import com.example.bookstoreapp.models.Book;
import com.example.bookstoreapp.utils.SessionManager;

public class BookDetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvAuthor, tvIsbn, tvPrice, tvStock, tvDescription;
    private EditText etQuantity;
    private Button btnPurchase;
    private Book book;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Initialize
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get book ID from intent
        int bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId == -1) {
            finish();
            return;
        }

        // Load book details
        book = dbHelper.getBookById(bookId);
        if (book == null) {
            finish();
            return;
        }

        // Initialize views
        initViews();
        displayBookDetails();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvIsbn = findViewById(R.id.tvIsbn);
        tvPrice = findViewById(R.id.tvPrice);
        tvStock = findViewById(R.id.tvStock);
        tvDescription = findViewById(R.id.tvDescription);
        etQuantity = findViewById(R.id.etQuantity);
        btnPurchase = findViewById(R.id.btnPurchase);

        // Hide purchase option for admin
        if (sessionManager.isAdmin()) {
            findViewById(R.id.purchaseContainer).setVisibility(View.GONE);
        }

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePurchase();
            }
        });
    }

    private void displayBookDetails() {
        setTitle(book.getTitle());
        tvTitle.setText(book.getTitle());
        tvAuthor.setText("by " + book.getAuthor());
        tvIsbn.setText("ISBN: " + book.getIsbn());
        tvPrice.setText("$" + String.format("%.2f", book.getPrice()));
        tvStock.setText("Stock: " + book.getStock());
        tvDescription.setText(book.getDescription());
    }

    private void handlePurchase() {
        String quantityStr = etQuantity.getText().toString().trim();
        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantity > book.getStock()) {
                Toast.makeText(this, "Not enough stock available", Toast.LENGTH_SHORT).show();
                return;
            }

            // Process purchase
            boolean success = dbHelper.purchaseBook(sessionManager.getUserId(), book.getId(), quantity);
            if (success) {
                Toast.makeText(this, "Purchase successful!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Purchase failed", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}