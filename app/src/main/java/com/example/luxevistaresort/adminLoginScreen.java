package com.example.luxevistaresort;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class adminLoginScreen extends AppCompatActivity {

    FirebaseFirestore fs;
    FirebaseAuth fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_screen);

        Button adminloginbtn = findViewById(R.id.adminLogin);
        EditText email = findViewById(R.id.adminemailtxt);
        EditText password = findViewById(R.id.adminPassword);
        fs = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();



        adminloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myemail = email.getText().toString();
                String mypassword = password.getText().toString();
                signInAccount(myemail ,mypassword);



            }
        });









    }

    //Login Function

    void signInAccount(String myemail, String mypassword) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query Firestore collection "AdminUsers" for the given email
        db.collection("AdminUsers")
                .whereEqualTo("emailAddress", myemail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String storedPassword = document.getString("password");

                                // Compare stored password with entered password
                                if (storedPassword != null && storedPassword.equals(mypassword)) {
                                    // Correct credentials, open admin activity
                                    Intent i = new Intent(adminLoginScreen.this, AdminHome.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    // Wrong password
                                    Toast.makeText(adminLoginScreen.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            // No matching email found
                            Toast.makeText(adminLoginScreen.this, "Admin user not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





}