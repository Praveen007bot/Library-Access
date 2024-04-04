package com.example.libraryaccess.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryaccess.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BorrowRequestActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE_FRONT = 1;
    private static final int REQUEST_IMAGE_CAPTURE_BACK = 2;

    private EditText editTextBookName;
    private EditText editTextFriendId;
    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private ImageView imageViewFront;
    private ImageView imageViewBack;
    private Button buttonCaptureFrontImage;
    private Button buttonCaptureBackImage;
    private Button buttonSendRequest;

    private Bitmap frontImageBitmap;
    private Bitmap backImageBitmap;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_request);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextBookName = findViewById(R.id.editTextBookName);
        editTextFriendId = findViewById(R.id.editTextFriendId);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        imageViewFront = findViewById(R.id.imageViewFront);
        imageViewBack = findViewById(R.id.imageViewBack);
        buttonCaptureFrontImage = findViewById(R.id.buttonCaptureFrontImage);
        buttonCaptureBackImage = findViewById(R.id.buttonCaptureBackImage);
        buttonSendRequest = findViewById(R.id.buttonSendRequest);

        buttonCaptureFrontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_FRONT);
            }
        });

        buttonCaptureBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_BACK);
            }
        });

        buttonSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBorrowRequest();
            }
        });
        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextStartDate);
            }
        });

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextEndDate);
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update EditText with selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String formattedDate = sdf.format(selectedDate.getTime());
                        editText.setText(formattedDate);
                    }
                }, year, month, day);

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        // Show DatePickerDialog
        datePickerDialog.show();
    }
    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (requestCode == REQUEST_IMAGE_CAPTURE_FRONT) {
                imageViewFront.setImageBitmap(imageBitmap);
                frontImageBitmap = imageBitmap;
            } else if (requestCode == REQUEST_IMAGE_CAPTURE_BACK) {
                imageViewBack.setImageBitmap(imageBitmap);
                backImageBitmap = imageBitmap;
            }
        }
    }

    private void sendBorrowRequest() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            String bookName = editTextBookName.getText().toString().trim();
            String friendIdOrEmail = editTextFriendId.getText().toString().trim();
            String startDate = editTextStartDate.getText().toString().trim();
            String endDate = editTextEndDate.getText().toString().trim();

            if (bookName.isEmpty() || friendIdOrEmail.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || frontImageBitmap == null || backImageBitmap == null) {
                Toast.makeText(this, "Please fill in all fields and capture both front and back images", Toast.LENGTH_SHORT).show();
                return;
            }

            // Upload front image to Firebase Storage
            uploadImageToStorage("front_image_" + System.currentTimeMillis(), frontImageBitmap, new ImageUploadCallback() {
                @Override
                public void onImageUploadSuccess(String frontImageUrl) {
                    // Upload back image to Firebase Storage
                    uploadImageToStorage("back_image_" + System.currentTimeMillis(), backImageBitmap, new ImageUploadCallback() {
                        @Override
                        public void onImageUploadSuccess(String backImageUrl) {
                            // Both images uploaded successfully
                            // Store borrow request details in Realtime Database with image URLs
                            storeRequestInDatabase(currentUserId, bookName, friendIdOrEmail, startDate, endDate, frontImageUrl, backImageUrl);
                        }

                        @Override
                        public void onImageUploadFailure(Exception e) {
                            Toast.makeText(BorrowRequestActivity.this, "Failed to upload back image. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onImageUploadFailure(Exception e) {
                    Toast.makeText(BorrowRequestActivity.this, "Failed to upload front image. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User is not signed in
            // Handle the case where the user is not signed in
        }
    }

    private void uploadImageToStorage(String imageName, Bitmap imageBitmap, final ImageUploadCallback callback) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("borrow_request_images").child(imageName + ".jpg");
        imagesRef.putBytes(imageData)
                .addOnSuccessListener(taskSnapshot -> {
                    imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        callback.onImageUploadSuccess(imageUrl);
                    }).addOnFailureListener(e -> callback.onImageUploadFailure(e));
                })
                .addOnFailureListener(e -> callback.onImageUploadFailure(e));
    }

    private void storeRequestInDatabase(String currentUserId, String bookName, String friendIdOrEmail, String startDate, String endDate, String frontImageUrl, String backImageUrl) {
        // Create a unique key for the borrow request
        String requestId = mDatabase.child("borrowRequests").child(currentUserId).push().getKey();

        if (requestId == null) {
            Toast.makeText(this, "Failed to send borrow request. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store borrow request details
        Map<String, Object> requestDetails = new HashMap<>();
        requestDetails.put("bookName", bookName);
        requestDetails.put("friendIdOrEmail", friendIdOrEmail);
        requestDetails.put("startDate", startDate);
        requestDetails.put("endDate", endDate);
        requestDetails.put("frontImageUrl", frontImageUrl);
        requestDetails.put("backImageUrl", backImageUrl);
        requestDetails.put("requestStatus", "pending"); // Status of the request

        // Get the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        requestDetails.put("requestDateTime", currentDateAndTime);

        // Write the borrow request details to the database under the current user's ID
        mDatabase.child("borrowRequests").child(currentUserId).child(requestId).setValue(requestDetails)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BorrowRequestActivity.this, "Borrow request sent successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BorrowRequestActivity.this, "Failed to send borrow request. Please try again later.", Toast.LENGTH_SHORT).show();
                });
    }

    interface ImageUploadCallback {
        void onImageUploadSuccess(String imageUrl);

        void onImageUploadFailure(Exception e);
    }

}
