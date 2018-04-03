package com.adiaz.deportesmadrid.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adiaz.deportesmadrid.R;
import com.adiaz.deportesmadrid.adapters.CompetitionAdapter;
import com.adiaz.deportesmadrid.db.DbContract.CompetitionEntry;
import com.adiaz.deportesmadrid.retrofit.CompetitionsRetrofitApi;
import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;
import com.adiaz.deportesmadrid.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<List<CompetitionRetrofitEntity>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.progressBar)
    ProgressBar pb;

    @BindView(R.id.view_result)
    View vResults;

    @BindView(R.id.tv_competitions)
    TextView tvCompetitionsText;

    @BindView(R.id.rv_sports)
    RecyclerView rvCompetitions;

    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        pb.setVisibility(View.INVISIBLE);
        vResults.setVisibility(View.INVISIBLE);
        mCursor = getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, null, null, null);
        if (mCursor.getCount()==0) {
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
        Log.d(TAG, "onResponse: response.body() -->" + response.body().size());
        List<ContentValues> contentValues = new ArrayList<>();
        for (CompetitionRetrofitEntity competitionRetrofitEntity : response.body()) {
            ContentValues cv = CompetitionEntry.retrofitEntityToContentValue(competitionRetrofitEntity);
            contentValues.add(cv);
        }
        ContentValues[] array = contentValues.toArray(new ContentValues[contentValues.size()]);

        //before insert it is necessary to delete all.
        getContentResolver().delete(CompetitionEntry.CONTENT_URI, null, null);
        getContentResolver().bulkInsert(CompetitionEntry.CONTENT_URI, array);
        //select all
        mCursor = getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, null, null, null);
        fillRecyclerview();
    }

    @Override
    public void onFailure(Call<List<CompetitionRetrofitEntity>> call, Throwable t) {
        Log.d(TAG, "onFailure: peto" + t.getMessage());
    }

    private void fillRecyclerview() {
        Set<String> sportsSet = new HashSet<>();
        while (mCursor.moveToNext()) {
            sportsSet.add(mCursor.getString(CompetitionEntry.INDEX_DEPORTE));
        }
        List<String> sportsList = new ArrayList<>();
        sportsList.addAll(sportsSet);
        tvCompetitionsText.setText("competitions found: " + mCursor.getCount());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        CompetitionAdapter competitionAdapter = new CompetitionAdapter(this, sportsList);
        rvCompetitions.setHasFixedSize(true);
        rvCompetitions.setLayoutManager(layoutManager);
        rvCompetitions.setAdapter(competitionAdapter);
        competitionAdapter.notifyDataSetChanged();
        pb.setVisibility(View.INVISIBLE);
        vResults.setVisibility(View.VISIBLE);
    }
}
