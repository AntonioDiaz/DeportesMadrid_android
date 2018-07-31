package com.adiaz.ligasmadrid.retrofit;

import com.adiaz.ligasmadrid.retrofit.groupslist.GroupRetrofitEntity;
import com.adiaz.ligasmadrid.retrofit.groupsdetails.GroupDetailsRetrofit;
import com.adiaz.ligasmadrid.retrofit.groupsdetails.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by adiaz on 22/3/18.
 */
public interface RetrofitApi {

    @GET("/server/groups")
    Call<List<GroupRetrofitEntity>> queryAllGroups();

    @GET("/server/findGroup/")
    Call<GroupDetailsRetrofit> findGroup(@Query("cod_group")String idGroup);

    @GET("/server/findTeam")
    Call<List<Team>> queryTeams(@Query("team_name") String teamName);



}
