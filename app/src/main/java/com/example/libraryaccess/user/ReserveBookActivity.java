package com.example.libraryaccess.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;
import com.example.libraryaccess.dataclass.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ReserveBookActivity extends AppCompatActivity {
    public static final String EXTRA_BOOK = "extra_book";

    // Declare variables for UI components
    private Button btnSelectStartDate;
    private Button btnSelectEndDate;
    private TextView tvStartDate;
    private TextView tvEndDate;

    // Variables for storing selected dates
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private Spinner spinnerTimeSlot;
    private Book selectedBook;
    private Button btnReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_book);

        // Initialize UI components
        btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        tvStartDate = findViewById(R.id.tvStartDateLabel);
        tvEndDate = findViewById(R.id.tvEndDateLabel);
        spinnerTimeSlot = findViewById(R.id.spinnerTimeSlot);
        btnReserve = findViewById(R.id.btnReserve);

        // Initialize Calendar instances
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_BOOK)) {
            selectedBook = intent.getParcelableExtra(EXTRA_BOOK);
        }

        // Set click listeners for date selection buttons
        btnSelectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(startDateCalendar, tvStartDate);
            }
        });

        btnSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(endDateCalendar, tvEndDate);
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveBook();
            }
        });

        String[] timeSlots = {"9:00 AM - 11:00 AM", "11:00 AM - 1:00 PM", "1:00 PM - 3:00 PM", "3:00 PM - 5:00 PM"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlot.setAdapter(adapter);

        // Set listener for spinner item selection
        spinnerTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle selection here if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection here if needed
            }
        });
    }

    private void showDatePicker(final Calendar calendar, final TextView textView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Update the text view with the selected date
                        textView.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },
                year, month, dayOfMonth
        );
        datePickerDialog.show();
    }


    private void reserveBook() {
        // Get selected time slot
        String selectedTimeSlot = spinnerTimeSlot.getSelectedItem().toString();

        // Get start date and end date
        String startDate = tvStartDate.getText().toString();
        String endDate = tvEndDate.getText().toString();

        // Get current user ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference().child("reservations").child(userId);

            // Check if the selected book is already reserved by the current user
            reservationsRef.orderByChild("bookId").equalTo(selectedBook.getBookId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Book already reserved by the user
                        Toast.makeText(ReserveBookActivity.this, "Book already reserved", Toast.LENGTH_SHORT).show();
                    } else {
                        // Book not yet reserved, proceed with reservation
                        // Create a unique reservation ID
                        String reservationId = reservationsRef.push().getKey();

                        // Create a reservation object
                        Reservation reservation = new Reservation(selectedBook.getBookId(), startDate, endDate, selectedTimeSlot, "pending");

                        // Add the reservation to the database
                        if (reservationId != null) {
                            reservationsRef.child(reservationId).setValue(reservation);
                            Toast.makeText(ReserveBookActivity.this, "Book successfully reserved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Toast.makeText(ReserveBookActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
