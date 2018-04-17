package com.adiaz.deportesmadrid.retrofit;

import com.adiaz.deportesmadrid.retrofit.competitiondetails.ClassificationRetrofit;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.CompetitionDetailsRetrofit;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;
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

    @GET("/server/findClassification/")
    Call<List<ClassificationRetrofit>> queryClassification(@Query("cod_competicion")String idCompetition);

    @GET("/server/findMatches/")
    Call<List<MatchRetrofit>> queryMatches(@Query("cod_competicion")String idCompetition);

    @GET("/server/findCompetition/")
    Call<CompetitionDetailsRetrofit> findCompetition(@Query("cod_competicion")String idCompetition);



}
