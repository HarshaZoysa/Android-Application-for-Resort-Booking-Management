package com.example.luxevistaresort;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerHome extends AppCompatActivity {
    String sImage;
    String currentuser , customerName;
    ImageView profilepic;
    TextView custname;
    FirebaseFirestore fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        fs = FirebaseFirestore.getInstance();
        profilepic = findViewById(R.id.customerpic);
        custname = findViewById(R.id.custname);


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
                    customerName = doc.getString("name");

                    byte[] bytes= Base64.decode(myimage, Base64.DEFAULT);

                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                    profilepic.setImageBitmap(bitmap);
                    custname.setText(customerName);
                }
            }
        });

//open bookings page

Button bookbtn = findViewById(R.id.myBooking);
        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerHome.this, customerBookingsActivity.class);
                startActivity(intent);
            }

        });


 Button manageprofile = findViewById(R.id.manageprofilebtn);
 manageprofile.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Intent intent = new Intent(CustomerHome.this , ProfileActivity.class
         );
         startActivity(intent);
     }
 });

 Button availableRoomsbtn = findViewById(R.id.roomsbtn);
 availableRoomsbtn.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Intent intent = new Intent (CustomerHome.this,room_list.class
         );
         startActivity(intent);
     }
 });



    }



}