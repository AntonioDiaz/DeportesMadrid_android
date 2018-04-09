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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesActivity extends AppCompatActivity implements GenericAdapter.ListItemClickListener {

    //private static final String TAG = CategoriesActivity.class.getSimpleName();

    @BindView(R.id.pb_loading_districts)
    ProgressBar pbLoadingDistricts;

    @BindView(R.id.rv_districts)
    RecyclerView rvDistricts;

    List<ListItem> elementsList;
    private String sportSelected;
    private String districtSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        sportSelected = getIntent().getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME);
        districtSelected = getIntent().getStringExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME);
        String count = getIntent().getStringExtra(Constants.EXTRA_COUNT);
        String subTitle = sportSelected + " > " + districtSelected + " (" + count + ")";
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(getString(R.string.title_category));
            getSupportActionBar().setSubtitle(subTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        showLoading();
        List<Competition> competitions = CompetitionsDAO.queryCompetitionsBySportAndDistrict(this, sportSelected, districtSelected);
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ListItem> initElementsList(List<Competition> competitions) {
        List<ListItem> listElements = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (Competition competition : competitions) {
            Integer count = map.get(competition.categoria());
            if (count==null) {
                count = 0;
            }
            map.put(competition.categoria(), count + 1);
        }
        for (String s : map.keySet()) {
            ListItem listItem = new ListItem(s, map.get(s).toString());
            listElements.add(listItem);
        }
        return listElements;
    }

    private void showLoading() {
        pbLoadingDistricts.setVisibility(View.VISIBLE);
        rvDistricts.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        pbLoadingDistricts.setVisibility(View.INVISIBLE);
        rvDistricts.setVisibility(View.VISIBLE);
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        String categorySeleted = elementsList.get(clickedItemIndex).getName();
        String count = elementsList.get(clickedItemIndex).getCount();
        Intent intent = new Intent(this, GroupsActivity.class);
        intent.putExtra(Constants.EXTRA_SPORT_SELECTED_NAME, sportSelected);
        intent.putExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME, districtSelected);
        intent.putExtra(Constants.EXTRA_CATEGORY_SELECTED_NAME, categorySeleted);
        intent.putExtra(Constants.EXTRA_COUNT, count);
        startActivity(intent);

    }
}
