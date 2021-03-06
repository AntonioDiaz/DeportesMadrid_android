@file:Suppress("DEPRECATION")

package com.adiaz.ligasmadrid.activities

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.adapters.FavoritesAdapter
import com.adiaz.ligasmadrid.adapters.SportsAdapter
import com.adiaz.ligasmadrid.adapters.TeamSearchAdapter
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.retrofit.RetrofitApi
import com.adiaz.ligasmadrid.retrofit.groupsdetails.Team
import com.adiaz.ligasmadrid.retrofit.groupslist.GroupRetrofitEntity
import com.adiaz.ligasmadrid.utils.*
import com.adiaz.ligasmadrid.utils.Utils.showSnack
import com.adiaz.ligasmadrid.utils.entities.TeamEntity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity :
        AppCompatActivity(),
        Callback<List<GroupRetrofitEntity>>,
        SportsAdapter.ListItemClickListener,
        FavoritesAdapter.ListItemClickListener,
        TeamSearchAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<List<Group>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SEARCH_GROUPS_LOADER = 22
    }

    private var elementsList: List<ListItem>? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mProgressDialogSearch: ProgressDialog? = null
    private var mTeamsSearch: MutableList<TeamEntity>? = null
    private var mMenu: Menu? = null
    private var retryCountSync = 0
    private var retryCountSearch = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppThemeNoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            toolbar.setLogo(R.mipmap.ic_launcher)
            toolbar.title = getString(R.string.app_name)
        }
        handleIntent(intent)
        viewSports.visibility = View.VISIBLE
        viewSearchResults.visibility = View.INVISIBLE
        tvEmptyList.visibility = View.INVISIBLE
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPICS_SYNC)
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPICS_GENERAL)
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (supportActionBar != null) {
            toolbar!!.subtitle = Utils.getYearDesc(this)
        }
        supportLoaderManager.initLoader(SEARCH_GROUPS_LOADER,  Bundle(), this)
        val loaderManager = supportLoaderManager
        val githubSearchLoader = loaderManager.getLoader<String>(SEARCH_GROUPS_LOADER)
        if (githubSearchLoader == null) {
            loaderManager.initLoader(SEARCH_GROUPS_LOADER, Bundle(), this)
        } else {
            loaderManager.restartLoader(SEARCH_GROUPS_LOADER, Bundle(), this)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
        updateMenuSyncText()
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val teamName = intent.getStringExtra(SearchManager.QUERY).toUpperCase()
            if (mProgressDialogSearch == null) {
                mProgressDialogSearch = ProgressDialog(this@MainActivity)
                mProgressDialogSearch!!.setTitle(getString(R.string.loading_search))
                mProgressDialogSearch!!.setMessage(getString(R.string.loading))
                mProgressDialogSearch!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            }
            mProgressDialogSearch!!.show()
            val retrofit = Retrofit.Builder()
                    .baseUrl(Utils.getServerUrl(this))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val retrofitApi = retrofit.create(RetrofitApi::class.java)
            val call = retrofitApi.queryTeams(teamName)
            viewSports!!.visibility = View.INVISIBLE
            retryCountSearch = 0
            call.enqueue(SearchTeamCallBack())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.mMenu = menu
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItemSearch = menu.findItem(R.id.search)
        val searchView = menuItemSearch.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        menuItemSearch.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem) = true
            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                viewSports!!.visibility = View.VISIBLE
                viewSearchResults!!.visibility = View.INVISIBLE
                return true
            }
        })
        updateMenuSyncText()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.action_sync -> syncCompetitions()
            R.id.action_preferences -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.action_about -> startActivity(Intent(this, AboutActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun syncCompetitions() {
        showLoading()
        val retrofit = Retrofit.Builder()
                .baseUrl(Utils.getServerUrl(applicationContext))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val retrofitApi = retrofit.create(RetrofitApi::class.java)
        val call = retrofitApi.queryAllGroups()
        retryCountSync = 0
        call.enqueue(this)

    }

    override fun onResponse(call: Call<List<GroupRetrofitEntity>>, response: Response<List<GroupRetrofitEntity>>) {
        if (response.body() == null) {
            Utils.showSnack(mainView, getString(R.string.error_getting_data))
        } else {
            GroupsDAO.insertCompetitions(this, response.body()!!)
            //select all
            fillRecyclerview(GroupsDAO.queryAllCompetitions(this))
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = preferences.edit()
            editor.putString(getString(R.string.pref_last_udpate), Utils.formatDate(Date().time))
            editor.putBoolean(getString(R.string.pref_need_update), false)
            editor.apply()
            updateMenuSyncText()
        }
    }

    override fun onFailure(call: Call<List<GroupRetrofitEntity>>, t: Throwable) {
        Log.d(TAG, "onFailure ($retryCountSync out of ${Constants.TOTAL_RETRIES}")
        if (retryCountSync++ < Constants.TOTAL_RETRIES) {
            call.clone().enqueue(this)
        } else {
            hideLoading()
            showSnack(mainView, getString(R.string.error_getting_data))
        }
    }

    private fun updateMenuSyncText() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lastUpdate = preferences.getString(getString(R.string.pref_last_udpate), "")
        var syncMenu = getString(R.string.action_sync)
        if (UtilsPreferences.isShowCompetitionsNumber(this) && mMenu != null && !TextUtils.isEmpty(lastUpdate)) {
            syncMenu = getString(R.string.action_sync_with_date, lastUpdate)
        }
        if (mMenu!=null) {
            val item = mMenu!!.findItem(R.id.action_sync)
            item.title = syncMenu
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun fillRecyclerview(competitionList: List<Group>) {
        elementsList = initElementsList(competitionList)
        val layoutManager = GridLayoutManager(this, 2)
        val sportsAdapter = SportsAdapter(this, this, elementsList!!)
        rvCompetitions.setHasFixedSize(true)
        rvCompetitions.layoutManager = layoutManager
        rvCompetitions.adapter = sportsAdapter
        sportsAdapter.notifyDataSetChanged()
        hideLoading()
    }

    private fun initElementsList(groups: List<Group>): List<ListItem> {
        val listElements = ArrayList<ListItem>()
        val map = HashMap<String, Int>()
        for (group in groups) {
            var count: Int? = map[group.deporte]
            if (count == null) {
                count = 0
            }
            map[group.deporte!!] = count + 1
        }
        for (s in map.keys) {
            val listItem = ListItem(s, map[s].toString())
            listElements.add(listItem)
        }
        Collections.sort(listElements, ListItem.ListItemCompartor())
        return listElements
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        if (clickedItemIndex == 0) {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, DistrictActivity::class.java)
            val sportName = elementsList!![clickedItemIndex - 1].name
            val count = elementsList!![clickedItemIndex - 1].count
            intent.putExtra(Constants.EXTRA_SPORT_SELECTED_NAME, sportName)
            intent.putExtra(Constants.EXTRA_COUNT, count)
            startActivity(intent)
        }
    }

    private fun hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    private fun showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this@MainActivity)
            mProgressDialog!!.setTitle(getString(R.string.loading_competitions))
            mProgressDialog!!.setMessage(getString(R.string.loading))
            mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }
        mProgressDialog!!.show()
    }

    override fun onListItemClickTeamSearch(clickedItemIndex: Int) {
        val teamEntity = mTeamsSearch!![clickedItemIndex]
        val intent = Intent(this, TeamDetailsActivity::class.java)
        intent.putExtra(Constants.ID_COMPETITION, teamEntity.idGroup)
        intent.putExtra(Constants.TEAM_ID, teamEntity.idTeam)
        intent.putExtra(Constants.TEAM_NAME, teamEntity.teamName)
        startActivity(intent)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<List<Group>> {
        return object : AsyncTaskLoader<List<Group>>(this) {
            override fun onStartLoading() = forceLoad()
            override fun loadInBackground() = GroupsDAO.queryAllCompetitions(this@MainActivity)
        }
    }

    override fun onLoadFinished(loader: Loader<List<Group>>, data: List<Group>) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (data.isEmpty() || preferences.getBoolean(getString(R.string.pref_need_update), true)) {
            syncCompetitions()
        } else {
            fillRecyclerview(data)
        }
    }

    override fun onLoaderReset(loader: Loader<List<Group>>) {}

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_year_key)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            preferences.edit().putBoolean(getString(R.string.pref_need_update), true).apply()
        }
    }

    internal inner class SearchTeamCallBack : Callback<List<Team>> {

        override fun onResponse(call: Call<List<Team>>, response: Response<List<Team>>) {
            if (mProgressDialogSearch != null) {
                mProgressDialogSearch!!.dismiss()
            }
            viewSearchResults!!.visibility = View.INVISIBLE
            rvSearchResults!!.visibility = View.INVISIBLE
            tvEmptyList!!.visibility = View.INVISIBLE
            if (response.body() != null) {
                viewSearchResults!!.visibility = View.VISIBLE
                if (response.body()!!.isNotEmpty()) {
                    rvSearchResults!!.visibility = View.VISIBLE
                    mTeamsSearch = ArrayList()
                    for (team in response.body()!!) {
                        for (idGroup in team.groups) {
                            mTeamsSearch!!.add(TeamEntity(team.id, team.name, idGroup))
                        }
                    }
                    val layoutManager = LinearLayoutManager(this@MainActivity)
                    val teamSearchAdapter = TeamSearchAdapter(this@MainActivity, this@MainActivity, mTeamsSearch)
                    rvSearchResults!!.setHasFixedSize(true)
                    rvSearchResults!!.layoutManager = layoutManager
                    rvSearchResults!!.adapter = teamSearchAdapter
                    teamSearchAdapter.notifyDataSetChanged()
                } else {
                    tvEmptyList!!.visibility = View.VISIBLE
                }
            }
        }

        override fun onFailure(call: Call<List<Team>>, t: Throwable) {
            Log.v(TAG, "onFailure ($retryCountSync out of $Constants.TOTAL_RETRIES)")
            if (retryCountSearch++ < Constants.TOTAL_RETRIES) {
                call.clone().enqueue(this)
            } else {
                mProgressDialogSearch!!.dismiss()
                viewSearchResults!!.visibility = View.VISIBLE
                showSnack(mainView, getString(R.string.error_getting_data))
            }
        }
    }
}
