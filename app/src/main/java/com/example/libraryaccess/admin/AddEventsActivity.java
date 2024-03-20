package com.example.libraryaccess.admin;

import static com.google.common.io.Files.getFileExtension;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class AddEventsActivity extends AppCompatActivity {

    private EditText editTextEventName, editTextEventDate, editTextOrganizerName, editTextOrganizerNumber;
    private ImageView imageViewEventPoster;
    private Button buttonAddEvent;

    private DatabaseReference eventsRef;
    private Uri selectedImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        // Initialize Views
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        editTextEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        editTextOrganizerName = findViewById(R.id.editTextOrganizerName);
        editTextOrganizerNumber = findViewById(R.id.editTextOrganizerNumber);
        imageViewEventPoster = findViewById(R.id.imageViewEventPoster);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);


        // Set OnClickListener for ImageView to open file chooser
        imageViewEventPoster.setOnClickListener(v -> openFileChooser());

        // Firebase Database reference
        eventsRef = FirebaseDatabase.getInstance().getReference().child("events");

        // Set OnClickListener for Add Event Button
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventToDatabase();
            }
        });
    }

    // Method to open file chooser to select image
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    // Method to handle result of file chooser
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData(); // Initialize selectedImageUri with the selected image URI
            // Load the selected image into ImageView using Glide
            Glide.with(this).load(selectedImageUri).into(imageViewEventPoster);
        }
    }

    // Method to add event to database
    private void addEventToDatabase() {
        // Get input values
        String eventName = editTextEventName.getText().toString().trim();
        String eventDate = editTextEventDate.getText().toString().trim();
        String organizerName = editTextOrganizerName.getText().toString().trim();
        String organizerNumber = editTextOrganizerNumber.getText().toString().trim();

        // Validate inputs
        if (eventName.isEmpty() || eventDate.isEmpty() || organizerName.isEmpty() || organizerNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if an image is selected
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an event poster", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a reference to the Firebase Storage location to store the image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("event_posters");
        StorageReference imageRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(String.valueOf(selectedImageUri)));

        // Upload image to Firebase Storage
        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Create Event object with poster URL
                        Event event = new Event(eventName, eventDate, organizerName, organizerNumber, uri.toString());

                        // Push the event data (including poster URL) to the database
                        eventsRef.push().setValue(event)
                                .addOnSuccessListener(aVoid -> {
                                    // Show success message
                                    Toast.makeText(AddEventsActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();

                                    // Clear input fields
                                    editTextEventName.setText("");
                                    editTextEventDate.setText("");
                                    editTextOrganizerName.setText("");
                                    editTextOrganizerNumber.setText("");
                                    imageViewEventPoster.setImageResource(R.drawable.default_poster);
                                })
                                .addOnFailureListener(e -> {
                                    // Show error message
                                    Toast.makeText(AddEventsActivity.this, "Failed to add event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    // Show error message
                    Toast.makeText(AddEventsActivity.this, "Failed to upload event poster: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and set date picker listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set selected date to EditText
                editTextEventDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        // Show DatePickerDialog
        datePickerDialog.show();
    }
}
