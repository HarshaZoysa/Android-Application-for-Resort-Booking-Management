package com.example.luxevistaresort;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import android.net.Uri;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.provider.MediaStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import android.graphics.Bitmap;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import android.widget.ImageView;



public class Register_page extends AppCompatActivity {

    ImageView profilepic;

    private EditText customerName, telephone, emailAddress, password;
    private Button signupBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    Uri profilepicURI;
    String sImage;
// Startof mainmethod
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Initialize views
        customerName = findViewById(R.id.customerName);
        telephone = findViewById(R.id.telNo);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.customerPassword);
        signupBtn = findViewById(R.id.signupbtn);
        profilepic = findViewById(R.id.customerpic);

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Set click listener for signup button
        signupBtn.setOnClickListener(view -> {
            String name = customerName.getText().toString().trim();
            String telphone = telephone.getText().toString().trim();
            String email = emailAddress.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Validate input fields
            if (name.isEmpty() || telphone.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(Register_page.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the user in Firebase Auth
            createUser(name, telphone, email, pass);
        });



        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profilepic.setImageBitmap(null);
                // Initialize intent
                Intent intent=new Intent(Intent.ACTION_PICK);
                // set type
                intent.setType("image/*");
                // start activity result
                imageactivity.launch(intent);
            }
        });
    }

    //END OF MAIN METHOD

    private void createUser(String name, String telphone, String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Add user details to Firestore
                            addUserToFirestore(name, telphone, email, pass);
                        } else {
                            // Show error if registration fails
                            Toast.makeText(Register_page.this, "Authentication failed: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    ActivityResultLauncher<Intent> imageactivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== RESULT_OK && result.getData() != null)
                    {
                        profilepicURI = result.getData().getData();
                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),profilepicURI);
                            ByteArrayOutputStream stream=new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                            byte[] bytes=stream.toByteArray();
                            sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        profilepic.setImageURI(profilepicURI);
                    }
                }
            });
    private void addUserToFirestore(String name, String telphone, String email, String pass) {
        HashMap<String, Object> customerData = new HashMap<>();
        customerData.put("name", name);
        customerData.put("telephone", telphone);
        customerData.put("email", email);
        customerData.put("password", pass);
        customerData.put("Image", sImage);

        // Use the email as the document ID
        DocumentReference docRef = firestore.collection("Customers").document(email);
        docRef.set(customerData, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register_page.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(Register_page.this, "Failed to save user data: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        customerName.setText("");
        telephone.setText("");
        emailAddress.setText("");
        password.setText("");

    }



}


