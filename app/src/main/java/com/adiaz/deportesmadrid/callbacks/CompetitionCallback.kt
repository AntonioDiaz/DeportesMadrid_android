package com.adiaz.deportesmadrid.callbacks

import com.adiaz.deportesmadrid.adapters.expandable.WeekGroup
import com.adiaz.deportesmadrid.db.entities.Group
import com.adiaz.deportesmadrid.retrofit.groupsdetails.ClassificationRetrofit
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit
import com.adiaz.deportesmadrid.retrofit.groupsdetails.Team

interface CompetitionCallback {
    fun queryCompetition(): Group?
    fun queryTeam(): Team?
    fun queryMatchesList(): List<MatchRetrofit>?
    fun queryClassificationList(): List<ClassificationRetrofit>?
    fun queryWeeks(): List<WeekGroup>?
}
