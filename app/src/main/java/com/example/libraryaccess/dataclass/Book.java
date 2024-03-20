package com.example.libraryaccess.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String bookId;
    private String bookName;
    private String authorName;
    private String bookDescription;
    private int quantity;
    private String imageUrl;
    private String category; // New field for category

    // Required default constructor for Firebase
    public Book() {
    }

    // Constructor without imageUrl, assuming imageUrl is stored in Firebase Storage
    public Book(String bookId, String bookName, String authorName, String bookDescription, int quantity, String category) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookDescription = bookDescription;
        this.quantity = quantity;
        this.category = category; // Initialize category
    }

    // Constructor with imageUrl
    public Book(String bookId, String bookName, String authorName, String bookDescription, int quantity, String imageUrl, String category) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookDescription = bookDescription;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.category = category; // Initialize category
    }

    // Getters and setters for all fields

    protected Book(Parcel in) {
        bookId = in.readString();
        bookName = in.readString();
        authorName = in.readString();
        bookDescription = in.readString();
        quantity = in.readInt();
        imageUrl = in.readString();
        category = in.readString(); // Read category from parcel
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookId);
        dest.writeString(bookName);
        dest.writeString(authorName);
        dest.writeString(bookDescription);
        dest.writeInt(quantity);
        dest.writeString(imageUrl);
        dest.writeString(category); // Write category to parcel
    }
}
