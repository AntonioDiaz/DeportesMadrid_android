package com.adiaz.deportesmadrid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.CompetitionDetailsActivity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabCalendar extends Fragment {

    @BindView(R.id.tv_calendar)
    TextView tvCalendar;

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
        if (CompetitionDetailsActivity.matchesList!=null) {
            for (MatchRetrofitEntity matchRetrofitEntity : CompetitionDetailsActivity.matchesList) {
                if (matchRetrofitEntity.getTeamLocal()!=null && matchRetrofitEntity.getTeamVisitor()!=null) {
                    tvCalendar.append(matchRetrofitEntity.getTeamLocal().getName());
                    tvCalendar.append(" - ");
                    tvCalendar.append(matchRetrofitEntity.getTeamVisitor().getName());
                    tvCalendar.append("\n");
                }
            }
        }
    }
}
