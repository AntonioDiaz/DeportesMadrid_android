package com.adiaz.ligasmadrid.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.adiaz.ligasmadrid.BuildConfig
import com.adiaz.ligasmadrid.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_settings.view.*

class AboutActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.subtitle = getString(R.string.title_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvVersion.text = BuildConfig.VERSION_NAME
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}