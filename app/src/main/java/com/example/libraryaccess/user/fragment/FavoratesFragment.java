package com.example.libraryaccess.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;
import com.example.libraryaccess.user.adapter.FavoriteBookAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoratesFragment extends Fragment implements FavoriteBookAdapter.OnRemoveFavoriteClickListener {

    private RecyclerView recyclerViewFavoriteBooks;
    private FavoriteBookAdapter favoriteBookAdapter;
    private List<Book> favoriteBooks = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorates, container, false);
        recyclerViewFavoriteBooks = root.findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavoriteBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteBookAdapter = new FavoriteBookAdapter(getContext(), new ArrayList<Book>(), this);
        recyclerViewFavoriteBooks.setAdapter(favoriteBookAdapter);
        // Load favorite books from Firebase and pass them to the adapter
        // You need to implement this part to fetch favorite books from Firebase
        loadFavoriteBooksFromFirebase();
        return root;
    }



    // Method to load favorite books from Firebase
    private void loadFavoriteBooksFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites").child(userId);
            favoritesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    favoriteBooks.clear(); // Clear the current list of favorite books
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Book book = snapshot.getValue(Book.class);
                        favoriteBooks.add(book); // Add book to the list of favorite books
                    }
                    favoriteBookAdapter.setBooks(favoriteBooks); // Update the adapter with the new list
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors, if any
                }
            });
        }
    }



    @Override
    public void onRemoveFavoriteClick(int position) {
        if (favoriteBooks.size() > position) {
            // Get the book at the clicked position
            Book bookToRemove = favoriteBooks.get(position);

            // Remove the book from the RecyclerView and notify the adapter
            favoriteBooks.remove(position);
            favoriteBookAdapter.notifyDataSetChanged(); // Notify the adapter that the dataset has changed

            // Remove the book from Firebase database
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("favorites").child(userId);

                // Find the specific child node corresponding to the book to be removed and remove it
                favoritesRef.child(bookToRemove.getBookId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle success, if needed
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure, if needed
                    }
                });
            }
        }
    }


}
