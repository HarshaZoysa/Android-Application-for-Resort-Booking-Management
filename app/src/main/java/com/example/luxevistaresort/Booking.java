package com.example.luxevistaresort;

public class Booking {

    private String price;
    private String roomID;
    private String roomName;
    private String selectedDate;

    public Booking() {

    }

    public Booking(String price, String roomID, String roomName, String selectedDate) {
        this.price = price;
        this.roomID = roomID;
        this.roomName = roomName;
        this.selectedDate = selectedDate;
    }

    // Getters and setters
    public String getPrice() {



        return String.valueOf(price); }
    public void setPrice(String price) { this.price = price; }
    public String getRoomID() { return roomID; }
    public void setRoomID(String roomID) { this.roomID = roomID; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public String getSelectedDate() { return selectedDate; }
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }

}
