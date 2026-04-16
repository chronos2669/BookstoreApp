package com.example.bookstoreapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.bookstoreapp.models.Book;
import com.example.bookstoreapp.models.Purchase;
import com.example.bookstoreapp.models.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_BOOKS = "books";
    private static final String TABLE_PURCHASES = "purchases";

    // User table columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ROLE = "role";
    private static final String KEY_EMAIL = "email";

    // Book table columns
    private static final String KEY_BOOK_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_ISBN = "isbn";
    private static final String KEY_PRICE = "price";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_DESCRIPTION = "description";

    // Purchase table columns
    private static final String KEY_PURCHASE_ID = "id";
    private static final String KEY_USER_ID_FK = "user_id";
    private static final String KEY_BOOK_ID_FK = "book_id";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_PURCHASE_DATE = "purchase_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_ROLE + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create books table
        String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_BOOKS + "("
                + KEY_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_AUTHOR + " TEXT,"
                + KEY_ISBN + " TEXT UNIQUE,"
                + KEY_PRICE + " REAL,"
                + KEY_STOCK + " INTEGER,"
                + KEY_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_BOOKS_TABLE);

        // Create purchases table
        String CREATE_PURCHASES_TABLE = "CREATE TABLE " + TABLE_PURCHASES + "("
                + KEY_PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID_FK + " INTEGER,"
                + KEY_BOOK_ID_FK + " INTEGER,"
                + KEY_QUANTITY + " INTEGER,"
                + KEY_TOTAL_PRICE + " REAL,"
                + KEY_PURCHASE_DATE + " TEXT,"
                + "FOREIGN KEY(" + KEY_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "),"
                + "FOREIGN KEY(" + KEY_BOOK_ID_FK + ") REFERENCES " + TABLE_BOOKS + "(" + KEY_BOOK_ID + ")" + ")";
        db.execSQL(CREATE_PURCHASES_TABLE);

        // Insert default admin and customer accounts
        insertDefaultUsers(db);
        // Insert sample books
        insertSampleBooks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertDefaultUsers(SQLiteDatabase db) {
        ContentValues adminValues = new ContentValues();
        adminValues.put(KEY_USERNAME, "admin");
        adminValues.put(KEY_PASSWORD, "admin123");
        adminValues.put(KEY_ROLE, "admin");
        adminValues.put(KEY_EMAIL, "admin@bookstore.com");
        db.insert(TABLE_USERS, null, adminValues);

        ContentValues customerValues = new ContentValues();
        customerValues.put(KEY_USERNAME, "customer");
        customerValues.put(KEY_PASSWORD, "customer123");
        customerValues.put(KEY_ROLE, "customer");
        customerValues.put(KEY_EMAIL, "customer@bookstore.com");
        db.insert(TABLE_USERS, null, customerValues);
    }

    private void insertSampleBooks(SQLiteDatabase db) {
        String[][] books = {
                {"The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", "12.99", "15", "A classic American novel"},
                {"To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", "14.99", "20", "A gripping tale of racial injustice"},
                {"1984", "George Orwell", "978-0-452-28423-4", "13.99", "25", "A dystopian social science fiction novel"},
                {"Pride and Prejudice", "Jane Austen", "978-0-14-143951-8", "11.99", "18", "A romantic novel of manners"},
                {"The Catcher in the Rye", "J.D. Salinger", "978-0-316-76948-0", "15.99", "12", "A story about teenage rebellion"}
        };

        for (String[] book : books) {
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, book[0]);
            values.put(KEY_AUTHOR, book[1]);
            values.put(KEY_ISBN, book[2]);
            values.put(KEY_PRICE, book[3]);
            values.put(KEY_STOCK, book[4]);
            values.put(KEY_DESCRIPTION, book[5]);
            db.insert(TABLE_BOOKS, null, values);
        }
    }

    // User operations
    public User authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_ID, KEY_USERNAME, KEY_PASSWORD, KEY_ROLE, KEY_EMAIL},
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setRole(cursor.getString(3));
            user.setEmail(cursor.getString(4));
        }
        if (cursor != null) cursor.close();
        return user;
    }

    // Book operations
    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, book.getTitle());
        values.put(KEY_AUTHOR, book.getAuthor());
        values.put(KEY_ISBN, book.getIsbn());
        values.put(KEY_PRICE, book.getPrice());
        values.put(KEY_STOCK, book.getStock());
        values.put(KEY_DESCRIPTION, book.getDescription());
        return db.insert(TABLE_BOOKS, null, values);
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getInt(0));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setIsbn(cursor.getString(3));
                book.setPrice(cursor.getDouble(4));
                book.setStock(cursor.getInt(5));
                book.setDescription(cursor.getString(6));
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookList;
    }

    public Book getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS,
                new String[]{KEY_BOOK_ID, KEY_TITLE, KEY_AUTHOR, KEY_ISBN, KEY_PRICE, KEY_STOCK, KEY_DESCRIPTION},
                KEY_BOOK_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        Book book = null;
        if (cursor != null && cursor.moveToFirst()) {
            book = new Book();
            book.setId(cursor.getInt(0));
            book.setTitle(cursor.getString(1));
            book.setAuthor(cursor.getString(2));
            book.setIsbn(cursor.getString(3));
            book.setPrice(cursor.getDouble(4));
            book.setStock(cursor.getInt(5));
            book.setDescription(cursor.getString(6));
        }
        if (cursor != null) cursor.close();
        return book;
    }

    public int updateBookStock(int bookId, int newStock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STOCK, newStock);
        return db.update(TABLE_BOOKS, values, KEY_BOOK_ID + " = ?",
                new String[]{String.valueOf(bookId)});
    }

    // Purchase operations
    public long addPurchase(Purchase purchase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID_FK, purchase.getUserId());
        values.put(KEY_BOOK_ID_FK, purchase.getBookId());
        values.put(KEY_QUANTITY, purchase.getQuantity());
        values.put(KEY_TOTAL_PRICE, purchase.getTotalPrice());
        values.put(KEY_PURCHASE_DATE, purchase.getPurchaseDate());
        return db.insert(TABLE_PURCHASES, null, values);
    }

    public boolean purchaseBook(int userId, int bookId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Get book details
            Book book = getBookById(bookId);
            if (book == null || book.getStock() < quantity) {
                return false;
            }

            // Update stock
            int newStock = book.getStock() - quantity;
            updateBookStock(bookId, newStock);

            // Create purchase record
            double totalPrice = book.getPrice() * quantity;
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Purchase purchase = new Purchase(userId, bookId, quantity, totalPrice, currentDate);
            addPurchase(purchase);

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public List<Purchase> getUserPurchases(int userId) {
        List<Purchase> purchases = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PURCHASES,
                new String[]{KEY_PURCHASE_ID, KEY_USER_ID_FK, KEY_BOOK_ID_FK, KEY_QUANTITY, KEY_TOTAL_PRICE, KEY_PURCHASE_DATE},
                KEY_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)}, null, null, KEY_PURCHASE_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Purchase purchase = new Purchase();
                purchase.setId(cursor.getInt(0));
                purchase.setUserId(cursor.getInt(1));
                purchase.setBookId(cursor.getInt(2));
                purchase.setQuantity(cursor.getInt(3));
                purchase.setTotalPrice(cursor.getDouble(4));
                purchase.setPurchaseDate(cursor.getString(5));
                purchases.add(purchase);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return purchases;
    }
}