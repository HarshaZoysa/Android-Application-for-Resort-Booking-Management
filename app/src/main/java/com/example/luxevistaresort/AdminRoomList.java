package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminRoomList extends AppCompatActivity {


    private ListView roomListView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private FirebaseFirestore db;
    private int roomID;
    public String isAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_list);

        roomListView = findViewById(R.id.adminRoomListView);
        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(this, roomList);
        roomListView.setAdapter(roomAdapter);

        db = FirebaseFirestore.getInstance();
        loadRoomsFromFirebase();

        roomListView.setOnItemClickListener((parent, view, position, id) -> {
            Room selectedRoom = roomList.get(position);
            Intent intent = new Intent(AdminRoomList.this, AdminRoomDetailsActivity.class);
            intent.putExtra("room", selectedRoom);
            startActivity(intent);
        });



//Add a room
        Button addroombtn = findViewById(R.id.addRoomButton);
        addroombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRoomList.this , RoomManagement.class);
                startActivity(intent);

            }
        });



    }


    private void loadRoomsFromFirebase() {
        db.collection("Rooms")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    try {
                        roomList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Add try-catch for each document processing
                            try {
                                String roomName = document.getString("roomName") != null ?
                                        document.getString("roomName") : "Unnamed Room";

                                String description = document.getString("description") != null ?
                                        document.getString("description") : "";

                                // Handle potential number format issues
                                double price = 0.0;
                                if (document.contains("price")) {
                                    Object priceObj = document.get("price");
                                    if (priceObj instanceof Double) {
                                        price = document.getDouble("price");
                                    } else if (priceObj instanceof Long) {
                                        price = ((Long) priceObj).doubleValue();
                                    }
                                }

                                String roomType = document.getString("roomType") != null ?
                                        document.getString("roomType") : "";

                                String isAvailable = document.getString("isAvailable") != null ?
                                        document.getString("isAvailable") : "";

                                String imageURL = document.getString("imageUrls");


                                Room room = new Room(roomName, description, price, roomType, imageURL,roomID,isAvailable);
                                roomList.add(room);
                            } catch (Exception e) {

                                continue;
                            }
                        }
                        roomAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("MainActivity", "Error processing documents", e);
                        Toast.makeText(AdminRoomList.this,
                                "Error loading some rooms: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MainActivity", "Error loading rooms", e);
                    Toast.makeText(AdminRoomList.this,
                            "Error loading rooms: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }



}