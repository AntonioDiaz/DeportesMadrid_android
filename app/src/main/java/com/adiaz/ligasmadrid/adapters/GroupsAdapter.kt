package com.adiaz.ligasmadrid.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.extensions.layoutInflater
import com.adiaz.ligasmadrid.utils.UtilsPreferences

import kotlinx.android.synthetic.main.listitem_group.view.*

class GroupsAdapter(var mContext: Context, var mGroupList: List<Group>?, mOnClickListener: ListItemClickListener) :
        RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {

    private val mOnClickListener: GroupsAdapter.ListItemClickListener = mOnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mContext.layoutInflater.inflate(R.layout.listitem_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGroupName.text = mGroupList!![position].nomGrupo
        holder.tvCompetition.text = mGroupList!![position].nomCompeticion
        holder.tvFase.text = mGroupList!![position].nomFase
        if (UtilsPreferences.isShowCompetitionsNumber(mContext)) {
            holder.tvGroupName.append(" (" + mGroupList!![position].id + ")")
        }
    }

    override fun getItemCount(): Int {
        return mGroupList!!.size
    }

    interface ListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tvGroupName: TextView = itemView.tvGroupName
        val cvListItem: CardView = itemView.cardView
        val tvCompetition: TextView = itemView.tvCompetition
        val tvFase: TextView = itemView.tvFase

        init {
            cvListItem.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            val position = adapterPosition
            mOnClickListener.onListItemClick(position)
        }
    }

}
