package com.android.petrolconsumption.voice;

public interface SpeechKitInterface {

    public void createRecognizer(String language,
            RecognizerListener recognizerListener);

    public void startRecognizing();

    public void stopRecognizing();

    public boolean isActiveSpeechKit();

    public void shutDown();
}
