package com.example.libraryaccess.dataclass;

public class BorrowRequest {
    private String bookName;
    private String friendIdOrEmail;
    private String startDate;
    private String endDate;
    private String frontImageUrl;
    private String backImageUrl;

    // Constructor, getters, setters

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getFriendIdOrEmail() {
        return friendIdOrEmail;
    }

    public void setFriendIdOrEmail(String friendIdOrEmail) {
        this.friendIdOrEmail = friendIdOrEmail;
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

    public String getFrontImageUrl() {
        return frontImageUrl;
    }

    public void setFrontImageUrl(String frontImageUrl) {
        this.frontImageUrl = frontImageUrl;
    }

    public String getBackImageUrl() {
        return backImageUrl;
    }

    public void setBackImageUrl(String backImageUrl) {
        this.backImageUrl = backImageUrl;
    }
}

