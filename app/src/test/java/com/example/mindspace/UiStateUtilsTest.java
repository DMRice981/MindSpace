package com.example.mindspace;

import com.mindspace.app.utils.UiStateUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UiStateUtilsTest {
    @Test
    public void networkErrorMessageUsesFallbackWhenErrorIsEmpty() {
        assertEquals("网络连接失败，请稍后重试", UiStateUtils.getNetworkErrorMessage(null));
        assertEquals("网络连接失败，请稍后重试", UiStateUtils.getNetworkErrorMessage(""));
    }

    @Test
    public void networkErrorMessageKeepsSpecificError() {
        assertEquals("同步失败：timeout", UiStateUtils.getNetworkErrorMessage("timeout"));
    }

    @Test
    public void loadingTextAddsDots() {
        assertEquals("登录中...", UiStateUtils.getLoadingText("登录"));
        assertEquals("发送中...", UiStateUtils.getLoadingText("发送"));
    }
}
