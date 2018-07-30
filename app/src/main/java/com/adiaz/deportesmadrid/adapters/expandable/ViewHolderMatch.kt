package com.adiaz.deportesmadrid.adapters.expandable

import android.content.Context
import android.view.View
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.listitem_child_match.view.*


class ViewHolderMatch(internal var mView: View, internal var mContext: Context) : ChildViewHolder(mView) {

    fun updateMatchFields(matchChild: MatchChildKotlin) {
        mView.tvCalendarTeamLocal.text = matchChild.teamLocal
        mView.tvCalendarTeamVisitor.text = matchChild.teamVisitor
        mView.tvScoreLocal.text = matchChild.scoreLocal
        mView.tvScoreVisitor.text = matchChild.scoreVisitor
        mView.tvCalendarDate.text = matchChild.dateStr
        mView.tvCalendarPlace.text = matchChild.placeName
        mView.tvState.text = matchChild.state
        mView.tag = matchChild
    }
}
