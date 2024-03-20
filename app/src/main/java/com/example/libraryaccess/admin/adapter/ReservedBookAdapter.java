package com.example.libraryaccess.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.ReservedBook;

import java.util.List;

public class ReservedBookAdapter extends RecyclerView.Adapter<ReservedBookAdapter.ReservedBookViewHolder> {
    private List<ReservedBook> reservedBooks;

    public ReservedBookAdapter(List<ReservedBook> reservedBooks) {
        this.reservedBooks = reservedBooks;
    }

    @NonNull
    @Override
    public ReservedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserved_book, parent, false);
        return new ReservedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservedBookViewHolder holder, int position) {
        ReservedBook reservedBook = reservedBooks.get(position);
        holder.bind(reservedBook);
    }

    @Override
    public int getItemCount() {
        return reservedBooks.size();
    }

    public class ReservedBookViewHolder extends RecyclerView.ViewHolder {
        TextView bookIdTextView, fineTextView, remainingDaysTextView;

        public ReservedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookIdTextView = itemView.findViewById(R.id.bookIdTextView);

            fineTextView = itemView.findViewById(R.id.fineTextView);
            remainingDaysTextView = itemView.findViewById(R.id.remainingDaysTextView);
        }

        public void bind(final ReservedBook reservedBook) {
            bookIdTextView.setText(reservedBook.getBookId());

            fineTextView.setText(String.valueOf(reservedBook.getFine()));
            remainingDaysTextView.setText(String.valueOf(reservedBook.getRemainingDays()));
        }
    }
}
