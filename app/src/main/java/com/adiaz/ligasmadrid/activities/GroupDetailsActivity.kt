@file:Suppress("DEPRECATION")

package com.adiaz.ligasmadrid.activities

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
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.adapters.LigasMadridFragmentStatePagerAdapter
import com.adiaz.ligasmadrid.adapters.expandable.CalendarAdapter
import com.adiaz.ligasmadrid.adapters.expandable.MatchChildKotlin
import com.adiaz.ligasmadrid.adapters.expandable.WeekGroup
import com.adiaz.ligasmadrid.callbacks.CompetitionCallback
import com.adiaz.ligasmadrid.db.daos.FavoritesDAO
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.db.entities.Favorite
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.extensions.getSportNameLocalized
import com.adiaz.ligasmadrid.fragments.TabClassification
import com.adiaz.ligasmadrid.fragments.TabGroupCalendar
import com.adiaz.ligasmadrid.fragments.TabGroupTeams
import com.adiaz.ligasmadrid.retrofit.RetrofitApi
import com.adiaz.ligasmadrid.retrofit.groupsdetails.ClassificationRetrofit
import com.adiaz.ligasmadrid.retrofit.groupsdetails.GroupDetailsRetrofit
import com.adiaz.ligasmadrid.retrofit.groupsdetails.MatchRetrofit
import com.adiaz.ligasmadrid.retrofit.groupsdetails.Team
import com.adiaz.ligasmadrid.utils.Constants
import com.adiaz.ligasmadrid.utils.MenuActionsUtils
import com.adiaz.ligasmadrid.utils.Utils
import kotlinx.android.synthetic.main.activity_competition.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class GroupDetailsActivity : AppCompatActivity(), CompetitionCallback, CalendarAdapter.ListItemClickListener {

    private var classificationList: List<ClassificationRetrofit> ? = null
    private var matchesList: List<MatchRetrofit> ? = null
    private var weekGroupList: MutableList<WeekGroup> = mutableListOf()
    private var adapter: LigasMadridFragmentStatePagerAdapter? = null
    private var mGroup: Group? = null
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competition)

        mIdGroup = intent.getStringExtra(Constants.ID_COMPETITION)
        val nameCompetition = intent.getStringExtra(Constants.NAME_COMPETITION)

        classificationList = ArrayList()
        matchesList = ArrayList()

        mGroup = GroupsDAO.queryCompetitionsById(this, mIdGroup!!)

        val subtitle = "${mGroup!!.getSportNameLocalized(this)} > ${mGroup!!.distrito} > ${mGroup!!.categoria}"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = nameCompetition
        supportActionBar?.subtitle = subtitle

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        adapter = LigasMadridFragmentStatePagerAdapter(supportFragmentManager)
        adapter?.addFragment(TabGroupCalendar(), "Calendario")
        adapter?.addFragment(TabClassification(), "Clasificación")
        adapter?.addFragment(TabGroupTeams(), "Equipos")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        mProgressDialog = ProgressDialog(this@GroupDetailsActivity)
        mProgressDialog?.setTitle(getString(R.string.loading_info))
        mProgressDialog?.setMessage(getString(R.string.loading))
        mProgressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        showLoading()
        val retrofit = Retrofit.Builder()
                .baseUrl(Utils.getServerUrl(applicationContext)).addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build()
        val retrofitApi = retrofit.create(RetrofitApi::class.java)
        val callCompetitionDetails = retrofitApi.findGroup(mIdGroup)
        callCompetitionDetails.enqueue(CallbackRequest())

        //hideLoading();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favorites, menu)
        for (i in 0 until menu.size()) {
            if (menu.getItem(i).itemId == R.id.action_favorites) {
                val favorite = FavoritesDAO.queryFavorite(this, mIdGroup!!)
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
                val favorite = FavoritesDAO.queryFavorite(this, mIdGroup!!)
                if (favorite != null) {
                    FavoritesDAO.deleteFavorite(this, favorite.id!!)
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_empty)
                    toast(R.string.favorites_competition_removed)
                } else {
                    val newFavorite = Favorite(idGroup = mIdGroup!!)
                    FavoritesDAO.insertFavorite(this, newFavorite)
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_favorite_fill)
                    toast(R.string.favorites_competition_added)
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

    private fun showLoading() {
        mProgressDialog!!.show()
        viewPager!!.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        mProgressDialog!!.dismiss()
        viewPager!!.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun dataReceived(groupDetailsRetrofit: GroupDetailsRetrofit) {
        this.matchesList = groupDetailsRetrofit.matchRetrofits
        val matchesMap = HashMap<Int, MutableList<MatchRetrofit>>()
        for (matchRetrofit in groupDetailsRetrofit.matchRetrofits) {
            if (!matchesMap.containsKey(matchRetrofit.numWeek)) {
                matchesMap[matchRetrofit.numWeek] = mutableListOf()
            }
            matchesMap[matchRetrofit.numWeek]!!.add(matchRetrofit)
        }
        weekGroupList = mutableListOf()
        for (weekNumber in matchesMap.keys) {
            val matches = mutableListOf<MatchChildKotlin>()
            val get = matchesMap.get(weekNumber)
            get?.forEach {
                val matchChild = MatchChildKotlin.matchRetrofit2MatchChild(it, this)
                matches.add(matchChild)
            }
            val weekTitle = application.getString(R.string.calendar_week, weekNumber)
            weekGroupList.add(WeekGroup(weekTitle, matches, weekNumber))
        }
        Collections.sort(weekGroupList) { a, b -> a.idWeek!!.compareTo(b.idWeek!!) }
        this.classificationList = groupDetailsRetrofit.classificationRetrofit
        viewPager!!.adapter = adapter
        tabLayout!!.setupWithViewPager(viewPager)
        hideLoading()
    }

    override fun queryClassificationList(): List<ClassificationRetrofit>? {
        return this.classificationList
    }

    override fun queryCompetition(): Group? {
        return mGroup
    }

    override fun queryTeam(): Team? {
        return null
    }

    override fun queryMatchesList(): List<MatchRetrofit>? {
        return this.matchesList
    }

    override fun queryWeeks(): List<WeekGroup> {
        return this.weekGroupList
    }

    override fun openMenu(view: View) {
        //registerForContextMenu(view)
        //openContextMenu(view)

        registerForContextMenu(view)
        openContextMenu(view)
        unregisterForContextMenu(view)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        mMatchChild = v!!.tag as MatchChildKotlin
        if (mMatchChild != null) {
            val menuInflater = menuInflater
            menuInflater.inflate(R.menu.menu_match, menu)
            menu!!.findItem(R.id.action_view_map).isEnabled = Constants.FIELD_EMPTY != mMatchChild!!.placeName
            menu!!.findItem(R.id.action_add_calendar).isEnabled = Constants.FIELD_EMPTY != mMatchChild!!.dateStr

        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_calendar -> MenuActionsUtils.addMatchEvent(this, mMatchChild!!, mGroup!!)
            R.id.action_view_map -> MenuActionsUtils.showMatchLocation(this, mMatchChild!!)
            R.id.action_share -> MenuActionsUtils.shareMatchDetails(this, mMatchChild!!, mGroup!!)
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

    companion object {
        private val TAG = GroupDetailsActivity::class.java.simpleName
        var mIdGroup: String? = null
        private var mMatchChild: MatchChildKotlin? = null
    }
}
