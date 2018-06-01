package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.TeamMatchesAdapter;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabTeamCalendar extends Fragment {



    @BindView(R.id.rv_team_matches)
    RecyclerView rvTeamMatches;

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
        View view = inflater.inflate(R.layout.fragment_team_calendar, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<MatchRetrofit> teamMatches = mCompetitionCallback.queryMatchesList();
        if (teamMatches!=null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
            String teamName = mCompetitionCallback.queryTeam()==null ? Constants.FIELD_EMPTY : mCompetitionCallback.queryTeam().getName();
            TeamMatchesAdapter teamMatchesAdapter = new TeamMatchesAdapter(this.getContext(), mCompetitionCallback.queryMatchesList(), teamName);
            rvTeamMatches.setHasFixedSize(true);
            rvTeamMatches.setLayoutManager(linearLayoutManager);
            rvTeamMatches.setAdapter(teamMatchesAdapter);
            teamMatchesAdapter.notifyDataSetChanged();
        }
    }
}
