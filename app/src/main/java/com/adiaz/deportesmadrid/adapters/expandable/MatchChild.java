package com.adiaz.deportesmadrid.adapters.expandable;

import android.content.Context;
import android.os.Parcelable;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.StateAnnotation;
import com.google.auto.value.AutoValue;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@AutoValue
public abstract class MatchChild implements Parcelable {

    public abstract String teamLocal();
    public abstract String teamVisitor();
    public abstract String scoreLocal();
    public abstract String scoreVisitor();
    public abstract String state();
    public abstract String placeName();
    public abstract String dateStr();
    public abstract Integer numWeek();
    public abstract Integer numMatch();

    public static Builder builder() {
        return new AutoValue_MatchChild.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder teamLocal(String teamLocal);
        public abstract Builder teamVisitor(String teamVisitor);
        public abstract Builder scoreLocal(String scoreLocal);
        public abstract Builder scoreVisitor(String scoreVisitor);
        public abstract Builder state(String state);
        public abstract Builder placeName(String placeName);
        public abstract Builder dateStr(String dateStr);
        public abstract Builder numWeek(Integer numWeek);
        public abstract Builder numMatch(Integer numMatch);
        public abstract MatchChild build();
    }


    public static MatchChild matchRetrofit2MatchChild(MatchRetrofit matchRetrofit, Context context) {
        String teamLocal = Constants.FIELD_EMPTY;
        String teamVisitor = Constants.FIELD_EMPTY;
        String placeName = Constants.FIELD_EMPTY;
        String dateStr = Constants.FIELD_EMPTY;
        String state = Constants.FIELD_EMPTY;
        String scoreLocal = "";
        String scoreVisitor = "";

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
                .numWeek(matchRetrofit.getNumWeek())
                .numMatch(matchRetrofit.getNumMatch())
                .build();
        return matchChild;
    }


}
