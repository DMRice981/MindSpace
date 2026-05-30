package com.mindspace.app.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.example.mindspace.R;
import com.mindspace.app.data.model.Comment;
import com.mindspace.app.data.model.CommunityPost;
import com.mindspace.app.data.repository.CommunityRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private CommunityRepository repository;
    private RecyclerView rvAdmin;
    private PostAdapter postAdapter;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        repository = new CommunityRepository(this);
        
        rvAdmin = findViewById(R.id.rv_admin);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        
        postAdapter = new PostAdapter();
        commentAdapter = new CommentAdapter();
        
        rvAdmin.setLayoutManager(new LinearLayoutManager(this));
        rvAdmin.setAdapter(postAdapter);
        
        loadReportedPosts();
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    rvAdmin.setAdapter(postAdapter);
                    loadReportedPosts();
                } else {
                    rvAdmin.setAdapter(commentAdapter);
                    loadReportedComments();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadReportedPosts() {
        repository.getReportedPosts(posts -> {
            runOnUiThread(() -> {
                postAdapter.setPosts(posts);
            });
        });
    }

    private void loadReportedComments() {
        repository.getReportedComments(comments -> {
            runOnUiThread(() -> {
                commentAdapter.setComments(comments);
            });
        });
    }

    class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
        private List<CommunityPost> posts = new ArrayList<>();

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
            
            holder.btnDelete.setOnClickListener(v -> {
                repository.deletePost(post.getId());
                posts.remove(position);
                notifyItemRemoved(position);
            });
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        class PostViewHolder extends RecyclerView.ViewHolder {
            TextView tvUsername;
            TextView tvContent;
            TextView tvMood;
            Button btnDelete;

            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                tvUsername = itemView.findViewById(R.id.tv_username);
                tvContent = itemView.findViewById(R.id.tv_content);
                tvMood = itemView.findViewById(R.id.tv_mood);
                btnDelete = itemView.findViewById(R.id.btn_like);
                btnDelete.setText("删除");
                itemView.findViewById(R.id.btn_comment).setVisibility(View.GONE);
                itemView.findViewById(R.id.btn_report).setVisibility(View.GONE);
                itemView.findViewById(R.id.tv_like_count).setVisibility(View.GONE);
            }
        }
    }

    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
        private List<Comment> comments = new ArrayList<>();

        public void setComments(List<Comment> comments) {
            this.comments = comments;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = comments.get(position);
            holder.tvUsername.setText(comment.getUsername());
            holder.tvContent.setText(comment.getContent());
            
            holder.itemView.setOnLongClickListener(v -> {
                repository.deleteComment(comment.getId());
                comments.remove(position);
                notifyItemRemoved(position);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {
            TextView tvUsername;
            TextView tvContent;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                tvUsername = itemView.findViewById(android.R.id.text1);
                tvContent = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
