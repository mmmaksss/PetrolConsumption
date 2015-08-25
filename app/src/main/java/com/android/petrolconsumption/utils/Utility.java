package com.android.petrolconsumption.utils;

import java.util.List;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.PetrolConsumptionApplication;
import com.android.petrolconsumption.widget.Widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.util.Log;

/**
 * Class contains common utility methods. Methods should be static. This class
 * cannot be instantiated.
 */
public class Utility {

    /**
     * Checks availability of any speech recognizing
     * @return true if any speech recognizer there available, false � if it's
     *         absent
     */

    public static boolean isGoogleRecognizerPresented() {
        try {
            PackageManager pm = PetrolConsumptionApplication.getAppContext().getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                    RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            Log.e("voiceRecognition", activities.get(0).toString());
            if (activities.size() != 0) {
                return true;
            }
        } catch (Throwable t) {
            // do nothing
        }

        return false;
    }

    /**
     * 
     * Asking the permission for installing Google Voice Search. If permission
     * granted � sent user to Google Play
     * 
     * @param context
     *            {@link Context}
     */

    public static void requestGoogleVoiceSearch(final Context context) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setMessage(
                        context.getString(R.string.dialog_google_market_message))
                // dialog message
                .setTitle(
                        context.getString(R.string.dialog_google_market_title))
                // dialog header
                .setPositiveButton(
                        context.getString(R.string.dialog_google_market_install),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                try {
                                    // TODO find way check market
                                    Intent market = new Intent(
                                            Intent.ACTION_VIEW)
                                            .setData(
                                                    Uri.parse("market://details?id=com.google.android.voicesearch"))
                                            .setFlags(
                                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                                                            | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    Intent website = new Intent(
                                            Intent.ACTION_VIEW)
                                            .setData(
                                                    Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.voicesearch"))
                                            .setFlags(
                                                    Intent.FLAG_ACTIVITY_NO_HISTORY
                                                            | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    try {
                                        context.startActivity(website);
                                    } catch (ActivityNotFoundException e) {
                                        context.startActivity(market);
                                    }
                                } catch (Exception ex) {
                                    // do nothing
                                }
                            }
                        })
                .setNegativeButton(
                        context.getString(R.string.dialog_google_market_cancel),
                        null).create();
        dialog.show();
    }

    public static void serviceShutDowh() {
        Intent stopWidgetIntent = new Intent(PetrolConsumptionApplication.getAppContext(),
                Widget.class);
        stopWidgetIntent.setAction(Widget.ACTION_WIDGET_STOP_COMMAND_LISTENING);
        PetrolConsumptionApplication.getAppContext().sendBroadcast(stopWidgetIntent);
    }
}
