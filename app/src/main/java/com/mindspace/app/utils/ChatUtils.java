package com.mindspace.app.utils;

public class ChatUtils {
    public static String getChatPairKey(long userId, long friendId) {
        long first = Math.min(userId, friendId);
        long second = Math.max(userId, friendId);
        return first + "_" + second;
    }

    public static boolean isPending(String status) {
        return "pending".equals(status);
    }

    public static boolean isAccepted(String status) {
        return "accepted".equals(status);
    }

    public static boolean isRejected(String status) {
        return "rejected".equals(status);
    }
}
