package com.example.roomieapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class UserDetailActivity extends AppCompatActivity {
    private int messageId = 0;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Seçili kullanıcıyı al
        selectedUser = (User) getIntent().getSerializableExtra("selectedUser");

        ImageView userImage = findViewById(R.id.user_image);
        TextView userNameTextView = findViewById(R.id.user_name);
        TextView userEmailTextView = findViewById(R.id.user_email);
        TextView userDepartmentTextView = findViewById(R.id.user_department);
        TextView userStudentClassTextView = findViewById(R.id.user_student_class);
        TextView userDistanceTextView = findViewById(R.id.user_distance);
        TextView userDurationTextView = findViewById(R.id.user_duration);
        TextView userStatusTextView = findViewById(R.id.user_status);
        TextView userContactTextView = findViewById(R.id.user_contact);
        Button sendRequestButton = findViewById(R.id.send_request_button);

        // Kullanıcı bilgilerini görüntüle
        if (selectedUser != null) {
            userNameTextView.setText(selectedUser.getName());
            userEmailTextView.setText(selectedUser.getEmail());
            userDepartmentTextView.setText(selectedUser.getDepartment());
            userStudentClassTextView.setText(selectedUser.getStudentClass());
            userDistanceTextView.setText(selectedUser.getDistance());
            userDurationTextView.setText(selectedUser.getDuration());
            userStatusTextView.setText(selectedUser.getStatus());
            userContactTextView.setText(selectedUser.getContact());


            // Fotoğraf URL'sini kullanarak profil resmini yükleme
            String photoUrl = selectedUser.getPhotoUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                Picasso.get().load(photoUrl).into(userImage);
            } else {
                // Eğer fotoğraf URL'si boş ise, varsayılan bir resim yükleyebilirsiniz
                userImage.setImageResource(R.drawable.dog);
            }
        }

        // Eşleşme isteği gönderme düğmesine tıklama olayını ekle
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMatchRequest();
            }
        });
    }

    // Eşleşme isteği gönderme işlemini gerçekleştir
    private void sendMatchRequest() {
        FCMSend.pushNotification(UserDetailActivity.this, "engNqbbdSxiLhJMBJf33qK:APA91bER6WyvouOQ6PfUWN25ZSrbw3zTHy_OnwurfANeIbjzgPdgai61yrIZ_gl_XZpvO-LeECAol9b6d9eS7luIaz-sQEC9shmu7qlw-otEE70eESrruf0WFnVhlCrBSVbLdvoaaP_n", "Hello", "Hello World message");
    }
}
