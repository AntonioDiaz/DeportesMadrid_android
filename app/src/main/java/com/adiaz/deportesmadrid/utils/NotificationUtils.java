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
import com.adiaz.deportesmadrid.activities.GroupDetailsActivity;
import com.adiaz.deportesmadrid.activities.MainActivity;
import com.adiaz.deportesmadrid.activities.TeamDetailsActivity;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.db.entities.Group;

public class NotificationUtils {

    private static final String UPDATE_GROUP_CHANNEL_ID = "update_group_chanel_id";
    //private static final int NOTIFICATION_ID = 123;
    private static final int UPDATED_COMPETITION_PENDING_INTENT_ID = 124;
    private static final int UPDATED_COMPETITION_NOTIFICATION_ID = 1213;

    public static final void showNotificationGeneral(Context context) {
        NotificationManager notificationManager = getNotificationManager(context);
        String notificationTitle = context.getString(R.string.update_notification_title);
        String notificationBody = "body";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, UPDATE_GROUP_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntentMain(context))
                .setAutoCancel(true);
        notificationManager.notify(UPDATED_COMPETITION_NOTIFICATION_ID, notificationBuilder.build());

    }

    public static final void showNotificationUpdatedGroup(Context context, Group group) {
        NotificationManager notificationManager = getNotificationManager(context);
        String notificationTitle = context.getString(R.string.update_notification_title);
        String notificationBody = context.getString(R.string.update_competition_notification_body, group.nomGrupo(), group.deporte(), group.categoria());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, UPDATE_GROUP_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntentGroup(context, group))
                .setStyle(inboxStyle)
                .setAutoCancel(true);
        notificationManager.notify("tag_" + group.id(), UPDATED_COMPETITION_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static final void showNotificationUpdatedTeam(Context context, Favorite favorite, Group group) {
        NotificationManager notificationManager = getNotificationManager(context);
        String notificationTitle = context.getString(R.string.update_notification_title);
        String notificationBody = context.getString(R.string.update_team_notification_body, favorite.nameTeam(), group.deporte(), group.nomGrupo());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, UPDATE_GROUP_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntentTeam(context, favorite))
                .setStyle(inboxStyle)
                .setAutoCancel(true);
        notificationManager.notify("tag_" + favorite.idTeam(), UPDATED_COMPETITION_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static NotificationManager getNotificationManager(Context context) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    UPDATE_GROUP_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        return notificationManager;
    }


    public static PendingIntent contentIntentMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, UPDATED_COMPETITION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static PendingIntent contentIntentTeam(Context context, Favorite favorite) {
        Intent intent = new Intent(context, TeamDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, favorite.idGroup());
        intent.putExtra(Constants.TEAM_ID, favorite.idTeam());
        intent.putExtra(Constants.TEAM_NAME, favorite.nameTeam());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, UPDATED_COMPETITION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
    public static PendingIntent contentIntentGroup(Context context, Group group) {
        Intent intent = new Intent(context, GroupDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, group.id());
        intent.putExtra(Constants.NAME_COMPETITION, group.nomGrupo());
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, UPDATED_COMPETITION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

}
