package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.entities.Match;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<Integer, List<MatchRetrofit>> calendarMap;

    @Nullable
    @BindView(R.id.tv_calendar_title)
    TextView tvCalendarTitle;

    @Nullable
    @BindView(R.id.tv_calendar_team_local)
    TextView tvTeamLocal;

    @Nullable
    @BindView(R.id.tv_calendar_team_visitor)
    TextView tvTeamVisitor;

    @Nullable
    @BindView(R.id.tv_calendar_date)
    TextView tvDate;

    @Nullable
    @BindView(R.id.tv_calendar_place)
    TextView tvPlace;

    @Nullable
    @BindView(R.id.tv_score_local)
    TextView tvScoreLocal;

    @Nullable
    @BindView(R.id.tv_score_visitor)
    TextView tvScoreVisitor;


    public CalendarAdapter(Context mContext, Map<Integer, List<MatchRetrofit>> calendarMap) {
        this.mContext = mContext;
        this.calendarMap = calendarMap;
    }

    @Override
    public int getGroupCount() {
        return calendarMap.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return calendarMap.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return calendarMap.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return calendarMap.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
        if (view==null) {
            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listitem_calendar_head, null);
        }
        ButterKnife.bind(this, view);
        tvCalendarTitle.setText("Jornada " + (groupPosition + 1));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        MatchRetrofit match = (MatchRetrofit) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listitem_calendar_child, null);
        }
        ButterKnife.bind(this, view);
        String teamLocal = Constants.FIELD_EMPTY;
        if (match.getTeamLocal()!=null && StringUtils.isNotEmpty(match.getTeamLocal().getName())) {
            teamLocal = match.getTeamLocal().getName();
        }
        String teamVisitor = Constants.FIELD_EMPTY;
        if (match.getTeamVisitor()!=null && StringUtils.isNotEmpty(match.getTeamVisitor().getName())) {
            teamVisitor = match.getTeamVisitor().getName();
        }
        /* check if a team is resting */
        if (teamLocal.equals(Constants.FIELD_EMPTY) && !teamVisitor.equals(Constants.FIELD_EMPTY)) {
            teamLocal = mContext.getString(R.string.RESTING);
        }
        if (teamVisitor.equals(Constants.FIELD_EMPTY) && !teamLocal.equals(Constants.FIELD_EMPTY)) {
            teamVisitor = mContext.getString(R.string.RESTING);
        }
        tvTeamLocal.setText(teamLocal);
        tvTeamVisitor.setText(teamVisitor);
        if (match.getTeamLocal()!=null && match.getTeamVisitor()!=null) {
            String dateStr = Constants.FIELD_EMPTY;
            String placeName = Constants.FIELD_EMPTY;
            if (match.getDate()!=null) {
                DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
                dateStr = dateFormat.format(match.getDate());
            }
            if (match.getPlace()!=null) {
                placeName = match.getPlace().getName();
            }
            String scoreLocalStr = match.getScoreLocal().toString();
            String scoreVisitorStr = match.getScoreVisitor().toString();
            tvDate.setVisibility(View.VISIBLE);
            tvPlace.setVisibility(View.VISIBLE);
            tvScoreVisitor.setVisibility(View.VISIBLE);
            tvScoreLocal.setVisibility(View.VISIBLE);
            tvDate.setText(dateStr);
            tvPlace.setText(placeName);
            tvScoreLocal.setText(scoreLocalStr);
            tvScoreVisitor.setText(scoreVisitorStr);
        } else {
            tvDate.setVisibility(View.INVISIBLE);
            tvPlace.setVisibility(View.INVISIBLE);
            tvScoreVisitor.setVisibility(View.INVISIBLE);
            tvScoreLocal.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
