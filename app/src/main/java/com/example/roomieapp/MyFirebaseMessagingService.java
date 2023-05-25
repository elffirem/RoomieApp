package com.example.roomieapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String CHANNEL_ID="MESSAGE";
            CharSequence name;
           NotificationChannel channel=new NotificationChannel(CHANNEL_ID,"message notification",NotificationManager.IMPORTANCE_HIGH);
           getSystemService(NotificationManager.class).createNotificationChannel(channel);
           //Bildirimi oluşturup özelleştiriyoruz
            Notification.Builder notification =new Notification.Builder(this,CHANNEL_ID).setContentTitle(title).setContentText(body).setSmallIcon(R.drawable.notification).setColor(Color.parseColor("#DAA520")).setAutoCancel(true);

            //Bildirimi gösteriyoruz
            NotificationManagerCompat.from(this).notify(1,notification.build());




        }
    }
}
