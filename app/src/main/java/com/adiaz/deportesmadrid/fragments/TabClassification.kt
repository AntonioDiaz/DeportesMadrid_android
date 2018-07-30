package com.adiaz.deportesmadrid.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.adapters.ClassificationAdapter
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback
import kotlinx.android.synthetic.main.fragment_classification.*

class TabClassification : Fragment() {

    lateinit var mCompetitionCallback: CompetitionCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCompetitionCallback = context as CompetitionCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement CompetitionCallback")
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_classification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val classificationList = mCompetitionCallback.queryClassificationList()
        val team = if (mCompetitionCallback.queryTeam() == null) "" else mCompetitionCallback.queryTeam()!!.name
        if (classificationList != null) {
            val layoutManager = LinearLayoutManager(this.context)
            val classificationAdapter = ClassificationAdapter(this.context, classificationList, team)
            val dividerItemDecoration = DividerItemDecoration(recyclerView!!.context, layoutManager.orientation)
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.layoutManager = layoutManager
            recyclerView!!.adapter = classificationAdapter
            recyclerView!!.addItemDecoration(dividerItemDecoration)
            classificationAdapter.notifyDataSetChanged()
        }
    }
}
