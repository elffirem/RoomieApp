package com.example.roomieapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPassword;
    private TextView signInNavigation;
    public void onStart() {
        super.onStart();

        currentUser = auth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {

            Toast.makeText(getApplicationContext(),"Zaten giris yapilmisolacak "+currentUser.getEmail(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forget_password);
        signInNavigation = findViewById(R.id.goRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail,password;
                mail= String.valueOf(emailEditText.getText());
                password= String.valueOf(passwordEditText.getText());
                if(TextUtils.isEmpty(mail)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Please fill all blanks", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    auth.signInWithEmailAndPassword(mail, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        if(user.isEmailVerified()){
                                            Toast.makeText(LoginActivity.this,"Login Succeed",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, HomePage.class));
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this,"\n" +
                                                    "You cannot log in because your e-mail has not been verified. Please check your e-mail account",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, ProfileSettings.class));
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Error",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
            }
        });

        signInNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }
}