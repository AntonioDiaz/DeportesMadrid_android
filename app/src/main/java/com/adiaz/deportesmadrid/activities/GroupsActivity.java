package com.adiaz.deportesmadrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.GroupsAdapter;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.UtilsPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupsActivity extends AppCompatActivity implements GroupsAdapter.ListItemClickListener {

    String sportSelected;
    String districtSelected;
    String categorySelected;
    List<Group> competitionsList;

    @BindView(R.id.rv_districts)
    RecyclerView recyclerView;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        sportSelected = getIntent().getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME);
        districtSelected = getIntent().getStringExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME);
        categorySelected = getIntent().getStringExtra(Constants.EXTRA_CATEGORY_SELECTED_NAME);
        String count = getIntent().getStringExtra(Constants.EXTRA_COUNT);
        String subtitle = sportSelected + Constants.PATH_SEPARATOR + districtSelected + Constants.PATH_SEPARATOR + categorySelected;
        if (UtilsPreferences.isShowCompetitionsNumber(this)) {
            subtitle += " (" + count + ")";
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(getString(R.string.title_grupo));
            getSupportActionBar().setSubtitle(subtitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        showLoading();
        competitionsList = GroupsDAO.queryCompetitionsBySportAndDistrictAndCategory(this, sportSelected, districtSelected, categorySelected);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GroupsAdapter genericAdapter = new GroupsAdapter(this, competitionsList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(genericAdapter);
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
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void hideLoading() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, GroupDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, competitionsList.get(clickedItemIndex).id());
        intent.putExtra(Constants.NAME_COMPETITION, competitionsList.get(clickedItemIndex).nomGrupo());
        startActivity(intent);
    }
}
