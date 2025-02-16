package com.example.luxevistaresort;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;

public class RoomAdapter extends ArrayAdapter<Room> {
    private Context context;
    private List<Room> rooms;
    private static final String TAG = "RoomAdapter";

    public RoomAdapter(Context context, List<Room> rooms) {
        super(context, 0, rooms);
        this.context = context;
        this.rooms = rooms;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.room_item, parent, false);
            }

            Room room = rooms.get(position);
            if (room == null) {
                return convertView;
            }

            TextView roomNameText = convertView.findViewById(R.id.roomName);
            TextView priceText = convertView.findViewById(R.id.price);
            TextView roomTypeText = convertView.findViewById(R.id.roomType);
            ImageView roomImage = convertView.findViewById(R.id.roomImage);
            TextView isAvailable = convertView.findViewById(R.id.isAvailable);

            // Set text with null checks
            roomNameText.setText(room.getRoomName() != null ? room.getRoomName() : "");
            priceText.setText(String.format("$%.2f", room.getPrice()));
            roomTypeText.setText(room.getRoomType() != null ? room.getRoomType() : "");
            isAvailable.setText(room.getAvailable());
            // Image loading with additional null/empty checks
            String imageUrl = room.getImageURL();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                try {
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                            Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("RoomAdapter", "Image load failed for URL: " + imageUrl, e);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model,
                                                               Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.d("RoomAdapter", "Image loaded successfully: " + imageUrl);
                                    return false;
                                }
                            })
                            .into(roomImage);
                } catch (Exception e) {
                    Log.e("RoomAdapter", "Error loading image: " + imageUrl, e);
                    roomImage.setImageResource(R.drawable.error_image);
                }
            } else {
                roomImage.setImageResource(R.drawable.placeholder_image);
            }

            return convertView;
        } catch (Exception e) {
            Log.e("RoomAdapter", "Error in getView", e);
            return convertView != null ? convertView : new View(context);
        }
    }

}








