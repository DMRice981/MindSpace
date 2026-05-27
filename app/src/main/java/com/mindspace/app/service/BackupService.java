package com.mindspace.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BackupService extends Service {

    private static final String TAG = "BackupService";
    private static final String BACKUP_FOLDER = "mindspace_backups";
    public static final String ACTION_BACKUP = "backup";
    public static final String ACTION_RESTORE = "restore";
    public static final String EXTRA_ACTION = "action";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra(EXTRA_ACTION)) {
            String action = intent.getStringExtra(EXTRA_ACTION);
            
            if (ACTION_BACKUP.equals(action)) {
                performBackup();
            } else if (ACTION_RESTORE.equals(action)) {
                performRestore();
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performBackup() {
        new Thread(() -> {
            FileOutputStream fos = null;
            try {
                File backupDir = new File(getExternalFilesDir(null), BACKUP_FOLDER);
                if (!backupDir.exists() && !backupDir.mkdirs()) {
                    Log.e(TAG, "Failed to create backup directory");
                    return;
                }

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
                File backupFile = new File(backupDir, "mindspace_backup_" + timestamp + ".json");

                String backupContent = createBackupContent(timestamp);
                
                fos = new FileOutputStream(backupFile);
                fos.write(backupContent.getBytes(StandardCharsets.UTF_8));
                fos.flush();

                Log.d(TAG, "Backup completed: " + backupFile.getAbsolutePath());
            } catch (IOException e) {
                Log.e(TAG, "Backup failed", e);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing file", e);
                    }
                }
            }
        }).start();
    }

    private void performRestore() {
        new Thread(() -> {
            FileInputStream fis = null;
            try {
                File backupDir = new File(getExternalFilesDir(null), BACKUP_FOLDER);
                if (!backupDir.exists()) {
                    Log.d(TAG, "No backup directory found");
                    return;
                }

                File[] backupFiles = backupDir.listFiles();
                if (backupFiles == null || backupFiles.length == 0) {
                    Log.d(TAG, "No backup files found");
                    return;
                }

                File latestBackup = backupFiles[backupFiles.length - 1];
                
                fis = new FileInputStream(latestBackup);
                byte[] data = new byte[(int) latestBackup.length()];
                int bytesRead = fis.read(data);
                
                if (bytesRead > 0) {
                    String jsonContent = new String(data, StandardCharsets.UTF_8);
                    Log.d(TAG, "Restore from: " + latestBackup.getName());
                    Log.d(TAG, "Backup content: " + jsonContent);
                }
                
            } catch (IOException e) {
                Log.e(TAG, "Restore failed", e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing file", e);
                    }
                }
            }
        }).start();
    }
    
    private String createBackupContent(String timestamp) {
        return "{\n" +
            "  \"backup_date\": \"" + timestamp + "\",\n" +
            "  \"version\": 1\n" +
            "}\n";
    }
}
