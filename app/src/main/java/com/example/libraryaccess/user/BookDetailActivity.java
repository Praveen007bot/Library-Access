package com.example.libraryaccess.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookDetailActivity extends AppCompatActivity {

    public static final String EXTRA_BOOK = "extra_book";

    private ImageView ivBookImage;
    private TextView tvAuthorName;
    private TextView tvBookDescription;
    private TextView tvQuantity;
    private TextView tvBookName;
    private Button btnAddToFavorites;
    private Button btnReserveBook;
    private TextView tvCategory;

    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Get the selected book from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_BOOK)) {
            selectedBook = intent.getParcelableExtra(EXTRA_BOOK);
        }

        // Initialize UI components
        ivBookImage = findViewById(R.id.ivBookImage);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvBookDescription = findViewById(R.id.tvBookDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvBookName = findViewById(R.id.tvBookName);
        btnAddToFavorites = findViewById(R.id.btnAddToFavorites);
        tvCategory = findViewById(R.id.tvCategory);
        btnReserveBook = findViewById(R.id.btnReserveBook);

        // Set book details
        if (selectedBook != null) {
            Glide.with(this).load(selectedBook.getImageUrl()).into(ivBookImage);
            tvAuthorName.setText(selectedBook.getAuthorName());
            tvBookDescription.setText(selectedBook.getBookDescription());
            tvQuantity.setText(String.valueOf(selectedBook.getQuantity()));
            tvBookName.setText(String.valueOf(selectedBook.getBookName()));
            tvCategory.setText(selectedBook.getCategory());
        }

        // Set click listener for "Add to Favorites" button
        btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add the selected book to favorites
                addToFavorites(selectedBook);
            }
        });
        btnReserveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ReservationActivity to reserve the book
                Intent reserveIntent = new Intent(BookDetailActivity.this, ReserveBookActivity.class);
                reserveIntent.putExtra(ReserveBookActivity.EXTRA_BOOK, selectedBook);
                startActivity(reserveIntent);
            }
        });
    }

    private void addToFavorites(Book book) {
        // Get the current user ID (assuming you're using Firebase Authentication)
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the "favorites" node in the Firebase Realtime Database
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites").child(userId);

        // Add the selected book to the user's favorites
        favoritesRef.child(book.getBookId()).setValue(book);

        // Inform the user that the book has been added to favorites
        // You can display a Toast or show a Snackbar here
    }
}
