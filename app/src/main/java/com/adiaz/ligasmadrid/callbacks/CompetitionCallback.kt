package com.adiaz.ligasmadrid.callbacks

import com.adiaz.ligasmadrid.adapters.expandable.WeekGroup
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.retrofit.groupsdetails.ClassificationRetrofit
import com.adiaz.ligasmadrid.retrofit.groupsdetails.MatchRetrofit
import com.adiaz.ligasmadrid.retrofit.groupsdetails.Team

interface CompetitionCallback {
    fun queryCompetition(): Group?
    fun queryTeam(): Team?
    fun queryMatchesList(): List<MatchRetrofit>?
    fun queryClassificationList(): List<ClassificationRetrofit>?
    fun queryWeeks(): List<WeekGroup>?
}
