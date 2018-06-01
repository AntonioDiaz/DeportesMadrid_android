package com.adiaz.deportesmadrid.utils;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.expandable.MatchChild;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
        return df.format(dateLong);
    }


    public static MatchChild matchRetrofit2MatchChild(MatchRetrofit matchRetrofit, Context context) {
        String teamLocal = Constants.FIELD_EMPTY;
        String teamVisitor = Constants.FIELD_EMPTY;
        String placeName = Constants.FIELD_EMPTY;
        String dateStr = Constants.FIELD_EMPTY;
        String state = Constants.FIELD_EMPTY;
        String scoreLocal = Constants.FIELD_EMPTY;
        String scoreVisitor = Constants.FIELD_EMPTY;
        if (matchRetrofit.getTeamLocal()!=null && StringUtils.isNotEmpty(matchRetrofit.getTeamLocal().getName())) {
            teamLocal = matchRetrofit.getTeamLocal().getName();
        }
        if (matchRetrofit.getTeamVisitor()!=null && StringUtils.isNotEmpty(matchRetrofit.getTeamVisitor().getName())) {
            teamVisitor = matchRetrofit.getTeamVisitor().getName();
        }
        /* check if a team is resting */
        if (teamLocal.equals(Constants.FIELD_EMPTY) && !teamVisitor.equals(Constants.FIELD_EMPTY)) {
            teamLocal = context.getString(R.string.RESTING);
        }
        if (teamVisitor.equals(Constants.FIELD_EMPTY) && !teamLocal.equals(Constants.FIELD_EMPTY)) {
            teamVisitor = context.getString(R.string.RESTING);
        }
        if (matchRetrofit.getTeamLocal()!=null && matchRetrofit.getTeamVisitor()!=null) {
            scoreLocal = matchRetrofit.getScoreLocal().toString();
            scoreVisitor = matchRetrofit.getScoreVisitor().toString();
            if (matchRetrofit.getDate()!=null) {
                DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
                dateStr = dateFormat.format(matchRetrofit.getDate());
            }
            if (matchRetrofit.getPlace()!=null) {
                placeName = matchRetrofit.getPlace().getName();
            }
            state = StateAnnotation.stringKey(matchRetrofit.getState());
        }
        MatchChild matchChild = MatchChild.builder()
                .teamLocal(teamLocal)
                .teamVisitor(teamVisitor)
                .scoreLocal(scoreLocal)
                .scoreVisitor(scoreVisitor)
                .state(state)
                .dateStr(dateStr)
                .placeName(placeName)
                .build();
        return matchChild;
    }
}
