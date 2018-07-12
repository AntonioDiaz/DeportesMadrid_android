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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
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
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<List<String>>>() {}.getType();
                            List<List<String>> teamsUpdated = gson.fromJson(updatedArray, listType);
                            for (List<String> strings : teamsUpdated) {
                                Long idTeam =  Long.parseLong(strings.get(0));
                                String idGroup = strings.get(1);
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
                        String title = data.get("title");
                        String body = data.get("body");
                        NotificationUtils.showNotificationGeneral(getApplicationContext(), title, body);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "onMessageReceived: " + e.getLocalizedMessage(), e);
        }
    }
}
