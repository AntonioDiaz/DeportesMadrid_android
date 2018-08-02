package com.adiaz.ligasmadrid.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.adapters.GroupsAdapter
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.utils.Constants
import com.adiaz.ligasmadrid.utils.Utils
import com.adiaz.ligasmadrid.utils.UtilsPreferences
import kotlinx.android.synthetic.main.activity_list.*

class GroupsActivity : AppCompatActivity(), GroupsAdapter.ListItemClickListener {

    private lateinit var sportSelected: String
    private lateinit var districtSelected: String
    private lateinit var categorySelected: String
    private var competitionsList: List<Group> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        sportSelected = intent.getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME)
        districtSelected = intent.getStringExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME)
        categorySelected = intent.getStringExtra(Constants.EXTRA_CATEGORY_SELECTED_NAME)
        val count = intent.getStringExtra(Constants.EXTRA_COUNT)
        val sportNameLocalized = Utils.getSportNameLocalized(this, sportSelected)
        var subtitle = "$sportNameLocalized > $districtSelected > $categorySelected"
        if (UtilsPreferences.isShowCompetitionsNumber(this)) {
            subtitle += " ($count)"
        }
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_grupo)
        supportActionBar?.subtitle = subtitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showLoading()
        competitionsList = GroupsDAO.queryCompetitionsBySportAndDistrictAndCategory(this, sportSelected, districtSelected, categorySelected)
        val layoutManager = LinearLayoutManager(this)
        val genericAdapter = GroupsAdapter(this, competitionsList, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = genericAdapter
        genericAdapter.notifyDataSetChanged()
        hideLoading()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideLoading() {
        recyclerView.visibility = View.VISIBLE
    }

    private fun showLoading() {
        recyclerView.visibility = View.INVISIBLE
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        val intent = Intent(this, GroupDetailsActivity::class.java)
        intent.putExtra(Constants.ID_COMPETITION, competitionsList!![clickedItemIndex].id)
        intent.putExtra(Constants.NAME_COMPETITION, competitionsList!![clickedItemIndex].nomGrupo)
        startActivity(intent)
    }
}
