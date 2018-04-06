package com.adiaz.deportesmadrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.CompetitionAdapter;
import com.adiaz.deportesmadrid.db.daos.CompetitionsDAO;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.ListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity implements CompetitionAdapter.ListItemClickListener {

    @BindView(R.id.rv_favorites)
    RecyclerView rvFavorites;

    List<ListItem> elementsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Favorite> favoriteList = FavoritesDAO.queryFavorites(this);

        elementsList = new ArrayList<>();
        for (Favorite favorite : favoriteList) {
            Competition competition = CompetitionsDAO.queryCompetitionsById(this, favorite.idCompetition());
            if (competition!=null) {
                elementsList.add(new ListItem(competition.nomGrupo(), competition.id()));
            }
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        CompetitionAdapter competitionAdapter = new CompetitionAdapter(this, this, elementsList);
        rvFavorites.setHasFixedSize(true);
        rvFavorites.setLayoutManager(layoutManager);
        rvFavorites.setAdapter(competitionAdapter);
        competitionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, CompetitionDetailsActivity.class);
        intent.putExtra(Constants.ID_COMPETITION, elementsList.get(clickedItemIndex).getCount());
        intent.putExtra(Constants.NAME_COMPETITION, elementsList.get(clickedItemIndex).getName());
        startActivity(intent);
    }
}
