package com.example.luxevistaresort;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminViewUsers extends AppCompatActivity {

    private ListView listView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_users);

        listView = findViewById(R.id.adminUsersViewList);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        listView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();
        loadUsers();
    }

    private void loadUsers() {
        db.collection("Customers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    String email = document.getString("email");
                    String phone = document.getString("telephone");
                    String image = document.getString("image");

                    userList.add(new User(name, email, phone, image));
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }
}