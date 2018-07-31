package com.adiaz.ligasmadrid.firebase

import android.preference.PreferenceManager
import android.util.Log
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.db.daos.FavoritesDAO
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.utils.Constants
import com.adiaz.ligasmadrid.utils.NotificationUtils
import com.adiaz.ligasmadrid.utils.UtilsPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: " + remoteMessage!!.from!!)
        try {
            when (remoteMessage.from) {
                Constants.TOPICS + Constants.TOPICS_SYNC -> {
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                    preferences.edit().putBoolean(getString(R.string.pref_need_update), true).apply()
                    if (UtilsPreferences.showNotifications(this)) {
                        val data = remoteMessage.data
                        val updatedArray = data["teams_updated"]
                        val gson = Gson()
                        val listType = object : TypeToken<List<List<String>>>() {

                        }.type
                        val teamsUpdated = gson.fromJson<List<List<String>>>(updatedArray, listType)
                        for (strings in teamsUpdated) {
                            val idTeam = java.lang.Long.parseLong(strings[0])
                            val idGroup = strings[1]
                            val favoriteGroup = FavoritesDAO.queryFavorite(applicationContext, idGroup)
                            if (favoriteGroup != null) {
                                val group = GroupsDAO.queryCompetitionsById(applicationContext, idGroup)
                                NotificationUtils.showNotificationUpdatedGroup(applicationContext, group!!)
                            } else {
                                val favoriteTeam = FavoritesDAO.queryFavorite(applicationContext, idGroup, idTeam)
                                if (favoriteTeam != null) {
                                    val group = GroupsDAO.queryCompetitionsById(applicationContext, idGroup)
                                    NotificationUtils.showNotificationUpdatedTeam(applicationContext, favoriteTeam, group!!)
                                }
                            }
                        }
                    }
                }
                Constants.TOPICS + Constants.TOPICS_GENERAL -> if (UtilsPreferences.showNotifications(this)) {
                    val data = remoteMessage.data
                    val title = data["title"]
                    val body = data["body"]
                    NotificationUtils.showNotificationGeneral(applicationContext, title!!, body!!)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onMessageReceived: " + e.localizedMessage, e)
        }
    }

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
    }
}
