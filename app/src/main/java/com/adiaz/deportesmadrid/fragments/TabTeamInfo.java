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
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabTeamInfo extends Fragment {

    @BindView(R.id.tv_competition)
    TextView tvCompetition;

    @BindView(R.id.tv_fase)
    TextView tvFase;

    @BindView(R.id.tv_grupo)
    TextView tvGroup;


    @BindView(R.id.tv_sport)
    TextView tvSport;

    @BindView(R.id.tv_district)
    TextView tvDistrict;

    @BindView(R.id.tv_category)
    TextView tvCategory;

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
        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        ButterKnife.bind(this, view);
        Competition competition = mCompetitionCallback.queryCompetition();
        String sportTag = Utils.normalizaSportName(competition.deporte());
        String sportLocated = Utils.getStringResourceByName(this.getContext(), sportTag);
        tvCompetition.setText(competition.nomCompeticion());
        tvFase.setText(competition.nomFase());
        tvGroup.setText(competition.nomGrupo());
        tvSport.setText(sportLocated);
        tvDistrict.setText(competition.distrito());
        tvCategory.setText(competition.categoria());
        return view;
    }
}
