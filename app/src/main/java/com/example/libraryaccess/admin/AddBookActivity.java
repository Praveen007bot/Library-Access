package com.example.libraryaccess.admin;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.libraryaccess.R;
import com.example.libraryaccess.dataclass.Book;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddBookActivity extends AppCompatActivity {

    private EditText etBookName, etAuthorName, etBookDescription, etQuantity;
    private Button btnSelectImage, btnSaveToFirebase;

    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Spinner spinnerCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        etBookName = findViewById(R.id.etBookName);
        etAuthorName = findViewById(R.id.etAuthorName);
        etBookDescription = findViewById(R.id.etBookDescription);
        etQuantity = findViewById(R.id.etQuantity);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSaveToFirebase = findViewById(R.id.btnSaveToFirebase);

        // Initialize Spinner
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // Define array of category options
        String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};

        // Create ArrayAdapter using the category array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Set the layout style for the dropdown items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the spinner
        spinnerCategory.setAdapter(adapter);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open a file picker to select an image
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnSaveToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFirebase();
            }
        });
    }

    private void saveToFirebase() {
        String bookName = etBookName.getText().toString().trim();
        String authorName = etAuthorName.getText().toString().trim();
        String bookDescription = etBookDescription.getText().toString().trim();
        int quantity = Integer.parseInt(etQuantity.getText().toString().trim());

        // Get the selected category from the spinner
        String selectedCategory = spinnerCategory.getSelectedItem().toString();

        if (bookName.isEmpty() || authorName.isEmpty() || bookDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Generate a new key for the book (unique bookId)
        String bookKey = databaseReference.push().getKey();

        // Store the image in Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("book_images").child(bookKey + ".jpg");

        // Assuming selectedImageUri is the URI of the selected image
        if (selectedImageUri != null) {
            storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL of the uploaded image
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Create a Book object with the download URL, category, and unique bookId
                    Book newBook = new Book(bookKey, bookName, authorName, bookDescription, quantity, uri.toString(), selectedCategory);

                    // Save the book to Firebase with the generated key (bookId)
                    databaseReference.child(bookKey).setValue(newBook);

                    Toast.makeText(this, "Book saved to Firebase", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    // Handle failure to get the download URL
                    Toast.makeText(this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                // Handle failure to upload the image
                Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            // If no image is selected, create a Book object without an image URL
            Book newBook = new Book(bookKey, bookName, authorName, bookDescription, quantity, "", selectedCategory);

            // Save the book to Firebase with the generated key (bookId)
            databaseReference.child(bookKey).setValue(newBook);

            Toast.makeText(this, "Book successfully Added", Toast.LENGTH_SHORT).show();
        }
    }



    // Override this method if you are using startActivityForResult for image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            // Save the selected image URI
            selectedImageUri = data.getData();
            // You can update an ImageView to display the selected image if needed
            // imageView.setImageURI(selectedImageUri);
        }
    }
}
