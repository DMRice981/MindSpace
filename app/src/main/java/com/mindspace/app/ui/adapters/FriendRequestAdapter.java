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

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.RequestViewHolder> {
    private final List<SupabaseDtos.FriendRequest> requests = new ArrayList<>();
    private OnRequestActionListener listener;

    public void setRequests(List<SupabaseDtos.FriendRequest> items) {
        requests.clear();
        if (items != null) {
            requests.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setOnRequestActionListener(OnRequestActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        SupabaseDtos.FriendRequest request = requests.get(position);
        String username = request.fromProfile == null ? "用户 " + request.fromUserId : request.fromProfile.username;
        holder.tvRequestName.setText(username);
        holder.btnAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAccept(request);
            }
        });
        holder.btnReject.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReject(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestName;
        MaterialButton btnAccept;
        MaterialButton btnReject;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestName = itemView.findViewById(R.id.tvRequestName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    public interface OnRequestActionListener {
        void onAccept(SupabaseDtos.FriendRequest request);
        void onReject(SupabaseDtos.FriendRequest request);
    }
}
