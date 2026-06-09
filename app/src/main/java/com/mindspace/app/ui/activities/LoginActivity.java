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
import com.mindspace.app.data.repository.UserRepository;
import com.mindspace.app.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private Button btnUserLogin;
    private Button btnAdminLogin;
    
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MindSpace);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = new UserRepository(this);
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
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        
        userRepository.login(username, password, user -> {
            runOnUiThread(() -> {
                if (user != null) {
                    sessionManager.login(user);
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    navigateToMain();
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
