package com.example.libraryaccess.user.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> favoriteBooksList;
    private OnRemoveFavoriteClickListener removeFavoriteClickListener;

    public interface OnRemoveFavoriteClickListener {
        void onRemoveFavoriteClick(int position);
    }

    public FavoriteBookAdapter(Context context, List<Book> favoriteBooksList, OnRemoveFavoriteClickListener listener) {
        this.context = context;
        this.favoriteBooksList = favoriteBooksList;
        this.removeFavoriteClickListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = favoriteBooksList.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return favoriteBooksList.size();
    }

    public void setFavoriteBooks(List<Book> favoriteBooks) {
        this.favoriteBooksList = favoriteBooks;
        notifyDataSetChanged();
    }

    public void clear() {
        favoriteBooksList.clear();
        notifyDataSetChanged();
    }
    public void addBook(Book book) {
        favoriteBooksList.add(book);
        notifyItemInserted(favoriteBooksList.size() - 1);
    }

    public void setBooks(List<Book> books) {
        this.favoriteBooksList = books;
        notifyDataSetChanged(); // Notify RecyclerView that dataset has changed
    }


    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewBook;
        TextView textViewBookTitle;
        TextView textViewAuthorName;
        TextView textViewCategory;
        Button btnRemoveFromFavorites;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBook = itemView.findViewById(R.id.imageViewBook);
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            textViewAuthorName = itemView.findViewById(R.id.textViewAuthorName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            btnRemoveFromFavorites = itemView.findViewById(R.id.btnRemoveFromFavorites);

            btnRemoveFromFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (removeFavoriteClickListener != null) {
                        removeFavoriteClickListener.onRemoveFavoriteClick(getAdapterPosition());
                    }
                }
            });
        }

        public void bind(final Book book) {
            // Load book image using Glide
            Glide.with(itemView.getContext())
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.default_book_cover) // Placeholder image while loading
                    .into(imageViewBook);

            textViewBookTitle.setText(book.getBookName());
            textViewAuthorName.setText(book.getAuthorName());
            textViewCategory.setText(book.getCategory());
        }


    }
}
