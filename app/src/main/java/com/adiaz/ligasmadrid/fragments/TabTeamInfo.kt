package com.adiaz.ligasmadrid.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.callbacks.CompetitionCallback
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.retrofit.groupsdetails.MatchRetrofit
import com.adiaz.ligasmadrid.utils.Constants
import com.adiaz.ligasmadrid.utils.Utils

import java.util.Calendar

import kotlinx.android.synthetic.main.fragment_team_info.*
import kotlinx.android.synthetic.main.listitem_child_match.*

class TabTeamInfo : Fragment() {

    lateinit internal var mCompetitionCallback: CompetitionCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mCompetitionCallback = context as CompetitionCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement CompetitionCallback")
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_team_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val group = mCompetitionCallback.queryCompetition()
        val sportTag = Utils.normalizaSportName(group!!.deporte)
        val sportLocated = Utils.getStringResourceByName(this.context, sportTag)
        tvCompetition.text = group.nomCompeticion
        tvFase.text = group.nomFase
        tvGroup.text = group.nomGrupo
        tvSportName.text = sportLocated
        tvDistrict.text = group.distrito
        tvCategory!!.text = group.categoria
        val team = if (mCompetitionCallback.queryTeam() == null) Constants.FIELD_EMPTY else mCompetitionCallback.queryTeam()!!.name
        cvNextWeek!!.visibility = View.INVISIBLE
        cvNextWeekFinished!!.visibility = View.INVISIBLE
        if (mCompetitionCallback.queryMatchesList() != null) {
            var match: MatchRetrofit? = null
            val timeInMillis = Calendar.getInstance().timeInMillis
            for (matchRetrofit in mCompetitionCallback.queryMatchesList()!!) {
                if (matchRetrofit.date > timeInMillis
                        && (match == null || match.date > matchRetrofit.date)
                        && matchRetrofit.teamLocal != null && matchRetrofit.teamLocal.name != null
                        && matchRetrofit.teamVisitor != null && matchRetrofit.teamVisitor.name != null) {
                    match = matchRetrofit
                }
            }
            if (match != null) {
                tvNextMatchDate!!.text = Utils.formatDate(match.date)
                tvNextMatchPlace!!.text = if (match.place != null) match.place.name else Constants.FIELD_EMPTY
                var opponent = context.getString(R.string.RESTING)
                if (match.teamLocal != null && match.teamLocal.name != team) {
                    opponent = match.teamLocal.name
                }
                if (match.teamVisitor != null && match.teamVisitor.name != team) {
                    opponent = match.teamVisitor.name
                }
                tvNextMatchOpponent!!.text = opponent
                cvNextWeek!!.visibility = View.VISIBLE
            } else {
                cvNextWeekFinished!!.visibility = View.VISIBLE

            }
        }
    }
}
