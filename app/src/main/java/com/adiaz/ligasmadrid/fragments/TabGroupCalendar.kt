package com.adiaz.ligasmadrid.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.adapters.expandable.CalendarAdapter
import com.adiaz.ligasmadrid.callbacks.CompetitionCallback
import com.adiaz.ligasmadrid.retrofit.groupsdetails.MatchRetrofit
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

class TabGroupCalendar : Fragment() {

    lateinit var mCompetitionCallback: CompetitionCallback
    lateinit var listItemClickListenerCallback: CalendarAdapter.ListItemClickListener
    lateinit var calendarAdapter: CalendarAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCompetitionCallback = context as CompetitionCallback
            listItemClickListenerCallback = context as CalendarAdapter.ListItemClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement CompetitionCallback")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val calendarMap = HashMap<Int, MutableList<MatchRetrofit>>()
        if (mCompetitionCallback.queryMatchesList() != null) {
            for (matchRetrofitEntity in mCompetitionCallback.queryMatchesList()!!) {
                val weekNum = matchRetrofitEntity.numWeek!! - 1
                var matchesOnWeek: MutableList<MatchRetrofit>? = calendarMap[weekNum]
                if (matchesOnWeek == null) {
                    matchesOnWeek = ArrayList()
                    calendarMap[weekNum] = matchesOnWeek
                }
                matchesOnWeek.add(matchRetrofitEntity)
            }
            calendarAdapter = CalendarAdapter(listItemClickListenerCallback, mCompetitionCallback.queryWeeks()!!)
            val layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = calendarAdapter
            recyclerView.addItemDecoration(dividerItemDecoration)
            calendarAdapter.notifyDataSetChanged()
        }
    }
}
