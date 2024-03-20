package com.example.libraryaccess.user.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;
import com.example.libraryaccess.user.BookDetailActivity;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private Context context;

    public BookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Bind book data to UI components in the ViewHolder
        Book book = bookList.get(position);
        holder.bind(book);


        // Set OnClickListener for the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open BookDetailActivity when a book item is clicked
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BookDetailActivity.EXTRA_BOOK, book);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        // UI components for book details
        private ImageView ivBookImage;
        private TextView tvAuthorName;

        private TextView tvQuantity;
        private TextView tvBookName;
        private TextView tvCategory;


        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize UI components from item_book.xml
            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);

            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvCategory = itemView.findViewById(R.id.tvCategory);

        }

        public void bind(Book book) {
            // Bind book data to UI components
            // Example:
            Glide.with(context).load(book.getImageUrl()).into(ivBookImage);
            tvAuthorName.setText(book.getAuthorName());

            tvQuantity.setText(String.valueOf(book.getQuantity()));
            tvBookName.setText(String.valueOf(book.getBookName()));
            tvCategory.setText(book.getCategory());
        }
    }
}

