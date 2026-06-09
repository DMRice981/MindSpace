package com.mindspace.app.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.example.mindspace.R;
import com.mindspace.app.data.local.AppDatabase;
import com.mindspace.app.data.model.Comment;
import com.mindspace.app.data.model.CommunityPost;
import com.mindspace.app.data.model.User;
import com.mindspace.app.data.repository.CommunityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminActivity extends AppCompatActivity {
    private CommunityRepository repository;
    private AppDatabase database;
    private RecyclerView rvAdmin;
    private PostAdapter postAdapter;
    private CommentAdapter commentAdapter;
    private UserAdapter userAdapter;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        repository = new CommunityRepository(this);
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        
        rvAdmin = findViewById(R.id.rv_admin);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        
        postAdapter = new PostAdapter();
        commentAdapter = new CommentAdapter();
        userAdapter = new UserAdapter();
        
        rvAdmin.setLayoutManager(new LinearLayoutManager(this));
        rvAdmin.setAdapter(postAdapter);
        
        loadReportedPosts();
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    rvAdmin.setAdapter(postAdapter);
                    loadReportedPosts();
                } else if (tab.getPosition() == 1) {
                    rvAdmin.setAdapter(commentAdapter);
                    loadReportedComments();
                } else if (tab.getPosition() == 2) {
                    rvAdmin.setAdapter(userAdapter);
                    loadAllUsers();
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

    private void loadAllUsers() {
        executorService.execute(() -> {
            List<User> users = database.userDao().getAllUsers();
            runOnUiThread(() -> {
                userAdapter.setUsers(users);
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

    class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
        private List<User> users = new ArrayList<>();

        public void setUsers(List<User> users) {
            this.users = users;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_admin, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = users.get(position);
            holder.tvUsername.setText(user.getUsername());
            holder.tvEmail.setText(user.getEmail());
            holder.tvRole.setText(user.isAdmin() ? "管理员" : "普通用户");
            holder.tvStatus.setText(user.isBanned() ? "已禁言" : "正常");
            holder.tvStatus.setTextColor(user.isBanned() ? 
                getResources().getColor(android.R.color.holo_red_dark) : 
                getResources().getColor(android.R.color.holo_green_dark));

            holder.btnBan.setText(user.isBanned() ? "解封" : "禁言");
            holder.btnBan.setOnClickListener(v -> {
                boolean newBanStatus = !user.isBanned();
                executorService.execute(() -> {
                    database.userDao().updateBanStatus(user.getId(), newBanStatus);
                    runOnUiThread(() -> {
                        user.setBanned(newBanStatus);
                        holder.tvStatus.setText(newBanStatus ? "已禁言" : "正常");
                        holder.tvStatus.setTextColor(newBanStatus ? 
                            getResources().getColor(android.R.color.holo_red_dark) : 
                            getResources().getColor(android.R.color.holo_green_dark));
                        holder.btnBan.setText(newBanStatus ? "解封" : "禁言");
                        Toast.makeText(AdminActivity.this, 
                            newBanStatus ? "已禁言用户" : "已解封用户", 
                            Toast.LENGTH_SHORT).show();
                    });
                });
            });

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(AdminActivity.this)
                    .setTitle("删除用户")
                    .setMessage("确定要删除用户 " + user.getUsername() + " 吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        executorService.execute(() -> {
                            database.userDao().delete(user.getId());
                            runOnUiThread(() -> {
                                users.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(AdminActivity.this, "用户已删除", Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .setNegativeButton("取消", null)
                    .show();
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView tvUsername;
            TextView tvEmail;
            TextView tvRole;
            TextView tvStatus;
            Button btnBan;
            Button btnDelete;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                tvUsername = itemView.findViewById(R.id.tv_username);
                tvEmail = itemView.findViewById(R.id.tv_email);
                tvRole = itemView.findViewById(R.id.tv_role);
                tvStatus = itemView.findViewById(R.id.tv_status);
                btnBan = itemView.findViewById(R.id.btn_ban);
                btnDelete = itemView.findViewById(R.id.btn_delete);
            }
        }
    }
}
