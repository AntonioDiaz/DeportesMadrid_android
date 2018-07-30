package com.adiaz.deportesmadrid.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.db.daos.GroupsDAO
import com.adiaz.deportesmadrid.db.entities.Favorite
import com.adiaz.deportesmadrid.extensions.layoutInflater
import com.adiaz.deportesmadrid.utils.Constants

import kotlinx.android.synthetic.main.listitem_favorites_team.view.*

class FavoritesAdapter(internal var mContext: Context, listItemClickListener: ListItemClickListener, internal var mFavoritesList: List<Favorite>?) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var mListItemClickListener: FavoritesAdapter.ListItemClickListener = listItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutId = R.layout.listitem_favorites_team
        if (viewType == TYPE_GROUP) {
            layoutId = R.layout.listitem_favorites_group
        }
        val view = mContext.layoutInflater.inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mFavoritesList != null && mFavoritesList!!.size >= position) {
            val group = GroupsDAO.queryCompetitionsById(mContext, mFavoritesList!![position].idGroup)
            if (group != null) {
                holder.tvGroup.text = group.nomGrupo
                holder.tvFase.text = group.nomFase
                holder.tvCompetition.text = group.nomCompeticion
                holder.tvPath.text = group.deporte + Constants.PATH_SEPARATOR + group.distrito + Constants.PATH_SEPARATOR + group.categoria
                val teamName = mFavoritesList!![position].nameTeam
                if (teamName != null) {
                    holder.tvTeam!!.text = teamName
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mFavoritesList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mFavoritesList!![position].idTeam == null) {
            TYPE_GROUP
        } else {
            TYPE_TEAM
        }
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var cvFavorites: CardView = itemView.cardView
        internal var tvTeam: TextView? = itemView.tvTeam
        internal var tvGroup: TextView = itemView.tvGroup
        internal var tvFase: TextView = itemView.tvFase
        internal var tvCompetition: TextView = itemView.tvCompetition
        internal var tvPath: TextView = itemView.tvPath

        init {
            cvFavorites.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mListItemClickListener.onListItemClick(adapterPosition)
        }
    }

    interface ListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }

    companion object {
        private const val TYPE_TEAM = 1
        private const val TYPE_GROUP = 2
    }
}
