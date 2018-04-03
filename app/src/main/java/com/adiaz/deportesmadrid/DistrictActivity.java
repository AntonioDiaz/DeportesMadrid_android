package com.adiaz.deportesmadrid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adiaz.deportesmadrid.adapters.CompetitionAdapter;
import com.adiaz.deportesmadrid.db.CompetitionsDAO;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.RecyclerElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistrictActivity extends AppCompatActivity implements CompetitionAdapter.ListItemClickListener {


    private static final String TAG = DistrictActivity.class.getSimpleName();

    @BindView(R.id.pb_loading_districts)
    ProgressBar pbLoadingDistricts;

    @BindView(R.id.rv_districts)
    RecyclerView rvDistricts;


    List<RecyclerElement> elementsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        String sportSelected = getIntent().getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME);
        Integer sportSelectedCount = getIntent().getIntExtra(Constants.EXTRA_SPORT_SELECTED_COUNT, 0);
        String subTitle = sportSelected + " - " + sportSelectedCount;
        ButterKnife.bind(this);
        showLoading();
        getSupportActionBar().setSubtitle(subTitle);
        List<Competition> competitions = CompetitionsDAO.queryCompetitionsBySport(this, sportSelected);
        Log.d(TAG, "onCreate: " + competitions.size());
        elementsList = initElementsList(competitions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        CompetitionAdapter competitionAdapter = new CompetitionAdapter(this, this, elementsList);
        rvDistricts.setHasFixedSize(true);
        rvDistricts.setLayoutManager(layoutManager);
        rvDistricts.setAdapter(competitionAdapter);
        competitionAdapter.notifyDataSetChanged();
        hideLoading();

    }

    private List<RecyclerElement> initElementsList(List<Competition> competitions) {
        List<RecyclerElement> listElements = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();
        for (Competition competition : competitions) {
            Integer count = map.get(competition.distrito());
            if (count==null) {
                count = 0;
            }
            map.put(competition.distrito(), count + 1);
        }
        for (String s : map.keySet()) {
            RecyclerElement recyclerElement = new RecyclerElement(s, map.get(s));
            listElements.add(recyclerElement);
        }
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
        Toast.makeText(this, "clickedItemIndex " + clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
