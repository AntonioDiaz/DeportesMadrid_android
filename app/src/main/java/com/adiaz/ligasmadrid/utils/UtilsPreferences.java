package com.adiaz.ligasmadrid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.adiaz.ligasmadrid.R;

public class UtilsPreferences {

    public static final boolean isShowCompetitionsNumber(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pref_competitons_num_key);
        return sharedPreferences.getBoolean(key, false);
    }
    public static final boolean showNotifications(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pref_notifications_key);
        return sharedPreferences.getBoolean(key, false);
    }

}
