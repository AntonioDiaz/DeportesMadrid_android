package com.adiaz.ligasmadrid.utils


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View

import com.adiaz.ligasmadrid.R
import com.adiaz.ligasmadrid.activities.DistrictActivity

import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.WordUtils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object Utils {

    private val TAG = Utils::class.java.simpleName

    /**
     * If input is null or empty return "-"
     * Else return input with first letter in uppercase.
     *
     * @param input
     * @return
     */
    fun normalizeString(input: String): String {
        return if (StringUtils.isEmpty(input)) {
            "-"
        } else {
            WordUtils.capitalizeFully(input.toLowerCase())
        }
    }


    fun showSnack(view: View, text: String) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
        snackbar.setAction("Dismiss") { snackbar.dismiss() }
        snackbar.show()
    }

    fun getStringResourceByName(context: Context, aString: String): String {
        val packageName = context.packageName
        var strResource = aString
        try {
            val resId = context.resources.getIdentifier(aString, "string", packageName)
            strResource = context.getString(resId)
        } catch (e: Exception) {
            Log.d(TAG, "resource not found at getStringResourceByName: $aString")
        }

        return strResource
    }

    fun normalizaSportName(sportNameOriginal: String): String {
        return sportNameOriginal.replace(" ", "_").replace("-", "_").toUpperCase()
    }

    fun formatDate(dateLong: Long?): String {
        val df = SimpleDateFormat(Constants.DATE_FORMAT)
        return df.format(dateLong)
    }

    @Throws(ParseException::class)
    fun parseDate(dateStr: String): Date {
        val df = SimpleDateFormat(Constants.DATE_FORMAT)
        return df.parse(dateStr)
    }

    fun getServerUrl(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val yearSelected = preferences.getString(context.getString(R.string.pref_year_key), context.getString(R.string.pref_year_default))
        return when (yearSelected) {
            context.getString(R.string.pref_year_2017_value) -> context.getString(R.string.url_2017)
            context.getString(R.string.pref_year_2018_value) -> context.getString(R.string.url_2018)
            context.getString(R.string.pref_year_2019_value) -> context.getString(R.string.url_2019)
            else -> context.getString(R.string.url_2019)
        }
    }

    fun getYearDesc(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val yearSelected = preferences.getString(context.getString(R.string.pref_year_key), context.getString(R.string.pref_year_default))
        return when (yearSelected) {
            context.getString(R.string.pref_year_2017_value) -> context.getString(R.string.pref_year_2017_label)
            context.getString(R.string.pref_year_2018_value) -> context.getString(R.string.pref_year_2018_label)
            context.getString(R.string.pref_year_2019_value) -> context.getString(R.string.pref_year_2019_label)
            else -> context.getString(R.string.pref_year_2019_label)
        }
    }

    fun getSportNameLocalized(context: Context, sportSelected: String): String {
        return getStringResourceByName(context, normalizaSportName(sportSelected))
    }
}
