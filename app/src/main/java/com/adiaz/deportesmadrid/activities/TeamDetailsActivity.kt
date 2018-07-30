package com.adiaz.deportesmadrid.activities

import android.app.ProgressDialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.adapters.DeportesMadridFragmentStatePagerAdapter
import com.adiaz.deportesmadrid.adapters.TeamMatchesAdapter
import com.adiaz.deportesmadrid.adapters.expandable.MatchChildKotlin
import com.adiaz.deportesmadrid.adapters.expandable.WeekGroup
import com.adiaz.deportesmadrid.callbacks.CompetitionCallback
import com.adiaz.deportesmadrid.db.daos.FavoritesDAO
import com.adiaz.deportesmadrid.db.daos.GroupsDAO
import com.adiaz.deportesmadrid.db.entities.Favorite
import com.adiaz.deportesmadrid.db.entities.Group
import com.adiaz.deportesmadrid.fragments.TabClassification
import com.adiaz.deportesmadrid.fragments.TabTeamCalendar
import com.adiaz.deportesmadrid.fragments.TabTeamGroups
import com.adiaz.deportesmadrid.fragments.TabTeamInfo
import com.adiaz.deportesmadrid.retrofit.RetrofitApi
import com.adiaz.deportesmadrid.retrofit.groupsdetails.ClassificationRetrofit
import com.adiaz.deportesmadrid.retrofit.groupsdetails.GroupDetailsRetrofit
import com.adiaz.deportesmadrid.retrofit.groupsdetails.MatchRetrofit
import com.adiaz.deportesmadrid.retrofit.groupsdetails.Team
import com.adiaz.deportesmadrid.utils.Constants
import com.adiaz.deportesmadrid.utils.MenuActionsUtils
import com.adiaz.deportesmadrid.utils.Utils
import kotlinx.android.synthetic.main.activity_team_details.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@Suppress("DEPRECATION")
class TeamDetailsActivity : AppCompatActivity(), CompetitionCallback, TeamMatchesAdapter.ListItemClickListener {

    internal var adapter: DeportesMadridFragmentStatePagerAdapter? = null

    lateinit var mIdGroup: String
    internal var mTeamId: Long? = null
    internal var mTeamName: String? = null
    internal var classificationRetrofitList: List<ClassificationRetrofit>? = null
    internal var matchesRetrofitList: MutableList<MatchRetrofit>? = null
    internal var mGroup: Group? = null
    internal var mTeam: Team? = null
    internal var mProgressDialog: ProgressDialog? = null
    internal var mMatchRetrofit: MatchRetrofit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_details)
        mIdGroup = intent.getStringExtra(Constants.ID_COMPETITION)
        mGroup = GroupsDAO.queryCompetitionsById(this, mIdGroup)
        mTeamId = intent.getLongExtra(Constants.TEAM_ID, 0L)
        mTeamName = intent.getStringExtra(Constants.TEAM_NAME)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = mTeamName
        supportActionBar?.subtitle = getString(R.string.team_subtitle, mGroup!!.nomGrupo)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        adapter = DeportesMadridFragmentStatePagerAdapter(supportFragmentManager)
        adapter?.addFragment(TabTeamInfo(), getString(R.string.team_details_tab_information))
        adapter?.addFragment(TabTeamCalendar(), getString(R.string.team_details_tab_calendar))
        adapter?.addFragment(TabClassification(), getString(R.string.team_details_tab_classification))
        adapter?.addFragment(TabTeamGroups(), getString(R.string.team_details_tab_grupos))

        viewPager.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)
        mProgressDialog = ProgressDialog(this@TeamDetailsActivity)
        mProgressDialog?.setTitle(getString(R.string.loading_info))
        mProgressDialog?.setMessage(getString(R.string.loading))
        mProgressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        showLoading()
        val retrofit = Retrofit.Builder()
                .baseUrl(Utils.getServerUrl(applicationContext)).addConverterFactory(GsonConverterFactory.create()).build()
        val retrofitApi = retrofit.create(RetrofitApi::class.java)
        val callCompetitionDetails = retrofitApi.findGroup(mIdGroup)
        callCompetitionDetails.enqueue(CallbackRequest())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorites, menu)
        for (i in 0 until menu.size()) {
            if (menu.getItem(i).itemId == R.id.action_favorites) {
                val favorite = FavoritesDAO.queryFavorite(this, mIdGroup, mTeamId!!)
                if (favorite != null) {
                    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
                    val drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill)
                    menu.getItem(i).icon = drawable
                }
                val icon = menu.getItem(i).icon
                val colorWhite = ContextCompat.getColor(this, R.color.colorWhite)
                val colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
                icon.colorFilter = colorFilter
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
                val drawable: Drawable
                val favorite = FavoritesDAO.queryFavorite(this, mIdGroup, mTeamId!!)
                if (favorite != null) {
                    FavoritesDAO.deleteFavorite(this, favorite.id!!)
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_empty)
                    toast(R.string.favorites_team_removed)
                } else {
                    //val newFavorite = Favorite.builder().idGroup(mIdGroup).idTeam(mTeamId).nameTeam(mTeamName).build()
                    val newFavorite = Favorite(idGroup = mIdGroup, idTeam = mTeamId, nameTeam = mTeamName!!)
                    FavoritesDAO.insertFavorite(this, newFavorite)
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill)
                    toast(R.string.favorites_team_added)
                }
                val colorWhite = ContextCompat.getColor(this, R.color.colorWhite)
                val colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
                drawable.colorFilter = colorFilter
                item.icon = drawable
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideLoading() {
        mProgressDialog?.dismiss()
        viewPager!!.visibility = View.VISIBLE
    }

    private fun showLoading() {
        mProgressDialog?.show()
        viewPager!!.visibility = View.INVISIBLE
    }

    private fun dataReceived(groupDetailsRetrofit: GroupDetailsRetrofit) {
        matchesRetrofitList = mutableListOf()
        if (groupDetailsRetrofit.matchRetrofits != null) {
            for (matchRetrofitEntity in groupDetailsRetrofit.matchRetrofits) {
                if (matchRetrofitEntity.teamLocal != null && matchRetrofitEntity.teamLocal.id == mTeamId || matchRetrofitEntity.teamVisitor != null && matchRetrofitEntity.teamVisitor.id == mTeamId) {
                    matchesRetrofitList?.add(matchRetrofitEntity)
                    if (mTeam == null) {
                        mTeam = Team()
                        if (matchRetrofitEntity.teamLocal != null && matchRetrofitEntity.teamLocal.id == mTeamId) {
                            mTeam!!.id = matchRetrofitEntity.teamLocal.id
                            mTeam!!.name = matchRetrofitEntity.teamLocal.name
                            mTeam!!.groups = matchRetrofitEntity.teamLocal.groups
                        }
                        if (matchRetrofitEntity.teamVisitor != null && matchRetrofitEntity.teamVisitor.id == mTeamId) {
                            mTeam!!.id = matchRetrofitEntity.teamVisitor.id
                            mTeam!!.name = matchRetrofitEntity.teamVisitor.name
                            mTeam!!.groups = matchRetrofitEntity.teamVisitor.groups
                        }
                    }
                }
            }
        }
        this.classificationRetrofitList = ArrayList()
        if (groupDetailsRetrofit.classificationRetrofit != null) {
            this.classificationRetrofitList = groupDetailsRetrofit.classificationRetrofit
        }
        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)
        hideLoading()
    }

    override fun queryClassificationList(): List<ClassificationRetrofit>? {
        return this.classificationRetrofitList
    }

    override fun queryTeam(): Team? {
        return mTeam
    }

    override fun queryMatchesList(): MutableList<MatchRetrofit>? {
        return matchesRetrofitList
    }

    override fun queryWeeks(): List<WeekGroup>? {
        return null
    }

    override fun queryCompetition(): Group? {
        return mGroup
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        mMatchRetrofit = matchesRetrofitList!![clickedItemIndex]
        registerForContextMenu(mainView)
        openContextMenu(mainView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (mMatchRetrofit != null) {
            val menuInflater = menuInflater
            menuInflater.inflate(R.menu.menu_match, menu)
            menu!!.findItem(R.id.action_view_map).isEnabled = mMatchRetrofit!!.place != null
            menu!!.findItem(R.id.action_add_calendar).isEnabled = mMatchRetrofit!!.date != null
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val matchChild = MatchChildKotlin.matchRetrofit2MatchChild(mMatchRetrofit!!, this)
        when (item.itemId) {
            R.id.action_add_calendar -> MenuActionsUtils.addMatchEvent(this, matchChild, mGroup!!)
            R.id.action_view_map -> MenuActionsUtils.showMatchLocation(this, matchChild)
            R.id.action_share -> MenuActionsUtils.shareMatchDetails(this, matchChild, mGroup!!)
        }
        return super.onContextItemSelected(item)
    }


    internal inner class CallbackRequest : Callback<GroupDetailsRetrofit> {

        override fun onResponse(call: Call<GroupDetailsRetrofit>, response: Response<GroupDetailsRetrofit>) {
            dataReceived(response.body()!!)
        }

        override fun onFailure(call: Call<GroupDetailsRetrofit>, t: Throwable) {
            hideLoading()
            Utils.showSnack(mainView, getString(R.string.error_getting_data))
        }
    }
}
