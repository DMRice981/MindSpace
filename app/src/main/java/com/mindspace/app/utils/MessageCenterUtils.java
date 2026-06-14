package com.mindspace.app.utils;

public class MessageCenterUtils {
    private static final int PREVIEW_LIMIT = 15;

    private MessageCenterUtils() {
    }

    public static String getMessagePreview(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "还没有聊天记录";
        }
        String trimmed = content.trim();
        if (trimmed.length() <= PREVIEW_LIMIT) {
            return trimmed;
        }
        return trimmed.substring(0, PREVIEW_LIMIT) + "...";
    }

    public static String getUnreadText(int count) {
        if (count <= 0) {
            return "";
        }
        if (count > 99) {
            return "99+";
        }
        return String.valueOf(count);
    }

    public static String getDisplayTime(String createdAt) {
        if (createdAt == null || createdAt.trim().isEmpty()) {
            return "刚刚";
        }
        String value = createdAt.replace("T", " ").replace("Z", "");
        if (value.length() >= 16) {
            return value.substring(0, 16);
        }
        return value;
    }
}
