package com.example.luxevistaresort;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class customerBookingsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    public String priceString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bookings);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.bookingsRecyclerView);
        adapter = new BookingAdapter(this);

        // Set horizontal layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Load bookings
        loadBookings();
    }

    void loadBookings() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                db.collection("bookings")
                        .document(userEmail)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    // Convert price from Long to String
                                    String priceString = String.valueOf(documentSnapshot.getLong("price"));

                                    // Convert roomID safely
                                    Object roomIDObj = documentSnapshot.get("roomID");
                                    String roomID = (roomIDObj != null) ? roomIDObj.toString() : "";

                                    // Convert roomName safely
                                    Object roomNameObj = documentSnapshot.get("roomName");
                                    String roomName = (roomNameObj != null) ? roomNameObj.toString() : "";

                                    // Convert selectedDate safely
                                    Object selectedDateObj = documentSnapshot.get("selectedDate");
                                    String selectedDate = (selectedDateObj != null) ? selectedDateObj.toString() : "";

                                    Booking booking = new Booking(
                                            priceString,
                                            roomID,
                                            roomName,
                                            selectedDate
                                    );

                                    // Create list with single booking
                                    List<Booking> bookings = new ArrayList<>();
                                    bookings.add(booking);
                                    adapter.updateBookings(bookings);
                                } else {
                                    Toast.makeText(customerBookingsActivity.this,
                                            "No bookings found",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(customerBookingsActivity.this,
                                        "Error loading bookings: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }







}
