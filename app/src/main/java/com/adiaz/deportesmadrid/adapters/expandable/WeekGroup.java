package com.adiaz.deportesmadrid.adapters.expandable;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class WeekGroup extends ExpandableGroup<MatchChild> {

    private Integer idWeek;

    public WeekGroup(String title, List<MatchChild> items, Integer idWeek) {
        super(title, items);
        this.idWeek = idWeek;
    }

    public Integer getIdWeek() {
        return idWeek;
    }

    public void setIdWeek(Integer idWeek) {
        this.idWeek = idWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeekGroup)) return false;
        WeekGroup genre = (WeekGroup) o;
        return getTitle() == genre.getTitle();
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }

}
