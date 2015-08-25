package com.android.petrolconsumption.voice.google;

import java.util.ArrayList;

import com.android.petrolconsumption.voice.RecognizerListener;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

public class GoogleSpeachListener implements RecognitionListener {
    private final static float RMSDB_CORRECTION = 35;
    private RecognizerListener listener = null;

    public GoogleSpeachListener() {
    }

    public void setListener(RecognizerListener listener) {
        this.listener = listener;
    }
    
    public void removeListener() {
        this.listener = null;
    }

    @Override
    public void onBeginningOfSpeech() {
        // do nothing
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        // do nothing
    }

    @Override
    public void onEndOfSpeech() {
        if (this.listener != null) {
            this.listener.onEndOfSpeech();
        }
    }

    @Override
    public void onError(int error) {
        if (this.listener != null) {
            this.listener.onError(error);
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        // do nothing
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        // do nothing
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        if (this.listener != null) {
            this.listener.onReadyForSpeech();
        }
    }


    @Override
    public void onRmsChanged(float rmsdB) {
        if (this.listener != null) {
            int audioLevel = (int) (rmsdB + GoogleSpeachListener.RMSDB_CORRECTION
                    - RecognizerListener.AUDIO_LEVEL_CORECTION);
            this.listener.onRmsChanged(audioLevel);
        }
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> strList = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String result = (strList.size() > 0) ? strList.get(0) : "";

        if (this.listener != null) {
            this.listener.onResults(result);
        }
    }
}
