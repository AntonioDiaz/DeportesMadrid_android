
package com.adiaz.deportesmadrid.retrofit.competitiondetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassificationRetrofit {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idGroup")
    @Expose
    private String idGroup;
    @SerializedName("idTeam")
    @Expose
    private Integer idTeam;
    @SerializedName("competition")
    @Expose
    private Competition_ competition;
    @SerializedName("team")
    @Expose
    private Team team;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("matchesPlayed")
    @Expose
    private Integer matchesPlayed;
    @SerializedName("matchesWon")
    @Expose
    private Integer matchesWon;
    @SerializedName("matchesDrawn")
    @Expose
    private Integer matchesDrawn;
    @SerializedName("matchesLost")
    @Expose
    private Integer matchesLost;
    @SerializedName("pointsFavor")
    @Expose
    private Integer pointsFavor;
    @SerializedName("pointsAgainst")
    @Expose
    private Integer pointsAgainst;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public Competition_ getCompetition() {
        return competition;
    }

    public void setCompetition(Competition_ competition) {
        this.competition = competition;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public Integer getMatchesDrawn() {
        return matchesDrawn;
    }

    public void setMatchesDrawn(Integer matchesDrawn) {
        this.matchesDrawn = matchesDrawn;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public Integer getPointsFavor() {
        return pointsFavor;
    }

    public void setPointsFavor(Integer pointsFavor) {
        this.pointsFavor = pointsFavor;
    }

    public Integer getPointsAgainst() {
        return pointsAgainst;
    }

    public void setPointsAgainst(Integer pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }

}
