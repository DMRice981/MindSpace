package com.mindspace.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindspace.R;
import com.mindspace.app.data.model.CommunityPost;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<CommunityPost> posts = new ArrayList<>();
    private OnPostActionListener listener;

    public interface OnPostActionListener {
        void onLike(CommunityPost post);
        void onComment(CommunityPost post);
        void onReport(CommunityPost post);
    }

    public void setOnPostActionListener(OnPostActionListener listener) {
        this.listener = listener;
    }

    public void setPosts(List<CommunityPost> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        CommunityPost post = posts.get(position);
        holder.tvUsername.setText(post.getUsername());
        holder.tvContent.setText(post.getContent());
        holder.tvMood.setText(post.getMood());
        holder.tvLikeCount.setText(post.getLikeCount() + " 个赞");
        
        holder.btnLike.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLike(post);
            }
        });
        
        holder.btnComment.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComment(post);
            }
        });
        
        holder.btnReport.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReport(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvContent;
        TextView tvMood;
        TextView tvLikeCount;
        Button btnLike;
        Button btnComment;
        Button btnReport;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvMood = itemView.findViewById(R.id.tv_mood);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnReport = itemView.findViewById(R.id.btn_report);
        }
    }
}
