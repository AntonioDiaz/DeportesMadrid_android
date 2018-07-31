package com.adiaz.ligasmadrid.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.view.View
import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.adapters.FavoritesAdapter
import com.adiaz.ligasmadrid.db.daos.FavoritesDAO
import com.adiaz.ligasmadrid.db.daos.GroupsDAO
import com.adiaz.ligasmadrid.db.entities.Favorite
import com.adiaz.ligasmadrid.utils.Constants
import kotlinx.android.synthetic.main.activity_favorites.*
import org.jetbrains.anko.toast

class FavoritesActivity : AppCompatActivity(), FavoritesAdapter.ListItemClickListener {

    private var mFavoriteList: List<Favorite>? = null
    private var mAdapter: FavoritesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.title = getString(R.string.app_name)
            supportActionBar!!.subtitle = getString(R.string.favorites_subtitle)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView(this, this)
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favorite = mFavoriteList!![position]
                FavoritesDAO.deleteFavorite(this@FavoritesActivity, favorite.id!!)
                updateRecyclerView(this@FavoritesActivity, this@FavoritesActivity)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun updateRecyclerView(context: Context, listItemClickListener: FavoritesAdapter.ListItemClickListener) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val yearSelected = preferences.getString(context.getString(R.string.pref_year_key), context.getString(R.string.pref_year_default))
        mFavoriteList = FavoritesDAO.queryFavoritesYear(context, yearSelected)
        recyclerView.visibility = View.GONE
        tvEmptyList.visibility = View.GONE
        if (mFavoriteList?.size == 0) {
            tvEmptyList.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(context)
            mAdapter = FavoritesAdapter(context, listItemClickListener, mFavoriteList)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = mAdapter
            mAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        val favorite = mFavoriteList!![clickedItemIndex]
        val idCompetition = favorite.idGroup
        if (favorite.idTeam != null) {
            val intent = Intent(this, TeamDetailsActivity::class.java)
            intent.putExtra(Constants.ID_COMPETITION, idCompetition)
            intent.putExtra(Constants.TEAM_ID, favorite!!.idTeam)
            intent.putExtra(Constants.TEAM_NAME, favorite.nameTeam)
            startActivity(intent)
        } else {
            val intent = Intent(this, GroupDetailsActivity::class.java)
            val group = GroupsDAO.queryCompetitionsById(this, idCompetition)
            if (group != null) {
                intent.putExtra(Constants.ID_COMPETITION, idCompetition)
                intent.putExtra(Constants.NAME_COMPETITION, group.nomGrupo)
                startActivity(intent)
            } else {
                toast(R.string.error_getting_data)
            }
        }
    }

    companion object {
        val TAG = FavoritesActivity::class.java.simpleName
    }
}
