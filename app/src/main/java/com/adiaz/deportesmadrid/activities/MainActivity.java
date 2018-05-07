package com.adiaz.deportesmadrid.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.SportsAdapter;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.retrofit.RetrofitApi;
import com.adiaz.deportesmadrid.retrofit.groupslist.GroupRetrofitEntity;
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
        implements Callback<List<GroupRetrofitEntity>>, SportsAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_view)
    View mainView;

    @BindView(R.id.view_results)
    View vResults;

    @BindView(R.id.rv_sports)
    RecyclerView rvCompetitions;

    @BindView(R.id.tb_sports)
    Toolbar toolbar;


    private List<Group> mCompetitionsList;
    private List<ListItem> elementsList;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);
        }
        //toolbar.setTitle("toma toma");
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle(getString(R.string.loading_competitions));
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showLoading();
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
        /*
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);
        */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<List<GroupRetrofitEntity>> call = retrofitApi.queryAllGroups();
        vResults.setVisibility(View.INVISIBLE);
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
        vResults.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mProgressDialog.show();
        vResults.setVisibility(View.INVISIBLE);
    }
}
