package com.adiaz.deportesmadrid.activities;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.DeportesMadridFragmentStatePagerAdapter;
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback;
import com.adiaz.deportesmadrid.db.daos.GroupsDAO;
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.fragments.TabClassification;
import com.adiaz.deportesmadrid.fragments.TabGroupCalendar;
import com.adiaz.deportesmadrid.fragments.TabGroupTeams;
import com.adiaz.deportesmadrid.retrofit.RetrofitApi;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.ClassificationRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.GroupDetailsRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.Team;
import com.adiaz.deportesmadrid.utils.Constants;
import com.adiaz.deportesmadrid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupDetailsActivity extends AppCompatActivity implements CompetitionCallback {

    //private static final String TAG = GroupDetailsActivity.class.getSimpleName();

    @BindView(R.id.view_competition)
    View mainView;

    @BindView(R.id.tb_competition_details)
    Toolbar toolbar;

    @BindView(R.id.tl_competition_details)
    TabLayout tabLayout;

    @BindView(R.id.vp_competition_details)
    ViewPager viewPager;

    List<ClassificationRetrofit> classificationList;
    List<MatchRetrofit> matchesList;
    DeportesMadridFragmentStatePagerAdapter adapter;
    Group mGroup;
    private ProgressDialog mProgressDialog;

    public static String mIdGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        ButterKnife.bind(this);

        mIdGroup = getIntent().getStringExtra(Constants.ID_COMPETITION);
        String nameCompetition = getIntent().getStringExtra(Constants.NAME_COMPETITION);

        classificationList = new ArrayList<>();
        matchesList = new ArrayList<>();

        mGroup = GroupsDAO.queryCompetitionsById(this, mIdGroup);
        String subtitle = mGroup.deporte() + " > " + mGroup.distrito() + " > " + mGroup.categoria();

        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(nameCompetition);
            getSupportActionBar().setSubtitle(subtitle);
       }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new DeportesMadridFragmentStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabGroupCalendar(), "Calendario");
        adapter.addFragment(new TabClassification(), "Clasificación");
        adapter.addFragment(new TabGroupTeams(), "Equipos");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        mProgressDialog = new ProgressDialog(GroupDetailsActivity.this);
        mProgressDialog.setTitle(getString(R.string.loading_info));
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        showLoading();


        /*        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                // add your other interceptors …

                // add logging as last interceptor
                httpClient.addInterceptor(logging);*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL).addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<GroupDetailsRetrofit> callCompetitionDetails = retrofitApi.findGroup(mIdGroup);
        callCompetitionDetails.enqueue(new CallbackRequest());

        //hideLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.action_favorites) {
                Favorite favorite = FavoritesDAO.queryFavorite(this, mIdGroup);
                if (favorite!=null) {
                    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
                    menu.getItem(i).setIcon(drawable);
                }
                Drawable icon = menu.getItem(i).getIcon();
                int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
                final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
                icon.setColorFilter(colorFilter);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                Drawable drawable;
                Favorite favorite = FavoritesDAO.queryFavorite(this, mIdGroup);
                if (favorite != null) {
                    FavoritesDAO.deleteFavorite(this, favorite.id());
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_empty);
                    Toast.makeText(this, R.string.favorites_competition_removed, Toast.LENGTH_SHORT).show();
                } else {
                    Favorite newFavorite = Favorite.builder().idGroup(mIdGroup).build();
                    FavoritesDAO.insertFavorite(this, newFavorite);
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill);
                    Toast.makeText(this, R.string.favorites_competition_added, Toast.LENGTH_SHORT).show();
                }
                int colorWhite = ContextCompat.getColor(this, R.color.colorWhite);
                final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN);
                drawable.setColorFilter(colorFilter);
                item.setIcon(drawable);
                break;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading() {
        mProgressDialog.show();
        viewPager.setVisibility(View.INVISIBLE);
    }

    private void hideLoading() {
        mProgressDialog.dismiss();
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void dataReceived (GroupDetailsRetrofit groupDetailsRetrofit) {
        this.matchesList = groupDetailsRetrofit.getMatchRetrofits();
        this.classificationList = groupDetailsRetrofit.getClassificationRetrofit();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        hideLoading();
    }

    @Override
    public List<ClassificationRetrofit> queryClassificationList() {
        return this.classificationList;
    }

    @Override
    public Group queryCompetition() {
        return mGroup;
    }

    @Override
    public Team queryTeam() {
        return null;
    }

    @Override
    public List<MatchRetrofit> queryMatchesList() {
        return this.matchesList;
    }


    class CallbackRequest implements Callback<GroupDetailsRetrofit> {

        @Override
        public void onResponse(Call<GroupDetailsRetrofit> call, Response<GroupDetailsRetrofit> response) {
            dataReceived(response.body());
        }

        @Override
        public void onFailure(Call<GroupDetailsRetrofit> call, Throwable t) {
            hideLoading();
            Utils.showSnack(mainView, getString(R.string.error_getting_data));
        }
    }


}
