package com.example.roomieapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;

public class ProfileSettings extends AppCompatActivity {
    FirebaseUser currentUser;
    User user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    Uri resultUri;
    boolean isPhotoUploaded;
    private ImageView profileImageView;
    private EditText profileName, profileEmail;
    private EditText departmentEditText, classEditText, distanceEditText, durationEditText, contactEditText;
    private Spinner statusSpinner;
    private Button saveButton;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try{
                    isPhotoUploaded=true;
                    InputStream stream= getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    profileImageView.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        user = new User(currentUser.getEmail(), currentUser.getUid() );
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        profileName=findViewById(R.id.profileName);
        profileEmail=findViewById(R.id.profileEmail);
        profileImageView = findViewById(R.id.profileImageView);
        departmentEditText = findViewById(R.id.departmentEditText);
        classEditText = findViewById(R.id.classEditText);
        distanceEditText = findViewById(R.id.distanceEditText);
        durationEditText = findViewById(R.id.durationEditText);
        statusSpinner = findViewById(R.id.statusSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        contactEditText = findViewById(R.id.contactEditText);
        saveButton = findViewById(R.id.saveButton);
        profileEmail.setText(currentUser.getEmail());

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    isPhotoUploaded=true;
                    CropImage.activity().start(ProfileSettings.this);//fotografi alma
                }

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

    }

    private void saveProfile() {

        String name=profileName.getText().toString().trim();

        String department = departmentEditText.getText().toString().trim();
        String studentClass = classEditText.getText().toString().trim();
        String distance = distanceEditText.getText().toString().trim();
        String duration = durationEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();
        String contact = contactEditText.getText().toString().trim();
        if (TextUtils.isEmpty(department) || TextUtils.isEmpty(studentClass) || TextUtils.isEmpty(distance)
                || TextUtils.isEmpty(duration) || TextUtils.isEmpty(status)
                || TextUtils.isEmpty(contact) || !isPhotoUploaded) {
            Toast.makeText(getApplicationContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
        }
        else{
            StorageReference photoRef = storageRef.child("users/" + currentUser.getUid() + "/profilePhoto.jpg");
            photoRef.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                    return photoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        user.UpdateUser(name,department,studentClass,distance,duration,status,contact,task.getResult().toString());


                        db.collection("User").document(currentUser.getUid()).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


//                                        Intent intent = new Intent(getApplicationContext(), ProfileDetailActivity.class);
//                                        intent.putExtra("user", user);
//                                        startActivity(intent);
//                                        finish();




                                        Toast.makeText(ProfileSettings.this, "Profil kaydedildi", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ProfileSettings.this,HomePage.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(ProfileSettings.this, "Profil güncellenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(ProfileSettings.this, "task is unsuccesfull", Toast.LENGTH_SHORT).show();



                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileSettings.this, "photo yuklenirken hta", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(ProfileSettings.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res2;
    }
    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(ProfileSettings.this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(ProfileSettings.this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return res1&&res2;
    }
}
