package com.adiaz.deportesmadrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.GenericAdapter;
import com.adiaz.deportesmadrid.adapters.SportsAdapter;
import com.adiaz.deportesmadrid.db.daos.CompetitionsDAO;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.retrofit.CompetitionsRetrofitApi;
import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.ListItem;
import com.adiaz.deportesmadrid.utils.Utils;

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
        implements Callback<List<CompetitionRetrofitEntity>>, SportsAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_view)
    View mainView;

    @BindView(R.id.progressBar)
    ProgressBar pb;

    @BindView(R.id.view_result)
    View vResults;

    @BindView(R.id.rv_sports)
    RecyclerView rvCompetitions;

    private List<Competition> mCompetitionsList;

    private List<ListItem> elementsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTheme(R.style.AppTheme);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            // TODO: 5/4/18 chapu: fix spaces add margin between icon and title.
            getSupportActionBar().setTitle("    " + getString(R.string.app_name));
        }
        pb.setVisibility(View.INVISIBLE);
        vResults.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompetitionsList = CompetitionsDAO.queryAllCompetitions(this);
        if (mCompetitionsList.size()==0) {
            syncCompetitions();
        } else {
            fillRecyclerview();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId== R.id.action_sync) {
            syncCompetitions();
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncCompetitions() {
        pb.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
        CompetitionsRetrofitApi retrofitApi = retrofit.create(CompetitionsRetrofitApi.class);
        Call<List<CompetitionRetrofitEntity>> call = retrofitApi.queryAllCompetition();
        vResults.setVisibility(View.INVISIBLE);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<CompetitionRetrofitEntity>> call, Response<List<CompetitionRetrofitEntity>> response) {
        if (response.body()==null) {
            Utils.showSnack(mainView, getString(R.string.error_getting_data));
        } else {
            CompetitionsDAO.insertCompetitions(this, response.body());
            //select all
            mCompetitionsList = CompetitionsDAO.queryAllCompetitions(this);
            fillRecyclerview();
        }
    }

    @Override
    public void onFailure(Call<List<CompetitionRetrofitEntity>> call, Throwable t) {
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
        pb.setVisibility(View.INVISIBLE);
        vResults.setVisibility(View.VISIBLE);
    }

    private List<ListItem> initElementsList(List<Competition> competitions) {
        List<ListItem> listElements = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (Competition competition : competitions) {
            Integer count = map.get(competition.deporte());
            if (count==null) {
                count = 0;
            }
            map.put(competition.deporte(), count + 1);
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
}
