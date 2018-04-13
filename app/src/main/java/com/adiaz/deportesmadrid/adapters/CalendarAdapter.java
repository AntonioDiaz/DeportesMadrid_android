package com.adiaz.deportesmadrid.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.db.entities.Match;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<Integer, List<Match>> calendarMap;

    @Nullable
    @BindView(R.id.tv_calendar_title)
    TextView tvCalendarTitle;

    @Nullable
    @BindView(R.id.tv_calendar_detail)
    TextView tvCalendarDetail;


    public CalendarAdapter(Context mContext, Map<Integer, List<Match>> calendarMap) {
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
        tvCalendarTitle.setText("Jornada " + groupPosition);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        Match match = (Match) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listitem_calendar_child, null);
        }
        ButterKnife.bind(this, view);
        tvCalendarDetail.setText(match.teamLocal() + " -- " + match.teamVisitor());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
