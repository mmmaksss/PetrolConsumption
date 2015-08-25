package com.android.petrolconsumption.utils;

import com.android.petrolconsumption.R;
import com.android.petrolconsumption.PetrolConsumptionApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NotificationsManager {
    private static volatile NotificationsManager instance_;
    private static final int NOTIFY_ON_OFF_ID = "com.android.voicerecognizer.utils.NotificationsManager".hashCode();
    public static final String ACTION_LAUNCH_APPLICATION = "ACTION LAUNCH APPLICATION";

    private boolean isBlocked = false;

    public static NotificationsManager getInstance() {
        if (instance_ == null) {
            instance_ = new NotificationsManager();
        }
        return instance_;
    }

    private final NotificationManager manager;
    private final Context context;

    private NotificationsManager() {
        this.context = PetrolConsumptionApplication.getAppContext();
        this.manager = (NotificationManager) this.context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public synchronized void sendOnOffNotification(boolean isOn) {
        if (!this.isBlocked) {
            Notification notification = createNotification(isOn);
            this.manager.cancel(NotificationsManager.NOTIFY_ON_OFF_ID);
            this.manager.notify(NotificationsManager.NOTIFY_ON_OFF_ID,
                    notification);
        }
    }

    public void setBlockedNotifications(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    private Notification createNotification(boolean isOn) {
        Notification notification = new Notification(
                (isOn) ? R.drawable.settings_green_icon
                        : R.drawable.settings_red_icon,
                (isOn) ? this.context.getString(R.string.notification_recognition_launched)
                       : this.context.getString(R.string.notification_recognition_stopped),
                System.currentTimeMillis());
        RemoteViews contentView = new RemoteViews(this.context.getPackageName(),
                R.layout.notification_layout);
        contentView.setTextViewText(R.id.notification_title,
                (isOn) ? this.context.getString(R.string.notification_recognition_launched_text)
                       : this.context.getString(R.string.notification_recognition_stopped_text));

        notification.contentView = contentView;
        notification.flags = Notification.FLAG_NO_CLEAR
               | Notification.FLAG_ONGOING_EVENT
               | Notification.FLAG_ONLY_ALERT_ONCE;

        Intent notificationIntent = new Intent(this.context, NotificationReceiver.class);
        notificationIntent.setAction(NotificationsManager.ACTION_LAUNCH_APPLICATION);
        PendingIntent contentIntent = PendingIntent.getBroadcast(this.context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = contentIntent;

        return notification;
      }

    public synchronized void cancelAllNotification() {
        this.manager.cancel(NotificationsManager.NOTIFY_ON_OFF_ID);
    }
}
