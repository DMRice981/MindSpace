package com.mindspace.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindspace.R;
import com.mindspace.app.network.supabase.SupabaseDtos;
import com.mindspace.app.utils.MessageCenterUtils;

import java.util.ArrayList;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {
    private final List<SupabaseDtos.ChatConversation> conversations = new ArrayList<>();
    private OnConversationClickListener listener;

    public void setConversations(List<SupabaseDtos.ChatConversation> items) {
        conversations.clear();
        if (items != null) {
            conversations.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setOnConversationClickListener(OnConversationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        SupabaseDtos.ChatConversation conversation = conversations.get(position);
        holder.tvConversationName.setText(conversation.friendUsername);
        holder.tvConversationPreview.setText(MessageCenterUtils.getMessagePreview(conversation.lastMessage));
        holder.tvConversationTime.setText(MessageCenterUtils.getDisplayTime(conversation.lastMessageTime));
        String unreadText = MessageCenterUtils.getUnreadText(conversation.unreadCount);
        holder.tvUnreadCount.setText(unreadText);
        holder.tvUnreadCount.setVisibility(unreadText.isEmpty() ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConversationClick(conversation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView tvConversationName;
        TextView tvConversationPreview;
        TextView tvConversationTime;
        TextView tvUnreadCount;

        ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConversationName = itemView.findViewById(R.id.tvConversationName);
            tvConversationPreview = itemView.findViewById(R.id.tvConversationPreview);
            tvConversationTime = itemView.findViewById(R.id.tvConversationTime);
            tvUnreadCount = itemView.findViewById(R.id.tvUnreadCount);
        }
    }

    public interface OnConversationClickListener {
        void onConversationClick(SupabaseDtos.ChatConversation conversation);
    }
}
