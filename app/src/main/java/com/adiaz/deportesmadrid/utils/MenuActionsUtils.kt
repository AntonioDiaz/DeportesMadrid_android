package com.adiaz.deportesmadrid.utils


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.support.v4.app.ShareCompat
import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.adapters.expandable.MatchChildKotlin
import com.adiaz.deportesmadrid.db.entities.Group
import com.adiaz.deportesmadrid.extensions.toast
import java.text.ParseException
import java.util.*

/**
 * Created by toni on 13/09/2017.
 */

object MenuActionsUtils {

    fun showMatchLocation(context: Context, matchChild: MatchChildKotlin) {
        var matchPlace = matchChild.placeName
        if (Constants.FIELD_EMPTY == matchChild.placeName) {
            context.toast(context.getString(R.string.no_match_address))
        } else {
            matchPlace = normalizePlaceName(matchPlace)
            val addressUri = Uri.parse("geo:0,0?q=$matchPlace")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = addressUri
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
    }

    private fun normalizePlaceName(matchPlace: String): String {
        var matchPlace = matchPlace
        val colonIndex = matchPlace.indexOf(":")
        if (colonIndex != -1) {
            matchPlace = matchPlace.substring(0, colonIndex)
        }
        matchPlace = matchPlace.replace("CDM", "Centro deportivo Municipal")
        matchPlace = matchPlace.replace("IB", "Centro deportivo Municipal")
        matchPlace = matchPlace.replace("Cº", "Colegio")
        matchPlace = matchPlace.replace("Sº", "Santo")
        return "$matchPlace Madrid"
    }

    fun shareMatchDetails(activity: Activity, matchChild: MatchChildKotlin, group: Group) {
        val localTeam = matchChild.teamLocal
        val visitorTeam = matchChild.teamVisitor
        val sportCenter = matchChild.placeName
        val groupName = group.nomGrupo
        val sport = group.deporte
        val category = group.categoria
        val dateStr = matchChild.dateStr
        val numWeek = Integer.toString(matchChild.numWeek)
        val mimeType = "text/plain"
        val titleStr = activity.getString(R.string.calendar_title, groupName, numWeek, localTeam, visitorTeam)
        val subject = activity.getString(R.string.match_description,
                groupName, sport, category, numWeek, localTeam, visitorTeam, dateStr, sportCenter)
        ShareCompat.IntentBuilder
                .from(activity)
                .setChooserTitle(titleStr)
                .setSubject(titleStr)
                .setType(mimeType)
                .setText(subject)
                .startChooser()
    }

    fun addMatchEvent(context: Context, matchChild: MatchChildKotlin, group: Group) {
        if (Constants.FIELD_EMPTY == matchChild.dateStr) {
            context.toast(context.getString(R.string.no_match_event))
        } else {
            val localTeam = matchChild.teamLocal
            val visitorTeam = matchChild.teamVisitor
            val sportCenter = matchChild.placeName
            val sport = group.deporte
            val category = group.categoria
            try {
                val date = Utils.parseDate(matchChild.dateStr)
                val beginTime = Calendar.getInstance()
                beginTime.time = date
                val endTime = Calendar.getInstance()
                endTime.time = date
                endTime.add(Calendar.HOUR, 2)
                val titleStr = context.getString(R.string.calendar_title, group.nomGrupo, matchChild.numWeek.toString(), localTeam, visitorTeam)
                val descMatch = context.getString(R.string.match_description,
                        group.nomGrupo, sport, category, matchChild.numWeek.toString(), localTeam, visitorTeam, matchChild.dateStr, sportCenter)
                val intent = Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
                        .putExtra(CalendarContract.Events.TITLE, titleStr)
                        .putExtra(CalendarContract.Events.DESCRIPTION, descMatch)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, sportCenter)
                context.startActivity(intent)
            } catch (e: ParseException) {
                context.toast(context.getString(R.string.no_match_event))
            }

        }
    }
}
