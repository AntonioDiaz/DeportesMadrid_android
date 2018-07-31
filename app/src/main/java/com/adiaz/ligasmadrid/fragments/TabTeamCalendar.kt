package com.adiaz.ligasmadrid.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.adapters.TeamMatchesAdapter
import com.adiaz.ligasmadrid.callbacks.CompetitionCallback
import com.adiaz.ligasmadrid.utils.Constants
import kotlinx.android.synthetic.main.fragment_team_calendar.*

class TabTeamCalendar : Fragment() {

    lateinit var mCompetitionCallback: CompetitionCallback
    lateinit var mListItemClickListener: TeamMatchesAdapter.ListItemClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCompetitionCallback = context as CompetitionCallback
            mListItemClickListener = context as TeamMatchesAdapter.ListItemClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement CompetitionCallback")
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_team_calendar, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val teamMatches = mCompetitionCallback.queryMatchesList()
        if (teamMatches != null) {
            val linearLayoutManager = LinearLayoutManager(this.context)
            val teamName = if (mCompetitionCallback.queryTeam() == null) Constants.FIELD_EMPTY else mCompetitionCallback.queryTeam()!!.name
            val teamMatchesAdapter = TeamMatchesAdapter(this.context, mCompetitionCallback.queryMatchesList()!!, teamName, mListItemClickListener)
            rvTeamMatches.setHasFixedSize(true)
            rvTeamMatches.layoutManager = linearLayoutManager
            rvTeamMatches.adapter = teamMatchesAdapter
            teamMatchesAdapter.notifyDataSetChanged()
        }
    }
}
