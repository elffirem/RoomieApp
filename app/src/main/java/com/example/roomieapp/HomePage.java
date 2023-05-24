package com.example.roomieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private List<User> userList, filteredList;
    private EditText durationEditText, distanceEditText;
    private Button filterButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv);
        db = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        filteredList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        durationEditText = findViewById(R.id.durationEditText);
        distanceEditText = findViewById(R.id.distanceEditText);
        filterButton = findViewById(R.id.filterButton);
        cancelButton = findViewById(R.id.cancelButton);

        filterButton.setOnClickListener(view -> {
            String duration = durationEditText.getText().toString().trim();
            String distance = distanceEditText.getText().toString().trim();

            // Apply the filter
            applyFilter(duration, distance);
            filterButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.VISIBLE);
        });

        cancelButton.setOnClickListener(view -> {
            // Cancel the filter
            cancelFilter();
            cancelButton.setVisibility(View.GONE);
            filterButton.setVisibility(View.VISIBLE);
        });

        db.collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                        ProfileAdapter adapter = new ProfileAdapter(userList);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notification) {
            // Bildirim öğesine tıklandığında yapılacak işlemler
            return true;
        } else if (id == R.id.logout) {
            // Çıkış öğesine tıklandığında yapılacak işlemler
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        // Bu, genellikle SharedPreference veya Firebase Auth kullanılarak yapılır

        // Giriş aktivitesini başlat
        Intent intent = new Intent(this, LoginActivity.class);
        // Bu flag, geri düğmesine basıldığında bu aktiviteye geri dönülmemesi için gereklidir.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // Bu aktiviteyi bitir
        finish();
    }




    private void applyFilter(String duration, String distance) {
        filteredList.clear();

        for (User user : userList) {
            boolean shouldAdd = true;

            if (!duration.isEmpty() && (user.getDuration() == null || !user.getDuration().equals(duration))) {
                shouldAdd = false;
            }

            if (!distance.isEmpty() && (user.getDistance() == null || !user.getDistance().equals(distance))) {
                shouldAdd = false;
            }

            if (shouldAdd) {
                filteredList.add(user);
            }
        }

        ProfileAdapter adapter = new ProfileAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }


    private void cancelFilter() {
        filteredList.clear();
        filteredList.addAll(userList);

        ProfileAdapter adapter = new ProfileAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        durationEditText.getText().clear();
        distanceEditText.getText().clear();
    }


}
