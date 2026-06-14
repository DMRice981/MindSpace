package com.mindspace.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.mindspace.R;
import com.mindspace.app.data.model.User;
import com.mindspace.app.data.repository.SupabaseRepository;
import com.mindspace.app.data.repository.UserRepository;
import com.mindspace.app.utils.SessionManager;
import com.mindspace.app.utils.UiStateUtils;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etRegUsername;
    private TextInputEditText etRegEmail;
    private TextInputEditText etRegPassword;
    private TextInputEditText etRegConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private boolean isLoading;
    
    private UserRepository userRepository;
    private SupabaseRepository supabaseRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepository = new UserRepository(this);
        supabaseRepository = new SupabaseRepository();
        sessionManager = new SessionManager(this);
        
        initViews();
        setupListeners();
    }

    private void initViews() {
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> register());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void register() {
        if (isLoading) {
            return;
        }
        String username = etRegUsername.getText() != null ? etRegUsername.getText().toString().trim() : "";
        String email = etRegEmail.getText() != null ? etRegEmail.getText().toString().trim() : "";
        String password = etRegPassword.getText() != null ? etRegPassword.getText().toString().trim() : "";
        String confirmPassword = etRegConfirmPassword.getText() != null ? etRegConfirmPassword.getText().toString().trim() : "";
        
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (username.length() < 3) {
            Toast.makeText(this, "用户名至少3个字符", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (password.length() < 6) {
            Toast.makeText(this, "密码至少6个字符", Toast.LENGTH_SHORT).show();
            return;
        }
        
        setLoading(true);
        userRepository.findByUsername(username, user -> {
            runOnUiThread(() -> {
                if (user != null) {
                    setLoading(false);
                    Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                } else {
                    User newUser = new User(username, password, email, false);
                    userRepository.insert(newUser, insertedUser -> {
                        runOnUiThread(() -> {
                            sessionManager.login(insertedUser);
                            syncProfileAndNavigate(insertedUser);
                        });
                    });
                }
            });
        });
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        btnRegister.setEnabled(!loading);
        tvBackToLogin.setEnabled(!loading);
        btnRegister.setText(loading ? UiStateUtils.getLoadingText("注册") : getString(R.string.btn_register));
    }

    private void syncProfileAndNavigate(User user) {
        supabaseRepository.syncProfile(user, (profile, error) -> runOnUiThread(() -> {
            if (profile != null) {
                sessionManager.setSupabaseUserId(profile.id);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                setLoading(false);
                Toast.makeText(this, UiStateUtils.getNetworkErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
