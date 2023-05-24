package com.example.roomieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db ;
    private EditText registerEmail, registerPassword, registerPasswordConfirm;
    private Button registerButton;
    private TextView goLogin;
    ProgressBar progressBar;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(),"Zaten giris yapilmisolacak ", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPasswordConfirm = findViewById(R.id.registerPasswordConfirm);
        registerButton = findViewById(R.id.registerButton);
        goLogin = findViewById(R.id.goLogin);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login sayfasına geçiş yap

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Register sayfasını kapat
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kayıt işlemlerini gerçekleştir

                progressBar.setVisibility(View.VISIBLE);
                if(registerPassword.getText().toString().isEmpty()||registerPasswordConfirm.getText().toString().isEmpty()||registerEmail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Lutfen tum bosluklari doldurun!",Toast.LENGTH_SHORT).show();

                }
                else{
                    if(registerPassword.getText().toString().equals(registerPasswordConfirm.getText().toString())){
                        performRegistration(registerEmail.getText().toString(),registerPassword.getText().toString());
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Sifreler Uyusmuyor",Toast.LENGTH_SHORT).show();
                    }

                }


                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void performRegistration(String mail,String password) {
        if(isYildizMail(mail)){
            auth.createUserWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    User newUser = new User(mail,authResult.getUser().getUid());

                    db.collection("User").document(newUser.getUid()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseUser currentUser = auth.getCurrentUser();
                            currentUser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),"Hesabiniz olusturuldu mailinizi onaylayarak giris yapabilirsiniz",Toast.LENGTH_LONG).show();

                                            } else {
                                                Toast.makeText(getApplicationContext(),"Hesabiniz olusturulurken hata mailinize gonderilemedi" ,Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                            Intent intent = new Intent(RegisterActivity.this, ProfileSettings.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Firestoreda sikinti"+e.getMessage(),Toast.LENGTH_LONG).show();


                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Authtta sikinti "+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });


        }
        else{
            Toast.makeText(getApplicationContext(),"Mailinizin @std.yildiz.edu.tr formatinda olmasi gerekmektedir!",Toast.LENGTH_SHORT).show();
        }
    }
    private void addUserTodb(){

    }
    private boolean isYildizMail(String email) {
        String emailSuffix = "@std.yildiz.edu.tr";

        return email.endsWith(emailSuffix);
    }

}