package com.adiaz.deportesmadrid.callbacks;

import com.adiaz.deportesmadrid.adapters.expandable.WeekGroup;
import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.ClassificationRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit;
import com.adiaz.deportesmadrid.retrofit.groupsdetails.Team;

import java.util.List;

public interface CompetitionCallback {
    Group queryCompetition();
    Team queryTeam();
    List<MatchRetrofit> queryMatchesList();
    List<ClassificationRetrofit> queryClassificationList();
    List<WeekGroup> queryWeeks();
}
