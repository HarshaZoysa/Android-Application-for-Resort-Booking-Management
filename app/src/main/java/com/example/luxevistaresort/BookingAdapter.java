package com.example.luxevistaresort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookingList;
    private Context context;

    public BookingAdapter(Context context) {
        this.context = context;
        this.bookingList = new ArrayList<>();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView roomNameTextView, roomIdTextView, dateTextView, priceTextView;
        Button cancelButton; // Add this

        public BookingViewHolder(View itemView) {
            super(itemView);
            roomNameTextView = itemView.findViewById(R.id.roomNameTextView);
            roomIdTextView = itemView.findViewById(R.id.roomIdTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.roomNameTextView.setText(booking.getRoomName());
        holder.roomIdTextView.setText("Room ID: " + booking.getRoomID());
        holder.dateTextView.setText("Date: " + booking.getSelectedDate());
        holder.priceTextView.setText("Price: $" + booking.getPrice());

        holder.cancelButton.setOnClickListener(v -> {
            deleteBooking(booking, position);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateBookings(List<Booking> newBookings) {
        bookingList.clear();
        bookingList.addAll(newBookings);
        notifyDataSetChanged();
    }

    private void deleteBooking(Booking booking, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userEmail = auth.getCurrentUser().getEmail();

        if (userEmail != null) {
            db.collection("bookings")
                    .document(userEmail)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        bookingList.remove(position);
                        notifyItemRemoved(position);

                        // Check if context is an instance of customerBookingsActivity before calling loadBookings()
                        if (context instanceof customerBookingsActivity) {
                            ((customerBookingsActivity) context).loadBookings(); // Reload list after deletion
                        }

                        Toast.makeText(context, "Booking Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to cancel booking: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }


}

