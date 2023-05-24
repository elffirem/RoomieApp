package com.example.roomieapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class UserDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ImageView userImage = findViewById(R.id.user_image);
        User selectedUser = (User) getIntent().getSerializableExtra("selectedUser");
        String photoUrl = selectedUser.getPhotoUrl();

        // Fotoğraf URL'sini kullanarak profil resmini yükleme
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Picasso.get().load(photoUrl).into(userImage);
        } else {
            // Eğer fotoğraf URL'si boş ise, varsayılan bir resim yükleyebilirsiniz
            userImage.setImageResource(R.drawable.dog);
        }


        TextView userNameTextView = findViewById(R.id.user_name);
        TextView userEmailTextView = findViewById(R.id.user_email);
        TextView userDepartmentTextView = findViewById(R.id.user_department);
        TextView userStudentClassTextView = findViewById(R.id.user_student_class);
        TextView userDistanceTextView = findViewById(R.id.user_distance);
        TextView userDurationTextView = findViewById(R.id.user_duration);
        TextView userStatusTextView = findViewById(R.id.user_status);
        TextView userContactTextView = findViewById(R.id.user_contact);
        Button sendRequestButton = findViewById(R.id.send_request_button);

        sendRequestButton.setOnClickListener(view -> {
            // Eşleşme isteği gönderme işlemlerini burada gerçekleştirin
            // İsteği göndermek için gerekli işlemleri yapabilirsiniz
        });


        if (selectedUser != null) {
            userNameTextView.setText(selectedUser.getName());
            userEmailTextView.setText(selectedUser.getEmail());
            userDepartmentTextView.setText(selectedUser.getDepartment());
            userStudentClassTextView.setText(selectedUser.getStudentClass());
            userDistanceTextView.setText( selectedUser.getDistance());
            userDurationTextView.setText(selectedUser.getDuration());
            userStatusTextView.setText(selectedUser.getStatus());
            userContactTextView.setText(selectedUser.getContact());
        }
    }
}
