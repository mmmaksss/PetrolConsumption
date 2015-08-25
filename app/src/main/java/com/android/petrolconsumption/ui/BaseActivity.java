package com.android.petrolconsumption.ui;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.Actions;
import com.android.petrolconsumption.commands.Actions.ActionListener;
import com.android.petrolconsumption.commands.BaseCommand;
import com.android.petrolconsumption.commands.commandsDb.CommandsId;
import com.android.petrolconsumption.commands.CommandsManager;
import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences;
import com.android.petrolconsumption.utils.NotificationsManager;
import com.android.petrolconsumption.utils.Utility;
import com.android.petrolconsumption.voice.RecognizerListener;
import com.android.petrolconsumption.voice.SpeechKitHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public abstract class BaseActivity extends Activity {
    private boolean isPaused = false;
    private Handler restartHandler;
    private Runnable restartRunable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.restartHandler = new Handler();
        this.restartRunable = new Runnable() {
            @Override
            public void run() {
                if (!BaseActivity.this.isPaused && VoiceRecognizerPreferences.isLooperEnabled()) {
                    SpeechKitHelper.startRecognizing();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        Utility.serviceShutDowh();

        NotificationsManager.getInstance().setBlockedNotifications(true);
        NotificationsManager.getInstance().cancelAllNotification();

        Actions.getInstance().setListener(this.actionsListener);

        super.onResume();

        this.isPaused = false;
        restartRecognizer(true);
    }

    @Override
    protected void onPause() {
        this.isPaused = true;

        if (this.restartHandler != null) {
            this.restartHandler.removeCallbacks(this.restartRunable);
        }
        Actions.getInstance().removeListener();
        SpeechKitHelper.stopRecognizing();
        NotificationsManager.getInstance().setBlockedNotifications(false);
        super.onPause();
    }

    protected void restartRecognizer(boolean recreate) {
        if (recreate) {
            SpeechKitHelper.recreateRecognizer(this.recognizerListener);
        }
        if (!this.isPaused && VoiceRecognizerPreferences.isLooperEnabled()
                && this.restartHandler != null
                && this.restartRunable != null) {

            this.restartHandler.removeCallbacks(this.restartRunable);
            this.restartHandler.postDelayed(this.restartRunable, 100);
        }
    }

    private RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onReadyForSpeech() {
            if (!BaseActivity.this.isPaused) {
                onVoiceReadyForSpeech();
            }
        }

        @Override
        public void onRmsChanged(int audioLevel) {
            if (!BaseActivity.this.isPaused) {
                onVoiceRmsChanged(audioLevel);
            }
        }

        @Override
        public void onEndOfSpeech() {
            if (!BaseActivity.this.isPaused) {
                onVoiceEndOfSpeech();
            }
        }

        @Override
        public void onError(int error) {
            if (!BaseActivity.this.isPaused) {
                onVoiceError(error);
                restartRecognizer(false);
            }
        }

        @Override
        public void onResults(String result) {
            if (!BaseActivity.this.isPaused) {
                onVoiceResults(result);
                if (result.trim().length() > 0) {
                    BaseCommand command = CommandsManager.getInstance()
                            .findCommand(result);
                    if (command != null) {
                        if (VoiceRecognizerPreferences.isShowConfitmDialog()) {
                            new ConfirmDialog(BaseActivity.this, command,
                                    BaseActivity.this.confirmDialogListener)
                                    .show();
                        } else {
                            command.execute();
                        }
                    } else {
                        restartRecognizer(false);
                    }
                }
            }
        }
    };

    private ConfirmDialogListener confirmDialogListener = new ConfirmDialogListener() {

        @Override
        public void onYes(BaseCommand command) {
            if (!BaseActivity.this.isPaused) {
                command.execute();
                restartRecognizer(true);
            }
        }

        @Override
        public void onNo(String why) {
            if (!BaseActivity.this.isPaused) {
                restartRecognizer(true);
            }
        }
    };

    private ActionListener actionsListener = new ActionListener() {

        @Override
        public void performAction(CommandsId id) {
            if (!BaseActivity.this.isPaused) {
                doAction(id);
            }
        }
    };

    protected boolean isPaused() {
        return this.isPaused;
    }

    protected void onVoiceResults(String result) {
        // do nothing
    }
    protected void onVoiceError(int error) {
        // do nothing
    }
    protected void onVoiceEndOfSpeech() {
        // do nothing
    }
    protected void onVoiceRmsChanged(int audioLevel) {
        // do nothing
    }
    protected void onVoiceReadyForSpeech() {
        // do nothing
    }
    protected void doAction(CommandsId id) {
        // do nothing
    }
}
