package com.adiaz.deportesmadrid.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.TeamDetailsActivity;
import com.adiaz.deportesmadrid.adapters.TeamMatchesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabTeamCalendar extends Fragment {

    @BindView(R.id.rv_team_matches)
    RecyclerView rvTeamMatches;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        TeamMatchesAdapter teamMatchesAdapter = new TeamMatchesAdapter(this.getContext(), TeamDetailsActivity.matches);
        rvTeamMatches.setHasFixedSize(true);
        rvTeamMatches.setLayoutManager(linearLayoutManager);
        rvTeamMatches.setAdapter(teamMatchesAdapter);
        teamMatchesAdapter.notifyDataSetChanged();
    }
}
