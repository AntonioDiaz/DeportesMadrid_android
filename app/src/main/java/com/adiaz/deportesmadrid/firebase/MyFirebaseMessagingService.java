package com.adiaz.deportesmadrid.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.NotificationUtils;
import com.adiaz.deportesmadrid.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        switch (remoteMessage.getFrom()) {
            case Constants.TOPICS + Constants.TOPICS_SYNC:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().putBoolean(getString(R.string.pref_need_update),true).apply();
                Map<String, String> data = remoteMessage.getData();
                String teamsUpdated = data.get("teams_updated");
                String[] teamsUpdatedArray = teamsUpdated.split("|");


                break;
            case Constants.TOPICS + Constants.TOPICS_GENERAL:
                NotificationUtils.showNotificationUpdatedTeam(getApplicationContext());
                break;

        }
    }
}
