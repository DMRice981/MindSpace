package com.mindspace.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.example.mindspace.R;
import com.mindspace.app.network.supabase.SupabaseDtos;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    private final List<SupabaseDtos.Friendship> friends = new ArrayList<>();
    private OnFriendActionListener listener;

    public void setFriends(List<SupabaseDtos.Friendship> items) {
        friends.clear();
        if (items != null) {
            friends.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setOnFriendActionListener(OnFriendActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        SupabaseDtos.Friendship friend = friends.get(position);
        holder.tvFriendName.setText(friend.friendUsername);
        holder.tvFriendId.setText("ID: " + friend.friendId);
        holder.btnChat.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChat(friend);
            }
        });
        holder.btnDeleteFriend.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(friend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        TextView tvFriendId;
        MaterialButton btnChat;
        MaterialButton btnDeleteFriend;

        FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            tvFriendId = itemView.findViewById(R.id.tvFriendId);
            btnChat = itemView.findViewById(R.id.btnChat);
            btnDeleteFriend = itemView.findViewById(R.id.btnDeleteFriend);
        }
    }

    public interface OnFriendActionListener {
        void onChat(SupabaseDtos.Friendship friend);
        void onDelete(SupabaseDtos.Friendship friend);
    }
}
