package com.adiaz.deportesmadrid.callbacks;

import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.ClassificationRetrofit;
import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;

import java.util.List;

public interface CompetitionCallback {
    Competition queryCompetition();
    String queryTeam();
    List<MatchRetrofit> queryMatchesList();
    List<ClassificationRetrofit> queryClassificationList();
}
