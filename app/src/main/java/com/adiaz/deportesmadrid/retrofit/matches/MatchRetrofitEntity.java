
package com.adiaz.deportesmadrid.retrofit.matches;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchRetrofitEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idCompetition")
    @Expose
    private String idCompetition;
    @SerializedName("idTeamLocal")
    @Expose
    private Integer idTeamLocal;
    @SerializedName("idTeamVisitor")
    @Expose
    private Integer idTeamVisitor;
    @SerializedName("idPlace")
    @Expose
    private Integer idPlace;
    @SerializedName("competition")
    @Expose
    private Competition competition;
    @SerializedName("teamLocal")
    @Expose
    private TeamLocal teamLocal;
    @SerializedName("teamVisitor")
    @Expose
    private TeamVisitor teamVisitor;
    @SerializedName("place")
    @Expose
    private Place place;
    @SerializedName("numWeek")
    @Expose
    private Integer numWeek;
    @SerializedName("numMatch")
    @Expose
    private Integer numMatch;
    @SerializedName("scoreLocal")
    @Expose
    private Integer scoreLocal;
    @SerializedName("scoreVisitor")
    @Expose
    private Integer scoreVisitor;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("scheduled")
    @Expose
    private Boolean scheduled;
    @SerializedName("date")
    @Expose
    private Long date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCompetition() {
        return idCompetition;
    }

    public void setIdCompetition(String idCompetition) {
        this.idCompetition = idCompetition;
    }

    public Integer getIdTeamLocal() {
        return idTeamLocal;
    }

    public void setIdTeamLocal(Integer idTeamLocal) {
        this.idTeamLocal = idTeamLocal;
    }

    public Integer getIdTeamVisitor() {
        return idTeamVisitor;
    }

    public void setIdTeamVisitor(Integer idTeamVisitor) {
        this.idTeamVisitor = idTeamVisitor;
    }

    public Integer getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(Integer idPlace) {
        this.idPlace = idPlace;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public TeamLocal getTeamLocal() {
        return teamLocal;
    }

    public void setTeamLocal(TeamLocal teamLocal) {
        this.teamLocal = teamLocal;
    }

    public TeamVisitor getTeamVisitor() {
        return teamVisitor;
    }

    public void setTeamVisitor(TeamVisitor teamVisitor) {
        this.teamVisitor = teamVisitor;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Integer getNumWeek() {
        return numWeek;
    }

    public void setNumWeek(Integer numWeek) {
        this.numWeek = numWeek;
    }

    public Integer getNumMatch() {
        return numMatch;
    }

    public void setNumMatch(Integer numMatch) {
        this.numMatch = numMatch;
    }

    public Integer getScoreLocal() {
        return scoreLocal;
    }

    public void setScoreLocal(Integer scoreLocal) {
        this.scoreLocal = scoreLocal;
    }

    public Integer getScoreVisitor() {
        return scoreVisitor;
    }

    public void setScoreVisitor(Integer scoreVisitor) {
        this.scoreVisitor = scoreVisitor;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Boolean getScheduled() {
        return scheduled;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

}
