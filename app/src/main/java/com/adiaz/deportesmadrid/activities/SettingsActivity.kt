package com.adiaz.deportesmadrid.activities

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.adiaz.deportesmadrid.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        val actionBar = this.supportActionBar
        // Set the action bar back button to look like an up button
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)
    }
}
