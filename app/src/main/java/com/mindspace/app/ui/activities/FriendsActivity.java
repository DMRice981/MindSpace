package com.mindspace.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.mindspace.R;
import com.mindspace.app.data.repository.SupabaseRepository;
import com.mindspace.app.network.supabase.SupabaseDtos;
import com.mindspace.app.ui.adapters.FriendAdapter;
import com.mindspace.app.ui.adapters.FriendRequestAdapter;
import com.mindspace.app.utils.SessionManager;
import com.mindspace.app.utils.UiStateUtils;

public class FriendsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private SupabaseRepository supabaseRepository;
    private FriendAdapter friendAdapter;
    private FriendRequestAdapter requestAdapter;
    private TextInputEditText etSearchUser;
    private LinearLayout layoutSearchResult;
    private TextView tvSearchResult;
    private TextView tvEmptyRequests;
    private TextView tvEmptyFriends;
    private TextView tvFriendsLoading;
    private MaterialButton btnAddFriend;
    private MaterialButton btnSearchUser;
    private MaterialButton btnRefreshFriends;
    private SupabaseDtos.Profile searchedProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        sessionManager = new SessionManager(this);
        supabaseRepository = new SupabaseRepository();

        initViews();
        setupLists();
        setupListeners();
        loadData();
    }

    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbarFriends);
        toolbar.setNavigationOnClickListener(v -> finish());
        etSearchUser = findViewById(R.id.etSearchUser);
        layoutSearchResult = findViewById(R.id.layoutSearchResult);
        tvSearchResult = findViewById(R.id.tvSearchResult);
        tvFriendsLoading = findViewById(R.id.tvFriendsLoading);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnSearchUser = findViewById(R.id.btnSearchUser);
        btnRefreshFriends = findViewById(R.id.btnRefreshFriends);
        tvEmptyRequests = findViewById(R.id.tvEmptyRequests);
        tvEmptyFriends = findViewById(R.id.tvEmptyFriends);
    }

    private void setupLists() {
        RecyclerView rvRequests = findViewById(R.id.rvRequests);
        RecyclerView rvFriends = findViewById(R.id.rvFriends);
        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new FriendRequestAdapter();
        friendAdapter = new FriendAdapter();
        rvRequests.setAdapter(requestAdapter);
        rvFriends.setAdapter(friendAdapter);
    }

    private void setupListeners() {
        btnSearchUser.setOnClickListener(v -> searchUser());
        btnRefreshFriends.setOnClickListener(v -> loadData());
        btnAddFriend.setOnClickListener(v -> addFriend());
        requestAdapter.setOnRequestActionListener(new FriendRequestAdapter.OnRequestActionListener() {
            @Override
            public void onAccept(SupabaseDtos.FriendRequest request) {
                supabaseRepository.acceptFriendRequest(request, sessionManager.getUsername(), (success, message) -> runOnUiThread(() -> {
                    Toast.makeText(FriendsActivity.this, message, Toast.LENGTH_SHORT).show();
                    loadData();
                }));
            }

            @Override
            public void onReject(SupabaseDtos.FriendRequest request) {
                supabaseRepository.rejectFriendRequest(request.id, (success, message) -> runOnUiThread(() -> {
                    Toast.makeText(FriendsActivity.this, message, Toast.LENGTH_SHORT).show();
                    loadData();
                }));
            }
        });
        friendAdapter.setOnFriendActionListener(new FriendAdapter.OnFriendActionListener() {
            @Override
            public void onChat(SupabaseDtos.Friendship friend) {
                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                intent.putExtra("friendId", friend.friendId);
                intent.putExtra("friendUsername", friend.friendUsername);
                startActivity(intent);
            }

            @Override
            public void onDelete(SupabaseDtos.Friendship friend) {
                confirmDeleteFriend(friend);
            }
        });
    }

    private void loadData() {
        long userId = sessionManager.getSupabaseUserId();
        if (userId <= 0) {
            Toast.makeText(this, "联网用户未同步，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        setRefreshing(true);
        supabaseRepository.getIncomingRequests(userId, (requests, error) -> runOnUiThread(() -> {
            requestAdapter.setRequests(requests);
            tvEmptyRequests.setText(error == null ? "暂无好友申请" : UiStateUtils.getNetworkErrorMessage(error));
            tvEmptyRequests.setVisibility(requests.isEmpty() ? View.VISIBLE : View.GONE);
        }));
        supabaseRepository.getFriends(userId, (friends, error) -> runOnUiThread(() -> {
            setRefreshing(false);
            friendAdapter.setFriends(friends);
            tvEmptyFriends.setText(error == null ? "暂无好友，先搜索用户添加吧" : UiStateUtils.getNetworkErrorMessage(error));
            tvEmptyFriends.setVisibility(friends.isEmpty() ? View.VISIBLE : View.GONE);
        }));
    }

    private void setRefreshing(boolean refreshing) {
        tvFriendsLoading.setVisibility(refreshing ? View.VISIBLE : View.GONE);
        btnRefreshFriends.setEnabled(!refreshing);
        btnRefreshFriends.setText(refreshing ? UiStateUtils.getLoadingText("刷新") : "刷新");
    }

    private void searchUser() {
        String username = etSearchUser.getText() == null ? "" : etSearchUser.getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        btnSearchUser.setEnabled(false);
        btnSearchUser.setText(UiStateUtils.getLoadingText("查找"));
        supabaseRepository.searchUser(username, (profile, error) -> runOnUiThread(() -> {
            btnSearchUser.setEnabled(true);
            btnSearchUser.setText("查找");
            layoutSearchResult.setVisibility(View.VISIBLE);
            searchedProfile = profile;
            if (profile == null) {
                tvSearchResult.setText(error == null ? "未找到该用户" : error);
                btnAddFriend.setVisibility(View.GONE);
            } else if (profile.id == sessionManager.getSupabaseUserId()) {
                tvSearchResult.setText(profile.username + "（你自己）");
                btnAddFriend.setVisibility(View.GONE);
            } else {
                tvSearchResult.setText(profile.username + "\n" + (profile.email == null ? "" : profile.email));
                btnAddFriend.setVisibility(View.VISIBLE);
            }
        }));
    }

    private void addFriend() {
        if (searchedProfile == null) {
            return;
        }
        btnAddFriend.setEnabled(false);
        btnAddFriend.setText(UiStateUtils.getLoadingText("添加"));
        supabaseRepository.sendFriendRequest(sessionManager.getSupabaseUserId(), searchedProfile.id, (success, message) -> runOnUiThread(() -> {
            btnAddFriend.setEnabled(true);
            btnAddFriend.setText("添加");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (success) {
                btnAddFriend.setVisibility(View.GONE);
            }
        }));
    }

    private void confirmDeleteFriend(SupabaseDtos.Friendship friend) {
        new AlertDialog.Builder(this)
                .setTitle("删除好友")
                .setMessage("确定要删除好友 " + friend.friendUsername + " 吗？")
                .setPositiveButton("删除", (dialog, which) -> supabaseRepository.deleteFriend(sessionManager.getSupabaseUserId(), friend.friendId, (success, message) -> runOnUiThread(() -> {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    loadData();
                })))
                .setNegativeButton("取消", null)
                .show();
    }
}
