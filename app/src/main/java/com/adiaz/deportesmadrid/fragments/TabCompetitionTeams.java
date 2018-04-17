package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.adiaz.deportesmadrid.activities.CompetitionDetailsActivity;
import com.adiaz.deportesmadrid.activities.TeamDetailsActivity;
import com.adiaz.deportesmadrid.adapters.TeamsAdapter;
import com.adiaz.deportesmadrid.callbacks.CalendarCallback;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabCompetitionTeams extends Fragment implements TeamsAdapter.ListItemClickListener {

    @BindView(R.id.rv_teams)
    RecyclerView rvTeams;

    CalendarCallback mCalendarCallback;
    private ArrayList<String> teamsNamesList;

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
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Set<String> teamsSet = new HashSet<>();
        for (MatchRetrofit matchRetrofit : mCalendarCallback.queryMatchesList()) {
            if (matchRetrofit.getTeamLocal()!=null) {
                teamsSet.add(matchRetrofit.getTeamLocal().getName());
            }
            if (matchRetrofit.getTeamVisitor()!=null) {
                teamsSet.add(matchRetrofit.getTeamVisitor().getName());
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        teamsNamesList = new ArrayList<>(teamsSet);
        Collections.sort(teamsNamesList);
        TeamsAdapter teamsAdapter = new TeamsAdapter(this.getContext(), CompetitionDetailsActivity.mIdCompetition, teamsNamesList, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvTeams.getContext(), layoutManager.getOrientation());
        rvTeams.setHasFixedSize(true);
        rvTeams.setLayoutManager(layoutManager);
        rvTeams.setAdapter(teamsAdapter);
        rvTeams.addItemDecoration(dividerItemDecoration);
        teamsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this.getContext(), TeamDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, CompetitionDetailsActivity.mIdCompetition);
        intent.putExtra(Constants.ID_TEAM, teamsNamesList.get(clickedItemIndex));
        startActivity(intent);
    }
}
