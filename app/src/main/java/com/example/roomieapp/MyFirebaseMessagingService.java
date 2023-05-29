package com.example.roomieapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "CHANNEL";
    private static final CharSequence CHANNEL_NAME = "Message Notifications";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            // Gönderenin email bilgisini al
            String senderEmail = remoteMessage.getData().get("senderEmail");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("User")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if (user.getEmail().equals(senderEmail)) {
                                    Intent intent = new Intent(this, UserDetailActivity.class);
                                    intent.putExtra("selectedUser", user);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                                    // Bildirim oluşturma ve ayarları
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.notification)
                                            .setContentTitle(title)
                                            .setContentText(body)
                                            .setAutoCancel(true)
                                            .setContentIntent(pendingIntent);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    // Android 8.0 ve üzeri için kanal oluşturma
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                                        channel.enableLights(true);
                                        channel.setLightColor(Color.RED);
                                        channel.enableVibration(true);
                                        notificationManager.createNotificationChannel(channel);
                                    }

                                    // Bildirimi gösterme
                                    notificationManager.notify(0, builder.build());
                                    break;
                                }
                            }
                        }
                    });
        }
    }
}
