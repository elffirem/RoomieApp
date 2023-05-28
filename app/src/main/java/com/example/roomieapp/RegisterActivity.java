package com.example.roomieapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db;
    private EditText registerEmail, registerPassword, registerPasswordConfirm;
    private Button registerButton;
    private TextView goLogin;
    ProgressBar progressBar;

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Zaten giris yapilmis", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPasswordConfirm = findViewById(R.id.registerPasswordConfirm);
        registerButton = findViewById(R.id.registerButton);
        goLogin = findViewById(R.id.goLogin);

        goLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        registerButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String mail = registerEmail.getText().toString();
            String password = registerPassword.getText().toString();
            String confirmPass = registerPasswordConfirm.getText().toString();

            if(mail.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(getApplicationContext(), "Lutfen tum bosluklari doldurun!", Toast.LENGTH_SHORT).show();
            }
            else{
                if(password.equals(confirmPass)){
                    performRegistration(mail, password);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Sifreler Uyusmuyor",Toast.LENGTH_SHORT).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void performRegistration(String mail, String password) {
        // Fetching FCM Token for the user
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                return;
            }

            String fcmToken = task.getResult(); // Assigning the FCM token
            Log.d("TAG", String.format("FCM Token: %s", fcmToken));

            // Create the user in FirebaseAuth
            auth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(authResult -> {
                User newUser = new User(mail, authResult.getUser().getUid());
                newUser.setFcmToken(fcmToken);  // Setting FCM token to the user object

                db.collection("User").document(newUser.getUid()).set(newUser).addOnSuccessListener(unused -> {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    currentUser.sendEmailVerification()
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Verify your account from your mail address", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegisterActivity.this, ProfileSettings.class);
                                    intent.putExtra("user", newUser);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Could not send verification to your mail address", Toast.LENGTH_SHORT).show();
                                }
                            });
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "An error occurred on Firestore" + e.getMessage(), Toast.LENGTH_LONG).show());
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "An error occurred on auth " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}

