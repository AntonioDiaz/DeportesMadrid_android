package com.adiaz.deportesmadrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.FavoritesAdapter;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity implements FavoritesAdapter.ListItemClickListener {

    @BindView(R.id.rv_favorites)
    RecyclerView rvFavorites;

    @BindView(R.id.tv_empty_list)
    View tvEmpty;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;

    private List<Favorite> mFavoriteList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
            getSupportActionBar().setSubtitle(getString(R.string.favorites_subtitle));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFavoriteList = FavoritesDAO.queryFavorites(this);
        rvFavorites.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
        if (mFavoriteList.size()==0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvFavorites.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this, this, mFavoriteList);
            rvFavorites.setHasFixedSize(true);
            rvFavorites.setLayoutManager(layoutManager);
            rvFavorites.setAdapter(favoritesAdapter);
            favoritesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Favorite favorite = mFavoriteList.get(clickedItemIndex);
        String idCompetition = favorite.idGroup();
        if (favorite.idTeam()!=null) {
            Intent intent = new Intent(this, TeamDetailsActivity.class);
            intent.putExtra(Constants.ID_COMPETITION, idCompetition);
            intent.putExtra(Constants.TEAM_ID, favorite.idTeam());
            intent.putExtra(Constants.TEAM_NAME, favorite.nameTeam());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, GroupDetailsActivity.class);
            Group group = GroupsDAO.queryCompetitionsById(this, idCompetition);
            intent.putExtra(Constants.ID_COMPETITION, idCompetition);
            intent.putExtra(Constants.NAME_COMPETITION, group.nomGrupo());
            startActivity(intent);
        }
    }
}
