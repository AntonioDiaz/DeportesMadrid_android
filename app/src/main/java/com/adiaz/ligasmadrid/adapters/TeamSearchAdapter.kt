package com.adiaz.ligasmadrid.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.extensions.getSportNameLocalized
import com.adiaz.ligasmadrid.utils.Constants
import com.adiaz.ligasmadrid.utils.entities.TeamEntity

import kotlinx.android.synthetic.main.listitem_favorites_team.view.*

class TeamSearchAdapter(internal var mContext: Context, mListItemClickListener: ListItemClickListener, internal var mTeams: List<TeamEntity>?) : RecyclerView.Adapter<TeamSearchAdapter.ViewHolder>() {

    private var mListItemClickListener: TeamSearchAdapter.ListItemClickListener = mListItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.listitem_favorites_team, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mTeams!!.size > position) {
            val group = GroupsDAO.queryCompetitionsById(mContext, mTeams!![position].idGroup)
            holder.tvGroup.text = group!!.nomGrupo
            holder.tvFase.text = group.nomFase
            holder.tvCompetition.text = group.nomCompeticion
            holder.tvTeam.text = mTeams!![position].teamName
            holder.tvPath.text = "${group.getSportNameLocalized(mContext)} > ${group.distrito} > ${group.categoria}"
        }
    }

    override fun getItemCount(): Int {
        return mTeams!!.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val cvFavorites: CardView = itemView.cardView
        val tvTeam: TextView = itemView.tvTeam
        val tvGroup: TextView = itemView.tvGroup
        val tvFase: TextView = itemView.tvFase
        val tvCompetition: TextView = itemView.tvCompetition
        val tvPath: TextView = itemView.tvPath

        init {
            cvFavorites.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListItemClickListener.onListItemClickTeamSearch(adapterPosition)
        }
    }

    interface ListItemClickListener {
        fun onListItemClickTeamSearch(clickedItemIndex: Int)
    }
}
