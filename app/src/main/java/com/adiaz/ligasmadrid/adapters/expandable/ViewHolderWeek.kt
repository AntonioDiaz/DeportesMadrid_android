package com.adiaz.ligasmadrid.adapters.expandable

import android.view.View
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView

import com.adiaz.ligasmadrid.R
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

import android.view.animation.Animation.RELATIVE_TO_SELF

class ViewHolderWeek(itemView: View) : GroupViewHolder(itemView) {

    private val tvWeekName: TextView
    private val arrow: ImageView


    init {
        tvWeekName = itemView.findViewById(R.id.tv_match)
        arrow = itemView.findViewById(R.id.list_item_genre_arrow)
    }

    fun setTitle(week: ExpandableGroup<*>) {
        if (week is WeekGroup) {
            tvWeekName.text = week.getTitle()
        }
    }

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
        animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }
}
