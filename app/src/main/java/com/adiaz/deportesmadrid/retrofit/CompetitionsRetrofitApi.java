package com.adiaz.deportesmadrid.retrofit;

import com.adiaz.deportesmadrid.retrofit.classification.ClassificationRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by adiaz on 22/3/18.
 */
public interface CompetitionsRetrofitApi {

    @GET("/server/competiciones")
    Call<List<CompetitionRetrofitEntity>> queryAllCompetition();

    @GET("/server/findClassification/")
    Call<List<ClassificationRetrofitEntity>> queryClassification(@Query("cod_competicion")String idCompetition);

    @GET("/server/findMatches/")
    Call<List<MatchRetrofitEntity>> queryMatches(@Query("cod_competicion")String idCompetition);


}
