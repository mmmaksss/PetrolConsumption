package com.android.petrolconsumption.voice;

import android.speech.SpeechRecognizer;

public interface RecognizerListener {
    public final static int AUDIO_LEVEL_CORECTION = 20;

    /**
     * Called when the recognizer is ready for the user to start speaking.
     */
    public void onReadyForSpeech();

    /**
     * The sound level in the audio stream has changed. There is no guarantee that this method will
     * be called.
     * 
     * @param audioLevel audio level from 0 to 100
     */
    void onRmsChanged(int audioLevel);

    /**
     * Called after the user stops speaking.
     */
    void onEndOfSpeech();

    /**
     * A network or recognition error occurred.
     * 
     * @param error code is defined in {@link SpeechRecognizer}
     */
    void onError(int error);

    /**
     * Called when recognition results are ready.
     */
    void onResults(String results);
}
