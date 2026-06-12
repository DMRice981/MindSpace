package com.mindspace.app.ui.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.mindspace.R;
import com.mindspace.app.data.repository.SupabaseRepository;
import com.mindspace.app.ui.adapters.ChatMessageAdapter;
import com.mindspace.app.utils.SessionManager;
import com.mindspace.app.utils.UiStateUtils;

public class ChatActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private SupabaseRepository supabaseRepository;
    private ChatMessageAdapter adapter;
    private TextInputEditText etMessage;
    private TextView tvEmptyMessages;
    private MaterialButton btnSendMessage;
    private RecyclerView rvMessages;
    private boolean isSending;
    private long friendId;
    private String friendUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sessionManager = new SessionManager(this);
        supabaseRepository = new SupabaseRepository();
        friendId = getIntent().getLongExtra("friendId", -1L);
        friendUsername = getIntent().getStringExtra("friendUsername");

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
        MaterialToolbar toolbar = findViewById(R.id.toolbarChat);
        toolbar.setTitle(friendUsername == null ? "聊天" : friendUsername);
        toolbar.setNavigationOnClickListener(v -> finish());
        etMessage = findViewById(R.id.etMessage);
        tvEmptyMessages = findViewById(R.id.tvEmptyMessages);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        rvMessages = findViewById(R.id.rvMessages);
    }

    private void setupList() {
        adapter = new ChatMessageAdapter();
        adapter.setCurrentUserId(sessionManager.getSupabaseUserId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(adapter);
    }

    private void setupListeners() {
        btnSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        long userId = sessionManager.getSupabaseUserId();
        if (userId <= 0 || friendId <= 0) {
            Toast.makeText(this, "聊天用户信息无效", Toast.LENGTH_SHORT).show();
            return;
        }
        supabaseRepository.getChatMessages(userId, friendId, (messages, error) -> runOnUiThread(() -> {
            adapter.setMessages(messages);
            tvEmptyMessages.setText(error == null ? "暂无消息，打个招呼吧" : UiStateUtils.getNetworkErrorMessage(error));
            tvEmptyMessages.setVisibility(messages.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
            if (!messages.isEmpty()) {
                rvMessages.scrollToPosition(messages.size() - 1);
            }
        }));
    }

    private void setSending(boolean sending) {
        isSending = sending;
        btnSendMessage.setEnabled(!sending);
        btnSendMessage.setText(sending ? UiStateUtils.getLoadingText("发送") : "发送");
    }

    private void sendMessage() {
        if (isSending) {
            return;
        }
        String content = etMessage.getText() == null ? "" : etMessage.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入消息", Toast.LENGTH_SHORT).show();
            return;
        }
        setSending(true);
        supabaseRepository.sendMessage(sessionManager.getSupabaseUserId(), friendId, content, (success, message) -> runOnUiThread(() -> {
            setSending(false);
            if (success) {
                etMessage.setText("");
                loadMessages();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
