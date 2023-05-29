package com.example.roomieapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationData> notificationList;
    private FirebaseFirestore db;
    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationData notification);
    }
    private OnNotificationClickListener clickListener;



    public NotificationAdapter(List<NotificationData> notificationList) {
        this.notificationList = notificationList;
        db = FirebaseFirestore.getInstance(); // FirebaseFirestore örneğini doğru şekilde başlatın
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationData notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.body.setText(notification.getBody());
        holder.senderEmail.setText(notification.getSenderEmail());

        // Gönderen e-postasına tıklama olayını ekle
//        holder.senderEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Tıklandı", "Tıklanmak");
//                String senderEmail = notification.getSenderEmail();
//
//                db.collection("User")
//                        .get()
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    User user = document.toObject(User.class);
//                                    if (user.getEmail().equals(senderEmail)) {
//                                        Intent intent = new Intent(v.getContext(), UserDetailActivity.class);
//                                        intent.putExtra("User", user);
//                                        v.getContext().startActivity(intent);
//                                        break;
//                                    }
//                                }
//                            }
//                        });
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onNotificationClick(notification);
                }
            }
        });

    }
    public void setOnNotificationClickListener(OnNotificationClickListener listener) {
        this.clickListener = listener;
    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView body;
        TextView senderEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            senderEmail = itemView.findViewById(R.id.senderEmail);
        }
    }
}
