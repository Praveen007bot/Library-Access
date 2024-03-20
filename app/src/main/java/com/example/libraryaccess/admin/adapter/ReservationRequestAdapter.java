package com.example.libraryaccess.admin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;
import com.example.libraryaccess.dataclass.ReservationRequest;
import com.example.libraryaccess.dataclass.ReservedBook;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReservationRequestAdapter extends RecyclerView.Adapter<ReservationRequestAdapter.ViewHolder> {

    private final List<ReservationRequest> reservationRequests;
    private Context context;

    public ReservationRequestAdapter(Context context, List<ReservationRequest> reservationRequests) {
        this.context = context;
        this.reservationRequests = reservationRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservationRequest reservationRequest = reservationRequests.get(position);



        // Display the details of the reservation request
        holder.textViewUserName.setText(reservationRequest.getUserName());
        holder.textViewBookName.setText(reservationRequest.getBookName());
        holder.textViewStartDate.setText(reservationRequest.getStartDate());
        holder.textViewEndDate.setText(reservationRequest.getEndDate());
        holder.textViewTimeSlot.setText(reservationRequest.getTimeSlot());
        holder.textViewStatus.setText(reservationRequest.getStatus());

        // You can load the book image using Picasso or any other image loading library
        // Here, we assume the imageUrl is available in the ReservationRequest object
        String imageUrl = reservationRequest.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.default_book_cover).into(holder.imageViewBook);
        }

        // Set click listeners for accept and reject buttons if needed
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve reservation details
                String userId = reservationRequest.getUserId();
                String bookId = reservationRequest.getBookId();
                String startDate = reservationRequest.getStartDate();
                String endDate = reservationRequest.getEndDate();

                // Calculate remaining days and fine (you need to implement this logic)

                // Add the book to the reservedbooks database
                addBookToReservedBooks(userId, bookId, startDate, endDate);
                acceptReservation(userId, bookId, startDate, endDate);

                // Optionally, update the UI or perform any other necessary actions
            }
        });


        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reject button click
            }
        });
    }


    private void acceptReservation(String userId, String bookId, String startDate, String endDate) {
        // Assuming you have a reference to the reservations node in the Firebase database
        DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference().child("reservations");

        // Query the reservation that needs to be accepted
        Query query = reservationsRef.child(userId).orderByChild("bookId").equalTo(bookId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot reservationSnapshot : dataSnapshot.getChildren()) {
                    // Update the status to "accepted"
                    reservationSnapshot.getRef().child("status").setValue("accepted");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase Error", "Error accepting reservation: " + databaseError.getMessage());
            }
        });
    }


    private void addBookToReservedBooks(String userId, String bookId, String startDate, String endDate) {
        int remainingDays = calculateRemainingDays(startDate, endDate);
        double fine = 0;
        // Assuming you have a DatabaseReference for reservedbooks
        DatabaseReference reservedBooksRef = FirebaseDatabase.getInstance().getReference().child("reservedbooks");

        // Create a new entry in the reservedbooks database
        String reservedBookId = reservedBooksRef.push().getKey();
        ReservedBook reservedBook = new ReservedBook(userId, bookId, startDate, endDate, remainingDays, fine);
        reservedBooksRef.child(reservedBookId).setValue(reservedBook)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Book added successfully
                            Log.d("Reservation", "Book added to reservedbooks");
                        } else {
                            // Error adding book
                            Log.e("Reservation", "Error adding book to reservedbooks: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public static int calculateRemainingDays(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            long difference = end.getTime() - start.getTime();
            return (int) (difference / (1000 * 60 * 60 * 24)); // Convert milliseconds to days
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if there's an error in parsing dates
        }
    }


    @Override
    public int getItemCount() {
        return reservationRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewBook;
        TextView textViewUserName;
        TextView textViewBookName;
        TextView textViewStartDate;
        TextView textViewEndDate;
        TextView textViewTimeSlot;
        TextView textViewStatus;
        Button btnAccept;
        Button btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBook = itemView.findViewById(R.id.imageViewBook);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewBookName = itemView.findViewById(R.id.textViewBookName);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);
            textViewTimeSlot = itemView.findViewById(R.id.textViewTimeSlot);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
