package com.adiaz.deportesmadrid.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.activities.GroupDetailsActivity
import com.adiaz.deportesmadrid.activities.TeamDetailsActivity
import com.adiaz.deportesmadrid.adapters.GenericAdapter
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback
import com.adiaz.deportesmadrid.retrofit.groupsdetails.Team
import com.adiaz.deportesmadrid.utils.Constants
import com.adiaz.deportesmadrid.utils.ListItem
import com.adiaz.deportesmadrid.utils.UtilsPreferences
import kotlinx.android.synthetic.main.fragment_teams.*
import java.util.*

class TabGroupTeams : Fragment(), GenericAdapter.ListItemClickListener {

    lateinit var mCompetitionCallback: CompetitionCallback
    private var teamsNamesList: ArrayList<Team>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCompetitionCallback = context as CompetitionCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement CompetitionCallback")
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val teamsSet = HashSet<Team>()
        for (matchRetrofit in mCompetitionCallback.queryMatchesList()!!) {
            if (matchRetrofit.teamLocal != null) {
                teamsSet.add(matchRetrofit.teamLocal)
            }
            if (matchRetrofit.teamVisitor != null) {
                teamsSet.add(matchRetrofit.teamVisitor)
            }
        }
        val layoutManager = LinearLayoutManager(this.context)
        teamsNamesList = ArrayList(teamsSet)
        Collections.sort(teamsNamesList!!) { team, t1 -> team.name.compareTo(t1.name) }
        val elements = ArrayList<ListItem>()
        var i = 0
        for (s in teamsNamesList!!) {
            var teamName = s.name
            if (UtilsPreferences.isShowCompetitionsNumber(context)) {
                teamName += " (" + s.id + ")"
            }
            elements.add(ListItem(teamName, Integer.toString(i++)))
        }
        val genericAdapter = GenericAdapter(this.context, this, elements)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = genericAdapter
        genericAdapter.notifyDataSetChanged()
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        val intent = Intent(this.context, TeamDetailsActivity::class.java)
        intent.putExtra(Constants.ID_COMPETITION, GroupDetailsActivity.mIdGroup)
        intent.putExtra(Constants.TEAM_ID, teamsNamesList!![clickedItemIndex].id)
        intent.putExtra(Constants.TEAM_NAME, teamsNamesList!![clickedItemIndex].name)
        startActivity(intent)
    }
}
