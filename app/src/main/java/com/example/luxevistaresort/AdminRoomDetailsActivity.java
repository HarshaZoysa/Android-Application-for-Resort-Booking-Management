package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminRoomDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AdminRoomDetails"; // For logging
    private ImageView roomImageView;
    private TextView roomNameText, descriptionText, priceText, roomTypeText;
    private Button deleteButton;
    private DatabaseReference databaseReference;
    private int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_details);

        // Initialize Firebase with correct collection name "Rooms"
        databaseReference = FirebaseDatabase.getInstance().getReference("Rooms");

        // Initialize views
        roomImageView = findViewById(R.id.detailRoomImage);
        roomNameText = findViewById(R.id.detailRoomName);
        descriptionText = findViewById(R.id.detailDescription);
        priceText = findViewById(R.id.detailPrice);
        roomTypeText = findViewById(R.id.detailRoomType);
        deleteButton = findViewById(R.id.DeleteRoomBtn);

        Room room = getIntent().getParcelableExtra("room");

        if (room != null) {
            roomId = room.getRoomID();
            Log.d(TAG, "Received Room ID: " + roomId);
            Log.d(TAG, "Full Room Details: " +
                    "\nName: " + room.getRoomName() +
                    "\nType: " + room.getRoomType() +
                    "\nPrice: " + room.getPrice() +
                    "\nID: " + room.getRoomID());

            // Rest of your existing onCreate code...
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(room.getRoomName());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            String Pricepernight = String.format("$%.2f", room.getPrice());
            roomNameText.setText(room.getRoomName());
            descriptionText.setText(room.getDescription());
            priceText.setText(Pricepernight + " " + "Per Night");
            roomTypeText.setText(room.getRoomType());

            if (room.getImageURL() != null && !room.getImageURL().isEmpty()) {
                Glide.with(this)
                        .load(room.getImageURL())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(roomImageView);
            }

            deleteButton.setOnClickListener(v -> {
                Log.d(TAG, "Delete button clicked for Room ID: " + roomId);
                showDeleteConfirmationDialog();
            });
        } else {
            Log.e(TAG, "Room object is null!");
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Room")
                .setMessage("Are you sure you want to delete this room?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Delete confirmed for Room ID: " + roomId);
                        deleteRoom();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }




    private void deleteRoom() {
        Log.d(TAG, "Attempting to delete room with ID: " + roomId);

        // Query to find the room with matching roomId
        Query query = databaseReference.orderByChild("roomId").equalTo(roomId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Query result count: " + dataSnapshot.getChildrenCount());

                if (dataSnapshot.exists()) {
                    for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                        // Get the room data to verify
                        Room room = roomSnapshot.getValue(Room.class);
                        Log.d(TAG, "Found room: " + room.getRoomName() + " with ID: " + room.getRoomID());

                        roomSnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AdminRoomDetailsActivity.this,
                                            "Room deleted successfully",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminRoomDetailsActivity.this,
                                            AdminRoomList.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error deleting room: " + e.getMessage());
                                    Toast.makeText(AdminRoomDetailsActivity.this,
                                            "Failed to delete room: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Log.d(TAG, "No room found with ID: " + roomId);
                    // Try querying without case sensitivity
                    Query backupQuery = databaseReference.orderByChild("roomID").equalTo(roomId);
                    backupQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot backupSnapshot) {
                            if (backupSnapshot.exists()) {
                                for (DataSnapshot roomSnapshot : backupSnapshot.getChildren()) {
                                    roomSnapshot.getRef().removeValue()
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(AdminRoomDetailsActivity.this,
                                                        "Room deleted successfully",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AdminRoomDetailsActivity.this,
                                                        AdminRoomList.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            });
                                }
                            } else {
                                Toast.makeText(AdminRoomDetailsActivity.this,
                                        "Room not found with ID: " + roomId,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "Backup query cancelled: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(AdminRoomDetailsActivity.this,
                        "Database error: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



}