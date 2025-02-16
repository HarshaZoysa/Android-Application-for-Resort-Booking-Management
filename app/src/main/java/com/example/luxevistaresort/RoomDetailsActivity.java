package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RoomDetailsActivity extends AppCompatActivity {


    private ImageView roomImageView;
    private TextView roomNameText, descriptionText, priceText, roomTypeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        Button bookBtn = findViewById(R.id.bookbtn);



        roomImageView = findViewById(R.id.detailRoomImage);
        roomNameText = findViewById(R.id.detailRoomName);
        descriptionText = findViewById(R.id.detailDescription);
        priceText = findViewById(R.id.detailPrice);
        roomTypeText = findViewById(R.id.detailRoomType);

        Room room = getIntent().getParcelableExtra("room");

        if (room != null) {
            // Set the action bar title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(room.getRoomName());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            // Display room details

            String Pricerpernight = String.format("$%.2f", room.getPrice());
            roomNameText.setText(room.getRoomName());
            descriptionText.setText(room.getDescription());
            priceText.setText(Pricerpernight  + " " + "Per Night");
            roomTypeText.setText(room.getRoomType());

            // Load image using Glide
            if (room.getImageURL() != null && !room.getImageURL().isEmpty()) {
                Glide.with(this)
                        .load(room.getImageURL())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(roomImageView);
            }


        }

        bookBtn.setOnClickListener(view -> {
            Intent intent = new Intent(RoomDetailsActivity.this, BookingActivity.class);
            intent.putExtra("room", room);
            startActivity(intent);
        });


    }}