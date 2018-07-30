package com.adiaz.deportesmadrid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.extensions.layoutInflater
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit
import com.adiaz.deportesmadrid.utils.Constants
import com.adiaz.deportesmadrid.utils.StateAnnotation
import com.adiaz.deportesmadrid.utils.Utils
import kotlinx.android.synthetic.main.listitem_team_match.view.*
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat

class TeamMatchesAdapter(
        private var mContext: Context,
        private var mMatchList: List<MatchRetrofit>,
        private var mTeamId: String,
        private val mListItemClickListener: ListItemClickListener) : RecyclerView.Adapter<TeamMatchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMatchesAdapter.ViewHolder {
        val view = mContext.layoutInflater.inflate(R.layout.listitem_team_match, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = mMatchList[position]
        holder.tvWeek.text = mContext.getString(R.string.calendar_week, match.numWeek)
        var teamLocal = Constants.FIELD_EMPTY
        if (match.teamLocal != null && StringUtils.isNotEmpty(match.teamLocal.name)) {
            teamLocal = match.teamLocal.name
        }
        var teamVisitor = Constants.FIELD_EMPTY
        if (match.teamVisitor != null && StringUtils.isNotEmpty(match.teamVisitor.name)) {
            teamVisitor = match.teamVisitor.name
        }
        /* check if a team is resting */
        if (teamLocal == Constants.FIELD_EMPTY && teamVisitor != Constants.FIELD_EMPTY) {
            teamLocal = mContext.getString(R.string.RESTING)
        }
        if (teamVisitor == Constants.FIELD_EMPTY && teamLocal != Constants.FIELD_EMPTY) {
            teamVisitor = mContext.getString(R.string.RESTING)
        }
        var isLocal = false
        if (match.teamLocal != null && match.teamLocal.name.equals(mTeamId, ignoreCase = true)) {
            isLocal = true
        }
        if (isLocal) {
            holder.tvLocalOrVisitor.text = mContext.getString(R.string.calendar_local)
            holder.tvOpponent.text = teamVisitor
        } else {
            holder.tvLocalOrVisitor.text = mContext.getString(R.string.calendar_visitor)
            holder.tvOpponent.text = teamLocal
        }
        var dateStr = Constants.FIELD_EMPTY
        if (match.date != null && match.state != StateAnnotation.DESCANSA) {
            val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT)
            dateStr = dateFormat.format(match.date)
        }
        holder.tvDate.text = dateStr
        var placeName = Constants.FIELD_EMPTY
        if (match.place != null) {
            placeName = match.place.name
        }
        holder.tvPlace.text = placeName
        holder.tvState.text = Utils.getStringResourceByName(mContext, StateAnnotation.stringKey(match.state!!))
        if (match.state == StateAnnotation.FINALIZADO || match.state == StateAnnotation.NO_PRESENTADO) {
            holder.tvScore.text = mContext.getString(R.string.calendar_score, match.scoreLocal, match.scoreVisitor)
            holder.tvScore.visibility = View.VISIBLE
            val scoreTeam: Int
            val scoreTeamOpponent: Int
            if (isLocal) {
                scoreTeam = match.scoreLocal!!
                scoreTeamOpponent = match.scoreVisitor!!
            } else {
                scoreTeam = match.scoreVisitor!!
                scoreTeamOpponent = match.scoreLocal!!
            }
            holder.ivThumbVictory.visibility = View.VISIBLE
            if (scoreTeam > scoreTeamOpponent) {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumb_up)
            } else if (scoreTeam < scoreTeamOpponent) {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumb_down)
            } else {
                holder.ivThumbVictory.setImageResource(R.drawable.ic_thumbs_up_down)
            }
        } else {
            holder.tvScore.visibility = View.INVISIBLE
            holder.ivThumbVictory.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mMatchList.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var vTeamCalendar: View = itemView.cardView
        var tvWeek: TextView = itemView.tvWeek
        var tvLocalOrVisitor: TextView = itemView.tvLocalVisitor
        var tvOpponent: TextView = itemView.tvOpponent
        var tvDate: TextView = itemView.tvDate
        var tvPlace: TextView = itemView.tvPlace
        var tvState: TextView = itemView.tvState
        var tvScore: TextView = itemView.tvScore
        var ivThumbVictory: ImageView = itemView.ivThumbVictory

        init {
            vTeamCalendar.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListItemClickListener.onListItemClick(adapterPosition)
        }
    }

    interface ListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }

    companion object {

        val TAG = TeamMatchesAdapter::class.java.simpleName
    }
}
