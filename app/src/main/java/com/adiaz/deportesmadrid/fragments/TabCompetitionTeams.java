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
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.CompetitionDetailsActivity;
import com.adiaz.deportesmadrid.adapters.GenericAdapter;
import com.adiaz.deportesmadrid.adapters.TeamsAdapter;
import com.adiaz.deportesmadrid.callbacks.CalendarCallback;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;
import com.adiaz.deportesmadrid.utils.ListItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabCompetitionTeams extends Fragment {

    @BindView(R.id.rv_teams)
    RecyclerView rvTeams;

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
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Set<String> teamsSet = new HashSet<>();
        for (MatchRetrofitEntity matchRetrofitEntity : mCalendarCallback.queryMatchesList()) {
            if (matchRetrofitEntity.getTeamLocal()!=null) {
                teamsSet.add(matchRetrofitEntity.getTeamLocal().getName());
            }
            if (matchRetrofitEntity.getTeamVisitor()!=null) {
                teamsSet.add(matchRetrofitEntity.getTeamVisitor().getName());
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        TeamsAdapter genericAdapter = new TeamsAdapter(this.getContext(), CompetitionDetailsActivity.mIdCompetition, new ArrayList<>(teamsSet));
        rvTeams.setHasFixedSize(true);
        rvTeams.setLayoutManager(layoutManager);
        rvTeams.setAdapter(genericAdapter);
        genericAdapter.notifyDataSetChanged();
    }
}
