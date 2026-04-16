package com.example.bookstoreapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.activities.BookDetailActivity;
import com.example.bookstoreapp.models.Book;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> bookList;
    private boolean isAdminView;

    public BookAdapter(Context context, List<Book> bookList, boolean isAdminView) {
        this.context = context;
        this.bookList = bookList;
        this.isAdminView = isAdminView;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvPrice.setText("$" + String.format("%.2f", book.getPrice()));
        holder.tvStock.setText("Stock: " + book.getStock());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvAuthor, tvPrice, tvStock;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStock = itemView.findViewById(R.id.tvStock);
        }
    }
}