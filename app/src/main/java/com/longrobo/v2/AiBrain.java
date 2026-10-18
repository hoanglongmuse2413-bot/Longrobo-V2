package com.longrobo.v2;

public class AiBrain {
    public static String getReply(String customerMessage) {
        if (customerMessage == null) return "";
        String lower = customerMessage.toLowerCase();
        if (lower.contains("gia") || lower.contains("bao nhieu")) {
            return "Da, ban quan tam san pham nao a? Minh gui bao gia chi tiet cho ban nhe.";
        }
        if (lower.contains("ship") || lower.contains("giao")) {
            return "Ben minh giao hang toan quoc, mien phi ship tu 2 san pham a.";
        }
        if (lower.contains("chao") || lower.contains("hello")) {
            return "Chao ban! Minh la tro ly cua Long, ban can tu van gi a?";
        }
        return "Cam on ban da nhan tin! Long se tu van chi tiet cho ban ngay nhe.";
    }
}
