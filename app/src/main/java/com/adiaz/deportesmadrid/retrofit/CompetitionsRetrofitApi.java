package com.adiaz.deportesmadrid.retrofit;

import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by adiaz on 22/3/18.
 */
public interface CompetitionsRetrofitApi {
    @GET("/server/competiciones")
    Call<List<CompetitionRetrofitEntity>> queryAllCompetition();
}
