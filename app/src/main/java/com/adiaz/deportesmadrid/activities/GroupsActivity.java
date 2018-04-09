package com.adiaz.deportesmadrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsActivity extends AppCompatActivity implements GenericAdapter.ListItemClickListener {

    String sportSelected;
    String districtSelected;
    String categorySelected;
    List<ListItem> competitionsList;

    @BindView(R.id.rv_districts)
    RecyclerView recyclerView;

    @BindView(R.id.pb_loading_districts)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        sportSelected = getIntent().getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME);
        districtSelected = getIntent().getStringExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME);
        categorySelected = getIntent().getStringExtra(Constants.EXTRA_CATEGORY_SELECTED_NAME);
        String count = getIntent().getStringExtra(Constants.EXTRA_COUNT);
        String subtitle = sportSelected + " > " + districtSelected + " > " + categorySelected + ": " + count;
        getSupportActionBar().setSubtitle(subtitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showLoading();
        List<Competition> competitions = CompetitionsDAO.queryCompetitionsBySportAndDistrictAndCategory(this, sportSelected, districtSelected, categorySelected);
        competitionsList = initElementsList(competitions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GenericAdapter genericAdapter = new GenericAdapter(this, this, competitionsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(genericAdapter);
        genericAdapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ListItem> initElementsList(List<Competition> competitions) {
        List<ListItem> listElements = new ArrayList<>();
        for (Competition competition : competitions) {
            listElements.add(new ListItem(competition.nomGrupo(), competition.id()));
        }
        return listElements;
    }
    
    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, CompetitionDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, competitionsList.get(clickedItemIndex).getCount());
        intent.putExtra(Constants.NAME_COMPETITION, competitionsList.get(clickedItemIndex).getName());
        startActivity(intent);
    }
}
