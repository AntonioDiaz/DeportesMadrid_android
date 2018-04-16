package com.adiaz.deportesmadrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.GenericAdapter;
import com.adiaz.deportesmadrid.db.daos.CompetitionsDAO;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistrictActivity extends AppCompatActivity implements GenericAdapter.ListItemClickListener {


    //private static final String TAG = DistrictActivity.class.getSimpleName();

    @BindView(R.id.pb_loading_districts)
    ProgressBar pbLoadingDistricts;

    @BindView(R.id.rv_districts)
    RecyclerView rvDistricts;


    String sportSelected;
    List<ListItem> elementsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        sportSelected = getIntent().getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME);
        String sportSelectedCount = getIntent().getStringExtra(Constants.EXTRA_COUNT);
        String subTitle = sportSelected + " (" + sportSelectedCount + ")";
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(getString(R.string.title_district));
            getSupportActionBar().setSubtitle(subTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        showLoading();
        List<Competition> competitions = CompetitionsDAO.queryCompetitionsBySport(this, sportSelected);
        elementsList = initElementsList(competitions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GenericAdapter genericAdapter = new GenericAdapter(this, this, elementsList);
        rvDistricts.setHasFixedSize(true);
        rvDistricts.setLayoutManager(layoutManager);
        rvDistricts.setAdapter(genericAdapter);
        genericAdapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_home:
                Intent i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ListItem> initElementsList(List<Competition> competitions) {
        List<ListItem> listElements = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (Competition competition : competitions) {
            Integer count = map.get(competition.distrito());
            if (count==null) {
                count = 0;
            }
            map.put(competition.distrito(), count + 1);
        }
        for (String s : map.keySet()) {
            //String value = map.get(s).toString();
            ListItem listItem = new ListItem(s, map.get(s).toString());
            listElements.add(listItem);
        }
        Collections.sort(listElements, new ListItem.ListItemCompartor());
        return listElements;
    }

    private void hideLoading() {
        pbLoadingDistricts.setVisibility(View.INVISIBLE);
        rvDistricts.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        pbLoadingDistricts.setVisibility(View.VISIBLE);
        rvDistricts.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        String districtName = elementsList.get(clickedItemIndex).getName();
        String count = elementsList.get(clickedItemIndex).getCount();
        intent.putExtra(Constants.EXTRA_SPORT_SELECTED_NAME, sportSelected);
        intent.putExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME, districtName);
        intent.putExtra(Constants.EXTRA_COUNT, count);
        startActivity(intent);
    }
}
