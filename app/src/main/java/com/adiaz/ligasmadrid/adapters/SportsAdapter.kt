package com.adiaz.ligasmadrid.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.extensions.layoutInflater
import com.adiaz.ligasmadrid.utils.ListItem
import com.adiaz.ligasmadrid.utils.Utils
import com.adiaz.ligasmadrid.utils.UtilsPreferences
import kotlinx.android.synthetic.main.listitem_sports.view.*

/**
 * Created by adiaz on 10/1/18.
 */

class SportsAdapter(
        private var mContext: Context,
        private var mListItemClickListener: ListItemClickListener,
        private var mListItems: List<ListItem>) : RecyclerView.Adapter<SportsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutIdForListItem = R.layout.listitem_sports
        val view = mContext.layoutInflater.inflate(layoutIdForListItem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorPrimaryDark = ResourcesCompat.getColor(mContext.resources, R.color.colorPrimaryDark, null)
        val colorAccent = ResourcesCompat.getColor(mContext.resources, R.color.colorAccent, null)
        if (position == 0) {
            holder.mSportName.text = mContext.getString(R.string.favorites)
            holder.mSportName.setBackgroundColor(colorAccent)
            holder.mSportName.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.rounded_corner_accent))
            holder.mIvSport.setImageResource(R.drawable.favorite)
            holder.mIvSport.setColorFilter(colorAccent, PorterDuff.Mode.SRC_IN)
        } else {
            val sportName = mListItems[position - 1].name
            val sportTag = Utils.normalizaSportName(sportName)
            val sportImage = Utils.getStringResourceByName(mContext, sportTag + "_IMG")
            var sportLocated = Utils.getStringResourceByName(mContext, sportTag)
            if (UtilsPreferences.isShowCompetitionsNumber(mContext)) {
                sportLocated += " (" + mListItems[position - 1].count + ")"
            }
            holder.mSportName.text = sportLocated
            holder.mIvSport.setColorFilter(colorPrimaryDark, PorterDuff.Mode.SRC_IN)
            try {
                val identifier = mContext.resources.getIdentifier(sportImage, "drawable", mContext.packageName)
                holder.mIvSport.setImageResource(identifier)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun getItemCount(): Int {
        return mListItems.size + 1
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mCvSports: View = itemView.cardView
        val mSportName: TextView = itemView.sportName
        val mIvSport: ImageView = itemView.imageView
        init {
            mCvSports.setOnClickListener(this)
        }
        override fun onClick(view: View) {
            mListItemClickListener.onListItemClick(adapterPosition)
        }
    }

    interface ListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }
}