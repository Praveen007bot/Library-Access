package com.example.libraryaccess.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.libraryaccess.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.dataclass.ReservedBook;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReservedBookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservedBookAdapter adapter;
    private List<ReservedBook> reservedBooks;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_book);
        setupDatabaseListener();

        // Change this line where you find the RecyclerView
        recyclerView = findViewById(R.id.myRecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reservedBooks = new ArrayList<>();
        adapter = new ReservedBookAdapter(reservedBooks);
        recyclerView.setAdapter(adapter);

        DatabaseReference reservedBooksRef = FirebaseDatabase.getInstance().getReference().child("reservedbooks");
        Query query = reservedBooksRef.orderByChild("remainingDays").equalTo(0);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reservedBooks.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReservedBook reservedBook = snapshot.getValue(ReservedBook.class);
                    if (reservedBook != null) {
                        reservedBooks.add(reservedBook);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservedBookActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupDatabaseListener() {
        DatabaseReference reservedBooksRef = FirebaseDatabase.getInstance().getReference().child("reservedbooks");

        reservedBooksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReservedBook reservedBook = snapshot.getValue(ReservedBook.class);
                    if (reservedBook != null) {
                        // Recalculate fine and remaining days
                        reservedBook.updateFineAndRemainingDays();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private class ReservedBookAdapter extends RecyclerView.Adapter<ReservedBookAdapter.ReservedBookViewHolder> {
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
            TextView bookIdTextView, examDateTextView, fineTextView, remainingDaysTextView;
            Button postFineButton;

            public ReservedBookViewHolder(@NonNull View itemView) {
                super(itemView);
                bookIdTextView = itemView.findViewById(R.id.bookIdTextView);
                examDateTextView = itemView.findViewById(R.id.examDateTextView);
                fineTextView = itemView.findViewById(R.id.fineTextView);
                remainingDaysTextView = itemView.findViewById(R.id.remainingDaysTextView);
                postFineButton = itemView.findViewById(R.id.postFineButton);
            }

            public void bind(final ReservedBook reservedBook) {
                bookIdTextView.setText(reservedBook.getBookId());
                examDateTextView.setText(reservedBook.getEndDate());
                fineTextView.setText(String.valueOf(reservedBook.getFine()));
                remainingDaysTextView.setText(String.valueOf(reservedBook.getRemainingDays()));

                if (reservedBook.getRemainingDays() == 0) {
                    postFineButton.setVisibility(View.VISIBLE);
                    postFineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Post fine implementation here
                            // You can use reservedBook.getUserId() and other relevant information
                            Toast.makeText(ReservedBookActivity.this, "Fine posted", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    postFineButton.setVisibility(View.GONE);
                }
            }
        }
    }
}
