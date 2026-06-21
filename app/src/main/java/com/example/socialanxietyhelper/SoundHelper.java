package com.example.socialanxietyhelper;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

@android.annotation.SuppressLint("MissingPermission")
public class SoundHelper {

    // Buton tıklama — kısa tık
    public static void playClick(Context ctx) {
        try {
            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 60);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void playAnchorEarned(Context ctx) {
        try {
            // Yüksek frekanslı kristal efekti — kısa aralıklarla üst üste
            new android.os.Handler().postDelayed(() -> {
                ToneGenerator tg1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 55);
                tg1.startTone(ToneGenerator.TONE_CDMA_HIGH_PBX_SS, 80);
            }, 0);
            new android.os.Handler().postDelayed(() -> {
                ToneGenerator tg2 = new ToneGenerator(AudioManager.STREAM_MUSIC, 70);
                tg2.startTone(ToneGenerator.TONE_CDMA_HIGH_PBX_SLS, 100);
            }, 90);
            new android.os.Handler().postDelayed(() -> {
                ToneGenerator tg3 = new ToneGenerator(AudioManager.STREAM_MUSIC, 85);
                tg3.startTone(ToneGenerator.TONE_CDMA_HIGH_PBX_S_X4, 150);
            }, 200);
            new android.os.Handler().postDelayed(() -> {
                ToneGenerator tg4 = new ToneGenerator(AudioManager.STREAM_MUSIC, 50);
                tg4.startTone(ToneGenerator.TONE_CDMA_HIGH_PBX_SS, 80);
            }, 370);

        } catch (Exception e) { e.printStackTrace(); }

        // Hafif titreşim
        Vibrator vib = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        if (vib != null && vib.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(
                        new long[]{0, 40, 40, 60}, -1));
            } else {
                vib.vibrate(new long[]{0, 40, 40, 60}, -1);
            }
        }
    }

    // Görev tamamlama — tatmin edici ses + titreşim
    public static void playTaskComplete(Context ctx) {
        try {
            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 90);
            tg.startTone(ToneGenerator.TONE_PROP_ACK, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Titreşim
        Vibrator vib = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        if (vib != null && vib.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vib.vibrate(80);
            }
        }
    }
}