package com.adiaz.deportesmadrid.activities;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.CompetitionFragmentStatePagerAdapter;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.fragments.TabCalendar;
import com.adiaz.deportesmadrid.fragments.TabClassification;
import com.adiaz.deportesmadrid.fragments.TabTeams;
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

public class CompetitionDetailsActivity extends AppCompatActivity {

    private static final String TAG = CompetitionDetailsActivity.class.getSimpleName();

    @BindView(R.id.view_competition)
    View mainView;

    @BindView(R.id.tb_competition_details)
    Toolbar toolbar;

    @BindView(R.id.tl_competition_details)
    TabLayout tabLayout;

    @BindView(R.id.vp_competition_details)
    ViewPager viewPager;

    @BindView(R.id.ll_progress_competition_details)
    LinearLayout llLoadingCompetition;

    public static List<ClassificationRetrofitEntity> classificationList;
    public static List<MatchRetrofitEntity> matchesList;
    CompetitionFragmentStatePagerAdapter adapter;
    String mIdCompetition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        ButterKnife.bind(this);

        mIdCompetition = getIntent().getStringExtra(Constants.ID_COMPETITION);
        String nameCompetition = getIntent().getStringExtra(Constants.NAME_COMPETITION);

        classificationList = new ArrayList<>();
        matchesList = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setSubtitle(nameCompetition);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new CompetitionFragmentStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabCalendar(), "Calendario");
        adapter.addFragment(new TabClassification(), "Clasificación");
        adapter.addFragment(new TabTeams(), "Equipos");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        showLoading();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
        CompetitionsRetrofitApi retrofitApi = retrofit.create(CompetitionsRetrofitApi.class);
        Call<List<MatchRetrofitEntity>> callMatches = retrofitApi.queryMatches(mIdCompetition);
        Call<List<ClassificationRetrofitEntity>> callClassification = retrofitApi.queryClassification(mIdCompetition);
        callMatches.enqueue(new CallbackMatchesRequest());
        callClassification.enqueue(new CallbackClassificationRequest());

        //hideLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_competition, menu);
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.action_favorites) {
                Favorite favorite = FavoritesDAO.queryFavoritesCompetition(this, mIdCompetition);
                if (favorite!=null) {
                    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
                    menu.getItem(i).setIcon(drawable);
                }
                Drawable icon = menu.getItem(i).getIcon();
                int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
                final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
                icon.setColorFilter(colorFilter);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                Drawable drawable;
                Favorite favorite = FavoritesDAO.queryFavoritesCompetition(this, mIdCompetition);
                if (favorite != null) {
                    FavoritesDAO.deleteFavorite(this, favorite.id());
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite);
                    Toast.makeText(this, R.string.favorites_competition_removed, Toast.LENGTH_SHORT).show();
                } else {
                    Favorite newFavorite = Favorite.builder().idCompetition(mIdCompetition).build();
                    FavoritesDAO.insertFavorite(this, newFavorite);
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
                    Toast.makeText(this, R.string.favorites_competition_added, Toast.LENGTH_SHORT).show();
                }
                int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
                final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
                drawable.setColorFilter(colorFilter);
                item.setIcon(drawable);
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading() {
        llLoadingCompetition.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        llLoadingCompetition.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
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
            Utils.showSnack(mainView, "error en al obtener los partidos.");
        }
    }

    class CallbackClassificationRequest implements Callback<List<ClassificationRetrofitEntity>> {
        @Override
        public void onResponse(Call<List<ClassificationRetrofitEntity>> call, Response<List<ClassificationRetrofitEntity>> response) {
            classificationLoaded(response.body());
        }

        @Override
        public void onFailure(Call<List<ClassificationRetrofitEntity>> call, Throwable t) {
            hideLoading();
            Utils.showSnack(mainView, "error en al obtener la classificacion.");
        }
    }
}
