package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.CalendarAdapter;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabGroupCalendar extends Fragment {

    @BindView(R.id.expandableListView_calendar)
    ExpandableListView expandableListViewCalendar;

    CompetitionCallback mCompetitionCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCompetitionCallback = (CompetitionCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CompetitionCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Map<Integer, List<MatchRetrofit>> calendarMap = new HashMap<>();
        if (mCompetitionCallback.queryMatchesList()!=null) {
            for (MatchRetrofit matchRetrofitEntity : mCompetitionCallback.queryMatchesList()) {
                Integer weekNum = matchRetrofitEntity.getNumWeek() - 1;
                List<MatchRetrofit> matchesOnWeek = calendarMap.get(weekNum);
                if (matchesOnWeek==null) {
                    matchesOnWeek = new ArrayList<>();
                    calendarMap.put(weekNum, matchesOnWeek);
                }
                matchesOnWeek.add(matchRetrofitEntity);
            }
            CalendarAdapter calendarAdapter = new CalendarAdapter(this.getContext(), calendarMap);
            expandableListViewCalendar.setAdapter(calendarAdapter);
            calendarAdapter.notifyDataSetChanged();
        }
    }
}
