package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManagement extends AppCompatActivity {

    private EditText roomName, roomDescription, roomPrice ,newRoomType, bedsCount;
    private AutoCompleteTextView airCondition, haveHottub;
    private Button selectPhotosBtn, addRoomBtn;
    private List<Uri> imageUris = new ArrayList<>();
    private FirebaseFirestore db;
    private static final String IMGBB_API_KEY = "b321ce145c9a0ebfe1c8d5c0e1ab7595"; // Replace with your key

    // Image Picker
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    if (result.getData().getClipData() != null) {
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            imageUris.add(result.getData().getClipData().getItemAt(i).getUri());
                        }
                    } else if (result.getData().getData() != null) {
                        imageUris.add(result.getData().getData());
                    }
                    Toast.makeText(this, imageUris.size() + " images selected", Toast.LENGTH_SHORT).show();
                }
            });








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_management);


        String[] acOptions = {"Yes", "No"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, acOptions);


        roomName = findViewById(R.id.newRoomName);
        roomDescription = findViewById(R.id.newRoomDescription);
        roomPrice = findViewById(R.id.newRoomPrice);
        selectPhotosBtn = findViewById(R.id.selectPhotosBtn);
        addRoomBtn = findViewById(R.id.addRoomBtn);
        newRoomType = findViewById(R.id.newRoomType);
        bedsCount = findViewById(R.id.bedsCount);


        db = FirebaseFirestore.getInstance();


        selectPhotosBtn.setOnClickListener(v -> openImagePicker());
        addRoomBtn.setOnClickListener(v -> uploadImagesToImgBB());



    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(intent);
    }

    private void uploadImagesToImgBB() {
        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Select at least one image", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> imageUrls = new ArrayList<>();
        for (Uri imageUri : imageUris) {
            uploadSingleImage(imageUri, imageUrls);
        }
    }

    private void uploadSingleImage(Uri imageUri, List<String> imageUrls) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();

            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            String uploadUrl = "https://api.imgbb.com/1/upload?key=" + IMGBB_API_KEY;

            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                    response -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String imageUrl = jsonResponse.getJSONObject("data").getString("url");
                            imageUrls.add(imageUrl);
                            if (imageUrls.size() == imageUris.size()) {
                                saveRoomData(imageUrls);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error parsing ImgBB response", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", encodedImage);
                    return params;
                }
            };

            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error reading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRoomData(List<String> imageUrls) {
        String name = roomName.getText().toString().trim();
        String description = roomDescription.getText().toString().trim();
        String price = roomPrice.getText().toString().trim();
        String room_type = newRoomType.getText().toString().trim();
        String bedscount = bedsCount.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || price.isEmpty() || imageUrls.isEmpty() || bedscount.isEmpty() ) {
            Toast.makeText(this, "All fields and at least one image are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> roomData = new HashMap<>();
        roomData.put("roomName", name);
        roomData.put("description", description);
        roomData.put("price", price);
        roomData.put("roomType", room_type);
        roomData.put("imageUrls", imageUrls);
        roomData.put("bedsCount",bedscount);

        db.collection("Rooms").add(roomData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RoomManagement.this, "Room added successfully!", Toast.LENGTH_SHORT).show();
                roomName.setText("");
                roomDescription.setText("");
                roomPrice.setText("");
                imageUris.clear();
                newRoomType.setText("");
                bedsCount.setText("");
            } else {
                Toast.makeText(RoomManagement.this, "Failed to add room", Toast.LENGTH_SHORT).show();
            }
        });
    }


}