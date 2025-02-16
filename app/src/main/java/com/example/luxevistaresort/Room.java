package com.example.luxevistaresort;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Room implements Parcelable {

    private String roomName;
    private String description;
    private double price;
    private String roomType;
    private String imageUrls;
    private int roomID;
    private String isAvailable;

    // Constructor
    public Room(String roomName, String description, double price, String roomType, String imageURL, int roomID,String isAvailable) {
        this.roomName = roomName;
        this.description = description;
        this.price = price;
        this.roomType = roomType;
        this.imageUrls = imageURL;
        this.roomID = roomID;
        this.isAvailable = isAvailable;
    }
    protected Room(Parcel in) {
        roomName = in.readString();
        description = in.readString();
        price = in.readDouble();
        roomType = in.readString();
        imageUrls = in.readString();
        roomID = in.readInt();
        isAvailable = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomName);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(roomType);
        dest.writeString(imageUrls);
        dest.writeInt(roomID);
        dest.writeString(isAvailable);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    // Getters and setters
    public String getRoomName() { return roomName; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getRoomType() { return roomType; }
    public String getImageURL() { return imageUrls; }

    public int getRoomID(){
        return roomID;
    }
    public String getAvailable(){
        return  isAvailable;


    };



}














