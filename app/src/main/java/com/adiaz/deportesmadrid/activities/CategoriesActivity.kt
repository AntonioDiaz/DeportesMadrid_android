package com.adiaz.deportesmadrid.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.adiaz.deportesmadrid.R
import com.adiaz.deportesmadrid.adapters.GenericAdapter
import com.adiaz.deportesmadrid.db.daos.GroupsDAO
import com.adiaz.deportesmadrid.db.entities.Group
import com.adiaz.deportesmadrid.utils.Constants
import com.adiaz.deportesmadrid.utils.ListItem
import com.adiaz.deportesmadrid.utils.UtilsPreferences
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*

class CategoriesActivity : AppCompatActivity(), GenericAdapter.ListItemClickListener {

    private var elementsList: List<ListItem>? = null
    private var sportSelected: String? = null
    private var districtSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        sportSelected = intent.getStringExtra(Constants.EXTRA_SPORT_SELECTED_NAME)
        districtSelected = intent.getStringExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME)
        val count = intent.getStringExtra(Constants.EXTRA_COUNT)
        var subTitle = sportSelected + Constants.PATH_SEPARATOR + districtSelected
        if (UtilsPreferences.isShowCompetitionsNumber(this)) {
            subTitle += " ($count)"
        }
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.title = getString(R.string.title_category)
            supportActionBar!!.subtitle = subTitle
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        showLoading()
        val groups = GroupsDAO.queryCompetitionsBySportAndDistrict(this, sportSelected!!, districtSelected!!)
        elementsList = initElementsList(groups)
        val layoutManager = LinearLayoutManager(this)
        val genericAdapter = GenericAdapter(this, this, elementsList)
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

    private fun initElementsList(groups: List<Group>): List<ListItem> {
        val listElements = ArrayList<ListItem>()
        val map = HashMap<String, Int>()
        for (group in groups) {
            var count: Int? = map[group.categoria]
            if (count == null) {
                count = 0
            }
            map[group.categoria!!] = count + 1
        }
        for (s in map.keys) {
            val listItem = ListItem(s, map[s].toString())
            listElements.add(listItem)
        }
        Collections.sort(listElements, ListItem.ListItemCompartor())
        return listElements
    }

    private fun showLoading() {
        recyclerView.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        recyclerView.visibility = View.VISIBLE
    }


    override fun onListItemClick(clickedItemIndex: Int) {
        val categorySeleted = elementsList!![clickedItemIndex].name
        val count = elementsList!![clickedItemIndex].count
        val intent = Intent(this, GroupsActivity::class.java)
        intent.putExtra(Constants.EXTRA_SPORT_SELECTED_NAME, sportSelected)
        intent.putExtra(Constants.EXTRA_DISTRICT_SELECTED_NAME, districtSelected)
        intent.putExtra(Constants.EXTRA_CATEGORY_SELECTED_NAME, categorySeleted)
        intent.putExtra(Constants.EXTRA_COUNT, count)
        startActivity(intent)

    }
}
