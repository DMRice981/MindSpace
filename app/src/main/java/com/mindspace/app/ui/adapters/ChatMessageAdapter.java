package com.mindspace.app.ui.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindspace.R;
import com.mindspace.app.network.supabase.SupabaseDtos;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {
    private final List<SupabaseDtos.ChatMessage> messages = new ArrayList<>();
    private long currentUserId;

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<SupabaseDtos.ChatMessage> items) {
        messages.clear();
        if (items != null) {
            messages.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        SupabaseDtos.ChatMessage message = messages.get(position);
        boolean mine = message.senderId == currentUserId;
        holder.messageRoot.setGravity(mine ? Gravity.END : Gravity.START);
        holder.tvMessageContent.setText(message.content);
        holder.tvMessageTime.setText(message.createdAt == null ? "" : message.createdAt.replace("T", " ").replace("Z", ""));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout messageRoot;
        TextView tvMessageContent;
        TextView tvMessageTime;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageRoot = itemView.findViewById(R.id.messageRoot);
            tvMessageContent = itemView.findViewById(R.id.tvMessageContent);
            tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
        }
    }
}
