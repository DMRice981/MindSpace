package com.mindspace.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindspace.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mindspace.app.data.repository.SupabaseRepository;
import com.mindspace.app.network.supabase.SupabaseDtos;
import com.mindspace.app.ui.adapters.ConversationAdapter;
import com.mindspace.app.utils.MessageCenterUtils;
import com.mindspace.app.utils.SessionManager;
import com.mindspace.app.utils.UiStateUtils;

public class MessagesActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private SupabaseRepository supabaseRepository;
    private ConversationAdapter adapter;
    private TextView tvMessagesLoading;
    private TextView tvEmptyConversations;
    private TextView tvRequestBadge;
    private TextView tvFriendRequestHint;
    private MaterialButton btnRefreshMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        sessionManager = new SessionManager(this);
        supabaseRepository = new SupabaseRepository();
        initViews();
        setupList();
        setupListeners();
        loadMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            loadMessages();
        }
    }

    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbarMessages);
        toolbar.setNavigationOnClickListener(v -> finish());
        tvMessagesLoading = findViewById(R.id.tvMessagesLoading);
        tvEmptyConversations = findViewById(R.id.tvEmptyConversations);
        tvRequestBadge = findViewById(R.id.tvRequestBadge);
        tvFriendRequestHint = findViewById(R.id.tvFriendRequestHint);
        btnRefreshMessages = findViewById(R.id.btnRefreshMessages);
    }

    private void setupList() {
        RecyclerView rvConversations = findViewById(R.id.rvConversations);
        rvConversations.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConversationAdapter();
        rvConversations.setAdapter(adapter);
    }

    private void setupListeners() {
        btnRefreshMessages.setOnClickListener(v -> loadMessages());
        MaterialCardView cardFriendRequests = findViewById(R.id.cardFriendRequests);
        cardFriendRequests.setOnClickListener(v -> startActivity(new Intent(this, FriendsActivity.class)));
        adapter.setOnConversationClickListener(conversation -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("friendId", conversation.friendId);
            intent.putExtra("friendUsername", conversation.friendUsername);
            startActivity(intent);
        });
    }

    private void loadMessages() {
        long userId = sessionManager.getSupabaseUserId();
        if (userId <= 0) {
            Toast.makeText(this, "联网用户未同步，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        setLoading(true);
        supabaseRepository.getConversations(userId, (conversations, error) -> runOnUiThread(() -> {
            setLoading(false);
            adapter.setConversations(conversations);
            tvEmptyConversations.setText(error == null ? "暂无聊天，去添加好友开始交流吧" : UiStateUtils.getNetworkErrorMessage(error));
            tvEmptyConversations.setVisibility(conversations.isEmpty() ? View.VISIBLE : View.GONE);
        }));
        supabaseRepository.getIncomingRequests(userId, (requests, error) -> runOnUiThread(() -> {
            int count = requests == null ? 0 : requests.size();
            String unreadText = MessageCenterUtils.getUnreadText(count);
            tvRequestBadge.setText(unreadText);
            tvRequestBadge.setVisibility(unreadText.isEmpty() ? View.GONE : View.VISIBLE);
            tvFriendRequestHint.setText(count > 0 ? "你有 " + count + " 条好友申请待处理" : "管理好友、处理申请");
        }));
    }

    private void setLoading(boolean loading) {
        tvMessagesLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnRefreshMessages.setEnabled(!loading);
        btnRefreshMessages.setText(loading ? UiStateUtils.getLoadingText("刷新") : "刷新");
    }
}
