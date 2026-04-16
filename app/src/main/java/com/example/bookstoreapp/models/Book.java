package com.example.bookstoreapp.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private double price;
    private int stock;
    private String description;

    public Book() {}

    public Book(String title, String author, String isbn, double price, int stock, String description) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}