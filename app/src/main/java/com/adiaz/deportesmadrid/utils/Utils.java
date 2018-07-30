package com.adiaz.deportesmadrid.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.adiaz.deportesmadrid.R;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * If input is null or empty return "-"
     * Else return input with first letter in uppercase.
     *
     * @param input
     * @return
     */
    public static final String normalizeString(String input) {
        if (StringUtils.isEmpty(input)) {
            return "-";
        } else {
            return WordUtils.capitalizeFully(input.toLowerCase());
        }
    }


    public static final void showSnack(View view, String text) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static String getStringResourceByName(Context context, String aString) {
        String packageName = context.getPackageName();
        String strResource = aString;
        try {
            int resId = context.getResources().getIdentifier(aString, "string", packageName);
            strResource = context.getString(resId);
        } catch (Exception e) {
            Log.d(TAG, "resource not found at getStringResourceByName: " + aString);
        }
        return strResource;
    }

    public static String normalizaSportName(String sportNameOriginal) {
        return sportNameOriginal.replace(" ", "_").replace("-", "_").toUpperCase();
    }

    public static String formatDate(Long dateLong) {
        DateFormat df = new SimpleDateFormat(Constants.INSTANCE.getDATE_FORMAT());
        return df.format(dateLong);
    }

    public static Date parseDate(String dateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat(Constants.INSTANCE.getDATE_FORMAT());
        return df.parse(dateStr);
    }

    public static String getServerUrl(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String yearSelected = preferences.getString(context.getString(R.string.pref_year_key), context.getString(R.string.pref_year_default));
        if (yearSelected.equals(context.getString(R.string.pref_year_2017_value))) {
            return context.getString(R.string.url_2017);
        } else if (yearSelected.equals(context.getString(R.string.pref_year_2018_value))) {
            return context.getString(R.string.url_2018);
        } else if (yearSelected.equals(context.getString(R.string.pref_year_2019_value))) {
            return context.getString(R.string.url_2019);
        }
        return context.getString(R.string.url_2019);
    }

    public static String getYearDesc(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String yearSelected = preferences.getString(context.getString(R.string.pref_year_key), context.getString(R.string.pref_year_default));
        if (yearSelected.equals(context.getString(R.string.pref_year_2017_value))) {
            return context.getString(R.string.pref_year_2017_label);
        } else if (yearSelected.equals(context.getString(R.string.pref_year_2018_value))) {
            return context.getString(R.string.pref_year_2018_label);
        } else if (yearSelected.equals(context.getString(R.string.pref_year_2019_value))) {
            return context.getString(R.string.pref_year_2019_label);
        }
        return context.getString(R.string.pref_year_2019_label);
    }
}
