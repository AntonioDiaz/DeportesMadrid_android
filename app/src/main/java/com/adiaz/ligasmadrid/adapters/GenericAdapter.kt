package com.adiaz.ligasmadrid.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.extensions.layoutInflater
import com.adiaz.ligasmadrid.utils.ListItem
import com.adiaz.ligasmadrid.utils.UtilsPreferences

import kotlinx.android.synthetic.main.listitem_generic.view.*

class GenericAdapter(var mContext: Context, val mOnClickListener: ListItemClickListener, var sportsList: List<ListItem>?) :
        RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mContext.layoutInflater.inflate(R.layout.listitem_generic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var str = sportsList!![position].name
        if (UtilsPreferences.isShowCompetitionsNumber(mContext)) {
            str += " (" + sportsList!![position].count + ")"
        }
        holder.tvCompetitionTitle.text = str
    }

    override fun getItemCount(): Int {
        return sportsList!!.size
    }

    interface ListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var tvCompetitionTitle: TextView = itemView.listitemTitle
        var cvListItem: CardView = itemView.cardView

        init {
            cvListItem.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            mOnClickListener.onListItemClick(position)
        }
    }
}
