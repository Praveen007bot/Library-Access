package com.example.libraryaccess.dataclass;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ReservedBook {
    private String userId;
    private String bookId;
    private String startDate;
    private String endDate;
    private int remainingDays;
    private double fine;

    public ReservedBook() {
        // Default constructor required for Firebase
    }

    public ReservedBook(String userId, String bookId, String startDate, String endDate, int remainingDays, double fine) {
        this.userId = userId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingDays = remainingDays;
        this.fine = fine;

        calculateFine();
        calculateRemainingDays();
    }


    public void updateFineAndRemainingDays() {
        calculateFine();
        calculateRemainingDays();
        if (bookId != null) {
            // Get reference to the specific reserved book node using bookId
            DatabaseReference reservedBookRef = FirebaseDatabase.getInstance().getReference().child("reservedbooks").child(bookId);

            // Update fine and remaining days for the specific reserved book
            Map<String, Object> updates = new HashMap<>();
            
            updates.put("fine", fine);
            updates.put("remainingDays", remainingDays);



            reservedBookRef.updateChildren(updates);

        }
    }



    private void calculateFine() {
        int fineAmount = 0;
        if (endDate != null && startDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date end = format.parse(endDate);
                Date start = format.parse(startDate);
                long difference = end.getTime() - start.getTime();
                int remainingDays = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
                int overdueDays = remainingDays - 2; // Assuming 2 days are allowed for free
                if (overdueDays > 0) {
                    fineAmount = overdueDays * 10; // Rs. 10 fine for each overdue day
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.fine = fineAmount;
    }


    private void calculateRemainingDays() {
        if (endDate != null && startDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date end = format.parse(endDate);
                Date start = format.parse(startDate);
                long difference = end.getTime() - start.getTime();
                this.remainingDays = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }



    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}

