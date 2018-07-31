package com.adiaz.ligasmadrid.adapters.expandable

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class WeekGroup(title: String, items: MutableList<MatchChildKotlin>, var idWeek: Int?) : ExpandableGroup<MatchChildKotlin>(title, items) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is WeekGroup) return false
        val genre = o as WeekGroup?
        return title === genre!!.title
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

}
