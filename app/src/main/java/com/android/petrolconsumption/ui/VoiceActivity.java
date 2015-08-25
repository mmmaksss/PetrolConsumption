package com.android.petrolconsumption.ui;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.commandsDb.CommandsId;
import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences;
import com.android.petrolconsumption.voice.SpeechKitHelper;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VoiceActivity extends BaseActivity {
    private TextView testTextView;
    private ImageView buttonStart;
    private ImageView imageMicrophone;
    private boolean isMicrophoneOn = false;
    private ProgressBar voiceProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        this.testTextView = (TextView) findViewById(R.id.test_text);

        this.buttonStart = (ImageView) findViewById(R.id.button_start);
        this.imageMicrophone = (ImageView) findViewById(R.id.microphone_image);
        OnClickListener onClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (VoiceActivity.this.isMicrophoneOn) {
                    SpeechKitHelper.stopRecognizing();
                } else {
                    SpeechKitHelper.startRecognizing();
                }
            }
        };
        this.buttonStart.setOnClickListener(onClickListener);
        this.imageMicrophone.setOnClickListener(onClickListener);

        this.voiceProgressBar = (ProgressBar) findViewById(R.id.voice_progress);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_voice_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
        case R.id.action_settings:
            Intent intentSettings = new Intent(VoiceActivity.this,
                    SettingsActivity.class);
            startActivity(intentSettings);
            break;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        VoiceRecognizerPreferences.setupLanguage();

        super.onResume();
    }

    @Override
    protected void onPause() {
        changeMicropoheState(false);
        if (testTextView != null) {
            testTextView.setText(null);
        }
        super.onPause();
    }

    private void changeMicropoheState(boolean on) {
        if (isPaused()) {
            return;
        }
        this.isMicrophoneOn = on;
        if (on) {
            if (VoiceActivity.this.buttonStart != null) {
                VoiceActivity.this.buttonStart
                        .setImageResource(R.drawable.microphone_on_icon);
            }
            if (VoiceActivity.this.imageMicrophone != null) {
                VoiceActivity.this.imageMicrophone
                        .setImageResource(R.drawable.microphone_green);
            }
        } else {
            if (VoiceActivity.this.buttonStart != null) {
                VoiceActivity.this.buttonStart
                        .setImageResource(R.drawable.microphone_off_icon);
            }
            if (VoiceActivity.this.imageMicrophone != null) {
                VoiceActivity.this.imageMicrophone
                        .setImageResource(R.drawable.microphone_red);
            }
            if (VoiceActivity.this.voiceProgressBar != null) {
                VoiceActivity.this.voiceProgressBar.setProgress(0);
            }
        }
    }

    @Override
    protected void onVoiceResults(String result) {
        if (!isPaused() && testTextView != null) {
            testTextView.setText(testTextView.getText() + "\n"
                    + SpeechKitHelper.getActiveSpeechKit().toString()
                    + " : onResults()=" + result);
        }
    }

    @Override
    protected void onVoiceError(int error) {
        if (!isPaused() && testTextView != null) {
            testTextView.setText(testTextView.getText() + "\n"
                    + SpeechKitHelper.getActiveSpeechKit().toString()
                    + " : onError()=" + error);
        }
    }

    @Override
    protected void onVoiceEndOfSpeech() {
        if (!isPaused() && testTextView != null) {
            testTextView.setText(testTextView.getText() + "\n"
                    + SpeechKitHelper.getActiveSpeechKit().toString()
                    + " : onEndOfSpeech()");
        }
        changeMicropoheState(false);
    }

    @Override
    protected void onVoiceRmsChanged(int audioLevel) {
        if (!isPaused()
                && VoiceActivity.this.voiceProgressBar != null) {
            VoiceActivity.this.voiceProgressBar.setProgress(audioLevel);
        }
    }

    @Override
    protected void onVoiceReadyForSpeech() {
        if (!isPaused() && testTextView != null) {
            testTextView.setText("\n"
                    + SpeechKitHelper.getActiveSpeechKit().toString()
                    + " : onReadyForSpeech()");
        }
        changeMicropoheState(true);
    }
    
    @Override
    protected void doAction(CommandsId id) {
        if (isPaused()) {
            return;
        }

        if (id == CommandsId.OPEN_APPLICATION_SETTINGS) {
            Intent openSettings = new Intent(VoiceActivity.this,
                    SettingsActivity.class);
            VoiceActivity.this.startActivity(openSettings);
        }
    }
}
