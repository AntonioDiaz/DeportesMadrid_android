package com.adiaz.deportesmadrid.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.NotificationUtils;
import com.adiaz.deportesmadrid.utils.Utils;
import com.adiaz.deportesmadrid.utils.UtilsPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;

import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        try {
            switch (remoteMessage.getFrom()) {
                case Constants.TOPICS + Constants.TOPICS_SYNC:
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                        preferences.edit().putBoolean(getString(R.string.pref_need_update),true).apply();
                        if (UtilsPreferences.showNotifications(this)) {
                            Map<String, String> data = remoteMessage.getData();
                            String updatedArray = data.get("teams_updated");
                            for (String s : updatedArray.split(Constants.TEAMS_UPDATED_SEPARATOR)) {
                                String[] teamAndGroupUpdated = s.split(Constants.GROUPS_UPDATED_SEPARATOR);
                                Long idTeam =  Long.parseLong(teamAndGroupUpdated[0]);
                                String idGroup = teamAndGroupUpdated[1];
                                Favorite favoriteGroup = FavoritesDAO.queryFavorite(getApplicationContext(), idGroup);
                                if (favoriteGroup!=null) {
                                    Group group = GroupsDAO.queryCompetitionsById(getApplicationContext(), idGroup);
                                    NotificationUtils.showNotificationUpdatedGroup(getApplicationContext(), group);
                                } else {
                                    Favorite favoriteTeam = FavoritesDAO.queryFavorite(getApplicationContext(), idGroup, idTeam);
                                    if (favoriteTeam!=null) {
                                        Group group = GroupsDAO.queryCompetitionsById(getApplicationContext(), idGroup);
                                        NotificationUtils.showNotificationUpdatedTeam(getApplicationContext(), favoriteTeam, group);
                                    }
                                }
                            }
                        }
                    break;
                case Constants.TOPICS + Constants.TOPICS_GENERAL:
                    if (UtilsPreferences.showNotifications(this)) {
                        Map<String, String> data = remoteMessage.getData();
                        String notificationMessage = data.get("notification_message");
                        NotificationUtils.showNotificationGeneral(getApplicationContext(), notificationMessage);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "onMessageReceived: " + e.getLocalizedMessage(), e);
        }
    }
}
