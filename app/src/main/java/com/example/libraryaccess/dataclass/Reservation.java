package com.example.libraryaccess.dataclass;

public class Reservation {
    private String bookId;
    private String startDate;
    private String endDate;
    private String timeSlot;
    private String status;

    public Reservation() {
        // Default constructor required for Firebase
    }

    public Reservation(String bookId, String startDate, String endDate, String timeSlot, String status) {
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeSlot = timeSlot;
        this.status = "pending"; // Set status to "pending" by default
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

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

