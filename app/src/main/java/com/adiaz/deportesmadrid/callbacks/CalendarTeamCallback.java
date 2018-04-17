package com.adiaz.deportesmadrid.callbacks;

import com.adiaz.deportesmadrid.db.entities.Match;

import java.util.List;

public interface CalendarTeamCallback {
    List<Match> queryTeamMatches();
}
