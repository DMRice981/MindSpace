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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private Button btnUserLogin;
    private Button btnAdminLogin;
    private boolean isLoading;
    
    private UserRepository userRepository;
    private SupabaseRepository supabaseRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MindSpace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = new UserRepository(this);
        supabaseRepository = new SupabaseRepository();
        sessionManager = new SessionManager(this);
        
        if (sessionManager.isLoggedIn()) {
            navigateToMain();
            return;
        }
        
        userRepository.initDefaultUsers(null);
        
        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        btnUserLogin = findViewById(R.id.btnQuickUser);
        btnAdminLogin = findViewById(R.id.btnQuickAdmin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> login());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        btnUserLogin.setOnClickListener(v -> {
            etUsername.setText("user");
            etPassword.setText("user123");
            login();
        });
        btnAdminLogin.setOnClickListener(v -> {
            etUsername.setText("admin");
            etPassword.setText("admin123");
            login();
        });
    }

    private void login() {
        if (isLoading) {
            return;
        }
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        
        setLoading(true);
        userRepository.login(username, password, user -> {
            runOnUiThread(() -> {
                if (user != null) {
                    sessionManager.login(user);
                    syncProfileAndNavigate(user);
                } else {
                    setLoading(false);
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void syncProfileAndNavigate(User user) {
        supabaseRepository.syncProfile(user, (profile, error) -> runOnUiThread(() -> {
            if (profile != null) {
                sessionManager.setSupabaseUserId(profile.id);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                navigateToMain();
            } else {
                setLoading(false);
                Toast.makeText(this, UiStateUtils.getNetworkErrorMessage(error), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
        btnLogin.setEnabled(!loading);
        btnUserLogin.setEnabled(!loading);
        btnAdminLogin.setEnabled(!loading);
        tvRegister.setEnabled(!loading);
        btnLogin.setText(loading ? UiStateUtils.getLoadingText("登录") : getString(R.string.btn_login));
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
