package com.adiaz.deportesmadrid.utils.entities;


public class TeamSearch {
    Integer idTeam;
    String teamName;
    String idGroup;

    public TeamSearch(Integer idTeam, String teamName, String idGroup) {
        this.idTeam = idTeam;
        this.teamName = teamName;
        this.idGroup = idGroup;
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }
}
