package com.adiaz.deportesmadrid.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.MainActivity;

public class NotificationUtils {

    private static final String UPDATE_GROUP_CHANNEL_ID = "update_group_chanel_id";
    private static final int NOTIFICATION_ID = 123;
    private static final int UPDATED_COMPETITION_PENDING_INTENT_ID = 124;
    private static final int UPDATED_COMPETITION_NOTIFICATION_ID = 1213;

    public static final void showNotificationUpdatedTeam(Context context) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(
                    UPDATE_GROUP_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        String notificationTitle = "titulo";
        String notificationBody = "body";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, UPDATE_GROUP_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntentCompetition(context))
                .setAutoCancel(true);
        notificationManager.notify(UPDATED_COMPETITION_NOTIFICATION_ID, notificationBuilder.build());

    }


    public static PendingIntent contentIntentCompetition(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, UPDATED_COMPETITION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

}
