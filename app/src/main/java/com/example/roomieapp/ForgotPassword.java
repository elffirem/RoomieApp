package com.example.roomieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText editMail;
    private Button resetButton;
    private TextView loginNavigation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editMail = findViewById(R.id.emailEditText);
        resetButton = findViewById(R.id.resetPasswordButton);
        loginNavigation = findViewById(R.id.goLogin);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = editMail.getText().toString().trim();

                if (!emailAddress.isEmpty()) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Your reset password has been sent to your " + emailAddress + " mail address", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(ForgotPassword.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }
            }

        });
        loginNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
