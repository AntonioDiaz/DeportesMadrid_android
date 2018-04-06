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

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabTeams extends Fragment {

    @BindView(R.id.tv_teams)
    TextView tvTeams;

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
        for (MatchRetrofitEntity matchRetrofitEntity : CompetitionDetailsActivity.matchesList) {
            if (matchRetrofitEntity.getTeamLocal()!=null) {
                teamsSet.add(matchRetrofitEntity.getTeamLocal().getName());
            }
            if (matchRetrofitEntity.getTeamVisitor()!=null) {
                teamsSet.add(matchRetrofitEntity.getTeamVisitor().getName());
            }
        }
        for (String s : teamsSet) {
            tvTeams.append("Team :" + s + "\n");
        }
    }
}
