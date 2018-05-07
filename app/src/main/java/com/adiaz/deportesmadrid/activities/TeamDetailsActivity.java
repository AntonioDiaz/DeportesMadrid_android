package com.adiaz.deportesmadrid.activities;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.DeportesMadridFragmentStatePagerAdapter;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.fragments.TabClassification;
import com.adiaz.deportesmadrid.fragments.TabTeamCalendar;
import com.adiaz.deportesmadrid.fragments.TabTeamGroups;
import com.adiaz.deportesmadrid.fragments.TabTeamInfo;
import com.adiaz.deportesmadrid.retrofit.RetrofitApi;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.ClassificationRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.GroupDetailsRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.Team;
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

public class TeamDetailsActivity extends AppCompatActivity implements CompetitionCallback {

    @BindView(R.id.main_view)
    View mainView;

    @BindView(R.id.tb_team_details)
    Toolbar toolbar;

    @BindView(R.id.tl_team_details)
    TabLayout tabLayout;

    @BindView(R.id.vp_team_details)
    ViewPager viewPager;


    DeportesMadridFragmentStatePagerAdapter adapter;

    String mIdGroup;
    String mIdTeam;
    List<ClassificationRetrofit> classificationRetrofitList;
    List<MatchRetrofit> matchesRetrofitList;
    Group mGroup;
    Team mTeam;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        ButterKnife.bind(this);
        mIdGroup = getIntent().getStringExtra(Constants.ID_COMPETITION);
        mGroup = GroupsDAO.queryCompetitionsById(this, mIdGroup);
        mIdTeam = getIntent().getStringExtra(Constants.ID_TEAM);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(mIdTeam);
            getSupportActionBar().setSubtitle(getString(R.string.team_subtitle, mGroup.nomGrupo()));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new DeportesMadridFragmentStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabTeamInfo(), getString(R.string.team_details_tab_information));
        adapter.addFragment(new TabTeamCalendar(), getString(R.string.team_details_tab_calendar));
        adapter.addFragment(new TabClassification(), getString(R.string.team_details_tab_classification));
        adapter.addFragment(new TabTeamGroups(), getString(R.string.team_details_tab_grupos));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        mProgressDialog = new ProgressDialog(TeamDetailsActivity.this);
        mProgressDialog.setTitle(getString(R.string.loading_info));
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showLoading();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<GroupDetailsRetrofit> callCompetitionDetails = retrofitApi.findGroup(mIdGroup);
        callCompetitionDetails.enqueue(new CallbackRequest());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.action_favorites) {
                Favorite favorite = FavoritesDAO.queryFavorite(this, mIdGroup, mIdTeam);
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
                Favorite favorite = FavoritesDAO.queryFavorite(this, mIdGroup, mIdTeam);
                if (favorite != null) {
                    FavoritesDAO.deleteFavorite(this, favorite.id());
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_empty);
                    Toast.makeText(this, R.string.favorites_team_removed, Toast.LENGTH_SHORT).show();
                } else {
                    Favorite newFavorite = Favorite.builder().idGroup(mIdGroup).idTeam(mIdTeam).build();
                    FavoritesDAO.insertFavorite(this, newFavorite);
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
                    Toast.makeText(this, R.string.favorites_team_added, Toast.LENGTH_SHORT).show();
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

    private void hideLoading() {
        mProgressDialog.dismiss();
        viewPager.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mProgressDialog.show();
        viewPager.setVisibility(View.INVISIBLE);
    }

    private void dataReceived(GroupDetailsRetrofit groupDetailsRetrofit) {
        matchesRetrofitList = new ArrayList<>();
        if (groupDetailsRetrofit.getMatchRetrofits() != null) {
            for (MatchRetrofit matchRetrofitEntity : groupDetailsRetrofit.getMatchRetrofits()) {
                if ((matchRetrofitEntity.getTeamLocal() != null && matchRetrofitEntity.getTeamLocal().getName().equals(mIdTeam))
                        || (matchRetrofitEntity.getTeamVisitor() != null && matchRetrofitEntity.getTeamVisitor().getName().equals(mIdTeam))) {
                    matchesRetrofitList.add(matchRetrofitEntity);
                    if (mTeam==null) {
                        mTeam = new Team();
                        if (matchRetrofitEntity.getTeamLocal() != null && matchRetrofitEntity.getTeamLocal().getName().equals(mIdTeam)) {
                            mTeam.setId(matchRetrofitEntity.getTeamLocal().getId());
                            mTeam.setName(matchRetrofitEntity.getTeamLocal().getName());
                            mTeam.setGroups(matchRetrofitEntity.getTeamLocal().getGroups());
                        }
                        if (matchRetrofitEntity.getTeamVisitor()!=null && matchRetrofitEntity.getTeamVisitor().getName().equals(mIdTeam)) {
                            mTeam.setId(matchRetrofitEntity.getTeamVisitor().getId());
                            mTeam.setName(matchRetrofitEntity.getTeamVisitor().getName());
                            mTeam.setGroups(matchRetrofitEntity.getTeamVisitor().getGroups());
                        }
                    }
                }
            }
        }
        this.classificationRetrofitList = new ArrayList<>();
        if (groupDetailsRetrofit.getClassificationRetrofit() != null) {
            this.classificationRetrofitList = groupDetailsRetrofit.getClassificationRetrofit();
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        hideLoading();
    }

    @Override
    public List<ClassificationRetrofit> queryClassificationList() {
        return this.classificationRetrofitList;
    }

    @Override
    public Team queryTeam() {
        return mTeam;
    }

    @Override
    public List<MatchRetrofit> queryMatchesList() {
        return matchesRetrofitList;
    }

    @Override
    public Group queryCompetition() {
        return mGroup;
    }

    class CallbackRequest implements Callback<GroupDetailsRetrofit> {

        @Override
        public void onResponse(Call<GroupDetailsRetrofit> call, Response<GroupDetailsRetrofit> response) {
            dataReceived(response.body());
        }

        @Override
        public void onFailure(Call<GroupDetailsRetrofit> call, Throwable t) {
            hideLoading();
            Utils.showSnack(mainView, getString(R.string.error_getting_data));
        }
    }
}
