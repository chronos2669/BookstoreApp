package com.example.bookstoreapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstoreapp.MainActivity;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.adapters.BookAdapter;
import com.example.bookstoreapp.database.DatabaseHelper;
import com.example.bookstoreapp.models.Book;
import com.example.bookstoreapp.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    private TextView tvWelcome, tvTotalBooks;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddBook;
    private CardView cardStats;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Admin Dashboard");

        // Initialize views
        initViews();
        loadBooks();
        updateStats();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalBooks = findViewById(R.id.tvTotalBooks);
        recyclerView = findViewById(R.id.recyclerView);
        fabAddBook = findViewById(R.id.fabAddBook);
        cardStats = findViewById(R.id.cardStats);

        // Set welcome message
        tvWelcome.setText("Welcome, " + sessionManager.getUserDetails().getUsername());

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup FAB
        fabAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddBookDialog();
            }
        });
    }

    private void loadBooks() {
        bookList = dbHelper.getAllBooks();
        bookAdapter = new BookAdapter(this, bookList, true); // true for admin view
        recyclerView.setAdapter(bookAdapter);
    }

    private void updateStats() {
        int totalBooks = bookList.size();
        int totalStock = 0;
        for (Book book : bookList) {
            totalStock += book.getStock();
        }
        tvTotalBooks.setText("Total Books: " + totalBooks + " | Total Stock: " + totalStock);
    }

    private void showAddBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_book, null);
        builder.setView(dialogView);

        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etAuthor = dialogView.findViewById(R.id.etAuthor);
        EditText etIsbn = dialogView.findViewById(R.id.etIsbn);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        EditText etStock = dialogView.findViewById(R.id.etStock);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);

        builder.setTitle("Add New Book");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String isbn = etIsbn.getText().toString().trim();
                String priceStr = etPrice.getText().toString().trim();
                String stockStr = etStock.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() ||
                        priceStr.isEmpty() || stockStr.isEmpty()) {
                    Toast.makeText(AdminDashboardActivity.this,
                            "Please fill all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double price = Double.parseDouble(priceStr);
                    int stock = Integer.parseInt(stockStr);

                    Book book = new Book(title, author, isbn, price, stock, description);
                    long result = dbHelper.addBook(book);

                    if (result != -1) {
                        Toast.makeText(AdminDashboardActivity.this,
                                "Book added successfully", Toast.LENGTH_SHORT).show();
                        loadBooks();
                        updateStats();
                    } else {
                        Toast.makeText(AdminDashboardActivity.this,
                                "Failed to add book", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(AdminDashboardActivity.this,
                            "Invalid price or stock value", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
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
}