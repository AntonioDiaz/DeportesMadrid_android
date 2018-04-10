package com.adiaz.deportesmadrid.callbacks;

import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;

import java.util.List;

public interface CalendarCallback {
    List<MatchRetrofitEntity> queryMatchesList();
}
