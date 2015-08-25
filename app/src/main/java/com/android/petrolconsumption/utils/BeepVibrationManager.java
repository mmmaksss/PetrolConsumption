package com.android.petrolconsumption.utils;

import com.android.petrolconsumption.PetrolConsumptionApplication;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class BeepVibrationManager {
    private static final int VIBRATE_TIME = 50;

    private static MediaPlayer mediaPlayer;
    private static Vibrator vibratorPlayer;
    public synchronized static void prepareBeepVibrationPlayer(int soundID) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(PetrolConsumptionApplication.getAppContext(),
                    soundID);
        }
        if (vibratorPlayer == null) {
            vibratorPlayer = (Vibrator) PetrolConsumptionApplication.getAppContext()
                    .getSystemService(Context.VIBRATOR_SERVICE);

        }
    }

    public synchronized static void playBeep() {
        AudioManager audioManager = (AudioManager) PetrolConsumptionApplication
                .getAppContext().getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()) {
        case AudioManager.RINGER_MODE_NORMAL:
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
            break;
        case AudioManager.RINGER_MODE_VIBRATE:
            if (vibratorPlayer != null) {
                vibratorPlayer.vibrate(BeepVibrationManager.VIBRATE_TIME);
            }
            break;
        case AudioManager.RINGER_MODE_SILENT:
        default:
            break;
        }
    }

    public synchronized static void playVibration() {
        AudioManager audioManager = (AudioManager) PetrolConsumptionApplication
                .getAppContext().getSystemService(Context.AUDIO_SERVICE);
        switch (audioManager.getRingerMode()) {
        case AudioManager.RINGER_MODE_NORMAL:
        case AudioManager.RINGER_MODE_VIBRATE:
            if (vibratorPlayer != null) {
                vibratorPlayer.vibrate(BeepVibrationManager.VIBRATE_TIME);
            }
            break;
        case AudioManager.RINGER_MODE_SILENT:
        default:
            break;
        }
    }

    public synchronized static void releaseBeepVibrationPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (vibratorPlayer != null) {
            vibratorPlayer.cancel();
            vibratorPlayer = null;
        }
    }
}
