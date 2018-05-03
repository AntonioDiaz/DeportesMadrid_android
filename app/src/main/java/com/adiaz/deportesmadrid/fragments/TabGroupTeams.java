package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.activities.GroupDetailsActivity;
import com.adiaz.deportesmadrid.activities.TeamDetailsActivity;
import com.adiaz.deportesmadrid.adapters.GenericAdapter;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabGroupTeams extends Fragment implements GenericAdapter.ListItemClickListener {

    @BindView(R.id.rv_teams)
    RecyclerView rvTeams;

    CompetitionCallback mCompetitionCallback;
    private ArrayList<String> teamsNamesList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCompetitionCallback = (CompetitionCallback)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CompetitionCallback");
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
        for (MatchRetrofit matchRetrofit : mCompetitionCallback.queryMatchesList()) {
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
        List<ListItem> elements = new ArrayList<>();
        int i = 0;
        for (String s : teamsNamesList) {
            elements.add(new ListItem(s, Integer.toString(i++)));
        }
        GenericAdapter genericAdapter = new GenericAdapter(this.getContext(), this, elements);
        rvTeams.setHasFixedSize(true);
        rvTeams.setLayoutManager(layoutManager);
        rvTeams.setAdapter(genericAdapter);
        genericAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this.getContext(), TeamDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, GroupDetailsActivity.mIdGroup);
        intent.putExtra(Constants.ID_TEAM, teamsNamesList.get(clickedItemIndex));
        startActivity(intent);
    }
}
