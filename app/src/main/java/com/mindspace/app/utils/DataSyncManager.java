package com.mindspace.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mindspace.app.data.local.AppDatabase;
import com.mindspace.app.data.model.MoodRecord;
import com.mindspace.app.data.model.Note;

import java.util.List;

public class DataSyncManager {

    private static final String PREF_NAME = "sync_prefs";
    private static final String KEY_LAST_SYNC_TIME = "last_sync_time";
    private static final String KEY_SYNC_ENABLED = "sync_enabled";

    private Context context;
    private AppDatabase database;
    private SharedPreferences prefs;

    public DataSyncManager(Context context) {
        this.context = context;
        this.database = AppDatabase.getInstance(context);
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setSyncEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SYNC_ENABLED, enabled).apply();
    }

    public boolean isSyncEnabled() {
        return prefs.getBoolean(KEY_SYNC_ENABLED, false);
    }

    public long getLastSyncTime() {
        return prefs.getLong(KEY_LAST_SYNC_TIME, 0);
    }

    private void updateLastSyncTime() {
        prefs.edit().putLong(KEY_LAST_SYNC_TIME, System.currentTimeMillis()).apply();
    }

    public static class SyncResult {
        public boolean success;
        public int syncedMoodCount;
        public int syncedNoteCount;
        public String message;

        public SyncResult() {
            this.success = false;
            this.syncedMoodCount = 0;
            this.syncedNoteCount = 0;
            this.message = "";
        }
    }

    public SyncResult performSync() {
        SyncResult result = new SyncResult();
        try {
            long lastSync = getLastSyncTime();
            
            List<MoodRecord> moodRecords = database.moodDao().getMoodsSince(lastSync);
            List<Note> notes = database.noteDao().getAllNotesSync();
            
            result.syncedMoodCount = moodRecords.size();
            result.syncedNoteCount = notes.size();
            
            updateLastSyncTime();
            result.success = true;
            result.message = "同步成功！\n心情记录: " + moodRecords.size() + " 条\n笔记: " + notes.size() + " 条";
            
        } catch (Exception e) {
            result.success = false;
            result.message = "同步失败: " + e.getMessage();
        }
        return result;
    }

    public String getSyncStatus() {
        if (!isSyncEnabled()) {
            return "同步功能已关闭";
        }
        long lastSync = getLastSyncTime();
        if (lastSync == 0) {
            return "尚未同步";
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        return "上次同步: " + sdf.format(new java.util.Date(lastSync));
    }
}
