package com.adiaz.deportesmadrid.utils;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

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
    
}
