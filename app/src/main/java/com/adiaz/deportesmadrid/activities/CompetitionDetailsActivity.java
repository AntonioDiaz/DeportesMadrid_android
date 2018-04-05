package com.adiaz.deportesmadrid.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.retrofit.CompetitionsRetrofitApi;
import com.adiaz.deportesmadrid.retrofit.classification.ClassificationRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;
import com.adiaz.deportesmadrid.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompetitionDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_details)
    TextView tvDetails;

    @BindView(R.id.pb_loading_competition)
    ProgressBar pbCompetition;

    @BindView(R.id.view_competition)
    View mainView;

    @BindView(R.id.sv_details)
    ScrollView svDetails;

    List<ClassificationRetrofitEntity> classificationList;
    List<MatchRetrofitEntity> matchesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        ButterKnife.bind(this);
        String codCompetition = getIntent().getStringExtra(Constants.COD_COMPETITION);
        String nameCompetition = getIntent().getStringExtra(Constants.NAME_COMPETITION);
        getSupportActionBar().setSubtitle(nameCompetition);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvDetails.setText("Competición: " + codCompetition);
        showLoading();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
        CompetitionsRetrofitApi retrofitApi = retrofit.create(CompetitionsRetrofitApi.class);
        Call<List<MatchRetrofitEntity>> callMatches = retrofitApi.queryMatches(codCompetition);
        Call<List<ClassificationRetrofitEntity>> callClassification = retrofitApi.queryClassification(codCompetition);
        callMatches.enqueue(new CallbackMatchesRequest());
        callClassification.enqueue(new CallbackClassificationRequest());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading() {
        pbCompetition.setVisibility(View.VISIBLE);
        svDetails.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        pbCompetition.setVisibility(View.INVISIBLE);
        svDetails.setVisibility(View.VISIBLE);
    }


    private void matchesLoaded(List<MatchRetrofitEntity> matchesList) {
        this.matchesList = matchesList;
        reloadView();
    }

    private void classificationLoaded (List<ClassificationRetrofitEntity> classificationList) {
        this.classificationList = classificationList;
        reloadView();
    }

    private void reloadView() {
        if (matchesList!=null && classificationList!=null) {
            tvDetails.append("\nPartidos " + matchesList.size());
            tvDetails.append("\nClasificación: " + classificationList.size());
            if (matchesList != null) {
                for (MatchRetrofitEntity matchRetrofitEntity : matchesList) {
                    if (matchRetrofitEntity != null && matchRetrofitEntity.getTeamLocal() != null && matchRetrofitEntity.getTeamVisitor() != null) {
                        tvDetails.append("\n" + matchRetrofitEntity.getTeamLocal().getName() + " vs " + matchRetrofitEntity.getTeamVisitor().getName());
                    }
                }
            }
            hideLoading();
        }
    }

    class CallbackMatchesRequest implements Callback<List<MatchRetrofitEntity>> {
        @Override
        public void onResponse(Call<List<MatchRetrofitEntity>> call, Response<List<MatchRetrofitEntity>> response) {
            matchesLoaded(response.body());
        }

        @Override
        public void onFailure(Call<List<MatchRetrofitEntity>> call, Throwable t) {
            hideLoading();
            final Snackbar snackbar = Snackbar.make(mainView, "Error al obtener los resultados.", Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    class CallbackClassificationRequest implements Callback<List<ClassificationRetrofitEntity>> {
        @Override
        public void onResponse(Call<List<ClassificationRetrofitEntity>> call, Response<List<ClassificationRetrofitEntity>> response) {
            classificationLoaded(response.body());
        }

        @Override
        public void onFailure(Call<List<ClassificationRetrofitEntity>> call, Throwable t) {
            final Snackbar snackbar = Snackbar.make(mainView, "erroraco Classificacion cargo", Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }
}
