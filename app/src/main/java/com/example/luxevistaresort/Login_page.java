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

public class Login_page extends AppCompatActivity {

    Button login_button;
    EditText email, password;
    FirebaseFirestore fs;
    FirebaseAuth fa;
    String myemail, mypassword;

    void signInAccount(String myemail, String mypassword) {

        fa.signInWithEmailAndPassword(myemail, mypassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(Login_page.this, CustomerHome.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(Login_page.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Initialize views
        login_button = findViewById(R.id.Login_button);
        email = findViewById(R.id.emailtxt);
        password = findViewById(R.id.passwordtxt);
        fs = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        // Set OnClickListener
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myemail = email.getText().toString();
                mypassword = password.getText().toString();

                // Call the method to sign in
                signInAccount(myemail, mypassword);
            }
        });
    }
}