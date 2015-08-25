package com.android.petrolconsumption.utils;

import com.android.petrolconsumption.PetrolConsumptionApplication;
import com.android.petrolconsumption.ui.VoiceActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(NotificationsManager
                .ACTION_LAUNCH_APPLICATION)) {
            Intent appStart = new Intent (PetrolConsumptionApplication.getAppContext(),
                    VoiceActivity.class);
            appStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PetrolConsumptionApplication.getAppContext().startActivity(appStart);

            Utility.serviceShutDowh();
            NotificationsManager.getInstance().cancelAllNotification();
        }
    }

}
