package com.adiaz.deportesmadrid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.Utils;

import java.util.Calendar;

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

    @BindView(R.id.tv_next_match_opponent)
    TextView tvNextOpponent;

    @BindView(R.id.tv_next_match_date)
    TextView tvNextDate;

    @BindView(R.id.tv_next_match_place)
    TextView tvNextPlace;

    @BindView(R.id.cv_next_week)
    CardView cvNextWeek;

    @BindView(R.id.cv_next_week_finished)
    CardView cvNextWeekFinished;

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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Competition competition = mCompetitionCallback.queryCompetition();
        String sportTag = Utils.normalizaSportName(competition.deporte());
        String sportLocated = Utils.getStringResourceByName(this.getContext(), sportTag);
        tvCompetition.setText(competition.nomCompeticion());
        tvFase.setText(competition.nomFase());
        tvGroup.setText(competition.nomGrupo());
        tvSport.setText(sportLocated);
        tvDistrict.setText(competition.distrito());
        tvCategory.setText(competition.categoria());
        String team = mCompetitionCallback.queryTeam();
        cvNextWeek.setVisibility(View.INVISIBLE);
        cvNextWeekFinished.setVisibility(View.INVISIBLE);
        if (mCompetitionCallback.queryMatchesList()!=null) {
            MatchRetrofit match = null;
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            for (MatchRetrofit matchRetrofit : mCompetitionCallback.queryMatchesList()) {
                if (matchRetrofit.getDate() > timeInMillis
                        && (match == null || match.getDate() > matchRetrofit.getDate())
                        && matchRetrofit.getTeamLocal()!=null && matchRetrofit.getTeamLocal().getName()!=null
                        && matchRetrofit.getTeamVisitor() != null && matchRetrofit.getTeamVisitor().getName()!=null) {
                    match = matchRetrofit;
                }
            }
            if (match != null) {
                tvNextDate.setText(Utils.formatDate(match.getDate()));
                tvNextPlace.setText(match.getPlace() != null ? match.getPlace().getName() : Constants.FIELD_EMPTY);
                String opponent = getContext().getString(R.string.RESTING);
                if (match.getTeamLocal() != null && !match.getTeamLocal().getName().equals(team)) {
                    opponent = match.getTeamLocal().getName();
                }
                if (match.getTeamVisitor() != null && !match.getTeamVisitor().getName().equals(team)) {
                    opponent = match.getTeamVisitor().getName();
                }
                tvNextOpponent.setText(opponent);
                cvNextWeek.setVisibility(View.VISIBLE);
            } else {
                cvNextWeekFinished.setVisibility(View.VISIBLE);

            }
        }
    }
}
