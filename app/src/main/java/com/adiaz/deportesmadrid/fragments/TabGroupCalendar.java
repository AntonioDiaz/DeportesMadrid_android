package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabGroupCalendar extends Fragment {

    @BindView(R.id.rv_calendar)
    RecyclerView rvCalendar;

    CompetitionCallback mCompetitionCallback;
    public com.adiaz.deportesmadrid.adapters.expandable.CalendarAdapter calendarAdapter;

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
            calendarAdapter = new com.adiaz.deportesmadrid.adapters.expandable.CalendarAdapter(mCompetitionCallback.queryWeeks());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCalendar.getContext(), layoutManager.getOrientation());
            rvCalendar.setLayoutManager(layoutManager);
            rvCalendar.setAdapter(calendarAdapter);
            rvCalendar.addItemDecoration(dividerItemDecoration);
            calendarAdapter.notifyDataSetChanged();
        }
    }
}
