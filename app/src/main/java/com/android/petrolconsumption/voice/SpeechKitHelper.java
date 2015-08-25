package com.android.petrolconsumption.voice;

import android.content.Context;

import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences;
import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences.VoiceRecognizerPreferenceKey;
import com.android.petrolconsumption.utils.Utility;
import com.android.petrolconsumption.voice.google.GoogleSpeechKit;

public final class SpeechKitHelper {
    public enum ActiveSpeechKit {
        NONE, GOOGLE
    }

    public static synchronized void initializeSpeechKits(Context context) {
        GoogleSpeechKit.initialize(context);
    }

    private static synchronized void createRecognizer(String language, RecognizerListener recognizerListener) {
        GoogleSpeechKit.getInstance().createRecognizer(language,
                recognizerListener);
    }

    public static synchronized void startRecognizing() {
            GoogleSpeechKit.getInstance().startRecognizing();
    }

    public static synchronized void stopRecognizing() {
        GoogleSpeechKit.getInstance().stopRecognizing();
    }

    public static synchronized void shutDown() {
        GoogleSpeechKit.getInstance().shutDown();
    }

    public static synchronized ActiveSpeechKit getActiveSpeechKit() {
        ActiveSpeechKit kit = ActiveSpeechKit.NONE;

        boolean google = GoogleSpeechKit.getInstance().isActiveSpeechKit();

        if (google) {
            kit = ActiveSpeechKit.GOOGLE;
        }

        return kit;
    }

    public static void recreateRecognizer(RecognizerListener listener) {
        SpeechKitHelper.stopRecognizing();
        boolean isNuance = VoiceRecognizerPreferences.isNuanceRecognizer();
        if (!isNuance) {
            boolean isGoogleRecognizerPresented = Utility
                    .isGoogleRecognizerPresented();
            if (!isGoogleRecognizerPresented) {
                isNuance = true;
                VoiceRecognizerPreferences
                        .getVoiceRecognizerSettings()
                        .setSetting(
                                VoiceRecognizerPreferenceKey.IS_NUANCE_RECOGNIZER,
                                isNuance);
            }
        }
        String language = VoiceRecognizerPreferences.getRecognizerLanguage();
        SpeechKitHelper.createRecognizer(language, listener);
    }
}
