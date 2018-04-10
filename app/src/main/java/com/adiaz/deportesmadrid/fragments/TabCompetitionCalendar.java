package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.CompetitionDetailsActivity;
import com.adiaz.deportesmadrid.callbacks.CalendarCallback;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabCompetitionCalendar extends Fragment {

    @BindView(R.id.tv_calendar)
    TextView tvCalendar;

    CalendarCallback mCalendarCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCalendarCallback = (CalendarCallback)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CalendarCallback");
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
        if (mCalendarCallback.queryMatchesList()!=null) {
            for (MatchRetrofitEntity matchRetrofitEntity : mCalendarCallback.queryMatchesList()) {
                String teamLocal = matchRetrofitEntity.getTeamLocal()==null?" - ":matchRetrofitEntity.getTeamLocal().getName();
                String teamVisitor = matchRetrofitEntity.getTeamVisitor()==null?" - ":matchRetrofitEntity.getTeamVisitor().getName();
                tvCalendar.append(teamLocal);
                tvCalendar.append(" - ");
                tvCalendar.append(teamVisitor);
                tvCalendar.append("\n");
            }
        }
    }
}
