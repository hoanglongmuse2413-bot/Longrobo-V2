package com.longrobo.v2;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class LongRoboAccessibilityService extends AccessibilityService {
    public static LongRoboAccessibilityService instance;
    @Override public void onServiceConnected() { super.onServiceConnected(); instance = this; }
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {}
    @Override public void onInterrupt() {}

    public static boolean clickByText(String text) {
        if (instance == null) return false;
        AccessibilityNodeInfo root = instance.getRootInActiveWindow();
        if (root == null) return false;
        for (AccessibilityNodeInfo node : root.findAccessibilityNodeInfosByText(text)) {
            if (node.isClickable()) { node.performAction(AccessibilityNodeInfo.ACTION_CLICK); return true; }
            if (node.getParent() != null && node.getParent().isClickable()) {
                node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK); return true;
            }
        }
        return false;
    }
}
