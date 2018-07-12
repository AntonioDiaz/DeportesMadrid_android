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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.DeportesMadridFragmentStatePagerAdapter;
import com.adiaz.deportesmadrid.adapters.TeamMatchesAdapter;
import com.adiaz.deportesmadrid.adapters.expandable.MatchChild;
import com.adiaz.deportesmadrid.adapters.expandable.WeekGroup;
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
import com.adiaz.deportesmadrid.utils.MenuActionsUtils;
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

public class TeamDetailsActivity extends AppCompatActivity implements CompetitionCallback, TeamMatchesAdapter.ListItemClickListener {

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
    Long mTeamId;
    String mTeamName;
    List<ClassificationRetrofit> classificationRetrofitList;
    List<MatchRetrofit> matchesRetrofitList;
    Group mGroup;
    Team mTeam;
    ProgressDialog mProgressDialog;
    MatchRetrofit mMatchRetrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        ButterKnife.bind(this);
        mIdGroup = getIntent().getStringExtra(Constants.ID_COMPETITION);
        mGroup = GroupsDAO.queryCompetitionsById(this, mIdGroup);
        mTeamId = getIntent().getLongExtra(Constants.TEAM_ID, 0L);
        mTeamName = getIntent().getStringExtra(Constants.TEAM_NAME);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(mTeamName);
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
                .baseUrl(Utils.getServerUrl(getApplicationContext())).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<GroupDetailsRetrofit> callCompetitionDetails = retrofitApi.findGroup(mIdGroup);
        callCompetitionDetails.enqueue(new CallbackRequest());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.action_favorites) {
                Favorite favorite = FavoritesDAO.queryFavorite(this, mIdGroup, mTeamId);
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
                Favorite favorite = FavoritesDAO.queryFavorite(this, mIdGroup, mTeamId);
                if (favorite != null) {
                    FavoritesDAO.deleteFavorite(this, favorite.id());
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_empty);
                    Toast.makeText(this, R.string.favorites_team_removed, Toast.LENGTH_SHORT).show();
                } else {
                    Favorite newFavorite = Favorite.builder().idGroup(mIdGroup).idTeam(mTeamId).nameTeam(mTeamName).build();
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
                if ((matchRetrofitEntity.getTeamLocal() != null && matchRetrofitEntity.getTeamLocal().getId().equals(mTeamId))
                        || (matchRetrofitEntity.getTeamVisitor() != null && matchRetrofitEntity.getTeamVisitor().getId().equals(mTeamId))) {
                    matchesRetrofitList.add(matchRetrofitEntity);
                    if (mTeam==null) {
                        mTeam = new Team();
                        if (matchRetrofitEntity.getTeamLocal() != null && matchRetrofitEntity.getTeamLocal().getId().equals(mTeamId)) {
                            mTeam.setId(matchRetrofitEntity.getTeamLocal().getId());
                            mTeam.setName(matchRetrofitEntity.getTeamLocal().getName());
                            mTeam.setGroups(matchRetrofitEntity.getTeamLocal().getGroups());
                        }
                        if (matchRetrofitEntity.getTeamVisitor()!=null && matchRetrofitEntity.getTeamVisitor().getId().equals(mTeamId)) {
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
    public List<WeekGroup> queryWeeks() {
        return null;
    }

    @Override
    public Group queryCompetition() {
        return mGroup;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mMatchRetrofit = matchesRetrofitList.get(clickedItemIndex);
        registerForContextMenu(mainView);
        openContextMenu(mainView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (mMatchRetrofit!=null) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_match, menu);
            menu.findItem(R.id.action_view_map).setEnabled(mMatchRetrofit.getPlace()!=null);
            menu.findItem(R.id.action_add_calendar).setEnabled(mMatchRetrofit.getDate()!=null);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        MatchChild matchChild = MatchChild.matchRetrofit2MatchChild(mMatchRetrofit, this);
        switch (item.getItemId()) {
            case R.id.action_add_calendar:
                MenuActionsUtils.addMatchEvent(this, matchChild, mGroup);
                //Toast.makeText(this, "comming son", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_view_map:
                MenuActionsUtils.showMatchLocation(this, matchChild);
                break;
            case R.id.action_share:
                MenuActionsUtils.shareMatchDetails(this, matchChild, mGroup);
                break;
                /*
            case R.id.action_notify_error:
               if (DeporteLocalUtils.isUserLogged()) {
                    SendIssueDialogFragment dialog = SendIssueDialogFragment.newInstance(mMatch, CompetitionActivity.mCompetition);
                    dialog.show(getSupportFragmentManager(), "dialog");
                } else {
                    Toast.makeText(this, getText(R.string.dialog_issue_not_allowed), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "comming son", Toast.LENGTH_SHORT).show();
                break;
                */
        }
        return super.onContextItemSelected(item);
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
