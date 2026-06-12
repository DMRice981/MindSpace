package com.mindspace.app.ui.fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.SwitchCompat;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mindspace.R;
import com.mindspace.app.ui.activities.FriendsActivity;
import com.mindspace.app.ui.activities.LoginActivity;
import com.mindspace.app.utils.DataBackupManager;
import com.mindspace.app.utils.DataSyncManager;
import com.mindspace.app.utils.ReminderManager;
import com.mindspace.app.utils.SessionManager;
import com.mindspace.app.utils.ThemeManager;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;
    private ThemeManager themeManager;
    private DataBackupManager backupManager;
    private DataSyncManager syncManager;
    private ReminderManager reminderManager;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvRole;
    private TextView tvThemeMode;
    private TextView tvSyncStatus;
    private TextView tvReminderStatus;
    private LinearLayout btnLogout;
    private LinearLayout btnTheme;
    private LinearLayout btnBackup;
    private LinearLayout btnRestore;
    private LinearLayout btnSync;
    private LinearLayout btnReminder;
    private LinearLayout btnFriends;
    private ImageView ivAvatar;

    private static final int[] AVATAR_IDS = {
        R.drawable.avatar_0,
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_3,
        R.drawable.avatar_4,
        R.drawable.avatar_5
    };

    private static final String[] AVATAR_NAMES = {
        "avatar_0",
        "avatar_1",
        "avatar_2",
        "avatar_3",
        "avatar_4",
        "avatar_5"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        sessionManager = new SessionManager(requireContext());
        themeManager = ThemeManager.getInstance(requireContext());
        backupManager = new DataBackupManager(requireContext());
        syncManager = new DataSyncManager(requireContext());
        reminderManager = new ReminderManager(requireContext());
        
        initViews(view);
        setupUserInfo();
        setupAvatar();
        setupTheme();
        setupBackup();
        setupSync();
        setupReminder();
        setupFriends();
        setupLogout();
        
        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvRole = view.findViewById(R.id.tvRole);
        tvThemeMode = view.findViewById(R.id.tvThemeMode);
        tvSyncStatus = view.findViewById(R.id.tvSyncStatus);
        tvReminderStatus = view.findViewById(R.id.tvReminderStatus);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnTheme = view.findViewById(R.id.btnTheme);
        btnBackup = view.findViewById(R.id.btnBackup);
        btnRestore = view.findViewById(R.id.btnRestore);
        btnSync = view.findViewById(R.id.btnSync);
        btnReminder = view.findViewById(R.id.btnReminder);
        btnFriends = view.findViewById(R.id.btnFriends);
        ivAvatar = view.findViewById(R.id.ivAvatar);
    }

    private void setupUserInfo() {
        tvUsername.setText(sessionManager.getUsername());
        tvEmail.setText(sessionManager.getEmail());
        tvRole.setText(sessionManager.isAdmin() ? "管理员" : "普通用户");
    }

    private void setupAvatar() {
        String avatarName = sessionManager.getAvatar();
        int avatarIndex = getAvatarIndex(avatarName);
        if (avatarIndex >= 0) {
            ivAvatar.setImageResource(AVATAR_IDS[avatarIndex]);
        }

        ivAvatar.setOnClickListener(v -> showAvatarDialog());
    }

    private int getAvatarIndex(String avatarName) {
        for (int i = 0; i < AVATAR_NAMES.length; i++) {
            if (AVATAR_NAMES[i].equals(avatarName)) {
                return i;
            }
        }
        return 0;
    }

    private void showAvatarDialog() {
        new AlertDialog.Builder(requireContext())
            .setTitle("选择头像")
            .setItems(new String[]{"头像 1", "头像 2", "头像 3", "头像 4", "头像 5", "头像 6"}, (dialog, which) -> {
                sessionManager.setAvatar(AVATAR_NAMES[which]);
                ivAvatar.setImageResource(AVATAR_IDS[which]);
                dialog.dismiss();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void setupTheme() {
        tvThemeMode.setText(themeManager.getThemeModeText());
        
        btnTheme.setOnClickListener(v -> showThemeDialog());
    }

    private void setupBackup() {
        btnBackup.setOnClickListener(v -> performBackup());
        btnRestore.setOnClickListener(v -> showRestoreDialog());
    }

    private void setupSync() {
        tvSyncStatus.setText(syncManager.getSyncStatus());
        btnSync.setOnClickListener(v -> showSyncDialog());
    }

    private void showSyncDialog() {
        final boolean[] syncEnabled = {syncManager.isSyncEnabled()};
        new AlertDialog.Builder(requireContext())
            .setTitle("数据同步")
            .setMessage(syncManager.getSyncStatus())
            .setPositiveButton("立即同步", (dialog, which) -> performSync())
            .setNeutralButton(syncEnabled[0] ? "关闭同步" : "开启同步", (dialog, which) -> {
                syncEnabled[0] = !syncEnabled[0];
                syncManager.setSyncEnabled(syncEnabled[0]);
                tvSyncStatus.setText(syncManager.getSyncStatus());
            })
            .setNegativeButton("关闭", null)
            .show();
    }

    private void performSync() {
        new Thread(() -> {
            DataSyncManager.SyncResult result = syncManager.performSync();
            requireActivity().runOnUiThread(() -> {
                tvSyncStatus.setText(syncManager.getSyncStatus());
                new AlertDialog.Builder(requireContext())
                    .setTitle(result.success ? "同步成功" : "同步失败")
                    .setMessage(result.message)
                    .setPositiveButton("确定", null)
                    .show();
            });
        }).start();
    }

    private void performBackup() {
        new Thread(() -> {
            File backupFile = backupManager.createBackup();
            requireActivity().runOnUiThread(() -> {
                if (backupFile != null) {
                    new AlertDialog.Builder(requireContext())
                        .setTitle("备份成功")
                        .setMessage("备份已保存到:\n" + backupFile.getAbsolutePath())
                        .setPositiveButton("确定", null)
                        .show();
                } else {
                    new AlertDialog.Builder(requireContext())
                        .setTitle("备份失败")
                        .setMessage("无法创建备份文件，请检查存储权限")
                        .setPositiveButton("确定", null)
                        .show();
                }
            });
        }).start();
    }

    private void showRestoreDialog() {
        File[] backupFiles = backupManager.getBackupFiles();
        if (backupFiles.length == 0) {
            new AlertDialog.Builder(requireContext())
                .setTitle("恢复数据")
                .setMessage("没有找到备份文件")
                .setPositiveButton("确定", null)
                .show();
            return;
        }

        String[] fileNames = new String[backupFiles.length];
        for (int i = 0; i < backupFiles.length; i++) {
            fileNames[i] = backupManager.formatBackupFileName(backupFiles[i]);
        }

        new AlertDialog.Builder(requireContext())
            .setTitle("选择备份文件")
            .setItems(fileNames, (dialog, which) -> {
                performRestore(backupFiles[which]);
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void performRestore(File backupFile) {
        new AlertDialog.Builder(requireContext())
            .setTitle("确认恢复")
            .setMessage("恢复将添加备份中的数据到当前数据中。\n确定要继续吗？")
            .setPositiveButton("确定", (dialog, which) -> {
                new Thread(() -> {
                    boolean success = backupManager.restoreBackup(backupFile);
                    requireActivity().runOnUiThread(() -> {
                        if (success) {
                            new AlertDialog.Builder(requireContext())
                                .setTitle("恢复成功")
                                .setMessage("数据已成功恢复")
                                .setPositiveButton("确定", null)
                                .show();
                        } else {
                            new AlertDialog.Builder(requireContext())
                                .setTitle("恢复失败")
                                .setMessage("无法恢复数据")
                                .setPositiveButton("确定", null)
                                .show();
                        }
                    });
                }).start();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void showThemeDialog() {
        String[] themes = {"浅色模式", "深色模式", "跟随系统"};
        int currentMode = themeManager.getThemeMode();
        
        new AlertDialog.Builder(requireContext())
            .setTitle("选择主题")
            .setSingleChoiceItems(themes, currentMode, (dialog, which) -> {
                themeManager.setThemeMode(which);
                tvThemeMode.setText(themeManager.getThemeModeText());
                dialog.dismiss();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void setupReminder() {
        updateReminderStatus();
        btnReminder.setOnClickListener(v -> showReminderDialog());
    }

    private void updateReminderStatus() {
        if (reminderManager.isReminderEnabled()) {
            int hour = reminderManager.getReminderHour();
            int minute = reminderManager.getReminderMinute();
            tvReminderStatus.setText(String.format("已开启 - %02d:%02d", hour, minute));
        } else {
            tvReminderStatus.setText("未开启");
        }
    }

    private void showReminderDialog() {
        boolean isEnabled = reminderManager.isReminderEnabled();
        int currentHour = isEnabled ? reminderManager.getReminderHour() : 21;
        int currentMinute = isEnabled ? reminderManager.getReminderMinute() : 0;

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_reminder, null);
        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        SwitchCompat switchReminder = dialogView.findViewById(R.id.switchReminder);

        timePicker.setIs24HourView(true);
        timePicker.setHour(currentHour);
        timePicker.setMinute(currentMinute);
        switchReminder.setChecked(isEnabled);

        new AlertDialog.Builder(requireContext())
                .setTitle("每日提醒")
                .setView(dialogView)
                .setPositiveButton("保存", (dialog, which) -> {
                    if (switchReminder.isChecked()) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        reminderManager.createNotificationChannel();
                        reminderManager.setDailyReminder(hour, minute);
                    } else {
                        reminderManager.cancelReminder();
                    }
                    updateReminderStatus();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void setupFriends() {
        btnFriends.setOnClickListener(v -> startActivity(new Intent(requireContext(), FriendsActivity.class)));
    }

    private void setupLogout() {
        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
