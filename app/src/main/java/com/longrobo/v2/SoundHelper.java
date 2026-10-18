package com.longrobo.v2;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;

public class SoundHelper {
    public static void playTing(Context ctx) {
        try {
            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(tg::release, 500);
        } catch (Exception e) {}
    }
}
