package com.adiaz.deportesmadrid.adapters.expandable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adiaz.deportesmadrid.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class CalendarAdapter extends ExpandableRecyclerViewAdapter <ViewHolderWeek, ViewHolderMatch> {

    public CalendarAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ViewHolderWeek onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_group_week, parent, false);
        return new ViewHolderWeek(view);
    }

    @Override
    public ViewHolderMatch onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_child_match, parent, false);
        return new ViewHolderMatch(view);
    }

    @Override
    public void onBindChildViewHolder(ViewHolderMatch holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final MatchChild matchChild = ((WeekGroup) group).getItems().get(childIndex);
        holder.updateMatchFields(matchChild);
    }

    @Override
    public void onBindGroupViewHolder(ViewHolderWeek holder, int flatPosition, ExpandableGroup group) {
        holder.setTitle(group);
    }

}
