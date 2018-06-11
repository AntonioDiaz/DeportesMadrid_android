package com.adiaz.deportesmadrid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.widget.Toast;


import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.expandable.MatchChild;
import com.adiaz.deportesmadrid.db.entities.Group;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by toni on 13/09/2017.
 */

public class MenuActionsUtils {

    public static void showMatchLocation(Context context, MatchChild matchChild) {
        String matchPlace = matchChild.placeName();
        if (Constants.FIELD_EMPTY.equals(matchChild.placeName())) {
            String noDateStr = context.getString(R.string.no_match_address);
            Toast.makeText(context, noDateStr, Toast.LENGTH_SHORT).show();
        } else {
            matchPlace = normalizePlaceName(matchPlace);
            Uri addressUri = Uri.parse("geo:0,0?q=" + matchPlace);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(addressUri);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
    }

    private static String normalizePlaceName(String matchPlace) {
        int colonIndex = matchPlace.indexOf(":");
        if (colonIndex!=-1) {
            matchPlace = matchPlace.substring(0, colonIndex);
        }
        matchPlace = matchPlace.replace("CDM", "Centro deportivo Municipal");
        matchPlace = matchPlace.replace("IB", "Centro deportivo Municipal");
        matchPlace = matchPlace.replace("Cº", "Colegio");
        matchPlace = matchPlace.replace("Sº", "Santo");
        return matchPlace + " Madrid";
    }

    public static void shareMatchDetails(Activity activity, MatchChild matchChild, Group group) {
        String localTeam = matchChild.teamLocal();
        String visitorTeam = matchChild.teamVisitor();
        String sportCenter = matchChild.placeName();
        String groupName = group.nomGrupo();
        String sport = group.deporte();
        String category = group.categoria();
        String dateStr = matchChild.dateStr();
        String numWeek = matchChild.numWeek().toString();
        String mimeType = "text/plain";
        String titleStr = activity.getString(R.string.calendar_title, groupName, numWeek, localTeam, visitorTeam);
        String subject = activity.getString(R.string.match_description,
                groupName, sport, category, numWeek, localTeam, visitorTeam, dateStr, sportCenter);
        ShareCompat.IntentBuilder
                .from(activity)
                .setChooserTitle(titleStr)
                .setSubject(titleStr)
                .setType(mimeType)
                .setText(subject)
                .startChooser();
    }

    public static void addMatchEvent(Context context, MatchChild matchChild, Group group) {
        if (Constants.FIELD_EMPTY.equals(matchChild.dateStr())) {
            String noDateStr = context.getString(R.string.no_match_event);
            Toast.makeText(context, noDateStr, Toast.LENGTH_SHORT).show();
        } else {
            String localTeam = matchChild.teamLocal();
            String visitorTeam = matchChild.teamVisitor();
            String sportCenter = matchChild.placeName();
            String sport = group.deporte();
            String category = group.categoria();
            SharedPreferences preferences = getDefaultSharedPreferences(context);
            //String town = preferences.getString(DeporteLocalConstants.KEY_TOWN_NAME, null);
            try {
                Date date = Utils.parseDate(matchChild.dateStr());
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTime(date);
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(date);
                endTime.add(Calendar.HOUR, 2);
                String titleStr = context.getString(R.string.calendar_title, group.nomGrupo(), String.valueOf(matchChild.numWeek()), localTeam, visitorTeam);
                String descMatch = context.getString(R.string.match_description,
                        group.nomGrupo(), sport, category, String.valueOf(matchChild.numWeek()), localTeam, visitorTeam, matchChild.dateStr(), sportCenter);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, titleStr)
                        .putExtra(CalendarContract.Events.DESCRIPTION, descMatch)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, sportCenter);
                context.startActivity(intent);
            } catch (ParseException e) {
                String noDateStr = context.getString(R.string.no_match_event);
                Toast.makeText(context, noDateStr, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
