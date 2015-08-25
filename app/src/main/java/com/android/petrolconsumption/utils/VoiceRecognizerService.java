package com.android.petrolconsumption.utils;

import com.android.petrolconsumption.PetrolConsumptionApplication;
import com.android.petrolconsumption.commands.BaseCommand;
import com.android.petrolconsumption.commands.CommandsManager;
import com.android.petrolconsumption.voice.RecognizerListener;
import com.android.petrolconsumption.voice.SpeechKitHelper;
import com.android.petrolconsumption.widget.Widget;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class VoiceRecognizerService extends Service {
    private static int ACTIVE_VOICE_RECOGNIZING_INTERVAL_MS = 15 * 1000;
    private boolean isRecognizingActive = false;
    private long timeLastExecuteCommand;
    private Handler restartHandler = new Handler();
    private Runnable restartRunable = new Runnable() {
        @Override
        public void run() {
            SpeechKitHelper.startRecognizing();
        }
    };

    private final RecognizerListener listener = new RecognizerListener() {

        @Override
        public void onRmsChanged(int audioLevel) {
            // nothing to do here
        }

        @Override
        public void onResults(String results) {
            Log.d("VoiceRecognizerService", "RecognizerListener.onResults:"
                    + results);
            Log.d("VoiceRecognizerService",
                    "RecognizerListener isRecognizingActive:"
                            + VoiceRecognizerService.this.isRecognizingActive);
            if (VoiceRecognizerService.this.isRecognizingActive) {
                BaseCommand command = CommandsManager.getInstance()
                        .findCommand(results);
                if (command != null) {
                    command.execute();
                    timeLastExecuteCommand = System.currentTimeMillis();
                }
                continueRecognizing();
            }
        }

        @Override
        public void onReadyForSpeech() {
            Log.d("VoiceRecognizerService",
                    "RecognizerListener.onReadyForSpeech");
            NotificationsManager.getInstance().sendOnOffNotification(true);
        }

        @Override
        public void onError(int error) {
            Log.d("VoiceRecognizerService", "RecognizerListener.onError: "
                    + error);
            if (VoiceRecognizerService.this.isRecognizingActive) {
                continueRecognizing();
            }
        }

        @Override
        public void onEndOfSpeech() {
            Log.d("VoiceRecognizerService", "RecognizerListener.onEndOfSpeech");
            NotificationsManager.getInstance().sendOnOffNotification(false);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("VoiceRecognizerService", "onCreate");

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("VoiceRecognizerService", "onDestroy");
        SpeechKitHelper.stopRecognizing();
        this.isRecognizingActive = false;
        NotificationsManager.getInstance().sendOnOffNotification(false);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("VoiceRecognizerService", "onStartCommand");

        PetrolConsumptionApplication.getInstance().initializeApplication();

        SpeechKitHelper.recreateRecognizer(listener);
        SpeechKitHelper.startRecognizing();
        this.isRecognizingActive = true;
        this.timeLastExecuteCommand = System.currentTimeMillis();
        return super.onStartCommand(intent, flags, startId);
    }

    private void continueRecognizing() {
        if ((System.currentTimeMillis() - this.timeLastExecuteCommand) < ACTIVE_VOICE_RECOGNIZING_INTERVAL_MS) {
            this.restartHandler.postDelayed(this.restartRunable, 100);
            this.isRecognizingActive = true;
        } else {
            Log.d("VoiceRecognizerService",
                    "stop recognizing service by timeout");
            Intent stopWidgetIntent = new Intent(this, Widget.class);
            stopWidgetIntent
                    .setAction(Widget.ACTION_WIDGET_STOP_COMMAND_LISTENING);
            sendBroadcast(stopWidgetIntent);
        }
    }
}
