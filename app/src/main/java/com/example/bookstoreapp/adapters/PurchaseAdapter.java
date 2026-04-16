package com.example.bookstoreapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookstoreapp.R;
import com.example.bookstoreapp.database.DatabaseHelper;
import com.example.bookstoreapp.models.Book;
import com.example.bookstoreapp.models.Purchase;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
    private Context context;
    private List<Purchase> purchaseList;
    private DatabaseHelper dbHelper;

    public PurchaseAdapter(Context context, List<Purchase> purchaseList, DatabaseHelper dbHelper) {
        this.context = context;
        this.purchaseList = purchaseList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_purchase, parent, false);
        return new PurchaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        Purchase purchase = purchaseList.get(position);
        Book book = dbHelper.getBookById(purchase.getBookId());

        if (book != null) {
            holder.tvBookTitle.setText(book.getTitle());
            holder.tvAuthor.setText("by " + book.getAuthor());
        }

        holder.tvQuantity.setText("Quantity: " + purchase.getQuantity());
        holder.tvTotalPrice.setText("Total: $" + String.format("%.2f", purchase.getTotalPrice()));
        holder.tvDate.setText("Date: " + purchase.getPurchaseDate());
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    static class PurchaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvAuthor, tvQuantity, tvTotalPrice, tvDate;

        PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}