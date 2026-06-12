package com.mindspace.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mindspace.app.data.model.User;

public class SessionManager {
    private static final String PREF_NAME = "MindSpaceSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_ADMIN = "isAdmin";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_SUPABASE_USER_ID = "supabaseUserId";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void login(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putBoolean(KEY_IS_ADMIN, user.isAdmin());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_AVATAR, user.getAvatar());
        editor.apply();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isAdmin() {
        return prefs.getBoolean(KEY_IS_ADMIN, false);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getAvatar() {
        return prefs.getString(KEY_AVATAR, "avatar_0");
    }

    public void setAvatar(String avatar) {
        editor.putString(KEY_AVATAR, avatar);
        editor.apply();
    }

    public long getSupabaseUserId() {
        return prefs.getLong(KEY_SUPABASE_USER_ID, -1L);
    }

    public void setSupabaseUserId(long userId) {
        editor.putLong(KEY_SUPABASE_USER_ID, userId);
        editor.apply();
    }
}
