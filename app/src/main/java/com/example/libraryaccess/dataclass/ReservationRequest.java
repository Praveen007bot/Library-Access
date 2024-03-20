package com.example.libraryaccess.dataclass;

public class ReservationRequest {
    private String userName;
    private String bookName;
    private String startDate;
    private String bookId;
    private String userId;


    public String getUserId() {
        return userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String endDate;
    private String timeSlot;
    private String status;
    private String imageUrl;




    // Default constructor (required for Firebase)
    public ReservationRequest() {
    }

    public ReservationRequest(String userName, String bookName, String startDate, String endDate, String timeSlot, String status, String imageUrl, String userId, String bookId) {
        this.userName = userName;
        this.bookName = bookName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeSlot = timeSlot;
        this.status = status;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.bookId = bookId;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
