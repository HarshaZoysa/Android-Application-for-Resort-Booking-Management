package com.example.luxevistaresort;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() { return userList.size(); }

    @Override
    public Object getItem(int position) { return userList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        }

        TextView nameText = convertView.findViewById(R.id.userName);
        TextView emailText = convertView.findViewById(R.id.userEmail);
        TextView phoneText = convertView.findViewById(R.id.userPhone);
        ImageView userImage = convertView.findViewById(R.id.userImage);

        User user = userList.get(position);
        nameText.setText(user.getName());
        emailText.setText(user.getEmail());
        phoneText.setText(user.getPhone());

        // Decode Base64 image and set it to ImageView
        String base64String = user.getBase64Image();

        if (base64String != null && !base64String.isEmpty()) {
            try {
                // Trim any unwanted characters (common issue)
                base64String = base64String.replace("\n", "").replace("\r", "");

                // Decode Base64
                byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

                // Convert to Bitmap
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2; // Reduce image size (adjust as needed)
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);


                // Set the Bitmap to ImageView
                userImage.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                userImage.setImageResource(R.drawable.placeholder_image); // Set default if error
            }
        } else {
            userImage.setImageResource(R.drawable.placeholder_image); // Default image if null
        }

        return convertView;
    }
}
