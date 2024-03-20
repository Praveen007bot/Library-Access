package com.example.libraryaccess.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.admin.adapter.ReservationRequestAdapter;
import com.example.libraryaccess.dataclass.ReservationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReservationRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationRequestAdapter adapter;
    private List<ReservationRequest> reservationRequests;

    // Database reference
    private DatabaseReference mDatabase;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_request);

        // Initialize the database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.reservationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ReservationRequestAdapter
        reservationRequests = new ArrayList<>();
        adapter = new ReservationRequestAdapter(this, reservationRequests);
        recyclerView.setAdapter(adapter);

        // Fetch reservation requests from Firebase
        fetchReservationRequests();
    }


    private void fetchReservationRequests() {
        DatabaseReference reservationsReference = FirebaseDatabase.getInstance().getReference().child("reservations");

        reservationsReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reservationRequests.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    for (DataSnapshot reservationSnapshot : userSnapshot.getChildren()) {
                        // Parse reservation data
                        String bookId = reservationSnapshot.child("bookId").getValue(String.class);
                        String startDate = reservationSnapshot.child("startDate").getValue(String.class);
                        String endDate = reservationSnapshot.child("endDate").getValue(String.class);
                        String timeSlot = reservationSnapshot.child("timeSlot").getValue(String.class);
                        String status = reservationSnapshot.child("status").getValue(String.class);

                        // Add reservation request only if the status is "pending"
                        if ("pending".equals(status)) {
                            // Get book details using book ID
                            DatabaseReference bookRef = mDatabase.child("books").child(bookId);
                            bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String bookName = dataSnapshot.child("bookName").getValue(String.class);
                                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                                        // Query the database to get the user's username
                                        DatabaseReference userRef = mDatabase.child("users").child(userId).child("userName");
                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String userName = dataSnapshot.getValue(String.class);

                                                    Log.d("Username", "Username: " + userName);
                                                    // Create ReservationRequest object
                                                    ReservationRequest reservationRequest = new ReservationRequest(userName, bookName, startDate, endDate, timeSlot, status, imageUrl, userId, bookId);
                                                    // Add reservation request to the list
                                                    reservationRequests.add(reservationRequest);
                                                    // Notify adapter that data set has changed
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    Log.d("Username", "Username data does not exist");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle errors
                                                Log.e("Firebase Error", "Error fetching username: " + databaseError.getMessage());
                                            }
                                        });
                                    } else {
                                        Log.d("Firebase Error", "Book details not found for book ID: " + bookId);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle errors
                                    Log.e("Firebase Error", "Error fetching book details: " + databaseError.getMessage());
                                }
                            });
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase Error", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }

}
