package com.adiaz.ligasmadrid.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat

import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.activities.GroupDetailsActivity
import com.adiaz.ligasmadrid.activities.MainActivity
import com.adiaz.ligasmadrid.activities.TeamDetailsActivity
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.db.entities.Favorite
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.utils.NotificationUtils.UPDATED_COMPETITION_PENDING_INTENT_ID

import org.apache.commons.lang3.StringUtils

object NotificationUtils {

    private val UPDATE_GROUP_CHANNEL_ID = "update_group_chanel_id"
    //private static final int NOTIFICATION_ID = 123;
    private val UPDATED_COMPETITION_PENDING_INTENT_ID = 124
    private val UPDATED_COMPETITION_NOTIFICATION_ID = 1213

    fun showNotificationGeneral(context: Context, title: String, body: String) {
        val notificationManager = getNotificationManager(context)

        var notificationTitle = context.getString(R.string.general_notification_title)
        if (StringUtils.isNotEmpty(title)) {
            notificationTitle = title
        }
        val notificationBuilder = NotificationCompat.Builder(context, UPDATE_GROUP_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntentMain(context))
                .setAutoCancel(true)
        notificationManager.notify(UPDATED_COMPETITION_NOTIFICATION_ID, notificationBuilder.build())

    }

    fun showNotificationUpdatedGroup(context: Context, group: Group) {
        val notificationManager = getNotificationManager(context)
        val notificationTitle = context.getString(R.string.update_notification_title)
        val notificationBody = context.getString(R.string.update_competition_notification_body, group.nomGrupo, group.deporte, group.categoria)
        val inboxStyle = NotificationCompat.InboxStyle()
        val notificationBuilder = NotificationCompat.Builder(context, UPDATE_GROUP_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntentGroup(context, group))
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        notificationManager.notify("tag_" + group.id!!, UPDATED_COMPETITION_NOTIFICATION_ID, notificationBuilder.build())
    }

    fun showNotificationUpdatedTeam(context: Context, favorite: Favorite, group: Group) {
        val notificationManager = getNotificationManager(context)
        val notificationTitle = context.getString(R.string.update_notification_title)
        val notificationBody = context.getString(R.string.update_team_notification_body, favorite.nameTeam, group.deporte, group.nomGrupo)
        val inboxStyle = NotificationCompat.InboxStyle()
        val notificationBuilder = NotificationCompat.Builder(context, UPDATE_GROUP_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntentTeam(context, favorite))
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        notificationManager.notify("tag_" + favorite.idTeam!!, UPDATED_COMPETITION_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                    UPDATE_GROUP_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }
        return notificationManager
    }


    fun contentIntentMain(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
                context, UPDATED_COMPETITION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun contentIntentTeam(context: Context, favorite: Favorite): PendingIntent {
        val intent = Intent(context, TeamDetailsActivity::class.java)
        intent.putExtra(Constants.ID_COMPETITION, favorite.idGroup)
        intent.putExtra(Constants.TEAM_ID, favorite.idTeam)
        intent.putExtra(Constants.TEAM_NAME, favorite.nameTeam)
        return PendingIntent.getActivity(
                context, UPDATED_COMPETITION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun contentIntentGroup(context: Context, group: Group): PendingIntent {
        val intent = Intent(context, GroupDetailsActivity::class.java)
        intent.putExtra(Constants.ID_COMPETITION, group.id)
        intent.putExtra(Constants.NAME_COMPETITION, group.nomGrupo)
        return PendingIntent.getActivity(
                context, UPDATED_COMPETITION_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

}
