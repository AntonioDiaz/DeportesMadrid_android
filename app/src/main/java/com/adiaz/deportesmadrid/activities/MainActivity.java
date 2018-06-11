package com.adiaz.deportesmadrid.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.FavoritesAdapter;
import com.adiaz.deportesmadrid.adapters.SportsAdapter;
import com.adiaz.deportesmadrid.adapters.TeamSearchAdapter;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.retrofit.RetrofitApi;
import com.adiaz.deportesmadrid.retrofit.groupslist.GroupRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.searchteams.Team;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.ListItem;
import com.adiaz.deportesmadrid.utils.Utils;
import com.adiaz.deportesmadrid.utils.entities.TeamSearch;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements Callback<List<GroupRetrofitEntity>>, SportsAdapter.ListItemClickListener, FavoritesAdapter.ListItemClickListener, TeamSearchAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_view)
    View mainView;

    @BindView(R.id.view_sports)
    View viewSports;

    @BindView(R.id.view_search_results)
    View viewSearchResults;

    @BindView(R.id.rv_sports)
    RecyclerView rvCompetitions;

    @BindView(R.id.tb_sports)
    Toolbar toolbar;

    @BindView(R.id.rv_search_results)
    RecyclerView rvSearchResults;

    @BindView(R.id.tv_empty_list)
    TextView tvEmptyList;


    private List<Group> mCompetitionsList;
    private List<ListItem> elementsList;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mProgressDialogSearch;
    private Integer retryCount = 0;
    private List<TeamSearch> mTeamsSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);
        }

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle(getString(R.string.loading_competitions));
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mProgressDialogSearch = new ProgressDialog(MainActivity.this);
        mProgressDialogSearch.setTitle(getString(R.string.loading_search));
        mProgressDialogSearch.setMessage(getString(R.string.loading));
        mProgressDialogSearch.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showLoading();
        handleIntent(getIntent());
        viewSports.setVisibility(View.VISIBLE);
        viewSearchResults.setVisibility(View.INVISIBLE);
        tvEmptyList.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent: " + intent.getAction());
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String teamName = intent.getStringExtra(SearchManager.QUERY);
            mProgressDialogSearch.show();
            retryCount = 0;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    //.client(httpClient.build())
                    .build();
            RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
            Call<List<Team>> call = retrofitApi.queryTeams(teamName);
            viewSports.setVisibility(View.INVISIBLE);
            call.enqueue(new SearchTeamCallBack());


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompetitionsList = GroupsDAO.queryAllCompetitions(this);
        if (mCompetitionsList.size()==0) {
            syncCompetitions();
        } else {
            fillRecyclerview();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItemSearch = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        menuItemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                viewSports.setVisibility(View.VISIBLE);
                viewSearchResults.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_sync:
                syncCompetitions();
                break;
            case R.id.action_preferences:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncCompetitions() {
        showLoading();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<List<GroupRetrofitEntity>> call = retrofitApi.queryAllGroups();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<GroupRetrofitEntity>> call, Response<List<GroupRetrofitEntity>> response) {
        if (response.body()==null) {
            Utils.showSnack(mainView, getString(R.string.error_getting_data));
        } else {
            GroupsDAO.insertCompetitions(this, response.body());
            //select all
            mCompetitionsList = GroupsDAO.queryAllCompetitions(this);
            fillRecyclerview();
        }
    }

    @Override
    public void onFailure(Call<List<GroupRetrofitEntity>> call, Throwable t) {
        Log.d(TAG, "onFailure: peto" + t.getMessage());
    }

    private void fillRecyclerview() {
        elementsList = initElementsList(mCompetitionsList);
        //getSupportActionBar().setSubtitle("Competiciones: " + mCompetitionsList.size());
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        SportsAdapter sportsAdapter = new SportsAdapter(this, this, elementsList);
        rvCompetitions.setHasFixedSize(true);
        rvCompetitions.setLayoutManager(layoutManager);
        rvCompetitions.setAdapter(sportsAdapter);
        sportsAdapter.notifyDataSetChanged();
        hideLoading();
    }

    private List<ListItem> initElementsList(List<Group> groups) {
        List<ListItem> listElements = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (Group group : groups) {
            Integer count = map.get(group.deporte());
            if (count==null) {
                count = 0;
            }
            map.put(group.deporte(), count + 1);
        }
        for (String s : map.keySet()) {
            ListItem listItem = new ListItem(s, map.get(s).toString());
            listElements.add(listItem);
        }
        Collections.sort(listElements, new ListItem.ListItemCompartor());
        return listElements;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (clickedItemIndex==0) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DistrictActivity.class);
            String sportName = elementsList.get(clickedItemIndex - 1).getName();
            String count = elementsList.get(clickedItemIndex - 1).getCount();
            intent.putExtra(Constants.EXTRA_SPORT_SELECTED_NAME, sportName);
            intent.putExtra(Constants.EXTRA_COUNT, count);
            startActivity(intent);
        }
    }

    private void hideLoading() {
        mProgressDialog.dismiss();
    }

    private void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void onListItemClickTeamSearch(int clickedItemIndex) {
        TeamSearch teamSearch = mTeamsSearch.get(clickedItemIndex);
        Intent intent = new Intent(this, TeamDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, teamSearch.getIdGroup());
        intent.putExtra(Constants.ID_TEAM, teamSearch.getTeamName());
        startActivity(intent);
    }

    class SearchTeamCallBack implements Callback<List<Team>> {

        @Override
        public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
            mProgressDialogSearch.dismiss();
            viewSearchResults.setVisibility(View.INVISIBLE);
            rvSearchResults.setVisibility(View.INVISIBLE);
            tvEmptyList.setVisibility(View.INVISIBLE);
            if (response!=null && response.body()!=null) {
                viewSearchResults.setVisibility(View.VISIBLE);
                if (response.body().size()>0) {
                    rvSearchResults.setVisibility(View.VISIBLE);
                    mTeamsSearch = new ArrayList<>();
                    for (Team team : response.body()) {
                        for (String idGroup : team.getGroups()) {
                           mTeamsSearch.add(new TeamSearch(team.getId(), team.getName(), idGroup));
                        }
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    TeamSearchAdapter teamSearchAdapter = new TeamSearchAdapter(MainActivity.this, MainActivity.this, mTeamsSearch);
                    rvSearchResults.setHasFixedSize(true);
                    rvSearchResults.setLayoutManager(layoutManager);
                    rvSearchResults.setAdapter(teamSearchAdapter);
                    teamSearchAdapter.notifyDataSetChanged();
                } else {
                    tvEmptyList.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onFailure(Call<List<Team>> call, Throwable t) {
            mProgressDialogSearch.dismiss();
            viewSearchResults.setVisibility(View.VISIBLE);
        }
    }
}
