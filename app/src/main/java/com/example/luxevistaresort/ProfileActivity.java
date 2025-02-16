package com.example.luxevistaresort;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {

EditText customerName,currentPhone,currentPassword;
Button editBtn , saveBtn;
ImageView profilePhoto;
String currentuser , custDbName , custDbPhone, custDBpassword;

FirebaseFirestore fs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fs = FirebaseFirestore.getInstance();

        editBtn = findViewById(R.id.editBtn);
        saveBtn = findViewById(R.id.saveBtn);
        customerName = findViewById(R.id.customerName);
        currentPhone = findViewById(R.id.currentPhone);
        currentPassword = findViewById(R.id.currentPassword);
        profilePhoto = findViewById(R.id.profilePhoto);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentuser = user.getEmail();
        }

        fs.collection("Customers").document(currentuser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    String myimage = doc.getString("Image");
                    custDbName = doc.getString("name");
                    custDbPhone = doc.getString("telephone");
                    custDBpassword = doc.getString("password");
                    byte[] bytes= Base64.decode(myimage, Base64.DEFAULT);

                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                    profilePhoto.setImageBitmap(bitmap);
                    customerName.setText(custDbName);
                    currentPhone.setText(custDbPhone);
                    currentPassword.setText(custDBpassword);

                }
            }
        });


editBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        customerName.setEnabled(true);
        currentPhone.setEnabled(true);
        currentPassword.setEnabled(true);
    }
});


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// Save code goes here

                custDbName = customerName.getText().toString().trim();
                custDbPhone = currentPhone.getText().toString().trim();
                custDBpassword  = currentPassword.getText().toString().trim();


                if ( custDbName.isEmpty() ||custDbPhone.isEmpty() || custDBpassword.isEmpty() ){

                    Toast.makeText(ProfileActivity.this, "Fields Cannot Be Empty", Toast.LENGTH_SHORT).show();

                }

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("name", customerName.getText().toString());
                updatedData.put("telephone", currentPhone.getText().toString());
                updatedData.put("password", currentPassword.getText().toString());
                fs.collection("Customers").document(currentuser).update(updatedData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Update successful
                                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                    customerName.setEnabled(false);
                                    currentPhone.setEnabled(false);
                                    currentPassword.setEnabled(false);

                                } else {
                                    // Handle error
                                    Toast.makeText(ProfileActivity.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });




    }
}