package com.example.libraryaccess.dataclass;

public class Event {
    private String eventName;
    private String eventDate;
    private String organizerName;
    private String organizerNumber;
    private String eventPosterUrl; // New field for event poster URL

    public Event() {
        // Default constructor required for Firebase
    }

    public Event(String eventName, String eventDate, String organizerName, String organizerNumber, String eventPosterUrl) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.organizerName = organizerName;
        this.organizerNumber = organizerNumber;
        this.eventPosterUrl = eventPosterUrl;
    }

    // Getters and setters for all fields
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getOrganizerNumber() {
        return organizerNumber;
    }

    public void setOrganizerNumber(String organizerNumber) {
        this.organizerNumber = organizerNumber;
    }

    public String getEventPosterUrl() {
        return eventPosterUrl;
    }

    public void setEventPosterUrl(String eventPosterUrl) {
        this.eventPosterUrl = eventPosterUrl;
    }
}
