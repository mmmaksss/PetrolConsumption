package com.android.petrolconsumption.ui;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.commands.commandsDb.CommandsId;
import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences;
import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences.VoiceRecognizerPreferenceKey;
import com.android.petrolconsumption.utils.Utility;
import com.android.petrolconsumption.voice.SpeechKitHelper;
import com.android.petrolconsumption.utils.BeepVibrationManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.content.Intent;

public class SettingsActivity extends BaseActivity {
    private CheckBox checkboxConfirmDialog;
    private Spinner spinnerLanguages;
    private Spinner spinnerRecognizers;
    private ImageView settingsMicrophoneIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] dataRecognizers = {
                getString(R.string.settings_recognizer_nuance),
                getString(R.string.settings_recognizer_google) };
        ArrayAdapter<String> adapterRecognizers = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, dataRecognizers);
        this.spinnerRecognizers = (Spinner) findViewById(R.id.settings_spinner_recignizer);
        adapterRecognizers
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerRecognizers.setAdapter(adapterRecognizers);
        this.spinnerRecognizers
                .setPrompt(getString(R.string.settings_recognizer_title));
        boolean isNuance = VoiceRecognizerPreferences
                .isNuanceRecognizer();
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
        this.spinnerRecognizers.setSelection((isNuance) ? 0 : 1);
        this.spinnerRecognizers
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        if (isPaused()) {
                            return;
                        }
                        boolean isNuance = (position == 0);
                        if (!isNuance) {
                            boolean isGoogleRecognizerPresented = Utility
                                    .isGoogleRecognizerPresented();
                            if (!isGoogleRecognizerPresented) {
                                Utility.requestGoogleVoiceSearch(SettingsActivity.this);
                                SettingsActivity.this.spinnerRecognizers.setSelection(0);
                                return;
                            }
                        }
                        VoiceRecognizerPreferences
                                .getVoiceRecognizerSettings()
                                .setSetting(
                                        VoiceRecognizerPreferenceKey.IS_NUANCE_RECOGNIZER,
                                        isNuance);

                        if (!isPaused() && VoiceRecognizerPreferences.isLooperEnabled()) {
                            restartRecognizer(true);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        String[] dataLanguages = { getString(R.string.settings_language_ru),
                getString(R.string.settings_language_en) };
        ArrayAdapter<String> adapterLanguages = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataLanguages);
        this.spinnerLanguages = (Spinner) findViewById(R.id.settings_spinner_language);
        adapterLanguages
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerLanguages.setAdapter(adapterLanguages);
        this.spinnerLanguages.setPrompt(getString(R.string.settings_language_title));
        this.spinnerLanguages
                .setSelection(((VoiceRecognizerPreferences
                        .getRecognizerLanguage()
                        .equals(VoiceRecognizerPreferences.RECOGNIZER_LANGUAGE_RUSSIAN)) ? 0
                        : 1));
        this.spinnerLanguages
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        if (isPaused()) {
                            return;
                        }
                        if (VoiceRecognizerPreferences
                                .setLanguage(
                                        ((position == 0) ? VoiceRecognizerPreferences.RECOGNIZER_LANGUAGE_RUSSIAN
                                                : VoiceRecognizerPreferences.RECOGNIZER_LANGUAGE_ENGLISH),
                                        SettingsActivity.this
                                                .getApplicationContext())) {
                            updateScreen();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        this.checkboxConfirmDialog = (CheckBox) findViewById(R.id.settings_checkbox_confirm_dialog);
        this.checkboxConfirmDialog.setChecked(VoiceRecognizerPreferences
                .isShowConfitmDialog());
        this.checkboxConfirmDialog
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if (isPaused()) {
                            return;
                        }
                        VoiceRecognizerPreferences
                                .getVoiceRecognizerSettings()
                                .setSetting(
                                        VoiceRecognizerPreferenceKey.SHOW_CONFIRM_DIALOG,
                                        isChecked);
                    }
                });

        CheckBox checkboxLooperButton = (CheckBox) findViewById(R.id.settings_checkbox_looper_button);
        checkboxLooperButton.setChecked(VoiceRecognizerPreferences
                .isLooperEnabled());
        checkboxLooperButton
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if (isPaused()) {
                            return;
                        }
                        VoiceRecognizerPreferences
                                .getVoiceRecognizerSettings()
                                .setSetting(
                                        VoiceRecognizerPreferenceKey.LOOPER_ENABLED,
                                        isChecked);

                        if (!isPaused() && VoiceRecognizerPreferences.isLooperEnabled()) {
                            restartRecognizer(true);
                        } else {
                            SpeechKitHelper.stopRecognizing();
                        }
                    }
                });

        this.settingsMicrophoneIcon = (ImageView) findViewById(R.id.settings_microphone_icon);
    }

    private void updateScreen() {
        if (this.spinnerLanguages != null) {
            Intent intentSettings = new Intent(this,
                    SettingsActivity.class);
            startActivity(intentSettings);
            overridePendingTransition(-1,
                    android.R.anim.slide_out_right); 
            this.finish();
        }
    }

    private void changeMicropoheState(boolean on) {
        if (isPaused()) {
            return;
        }

        if (on) {
            if (SettingsActivity.this.settingsMicrophoneIcon != null) {
                SettingsActivity.this.settingsMicrophoneIcon
                        .setImageResource(R.drawable.settings_green_icon);
            }
            BeepVibrationManager.playBeep();
        } else {
            if (SettingsActivity.this.settingsMicrophoneIcon != null) {
                SettingsActivity.this.settingsMicrophoneIcon
                        .setImageResource(R.drawable.settings_red_icon);
            }
            BeepVibrationManager.playVibration();
        }
    }

    @Override
    protected void onVoiceEndOfSpeech() {
        changeMicropoheState(false);
    }

    @Override
    protected void onVoiceReadyForSpeech() {
        changeMicropoheState(true);
    }
    
    @Override
    protected void doAction(CommandsId id) {
        if (isPaused()) {
            return;
        }

        switch (id) {
        case CLOSE_APPLICATION_SETTINGS:
            SettingsActivity.this.finish();
            break;

        case CHANGE_DIALOG_SETTINGS:
            if (SettingsActivity.this.checkboxConfirmDialog != null) {
                SettingsActivity.this.checkboxConfirmDialog
                        .setChecked(!SettingsActivity.this.checkboxConfirmDialog
                                .isChecked());
            }
            break;

        case CHANGE_LANGUAGE:
            if (SettingsActivity.this.spinnerLanguages != null) {
                SettingsActivity.this.spinnerLanguages
                .setSelection((SettingsActivity.this.spinnerLanguages
                        .getSelectedItemPosition() == 0) ? 1 : 0);
            }
            break;

        case CHANGE_RECOGNIZER:
            if (SettingsActivity.this.spinnerRecognizers != null) {
                SettingsActivity.this.spinnerRecognizers
                .setSelection((SettingsActivity.this.spinnerRecognizers
                        .getSelectedItemPosition() == 0) ? 1 : 0);
            }
            break;

        default:
            break;
        }
    }
}
