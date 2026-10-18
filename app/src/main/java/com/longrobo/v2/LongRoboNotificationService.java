package com.longrobo.v2;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class LongRoboNotificationService extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null) return;
        String pkg = sbn.getPackageName();
        if (!pkg.contains("facebook") && !pkg.contains("messenger") && !pkg.contains("zalo")) return;
        Notification notif = sbn.getNotification();
        if (notif == null || notif.extras == null) return;
        String text = notif.extras.getCharSequence(Notification.EXTRA_TEXT) != null ? notif.extras.getCharSequence(Notification.EXTRA_TEXT).toString() : "";
        if (text.isEmpty()) return;
        String replyText = AiBrain.getReply(text);
        try {
            if (notif.actions != null) {
                for (Notification.Action act : notif.actions) {
                    if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) {
                        android.content.Intent intent = new android.content.Intent();
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence(act.getRemoteInputs()[0].getResultKey(), replyText);
                        android.app.RemoteInput.addResultsToIntent(act.getRemoteInputs(), intent, bundle);
                        act.actionIntent.send(getApplicationContext(), 0, intent);
                        SoundHelper.playTing(getApplicationContext());
                        break;
                    }
                }
            }
        } catch (Exception e) {}
    }
}
