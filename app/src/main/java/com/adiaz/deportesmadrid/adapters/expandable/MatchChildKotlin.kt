package com.adiaz.deportesmadrid.adapters.expandable

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit
import com.adiaz.deportesmadrid.utils.Constants
import com.adiaz.deportesmadrid.utils.StateAnnotation
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat

class MatchChildKotlin(
        val teamLocal: String,
        val teamVisitor: String,
        val scoreLocal: String,
        val scoreVisitor: String,
        val state: String,
        val placeName: String,
        val dateStr: String,
        val numWeek: Int,
        val numMatch: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(teamLocal)
        parcel.writeString(teamVisitor)
        parcel.writeString(scoreLocal)
        parcel.writeString(scoreVisitor)
        parcel.writeString(state)
        parcel.writeString(placeName)
        parcel.writeString(dateStr)
        parcel.writeInt(numWeek)
        parcel.writeInt(numMatch)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MatchChildKotlin> {
        override fun createFromParcel(parcel: Parcel): MatchChildKotlin {
            return MatchChildKotlin(parcel)
        }

        override fun newArray(size: Int): Array<MatchChildKotlin?> {
            return arrayOfNulls(size)
        }

        fun matchRetrofit2MatchChild(matchRetrofit: MatchRetrofit, context: Context): MatchChildKotlin {
            var teamLocal = Constants.FIELD_EMPTY
            var teamVisitor = Constants.FIELD_EMPTY
            var scoreLocal = ""
            var scoreVisitor = ""
            var state: String = Constants.FIELD_EMPTY
            var placeName = Constants.FIELD_EMPTY
            var dateStr = Constants.FIELD_EMPTY
            var numWeek = 0;
            var numMatch = 0;

            if (matchRetrofit.teamLocal != null && StringUtils.isNotEmpty(matchRetrofit.teamLocal.name)) {
                teamLocal = matchRetrofit.teamLocal.name
            }
            if (matchRetrofit.teamVisitor != null && StringUtils.isNotEmpty(matchRetrofit.teamVisitor.name)) {
                teamVisitor = matchRetrofit.teamVisitor.name
            }
            /* check if a team is resting */
            if (teamLocal == Constants.FIELD_EMPTY && teamVisitor != Constants.FIELD_EMPTY) {
                teamLocal = context.getString(R.string.RESTING)
            }
            if (teamVisitor == Constants.FIELD_EMPTY && teamLocal != Constants.FIELD_EMPTY) {
                teamVisitor = context.getString(R.string.RESTING)
            }
            if (matchRetrofit.teamLocal != null && matchRetrofit.teamVisitor != null) {
                scoreLocal = matchRetrofit.scoreLocal!!.toString()
                scoreVisitor = matchRetrofit.scoreVisitor!!.toString()
                if (matchRetrofit.date != null) {
                    val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
                    dateStr = dateFormat.format(matchRetrofit.date)
                }
                if (matchRetrofit.place != null) {
                    placeName = matchRetrofit.place.name
                }
                state = StateAnnotation.stringKey(matchRetrofit.state!!)
            }
            return MatchChildKotlin(teamLocal, teamVisitor, scoreLocal, scoreVisitor, state, placeName, dateStr, numWeek, numMatch);
        }
    }
}