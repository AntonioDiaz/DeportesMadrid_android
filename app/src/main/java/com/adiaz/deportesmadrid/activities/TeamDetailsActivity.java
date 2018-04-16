package com.adiaz.deportesmadrid.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.DeportesMadridFragmentStatePagerAdapter;
import com.adiaz.deportesmadrid.callbacks.ClassificationCallback;
import com.adiaz.deportesmadrid.db.daos.CompetitionsDAO;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.db.entities.Match;
import com.adiaz.deportesmadrid.fragments.TabClassification;
import com.adiaz.deportesmadrid.fragments.TabTeamCalendar;
import com.adiaz.deportesmadrid.fragments.TabTeamInfo;
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

public class TeamDetailsActivity extends AppCompatActivity implements ClassificationCallback {

    @BindView(R.id.main_view)
    View mainView;

    @BindView(R.id.tb_team_details)
    Toolbar toolbar;

    @BindView(R.id.tl_team_details)
    TabLayout tabLayout;

    @BindView(R.id.vp_team_details)
    ViewPager viewPager;

    @BindView(R.id.ll_progress_team_details)
    LinearLayout llLoading;

    DeportesMadridFragmentStatePagerAdapter adapter;

    String mIdCompetition;
    String mIdTeam;
    List<ClassificationRetrofitEntity> classificationList;
    List<MatchRetrofitEntity> matchesList;
    public static List<Match> matches = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        ButterKnife.bind(this);
        mIdCompetition = getIntent().getStringExtra(Constants.ID_COMPETITION);
        Competition competition = CompetitionsDAO.queryCompetitionsById(this, mIdCompetition);
        mIdTeam = getIntent().getStringExtra(Constants.ID_TEAM);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(mIdTeam);
            getSupportActionBar().setSubtitle(competition.nomGrupo());
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new DeportesMadridFragmentStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabTeamCalendar(), "Calendario");
        adapter.addFragment(new TabClassification(), "Clasificación");
        adapter.addFragment(new TabTeamInfo(), "Información");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        showLoading();
        classificationList = null;
        matchesList = null;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
        CompetitionsRetrofitApi retrofitApi = retrofit.create(CompetitionsRetrofitApi.class);
        Call<List<MatchRetrofitEntity>> callMatches = retrofitApi.queryMatches(mIdCompetition);
        Call<List<ClassificationRetrofitEntity>> callClassification = retrofitApi.queryClassification(mIdCompetition);
        callMatches.enqueue(new TeamDetailsActivity.CallbackMatchesRequest());
        callClassification.enqueue(new TeamDetailsActivity.CallbackClassificationRequest());
    }

    private void hideLoading() {
        llLoading.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        llLoading.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void matchesLoaded(List<MatchRetrofitEntity> matchesList) {
        this.matchesList =new ArrayList<>();
        if (matchesList!=null) {
            this.matchesList = matchesList;
        }
        reloadView();
    }

    public void classificationLoaded(List<ClassificationRetrofitEntity> classificationList) {
        this.classificationList = new ArrayList<>();
        if (classificationList!=null) {
            this.classificationList = classificationList;
        }
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
                    String teamLocalName = matchRetrofitEntity.getTeamLocal()==null? "-" : matchRetrofitEntity.getTeamLocal().getName();
                    String teamVisitorName = matchRetrofitEntity.getTeamVisitor()==null ? "-" : matchRetrofitEntity.getTeamVisitor().getName();
                    Match match = Match.builder()
                            .teamLocal(teamLocalName)
                            .teamVisitor(teamVisitorName)
                            .build();
                    matches.add(match);
                }
            }
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            hideLoading();
        }
    }

    @Override
    public List<ClassificationRetrofitEntity> queryClassificationList() {
        return this.classificationList;
    }

    @Override
    public String underlineTeam() {
        return mIdTeam;
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
