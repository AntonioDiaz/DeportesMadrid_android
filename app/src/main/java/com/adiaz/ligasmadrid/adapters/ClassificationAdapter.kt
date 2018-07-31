package com.adiaz.ligasmadrid.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.extensions.layoutInflater
import com.adiaz.ligasmadrid.retrofit.groupsdetails.ClassificationRetrofit

import kotlinx.android.synthetic.main.listitem_classification.view.*

class ClassificationAdapter(internal var mContext: Context, internal var mClassificationList: List<ClassificationRetrofit>, internal var mIdTeam: String) : RecyclerView.Adapter<ClassificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mContext.layoutInflater.inflate(R.layout.listitem_classification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.tvPosition.text = ""
            holder.tvTeam.text = mContext.getString(R.string.classification_header_team)
            holder.tvMatches.text = mContext.getString(R.string.classification_header_matches)
            holder.tvMatchesWon.text = mContext.getString(R.string.classification_header_matches_won)
            holder.tvMatchesDrawn.text = mContext.getString(R.string.classification_header_matches_drawn)
            holder.tvMatchesLost.text = mContext.getString(R.string.classification_header_matches_lost)
            holder.tvGoalsFor.text = mContext.getString(R.string.classification_header_goals_favor)
            holder.tvGoalsAgainst.text = mContext.getString(R.string.classification_header_goals_against)
            holder.tvPoints.text = mContext.getString(R.string.classification_header_points)
            val color = ContextCompat.getColor(mContext, R.color.colorPrimaryDark)
            val colorWhite = ContextCompat.getColor(mContext, R.color.colorWhite)
            holder.clClassification.setBackgroundColor(color)
            holder.tvTeam.setTextColor(colorWhite)
            holder.tvMatches.setTextColor(colorWhite)
            holder.tvMatchesWon.setTextColor(colorWhite)
            holder.tvMatchesDrawn.setTextColor(colorWhite)
            holder.tvMatchesLost.setTextColor(colorWhite)
            holder.tvGoalsFor.setTextColor(colorWhite)
            holder.tvGoalsAgainst.setTextColor(colorWhite)
            holder.tvPoints.setTextColor(colorWhite)
        } else {
            val entity = mClassificationList[position - 1]
            if (entity != null) {
                holder.tvPosition.text = entity.position.toString()
                if (entity.team != null && entity.team.name != null) {
                    holder.tvTeam.text = entity.team.name.toString()
                }
                holder.tvMatches.text = entity.matchesPlayed.toString()
                holder.tvMatchesWon.text = entity.matchesWon.toString()
                holder.tvMatchesDrawn.text = entity.matchesDrawn.toString()
                holder.tvMatchesLost.text = entity.matchesLost.toString()
                holder.tvGoalsFor.text = entity.pointsFavor.toString()
                holder.tvGoalsAgainst.text = entity.pointsAgainst.toString()
                holder.tvPoints.text = entity.points.toString()
                if (entity.team != null && entity.team.name != null && entity.team.name == mIdTeam) {
                    val color = ContextCompat.getColor(mContext, R.color.colorAccent)
                    holder.clClassification.setBackgroundColor(color)
                    holder.tvTeam.setTypeface(null, Typeface.BOLD)
                    val colorWhite = Color.WHITE
                    holder.tvPosition.setTextColor(colorWhite)
                    holder.tvTeam.setTextColor(colorWhite)
                    holder.tvMatches.setTextColor(colorWhite)
                    holder.tvMatchesWon.setTextColor(colorWhite)
                    holder.tvMatchesDrawn.setTextColor(colorWhite)
                    holder.tvMatchesLost.setTextColor(colorWhite)
                    holder.tvGoalsFor.setTextColor(colorWhite)
                    holder.tvGoalsAgainst.setTextColor(colorWhite)
                    holder.tvPoints.setTextColor(colorWhite)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return this.mClassificationList.size + 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clClassification: ConstraintLayout = itemView.clClassification
        var tvPosition: TextView = itemView.tvClassificationPosition
        var tvTeam: TextView = itemView.tvClassificationTeam
        var tvMatches: TextView = itemView.tvClassificationMatches
        var tvMatchesWon: TextView = itemView.tvClassificationMatchesWon
        var tvMatchesDrawn: TextView = itemView.tvClassificationMatchesDrawn
        var tvMatchesLost: TextView = itemView.tvClassificationMatchesDrawn
        var tvGoalsFor: TextView = itemView.tvClassificationGoalsFor
        var tvGoalsAgainst: TextView = itemView.tvClassificationGoalsAgainst
        var tvPoints: TextView = itemView.tvClassificationPoints
    }
}
