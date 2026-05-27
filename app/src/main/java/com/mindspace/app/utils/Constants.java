package com.mindspace.app.utils;

public class Constants {

    public static final class App {
        public static final String APP_NAME = "MindSpace";
        public static final String DATABASE_NAME = "mindspace_db";
        public static final int DATABASE_VERSION = 1;
    }

    public static final class Colors {
        public static final String PRIMARY = "#6C63FF";
        public static final String ACCENT = "#FF6584";
        public static final String BACKGROUND = "#F5F5F5";
        public static final String TEXT_PRIMARY = "#333333";
        public static final String TEXT_SECONDARY = "#666666";
        public static final String SUCCESS = "#4CAF50";
        public static final String WARNING = "#FF9800";
        public static final String INFO = "#2196F3";
        public static final String ERROR = "#F44336";
    }

    public static final class Intent {
        public static final String EXTRA_MOOD_ID = "extra_mood_id";
        public static final String EXTRA_NOTE_ID = "extra_note_id";
        public static final String EXTRA_CATEGORY = "extra_category";
    }

    public static final class Request {
        public static final int PICK_IMAGE = 1001;
        public static final int CAMERA_REQUEST = 1002;
    }

    public static final class Preference {
        public static final String PREF_NAME = "mindspace_prefs";
        public static final String KEY_FIRST_LAUNCH = "first_launch";
        public static final String KEY_THEME_MODE = "theme_mode";
        public static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    }

    public static final class Notification {
        public static final String CHANNEL_ID = "mindspace_channel";
        public static final String CHANNEL_NAME = "MindSpace提醒";
        public static final String CHANNEL_DESCRIPTION = "心情记录提醒";
    }

    public static final class Config {
        public static final int MAX_MOOD_CONTENT_LENGTH = 500;
        public static final int MAX_NOTE_TITLE_LENGTH = 100;
        public static final int MAX_NOTE_CONTENT_LENGTH = 5000;
        public static final int RECENT_ITEMS_LIMIT = 10;
        public static final int CHART_DAYS_RANGE = 30;
    }
}
