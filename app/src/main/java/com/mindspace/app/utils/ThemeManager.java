package com.mindspace.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";
    
    public static final int THEME_MODE_LIGHT = 0;
    public static final int THEME_MODE_DARK = 1;
    public static final int THEME_MODE_SYSTEM = 2;
    
    private static ThemeManager instance;
    private SharedPreferences prefs;
    private int currentThemeMode;
    
    private ThemeManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentThemeMode = prefs.getInt(KEY_THEME_MODE, THEME_MODE_SYSTEM);
    }
    
    public static synchronized ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context);
        }
        return instance;
    }
    
    public void setThemeMode(int mode) {
        currentThemeMode = mode;
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply();
        
        switch (mode) {
            case THEME_MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_MODE_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    public int getThemeMode() {
        return currentThemeMode;
    }
    
    public String getThemeModeText() {
        switch (currentThemeMode) {
            case THEME_MODE_LIGHT:
                return "浅色模式";
            case THEME_MODE_DARK:
                return "深色模式";
            case THEME_MODE_SYSTEM:
            default:
                return "跟随系统";
        }
    }
    
    public void applyTheme() {
        setThemeMode(currentThemeMode);
    }
}
