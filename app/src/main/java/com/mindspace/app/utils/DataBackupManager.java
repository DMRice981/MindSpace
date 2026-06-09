package com.mindspace.app.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindspace.app.data.local.AppDatabase;
import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.data.model.Note;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBackupManager {

    private static final String BACKUP_DIR = "MindSpace_Backups";
    private static final String BACKUP_PREFIX = "mindspace_backup_";
    private static final String BACKUP_EXTENSION = ".json";

    private Context context;
    private AppDatabase database;
    private Gson gson;

    public DataBackupManager(Context context) {
        this.context = context;
        this.database = AppDatabase.getInstance(context);
        this.gson = new Gson();
    }

    public static class BackupData {
        public String version;
        public long backupTime;
        public List<MoodRecord> moodRecords;
        public List<Note> notes;

        public BackupData() {
            this.version = "1.0";
            this.backupTime = System.currentTimeMillis();
        }
    }

    public File createBackup() {
        try {
            BackupData backupData = new BackupData();
            backupData.moodRecords = database.moodDao().getAllMoodRecordsSync();
            backupData.notes = database.noteDao().getAllNotesSync();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = BACKUP_PREFIX + timestamp + BACKUP_EXTENSION;

            File backupDir = getBackupDirectory();
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            File backupFile = new File(backupDir, fileName);
            FileWriter writer = new FileWriter(backupFile);
            gson.toJson(backupData, writer);
            writer.close();

            return backupFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean restoreBackup(File backupFile) {
        try {
            FileReader reader = new FileReader(backupFile);
            Type type = new TypeToken<BackupData>() {}.getType();
            BackupData backupData = gson.fromJson(reader, type);
            reader.close();

            if (backupData == null) {
                return false;
            }

            if (backupData.moodRecords != null) {
                for (MoodRecord record : backupData.moodRecords) {
                    record.setId(0);
                    database.moodDao().insert(record);
                }
            }

            if (backupData.notes != null) {
                for (Note note : backupData.notes) {
                    note.setId(0);
                    database.noteDao().insert(note);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public File[] getBackupFiles() {
        File backupDir = getBackupDirectory();
        if (backupDir.exists() && backupDir.isDirectory()) {
            return backupDir.listFiles((dir, name) -> name.startsWith(BACKUP_PREFIX) && name.endsWith(BACKUP_EXTENSION));
        }
        return new File[0];
    }

    private File getBackupDirectory() {
        File publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        return new File(publicDir, BACKUP_DIR);
    }

    public String formatBackupFileName(File file) {
        String name = file.getName();
        if (name.startsWith(BACKUP_PREFIX) && name.endsWith(BACKUP_EXTENSION)) {
            String timestamp = name.substring(BACKUP_PREFIX.length(), name.length() - BACKUP_EXTENSION.length());
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                Date date = inputFormat.parse(timestamp);
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
                return "备份 - " + outputFormat.format(date);
            } catch (Exception e) {
                return name;
            }
        }
        return name;
    }
}
