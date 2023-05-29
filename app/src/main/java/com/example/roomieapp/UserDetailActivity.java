package com.example.roomieapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserDetailActivity extends AppCompatActivity {
    private int messageId = 0;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        String senderEmail = getIntent().getStringExtra("senderEmail");

        // Seçili kullanıcıyı al
        selectedUser = (User) getIntent().getSerializableExtra("selectedUser");

        ImageView userImage = findViewById(R.id.user_image);
        ImageView mail_icon = findViewById(R.id.mail_icon);
        ImageView message_icon = findViewById(R.id.message_icon);
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



            String photoUrl = selectedUser.getPhotoUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                Picasso.get().load(photoUrl).into(userImage);
            } else {

                userImage.setImageResource(R.drawable.dog);
            }
        }


        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMatchRequest();
            }
        });

        mail_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGmailInstalled()) {

                    sendEmail(selectedUser.getEmail());
                } else {

                }


            }
            private void sendEmail(String emailAddress) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + emailAddress));
                intent.putExtra(Intent.EXTRA_SUBJECT, "RoomieApp");

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(Intent.createChooser(intent, "Choose mail app"));
                } else {

                }
            }

            private boolean isGmailInstalled() {
                PackageManager packageManager = getPackageManager();
                try {
                    packageManager.getPackageInfo("com.google.android.gm", PackageManager.GET_ACTIVITIES);
                    return true;
                } catch (PackageManager.NameNotFoundException e) {

                    return false;
                }
            }
        });

        message_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is = isWhatsappInstalled();
                System.out.println(is);
                String phoneNumber ="+90"+ selectedUser.getContact();


                Intent whatsappIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                whatsappIntent.setPackage("com.whatsapp");


                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Your message");


                Intent chooser = Intent.createChooser(whatsappIntent, "WhatsApp ile Mesaj Gönder");
                if (whatsappIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                } else {
                    Toast.makeText(UserDetailActivity.this, "WhatsApp yüklü değil.", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isWhatsappInstalled(){
                PackageManager packageManager=getPackageManager();
                boolean whatsappInstalled;
                try{
                    packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
                    whatsappInstalled=true;
                }
                catch (PackageManager.NameNotFoundException e){
                    whatsappInstalled=false;

                }
                return whatsappInstalled;


            }
        });
    }


    private void sendMatchRequest() {
        if (selectedUser != null && selectedUser.getFcmToken() != null) {
            Log.d("TAGGG", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            FCMSend.pushNotification(UserDetailActivity.this, selectedUser.getFcmToken(), "Match Request", "You have received a match request",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        } else {

        }
    }

}
