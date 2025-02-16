package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;

public class BookingActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button confirmBookBtn;
    private String selectedDate;
    private Room room;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String userEmail;

    public int currentRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);




        calendarView = findViewById(R.id.calendarView);
        confirmBookBtn = findViewById(R.id.confirmBookBtn);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail(); // Get logged-in user email
        } else {
            Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Get Room Data from Intent
        room = getIntent().getParcelableExtra("room");

        // Handle date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year
        );

        // Confirm Booking Button Click
        confirmBookBtn.setOnClickListener(view -> {
            if (selectedDate == null || room == null) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            saveBookingToFirebase();;

        });
    }

    private void saveBookingToFirebase() {
        if (userEmail == null) return;
        String roomIDString = String.valueOf(room.getRoomID());
        // Create booking data
        Map<String, Object> booking = new HashMap<>();
        booking.put("roomID", room.getRoomID());
        booking.put("roomName", room.getRoomName());
        booking.put("price", room.getPrice());
        booking.put("selectedDate", selectedDate);

        // Save under user’s email as document ID
        db.collection("bookings")
                .document(userEmail)
                .set(booking)
                .addOnSuccessListener(aVoid ->{
                        Toast.makeText(BookingActivity.this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                        updateRoomAvailability(roomIDString);

                }

                )
                .addOnFailureListener(e ->
                        Toast.makeText(BookingActivity.this, "Booking Failed!", Toast.LENGTH_SHORT).show()
                );
    }


    private void updateRoomAvailability(String roomID) {
        db.collection("Rooms") // Assuming rooms are stored in "rooms" collection
                .document(roomID)
                .update("isAvailable", "No");

    }

}