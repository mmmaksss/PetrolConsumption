package com.android.petrolconsumption;

import com.android.petrolconsumption.commands.CommandsManager;
import com.android.petrolconsumption.preferences.VoiceRecognizerPreferences;
import com.android.petrolconsumption.voice.SpeechKitHelper;

import android.app.Application;
import android.content.Context;

public class PetrolConsumptionApplication extends Application {
    private static PetrolConsumptionApplication instance_;
    public static Context context;

    /**
     * Returns instance of {@link PetrolConsumptionApplication}
     */
    public static PetrolConsumptionApplication getInstance() {
        return instance_;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance_ = this;
        context = getApplicationContext();
    }

    public void initializeApplication() {
        if (instance_ != null) {
            VoiceRecognizerPreferences.getVoiceRecognizerSettings();
            SpeechKitHelper.initializeSpeechKits(context);
            CommandsManager.initialize(context);
        }
    }

    public static Context getAppContext() {
        return context;
    }
}
