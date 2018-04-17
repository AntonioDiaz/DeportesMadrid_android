package com.adiaz.deportesmadrid.callbacks;

import com.adiaz.deportesmadrid.retrofit.competitiondetails.MatchRetrofit;

import java.util.List;

public interface CalendarCallback {
    List<MatchRetrofit> queryMatchesList();
}
