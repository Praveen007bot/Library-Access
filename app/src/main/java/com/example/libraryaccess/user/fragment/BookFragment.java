package com.example.libraryaccess.user.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;
import com.example.libraryaccess.user.adapter.BookAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    private RecyclerView recyclerViewBooks;
    private BookAdapter bookAdapter;

    public BookFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        recyclerViewBooks = view.findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Fetch book data from Firebase
        fetchBookData();

        return view;
    }

    private void fetchBookData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Add a listener to fetch data once from the database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Book> bookList = new ArrayList<>();

                // Iterate through each book node in the database
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    // Convert the dataSnapshot to a Book object
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }

                // Create and set the adapter with the retrieved book data
                bookAdapter = new BookAdapter(bookList, getContext());
                recyclerViewBooks.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}
