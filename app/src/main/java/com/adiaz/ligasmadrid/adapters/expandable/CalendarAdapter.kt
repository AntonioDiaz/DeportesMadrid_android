package com.adiaz.ligasmadrid.adapters.expandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adiaz.ligasmadrid.R
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import org.jetbrains.anko.AnkoLogger

class CalendarAdapter(var listItemClickListener:ListItemClickListener, groups: List<ExpandableGroup<*>>) :
        ExpandableRecyclerViewAdapter<ViewHolderWeek, ViewHolderMatch>(groups), AnkoLogger {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWeek {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.listitem_group_week, parent, false)
        return ViewHolderWeek(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMatch {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.listitem_child_match, parent, false)
        return ViewHolderMatch(view, parent.context)
    }

    override fun onBindChildViewHolder(holder: ViewHolderMatch, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val matchChild = (group as WeekGroup).items[childIndex]
        holder.updateMatchFields(matchChild)
       holder.mView.setOnClickListener() {
            listItemClickListener.openMenu(it)
        }
    }

    override fun onBindGroupViewHolder(holder: ViewHolderWeek, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.setTitle(group)
    }

    interface ListItemClickListener {
        fun openMenu(view: View)
    }
}
