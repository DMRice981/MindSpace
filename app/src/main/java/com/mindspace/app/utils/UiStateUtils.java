package com.mindspace.app.utils;

public class UiStateUtils {
    private UiStateUtils() {
    }

    public static String getNetworkErrorMessage(String error) {
        if (error == null || error.trim().isEmpty()) {
            return "网络连接失败，请稍后重试";
        }
        return "同步失败：" + error;
    }

    public static String getLoadingText(String action) {
        return action + "中...";
    }
}
