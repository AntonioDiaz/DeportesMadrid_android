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
import com.adiaz.deportesmadrid.adapters.GroupsAdapter
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback
import com.adiaz.deportesmadrid.db.daos.GroupsDAO
import com.adiaz.deportesmadrid.db.entities.Group
import com.adiaz.deportesmadrid.utils.Constants
import kotlinx.android.synthetic.main.fragment_team_groups.*
import java.util.*

class TabTeamGroups : Fragment(), GroupsAdapter.ListItemClickListener {

    internal lateinit var mCompetitionCallback: CompetitionCallback
    internal lateinit var mGroupsList: MutableList<Group>

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCompetitionCallback = context as CompetitionCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement CompetitionCallback")
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_team_groups, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mGroupsList = ArrayList()
        if (mCompetitionCallback.queryTeam() != null && mCompetitionCallback.queryTeam()!!.groups != null) {
            for (idGroup in mCompetitionCallback.queryTeam()!!.groups) {
                val group = GroupsDAO.queryCompetitionsById(this.context, idGroup)
                mGroupsList.add(group!!)
            }
            val layoutManager = LinearLayoutManager(this.context)
            val genericAdapter = GroupsAdapter(this.context, mGroupsList, this)
            rvGroups.setHasFixedSize(true)
            rvGroups.layoutManager = layoutManager
            rvGroups.adapter = genericAdapter
            genericAdapter.notifyDataSetChanged()
        }

    }

    override fun onListItemClick(clickedItemIndex: Int) {
        val intent = Intent(this.context, GroupDetailsActivity::class.java)
        intent.putExtra(Constants.ID_COMPETITION, mGroupsList[clickedItemIndex].id)
        intent.putExtra(Constants.NAME_COMPETITION, mGroupsList[clickedItemIndex].nomGrupo)
        startActivity(intent)
    }
}
