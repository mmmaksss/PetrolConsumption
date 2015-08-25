package com.android.petrolconsumption.voice.google;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import com.android.petrolconsumption.utils.NotInitializedException;
import com.android.petrolconsumption.voice.RecognizerListener;

public final class GoogleSpeechKit {

    private static Long COMPLETE_SILENCE_LENGTH_MILLIS = Long.valueOf(2000);
    private static GoogleSpeechKit instance_;
    private SpeechRecognizer recognizer;
    private Intent recognizerIntent;
    private final Context context;
    private GoogleSpeachListener listenerGoogleSpeechKit;
    private java.lang.String language;
    private boolean isCreated = false;

    public static synchronized void initialize(Context context) {
        if (instance_ == null) {
            instance_ = new GoogleSpeechKit(context);
        }
    }

    public static GoogleSpeechKit getInstance() {
        if (instance_ == null) {
            throw new NotInitializedException(
                    "GoogleSpeechKit instance not initialized!");
        }
        return instance_;
    }

    private GoogleSpeechKit(Context context) {
        this.context = context.getApplicationContext();
        this.listenerGoogleSpeechKit = new GoogleSpeachListener();
    }

    public synchronized void shutDown() {
        if (this.recognizer != null) {
            this.recognizer.cancel();
            this.recognizer.destroy();
            this.recognizer = null;
        }
        this.listenerGoogleSpeechKit.removeListener();
        this.isCreated = false;
    }

    public synchronized void createRecognizer(java.lang.String language,
            RecognizerListener listener) {
        if (this.recognizer != null) {
            this.recognizer.cancel();
            this.recognizer.destroy();
            this.recognizer = null;
        }

        this.listenerGoogleSpeechKit.setListener(listener);
        this.language = language;
        this.isCreated = true;
    }

    public synchronized void startRecognizing() {
        if (this.recognizer != null) {
            this.recognizer.cancel();
            this.recognizer.destroy();
            this.recognizer = null;
        }
        if (this.isCreated) {
            this.recognizer = SpeechRecognizer
                    .createSpeechRecognizer(this.context);
            this.recognizer.setRecognitionListener(this.listenerGoogleSpeechKit);

            this.recognizerIntent = new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            this.recognizerIntent.putExtra(
                    RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                            .getPackage().getName());
            this.recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "TODO TEXT");
            this.recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    language);
            this.recognizerIntent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
            this.recognizerIntent.putExtra(
                    RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
                    language);
            this.recognizerIntent
                    .putExtra(
                            RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                            GoogleSpeechKit.COMPLETE_SILENCE_LENGTH_MILLIS);

            this.recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,
                    1);

            this.recognizer.startListening(this.recognizerIntent);
        }
    }

    public synchronized void stopRecognizing() {
        if (this.recognizer != null) {
            this.recognizer.stopListening();
        }
    }

    public synchronized boolean isActiveSpeechKit() {
        return this.isCreated;
    }
}
