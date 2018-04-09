package com.adiaz.deportesmadrid.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.TeamMatchesAdapter;
import com.adiaz.deportesmadrid.db.entities.Match;
import com.adiaz.deportesmadrid.retrofit.CompetitionsRetrofitApi;
import com.adiaz.deportesmadrid.retrofit.classification.ClassificationRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeamDetailsActivity extends AppCompatActivity {

    @BindView(R.id.main_view)
    View mainView;

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    @BindView(R.id.rv_team_matches)
    RecyclerView rvTeamMatches;

    String mIdCompetition;
    String mIdTeam;
    public static List<ClassificationRetrofitEntity> classificationList;
    public static List<MatchRetrofitEntity> matchesList;
    List<Match> matches;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_matches);
        ButterKnife.bind(this);
        showLoading();
        classificationList = null;
        matchesList = null;
        mIdCompetition = getIntent().getStringExtra(Constants.ID_COMPETITION);
        mIdTeam = getIntent().getStringExtra(Constants.ID_TEAM);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(mIdTeam);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
        CompetitionsRetrofitApi retrofitApi = retrofit.create(CompetitionsRetrofitApi.class);
        Call<List<MatchRetrofitEntity>> callMatches = retrofitApi.queryMatches(mIdCompetition);
        Call<List<ClassificationRetrofitEntity>> callClassification = retrofitApi.queryClassification(mIdCompetition);
        callMatches.enqueue(new TeamDetailsActivity.CallbackMatchesRequest());
        callClassification.enqueue(new TeamDetailsActivity.CallbackClassificationRequest());

        //hideLoading();
    }

    public void matchesLoaded(List<MatchRetrofitEntity> matchesList) {
        TeamDetailsActivity.matchesList = matchesList;
        reloadView();
    }

    public void classificationLoaded(List<ClassificationRetrofitEntity> classificationList) {
        TeamDetailsActivity.classificationList = classificationList;
        reloadView();
    }

    public void failLoading(String errorDesc) {
        hideLoading();
        Utils.showSnack(mainView, errorDesc);
    }

    private void reloadView() {
        if (matchesList!=null && classificationList!=null) {
            matches = new ArrayList<>();
            for (MatchRetrofitEntity matchRetrofitEntity : matchesList) {
                if ((matchRetrofitEntity.getTeamLocal()!=null && matchRetrofitEntity.getTeamLocal().getName().equals(mIdTeam))
                            || (matchRetrofitEntity.getTeamVisitor()!=null && matchRetrofitEntity.getTeamVisitor().getName().equals(mIdTeam))) {
                    String teamLocalName = matchRetrofitEntity.getTeamLocal()==null?"-":matchRetrofitEntity.getTeamLocal().getName();
                    String teamVisitorName = matchRetrofitEntity.getTeamVisitor()==null?"-":matchRetrofitEntity.getTeamVisitor().getName();
                    Match match = Match.builder()
                            .teamLocal(teamLocalName)
                            .teamVisitor(teamVisitorName)
                            .build();
                    matches.add(match);
                }
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            TeamMatchesAdapter teamMatchesAdapter = new TeamMatchesAdapter(this, matches);
            rvTeamMatches.setHasFixedSize(true);
            rvTeamMatches.setLayoutManager(layoutManager);
            rvTeamMatches.setAdapter(teamMatchesAdapter);
            teamMatchesAdapter.notifyDataSetChanged();
            hideLoading();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideLoading() {
        llLoading.setVisibility(View.INVISIBLE);
        rvTeamMatches.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        llLoading.setVisibility(View.VISIBLE);
        rvTeamMatches.setVisibility(View.INVISIBLE);
    }


    class CallbackMatchesRequest implements Callback<List<MatchRetrofitEntity>> {


        @Override
        public void onResponse(Call<List<MatchRetrofitEntity>> call, Response<List<MatchRetrofitEntity>> response) {
            matchesLoaded(response.body());
        }

        @Override
        public void onFailure(Call<List<MatchRetrofitEntity>> call, Throwable t) {
            failLoading("Error al obtener los partidos.");
        }
    }

    class CallbackClassificationRequest implements Callback<List<ClassificationRetrofitEntity>> {


        @Override
        public void onResponse(Call<List<ClassificationRetrofitEntity>> call, Response<List<ClassificationRetrofitEntity>> response) {
            classificationLoaded(response.body());
        }

        @Override
        public void onFailure(Call<List<ClassificationRetrofitEntity>> call, Throwable t) {
           failLoading("Error al obtener la classificación.");
        }
    }

}
