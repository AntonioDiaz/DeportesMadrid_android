package com.adiaz.ligasmadrid.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.adapters.GenericAdapter
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.utils.Constants
import com.adiaz.ligasmadrid.utils.ListItem
import com.adiaz.ligasmadrid.utils.UtilsPreferences
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*

class DistrictActivity : AppCompatActivity(), GenericAdapter.ListItemClickListener {

    private var sportSelected: String? = null
    private var elementsList: List<ListItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        sportSelected = intent.getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME)
        val sportSelectedCount = intent.getStringExtra(Constants.EXTRA_COUNT)
        var subTitle = sportSelected
        if (UtilsPreferences.isShowCompetitionsNumber(this)) {
            subTitle += " ($sportSelectedCount)"
        }
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.title = getString(R.string.title_district)
            supportActionBar!!.subtitle = subTitle
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        val groups = GroupsDAO.queryCompetitionsBySport(this, sportSelected!!)
        elementsList = initElementsList(groups)
        val layoutManager = LinearLayoutManager(this)
        val genericAdapter = GenericAdapter(this, this, elementsList)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = genericAdapter
        genericAdapter.notifyDataSetChanged()
        recyclerView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_home -> {
                val i = Intent(this, MainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initElementsList(groups: List<Group>): List<ListItem> {
        val listElements = ArrayList<ListItem>()
        val map = HashMap<String, Int>()
        for (group in groups) {
            var count: Int? = map[group.distrito]
            if (count == null) {
                count = 0
            }
            map[group.distrito!!] = count + 1
        }
        for (s in map.keys) {
            //String value = map.get(s).toString();
            val listItem = ListItem(s, map[s].toString())
            listElements.add(listItem)
        }
        Collections.sort(listElements, ListItem.ListItemCompartor())
        return listElements
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        val intent = Intent(this, CategoriesActivity::class.java)
        val districtName = elementsList!![clickedItemIndex].name
        val count = elementsList!![clickedItemIndex].count
        intent.putExtra(Constants.EXTRA_SPORT_SELECTED_NAME, sportSelected)
        intent.putExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME, districtName)
        intent.putExtra(Constants.EXTRA_COUNT, count)
        startActivity(intent)
    }
}
