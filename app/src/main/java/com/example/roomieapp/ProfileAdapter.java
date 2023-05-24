package com.example.roomieapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.UserViewHolder> {

    private List<User> userList;

    public ProfileAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new UserViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.fullName.setText(user.getName());
        holder.status.setText(user.getStatus());

        Glide.with(holder.itemView.getContext())
                .load(user.getPhotoUrl())
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemImage;
        TextView fullName, status;

        UserViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            fullName = view.findViewById(R.id.fullName);
            status = view.findViewById(R.id.status);
            view.setOnClickListener((View.OnClickListener) this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            User selectedUser = userList.get(position);

            Intent intent = new Intent(v.getContext(), UserDetailActivity.class);
            intent.putExtra("selectedUser", selectedUser);
            v.getContext().startActivity(intent);
        }

    }
}
